package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.DeparturePolicy;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.PathSystem;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy.SystemSpec;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Strict JSON parser for the generic economy's configuration vocabulary.
 *
 * PathRegistry installs the validated catalog for the runtime. Verified
 * source routing remains server-owned.
 * No slot count, magic-only mode or hard-coded Path ID is assumed here.
 */
public final class ExperienceEconomyConfig {
    private ExperienceEconomyConfig() {}

    public record SkillKey(String path, String skill) {}

    public record PurchaseSource(String system, int maxRanks, int pointsPerRank) {
        public PurchaseSource {
            if (maxRanks < 1 || maxRanks > ExperienceEconomy.MAX_PAID_RANK_RECEIPTS
                    || pointsPerRank < 1 || pointsPerRank > 1_000_000)
                throw new IllegalArgumentException("Invalid purchase source caps or point cost");
        }
    }

    public record SkillPurchases(int totalRankCap, List<PurchaseSource> sources) {
        public SkillPurchases {
            sources = List.copyOf(sources);
            if (totalRankCap < 1 || totalRankCap > ExperienceEconomy.MAX_PAID_RANK_RECEIPTS
                    || sources.isEmpty())
                throw new IllegalArgumentException("Skill needs valid rank cap and purchase sources");
        }
    }

    public record Transition(DeparturePolicy onLeave, DeparturePolicy onRespec,
                             boolean wipeUnallocated) {}

    public record Catalog(Map<String, SystemSpec> systems, Map<String, Double> defaultGrowth,
                          Transition defaultTransition, Map<PathSystem, Transition> pathChanges,
                          Map<SkillKey, SkillPurchases> skills) {
        public Catalog {
            systems = Map.copyOf(systems);
            defaultGrowth = Map.copyOf(defaultGrowth);
            pathChanges = Map.copyOf(pathChanges);
            skills = Map.copyOf(skills);
        }
        public Transition policy(String path, String system) {
            return pathChanges.getOrDefault(new PathSystem(path, system), defaultTransition);
        }
        public double growth(String system) {
            Double value = defaultGrowth.get(system);
            if (value == null) throw new IllegalArgumentException("Unknown XP system " + system);
            return value;
        }
    }

    /** Bootstrap only; active pack values are loaded from the datapack on reload. */
    public static Catalog tmmDefault() {
        var mxp=SystemSpec.mxp();
        return new Catalog(Map.of(mxp.id(),mxp),Map.of(mxp.id(),1.02),
                new Transition(DeparturePolicy.FORFEIT,DeparturePolicy.REFUND,true),
                Map.of(),Map.of());
    }

    public static Catalog read(JsonObject root) {
        only(root, "schema", "systems", "default_path_change", "path_changes", "skills");
        if (root.has("schema") && root.get("schema").getAsBigDecimal().intValueExact() != 1)
            throw new IllegalArgumentException("Unsupported economy schema");
        if (!root.has("systems")) throw new IllegalArgumentException("Missing XP systems");
        Map<String, SystemSpec> systems = new HashMap<>();
        Map<String, Double> growth = new HashMap<>();
        for (JsonElement element : root.getAsJsonArray("systems")) {
            JsonObject j = element.getAsJsonObject();
            only(j, "id", "xp_per_point", "growth_factor", "max_level", "max_base_award",
                    "min_reward", "round_subminimum_up");
            String id = required(j, "id");
            BigDecimal threshold = number(j, "xp_per_point", "1000");
            BigDecimal awardCap = number(j, "max_base_award", "1e12");
            long maxLevel = j.has("max_level")
                    ? j.get("max_level").getAsBigDecimal().longValueExact()
                    : ExperienceEconomy.HARD_MAX_LEVEL;
            BigDecimal minReward = number(j, "min_reward", ExperienceEconomy.HARD_MIN_REWARD.toString());
            boolean roundUp = false;
            if (j.has("round_subminimum_up")) {
                var value = j.get("round_subminimum_up");
                if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isBoolean())
                    throw new IllegalArgumentException("round_subminimum_up must be boolean");
                roundUp = value.getAsBoolean();
            }
            var spec = new SystemSpec(id, threshold, maxLevel, awardCap, minReward, roundUp);
            double base = ExperienceEconomy.validateGrowth(
                    j.has("growth_factor") ? j.get("growth_factor").getAsDouble() : 1.02);
            if (systems.putIfAbsent(id, spec) != null)
                throw new IllegalArgumentException("Duplicate XP system " + id);
            growth.put(id, base);
        }
        if (systems.isEmpty()) throw new IllegalArgumentException("No XP systems declared");

