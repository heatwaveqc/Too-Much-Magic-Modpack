package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.github.heatwaveqc.tmm.progression.runtime.EffectHandlers;
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
    private static String selector(String text) {
        return "*".equals(text) ? text : id(text);
    }
    private static String event(String text) {
        if (!Set.of("kill", "unarmed_kill", "block_break", "barehand_block_break", "external", "food_consumed", "food_discovered", "animal_petted", "advancement", "item_crafted").contains(text))
            throw new IllegalArgumentException("Unsupported event " + text);
        return text;
    }

    public static Definition.Path read(String pathId, JsonObject source) {
        id(pathId);
        if (number(source,"schema",0) != 1) throw new IllegalArgumentException("schema must be 1");
        if (!Set.of("admin","manual","sunset","potion","automatic","food").contains(str(source,"enrollment","admin")))
            throw new IllegalArgumentException("Unknown enrollment mechanism");
        FoodRiteSpec.parse(source);
        PotionEnrollmentTrigger.parse(source); // fail closed before path is accepted
        HunterXpPolicy.parse(source); // strictly validate any dynamic XP policy
        HunterMutationPolicy.parse(source); // blacklist, hunger and target must be safe
        List<Long> thresholds = new ArrayList<>();
        for (JsonElement el : entries(source,"rank_xp")) {
            long n = el.getAsLong();
            if (n < 0 || (!thresholds.isEmpty() && n <= thresholds.getLast()))
                throw new IllegalArgumentException("rank_xp must be strictly increasing");
            thresholds.add(n);
        }
        if (thresholds.isEmpty() || thresholds.size() > 200 || thresholds.getFirst() != 0)
            throw new IllegalArgumentException("rank_xp must start at 0 and contain 1..200 entries");
        int declaredMax = number(source,"max_level",thresholds.size());
        if (declaredMax != thresholds.size())
            throw new IllegalArgumentException("max_level must equal rank_xp length");
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
                selector(str(j,"target","")),xp,cooldown));
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
                    selector(str(c,"target","")),n));
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
            List<Integer> prices=new ArrayList<>();
            for(var cost:entries(j,"rank_costs")){
                int n=cost.getAsBigDecimal().intValueExact();
                if(n<0)throw new IllegalArgumentException("Negative rank price");
                prices.add(n);
            }
            if(j.has("rank_costs")&&(prices.size()!=max||j.has("base_cost")||j.has("step_cost")))
                throw new IllegalArgumentException("rank_costs must cover every rank and replace linear cost");
            List<Integer> rankRequirements=new ArrayList<>();
            for(var need:entries(j,"rank_level_requirements")){
                int n=need.getAsBigDecimal().intValueExact();
                if(n<1||n>thresholds.size()||!rankRequirements.isEmpty()&&n<rankRequirements.getLast())
                    throw new IllegalArgumentException("Invalid rank level gate");
                rankRequirements.add(n);
            }
            if(j.has("rank_level_requirements")&&(rankRequirements.size()!=max||j.has("required_rank")))
                throw new IllegalArgumentException("rank_level_requirements must cover all ranks and replace required_rank");
            List<Definition.Effect> effects = new ArrayList<>();
            for (JsonElement effect : entries(j,"effects")) {
                JsonObject f = effect.getAsJsonObject();
                String type = str(f,"type","");
                if (!EffectHandlers.known(type))
                    throw new IllegalArgumentException("Unregistered effect handler: " + type);
                String attribute = str(f,"attribute","");
                String target = str(f,"target","");
                String weapon = str(f,"weapon","");
                String damageTag = str(f,"damage_tag","");
                if (!attribute.isEmpty()) id(attribute);
                if (!target.isEmpty()) selector(target);
                if (!weapon.isEmpty()) selector(weapon);
                if (!damageTag.isEmpty()) selector(damageTag);
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
                if (type.equals("evasion") && (!target.startsWith("#") || amount < 0 || amount > 0.75))
                    throw new IllegalArgumentException("Evasion requires a quarry entity tag and a chance in [0,0.75]");
                if (type.equals("hunter_specialty") && (!target.startsWith("#") || !weapon.isEmpty()))
                    throw new IllegalArgumentException("Hunter specialty requires a creature category tag");
                if (type.equals("fast_hands") && (weapon.isEmpty() || amount < 0 || amount > 2))
                    throw new IllegalArgumentException("Fast Hands requires a weapon ID/tag and bounded speed bonus");
                Map<String,String> params = new HashMap<>();
                if (f.has("params")) {
                    f.getAsJsonObject("params").entrySet().forEach(entry -> {
                        String value = entry.getValue().getAsString();
                        if (entry.getKey().length() > 80 || value.length() > 1024)
                            throw new IllegalArgumentException("Effect parameter too long");
                        params.put(entry.getKey(),value);
                    });
                }
                if (type.equals("gear_durability")) {
                    String slot = params.getOrDefault("slot","mainhand");
                    String action = params.getOrDefault("action","repair");
                    int interval;
                    try { interval = Integer.parseInt(params.getOrDefault("interval_ticks","20")); }
                    catch (NumberFormatException bad) { throw new IllegalArgumentException("Invalid gear interval"); }
                    if (!Set.of("mainhand","offhand","head","chest","legs","feet","armor","all").contains(slot)
                            || !Set.of("repair","damage").contains(action) || interval < 1 || interval > 72000
                            || amount < 0 || amount > 64)
                        throw new IllegalArgumentException("Unsafe gear durability effect");
                }
                if (type.equals("damage_bonus") &&
                    !Set.of("melee","arrow","any").contains(params.getOrDefault("delivery","melee")))
                    throw new IllegalArgumentException("Damage delivery must be melee, arrow or any");
                List<Double> rankValues=new ArrayList<>();
                for(var value:entries(f,"rank_values")){
                    double n=value.getAsDouble();
                    if(!Double.isFinite(n)||Math.abs(n)>1000000)throw new IllegalArgumentException("Unsafe rank value");
                    rankValues.add(n);
                }
                if(f.has("rank_values")&&(rankValues.size()!=max||f.has("per_rank")))
                    throw new IllegalArgumentException("rank_values must cover every rank and replace per_rank");
                var parsedEffect=new Definition.Effect(type,attribute,operation,target,weapon,damageTag,
                    amount,tier,flag(f,"empty_hand"),flag(f,"unarmored"),params,rankValues);
                if(rankValues.isEmpty())MechanicalSpec.validate(parsedEffect);
                else{
                    // Only handlers whose numeric contribution is explicitly rank-scaled opt into this schema.
                    if(!Set.of("attribute","melee_bonus","damage_bonus","damage_multiplier","break_speed",
                        "magnet","heal_on_kill","heal_on_hit","critical_hit","status_on_hit","status_on_hurt",
                        "damage_at_low_health","food_efficiency","vanilla_xp_bonus","crop_bonus","ordinary_drop_bonus",
                        "virtual_fortune","virtual_looting","magic_cost_discount","hunger_efficiency",
                        "jump_bonus","fall_reduction","delayed_regeneration","lava_vision","experience_gain_bonus","glide_control_bonus","backstab","counter_attack","breeding_cooldown","baby_growth","bonemeal_growth","projectile_deflection").contains(type))
                        throw new IllegalArgumentException("rank_values not supported for "+type);
                    for(int rank=1;rank<=max;rank++){
                        var effective=parsedEffect.atRank(rank);
                        if(Math.abs(effective.amountPerRank())>10000||
                            type.equals("damage_multiplier")&&(effective.amountPerRank()<0||effective.amountPerRank()>1))
                            throw new IllegalArgumentException("Unsafe rank value for "+type);
                        MechanicalSpec.validate(effective);
                    }
                }
                effects.add(parsedEffect);
            }
            List<Integer> auto = new ArrayList<>();
            for (JsonElement r : entries(j,"auto_ranks")) {
                int at = r.getAsInt();
                if (at < 1 || at > thresholds.size() || (!auto.isEmpty() && at <= auto.getLast()))
                    throw new IllegalArgumentException("auto_ranks must ascend within path ranks");
                auto.add(at);
            }
            String choiceGroup = str(j,"choice_group","");
            if (!choiceGroup.isEmpty()) id(choiceGroup);
            List<String> anySkills = strings(j,"requires_any_skills");
            if (auto.size() > max || (!auto.isEmpty() && (!strings(j,"requires_skills").isEmpty()
                    || !anySkills.isEmpty() || !strings(j,"requires_milestones").isEmpty()
                    || !choiceGroup.isEmpty())))
                throw new IllegalArgumentException("Automatic baseline cannot have paid prerequisites or choices");
            skills.add(new Definition.Skill(sid,max,required,base,step,strings(j,"requires_skills"),
                anySkills, strings(j,"requires_milestones"),choiceGroup,auto,effects,prices,rankRequirements));
        }
        for (Definition.Skill skill : skills) {
            for (String needed : skill.requiresSkills()) {
                if (!skillsSeen.contains(needed))
                    throw new IllegalArgumentException("Unknown required skill " + needed);
                if (!skill.choiceGroup().isEmpty() && skills.stream().anyMatch(other ->
                    other.id().equals(needed) && other.choiceGroup().equals(skill.choiceGroup())))
                    throw new IllegalArgumentException("Choice cannot require mutually exclusive skill " + needed);
            }
            for (String needed : skill.requiresAnySkills())
                if (!skillsSeen.contains(needed))
                    throw new IllegalArgumentException("Unknown alternative required skill " + needed);
            if (!skill.choiceGroup().isEmpty() && !skill.requiresAnySkills().isEmpty() &&
                skill.requiresAnySkills().stream().allMatch(needed -> skills.stream().anyMatch(other ->
                    other.id().equals(needed) && other.choiceGroup().equals(skill.choiceGroup()))))
                throw new IllegalArgumentException("All alternatives mutually exclusive with " + skill.id());
            if (!skill.choiceGroup().isEmpty() &&
                    skill.requiresSkills().contains(skill.id()))
                throw new IllegalArgumentException("Self-required skill " + skill.id());
            for (String needed : skill.requiresMilestones())
                if (!milestonesSeen.contains(needed))
                    throw new IllegalArgumentException("Unknown required milestone " + needed);
        }
        for (Definition.Milestone m : milestones)
            for (String prereq : m.after())
                if (!milestonesSeen.contains(prereq))
                    throw new IllegalArgumentException("Unknown milestone prerequisite " + prereq);
        // Fail closed on circular dependency graphs, even via alternative branches.
        Map<String,Integer> visits = new HashMap<>();
        for (Definition.Skill skill : skills) checkAcyclic(skill.id(),skills,visits);
        return new Definition.Path(pathId,str(source,"exclusive_group",""),
            thresholds,pointsPerRank,activities,milestones,skills);
    }
    private static void checkAcyclic(String id, List<Definition.Skill> skills,
                                     Map<String,Integer> visits) {
        int visit = visits.getOrDefault(id,0);
        if (visit == 2) return;
        if (visit == 1) throw new IllegalArgumentException("Cyclic skill prerequisites at " + id);
        visits.put(id,1);
        for (Definition.Skill skill : skills) {
            if (!skill.id().equals(id)) continue;
            for (String prerequisite : skill.requiresSkills())
                checkAcyclic(prerequisite,skills,visits);
            for (String prerequisite : skill.requiresAnySkills())
                checkAcyclic(prerequisite,skills,visits);
        }
        visits.put(id,2);
    }

}
