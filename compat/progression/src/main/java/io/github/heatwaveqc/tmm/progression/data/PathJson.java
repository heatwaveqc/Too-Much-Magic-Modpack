package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Strict data-only parser. Unknown effect types fail closed rather than silently executing. */
public final class PathJson {
    private PathJson() {}

    private static String str(JsonObject j, String key, String fallback) {
        return j.has(key) ? j.get(key).getAsString() : fallback;
    }
    private static int number(JsonObject j, String key, int fallback) {
        return j.has(key) ? j.get(key).getAsInt() : fallback;
    }
    private static double decimal(JsonObject j, String key, double fallback) {
        return j.has(key) ? j.get(key).getAsDouble() : fallback;
    }
    private static boolean flag(JsonObject j, String key) {
        return j.has(key) && j.get(key).getAsBoolean();
    }
    private static JsonArray entries(JsonObject j, String key) {
        return j.has(key) ? j.getAsJsonArray(key) : new JsonArray();
    }
    private static List<String> strings(JsonObject j, String key) {
        List<String> out = new ArrayList<>();
        for (JsonElement entry : entries(j, key)) out.add(entry.getAsString());
        return out;
    }
    private static String id(String text) {
        String body = text.startsWith("#") ? text.substring(1) : text;
        if (!body.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Expected namespaced ID/tag: " + text);
        return text;
    }
    private static String event(String text) {
        if (!Set.of("kill", "block_break", "external").contains(text))
            throw new IllegalArgumentException("Unsupported event " + text);
        return text;
    }

    public static Definition.Path read(String pathId, JsonObject source) {
        id(pathId);
        if (number(source,"schema",0) != 1) throw new IllegalArgumentException("schema must be 1");
        List<Long> thresholds = new ArrayList<>();
        for (JsonElement el : entries(source,"rank_xp")) {
            long n = el.getAsLong();
            if (n < 0 || (!thresholds.isEmpty() && n <= thresholds.getLast()))
                throw new IllegalArgumentException("rank_xp must be strictly increasing");
            thresholds.add(n);
        }
        if (thresholds.isEmpty() || thresholds.size() > 200 || thresholds.getFirst() != 0)
            throw new IllegalArgumentException("rank_xp must start at 0 and contain 1..200 entries");
        int pointsPerRank = number(source,"points_per_rank",1);
        if (pointsPerRank < 0 || pointsPerRank > 1000)
            throw new IllegalArgumentException("Bad points_per_rank");

        List<Definition.Activity> activities = new ArrayList<>();
        for (JsonElement el : entries(source,"activities")) {
            JsonObject j = el.getAsJsonObject();
            long xp = j.get("xp").getAsLong();
            int cooldown = number(j,"cooldown_ticks",0);
            if (xp < 0 || xp > 1000000 || cooldown < 0 || cooldown > 72000)
                throw new IllegalArgumentException("Bad activity award/cooldown");
            activities.add(new Definition.Activity(event(str(j,"event","")),
                id(str(j,"target","")),xp,cooldown));
        }

        List<Definition.Milestone> milestones = new ArrayList<>();
        Set<String> milestonesSeen = new HashSet<>();
        for (JsonElement el : entries(source,"milestones")) {
            JsonObject j = el.getAsJsonObject();
            String mid = id(str(j,"id",""));
            if (!milestonesSeen.add(mid)) throw new IllegalArgumentException("Duplicate milestone " + mid);
            List<Definition.Criterion> criteria = new ArrayList<>();
            for (JsonElement child : entries(j,"criteria")) {
                JsonObject c = child.getAsJsonObject();
                int n = number(c,"count",1);
                if (n < 1 || n > 1000000) throw new IllegalArgumentException("Bad criterion count");
                criteria.add(new Definition.Criterion(event(str(c,"event","")),
                    id(str(c,"target","")),n));
            }
            if (criteria.isEmpty()) throw new IllegalArgumentException("No criteria for " + mid);
            long xp = j.has("xp") ? j.get("xp").getAsLong() : 0;
            int points = number(j,"points",0);
            if (xp < 0 || xp > 1000000 || points < 0 || points > 10000)
                throw new IllegalArgumentException("Unsafe milestone reward");
            milestones.add(new Definition.Milestone(mid,criteria,strings(j,"after"),xp,points));
        }

        List<Definition.Skill> skills = new ArrayList<>();
        Set<String> skillsSeen = new HashSet<>();
        for (JsonElement el : entries(source,"skills")) {
            JsonObject j = el.getAsJsonObject();
            String sid = id(str(j,"id",""));
            if (!skillsSeen.add(sid)) throw new IllegalArgumentException("Duplicate skill " + sid);
            int max = number(j,"max_rank",1);
            int required = number(j,"required_rank",1);
            int base = number(j,"base_cost",1);
            int step = number(j,"step_cost",0);
            if (max < 1 || max > 100 || required < 1 || required > thresholds.size()
                || base < 0 || step < 0 || (long) base + (long) max * step > Integer.MAX_VALUE)
                throw new IllegalArgumentException("Invalid skill budget for " + sid);
            List<Definition.Effect> effects = new ArrayList<>();
            for (JsonElement effect : entries(j,"effects")) {
                JsonObject f = effect.getAsJsonObject();
                String type = str(f,"type","");
                if (!Set.of("attribute","melee_bonus","damage_multiplier",
                            "break_speed","harvest_tier").contains(type))
                    throw new IllegalArgumentException("Unregistered effect handler: " + type);
                String attribute = str(f,"attribute","");
                String target = str(f,"target","");
                String weapon = str(f,"weapon","");
                String damageTag = str(f,"damage_tag","");
                if (!attribute.isEmpty()) id(attribute);
                if (!target.isEmpty()) id(target);
                if (!weapon.isEmpty()) id(weapon);
                if (!damageTag.isEmpty()) id(damageTag);
                String operation = str(f,"operation","add_value");
                if (!Set.of("add_value","add_multiplied_base","add_multiplied_total").contains(operation))
                    throw new IllegalArgumentException("Bad attribute operation");
                double amount = decimal(f,"per_rank",0);
                int tier = number(f,"tier",0);
                if (!Double.isFinite(amount) || Math.abs(amount) > 10000 || tier < 0 || tier > 5)
                    throw new IllegalArgumentException("Unsafe effect value");
                if (type.equals("attribute") && attribute.isEmpty())
                    throw new IllegalArgumentException("Attribute handler requires attribute");
                if (type.equals("damage_multiplier") && (amount < 0 || amount > 1))
                    throw new IllegalArgumentException("Damage reduction must be in [0,1]");
                effects.add(new Definition.Effect(type,attribute,operation,target,weapon,damageTag,
                    amount,tier,flag(f,"empty_hand"),flag(f,"unarmored")));
            }
            List<Integer> auto = new ArrayList<>();
            for (JsonElement r : entries(j,"auto_ranks")) {
                int at = r.getAsInt();
                if (at < 1 || at > thresholds.size() || (!auto.isEmpty() && at <= auto.getLast()))
                    throw new IllegalArgumentException("auto_ranks must ascend within path ranks");
                auto.add(at);
            }
            if (auto.size() > max || (!auto.isEmpty() && (!strings(j,"requires_skills").isEmpty()
                    || !strings(j,"requires_milestones").isEmpty())))
                throw new IllegalArgumentException("Automatic baseline cannot have paid prerequisites");
            skills.add(new Definition.Skill(sid,max,required,base,step,strings(j,"requires_skills"),
                strings(j,"requires_milestones"),auto,effects));
        }
        for (Definition.Skill skill : skills) {
            for (String needed : skill.requiresSkills())
                if (!skillsSeen.contains(needed))
                    throw new IllegalArgumentException("Unknown required skill " + needed);
            for (String needed : skill.requiresMilestones())
                if (!milestonesSeen.contains(needed))
                    throw new IllegalArgumentException("Unknown required milestone " + needed);
        }
        for (Definition.Milestone m : milestones)
            for (String prereq : m.after())
                if (!milestonesSeen.contains(prereq))
                    throw new IllegalArgumentException("Unknown milestone prerequisite " + prereq);
        return new Definition.Path(pathId,str(source,"exclusive_group",""),
            thresholds,pointsPerRank,activities,milestones,skills);
    }
}