        Transition fallback = root.has("default_path_change")
                ? transition(root.getAsJsonObject("default_path_change"), null)
                : new Transition(DeparturePolicy.RETAIN, DeparturePolicy.REFUND, false);

        Map<PathSystem, Transition> changes = new HashMap<>();
        if (root.has("path_changes")) for (JsonElement element : root.getAsJsonArray("path_changes")) {
            JsonObject j = element.getAsJsonObject();
            only(j, "path", "system", "on_leave", "on_respec", "wipe_unallocated");
            String path = required(j, "path");
            String system = required(j, "system");
            if (!systems.containsKey(system))
                throw new IllegalArgumentException("Unknown Path policy XP system " + system);
            var key = new PathSystem(path, system);
            Transition rule = transition(j, fallback);
            if (changes.putIfAbsent(key, rule) != null)
                throw new IllegalArgumentException("Duplicate Path XP policy " + key);
        }

        Map<SkillKey, SkillPurchases> skills = new HashMap<>();
        if (root.has("skills")) for (JsonElement element : root.getAsJsonArray("skills")) {
            JsonObject j = element.getAsJsonObject();
            only(j, "path", "skill", "total_rank_cap", "purchases");
            String path = required(j, "path");
            String skill = required(j, "skill");
            if (!j.has("purchases")) throw new IllegalArgumentException("Missing skill purchase sources");
            JsonArray sources = j.getAsJsonArray("purchases");
            var purchases = new ArrayList<PurchaseSource>();
            var seen = new HashSet<String>();
            for (JsonElement purchase : sources) {
                JsonObject p = purchase.getAsJsonObject();
                only(p, "system", "max_ranks", "points_per_rank");
                String system = required(p, "system");
                if (!systems.containsKey(system))
                    throw new IllegalArgumentException("Unknown purchase XP system " + system);
                if (!seen.add(system)) throw new IllegalArgumentException(
                        "Duplicate system for skill purchase " + system);
                int max = p.get("max_ranks").getAsBigDecimal().intValueExact();
                int cost = p.has("points_per_rank") ? p.get("points_per_rank").getAsBigDecimal().intValueExact() : 1;
                purchases.add(new PurchaseSource(system, max, cost));
            }
            int cap = j.get("total_rank_cap").getAsBigDecimal().intValueExact();
            var key = new SkillKey(path, skill);
            if (skills.putIfAbsent(key, new SkillPurchases(cap, purchases)) != null)
                throw new IllegalArgumentException("Duplicate configured skill " + key);
        }
        return new Catalog(systems, growth, fallback, changes, skills);
    }

    private static Transition transition(JsonObject j, Transition inherited) {
        only(j, "path", "system", "on_leave", "on_respec", "wipe_unallocated");
        return new Transition(
                j.has("on_leave") ? policy(j.get("on_leave").getAsString())
                        : inherited == null ? DeparturePolicy.RETAIN : inherited.onLeave(),
                j.has("on_respec") ? policy(j.get("on_respec").getAsString())
                        : inherited == null ? DeparturePolicy.REFUND : inherited.onRespec(),
                j.has("wipe_unallocated") ? j.get("wipe_unallocated").getAsBoolean()
                        : inherited != null && inherited.wipeUnallocated());
    }

    private static DeparturePolicy policy(String value) {
        return switch (value) {
            case "retain" -> DeparturePolicy.RETAIN;
            case "refund" -> DeparturePolicy.REFUND;
            case "forfeit" -> DeparturePolicy.FORFEIT;
            default -> throw new IllegalArgumentException("Unknown Path transition policy " + value);
        };
    }

    private static String required(JsonObject j, String key) {
        if (!j.has(key)) throw new IllegalArgumentException("Missing " + key);
        String id = j.get(key).getAsString();
        if (!id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid namespaced " + key + ": " + id);
        return id;
    }

    private static BigDecimal number(JsonObject j, String key, String fallback) {
        return j.has(key) ? j.get(key).getAsBigDecimal() : new BigDecimal(fallback);
    }

    private static void only(JsonObject object, String... fields) {
        Set<String> allowed = Set.of(fields);
        for (String key : object.keySet())
            if (!allowed.contains(key))
                throw new IllegalArgumentException("Unknown economy configuration field " + key);
    }
}
