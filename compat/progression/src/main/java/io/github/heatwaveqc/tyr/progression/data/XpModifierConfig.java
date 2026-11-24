package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Kind;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Modifier;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Operation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Optional declarative rules in economy.json; no client-supplied eligibility. */
public final class XpModifierConfig {
    private XpModifierConfig() {}
    public static final int MAX_RULES = 4096;

    public record Rule(Kind kind, String system, String source, String path,
                       String skill, int minRank, Integer effectIndex, String achievement,
                       boolean includeFixed, boolean perRank, Modifier modifier) {
        public boolean targets(String systemId, String sourceId, boolean fixed) {
            return (system.equals("*") || system.equals(systemId))
                    && (source.equals("*") || source.equals(sourceId))
                    && (!fixed || includeFixed);
        }

        public Modifier contribution(int rank) {
            if (!perRank) return modifier;
            double scaled = modifier.value() * rank;
            scaled = Math.max(-XpModifierMath.MAX_REWARD_MULTIPLIER,
                    Math.min(XpModifierMath.MAX_REWARD_MULTIPLIER, scaled));
            return new Modifier(modifier.id(), modifier.priority(), modifier.stack(),
                    modifier.operation(), scaled, modifier.minimum(), modifier.maximum());
        }
    }

    public static List<Rule> read(JsonObject root, Set<String> systems) {
        if (!root.has("modifiers")) return List.of();
        var rows = root.getAsJsonArray("modifiers");
        if (rows.size() > MAX_RULES) throw new IllegalArgumentException("Too many XP modifiers");
        var result = new ArrayList<Rule>();
        var ids = new HashSet<String>();
        for (JsonElement row : rows) {
            JsonObject j = row.getAsJsonObject();
            Set<String> allowed = Set.of("id", "kind", "system", "source", "path", "skill",
                    "min_rank", "effect_index", "achievement", "include_fixed", "per_rank",
                    "operation", "value", "priority", "stack", "minimum", "maximum");
            for (String key : j.keySet()) if (!allowed.contains(key))
                throw new IllegalArgumentException("Unknown XP modifier field " + key);
            String id = id(j, "id", null, false);
            if (!ids.add(id)) throw new IllegalArgumentException("Duplicate XP modifier " + id);
            Kind kind = switch (string(j, "kind", null)) {
                case "reward" -> Kind.REWARD;
                case "growth" -> Kind.GROWTH;
                default -> throw new IllegalArgumentException("Unknown XP modifier kind");
            };
            String system = id(j, "system", "*", true);
            if (!system.equals("*") && !systems.contains(system))
                throw new IllegalArgumentException("Unknown XP modifier system " + system);
            String source = id(j, "source", "*", true);
            String path = id(j, "path", "", false);
            String skill = id(j, "skill", "", false);
            String achievement = id(j, "achievement", "", false);
            int minRank = integer(j, "min_rank", 1);
            Integer index = j.has("effect_index") ? integer(j, "effect_index", -1) : null;
            if (path.isEmpty() && (!skill.isEmpty() || index != null)
                    || skill.isEmpty() && (j.has("min_rank") || index != null)
                    || minRank < 1 || minRank > 1_000_000
                    || index != null && (index < 0 || index > 10_000))
                throw new IllegalArgumentException("Invalid XP modifier eligibility");
            if (path.isEmpty() && achievement.isEmpty())
                throw new IllegalArgumentException("XP modifier requires a Path or achievement");
            Operation operation = switch (string(j, "operation", null)) {
                case "add" -> Operation.ADD;
                case "multiply" -> Operation.MULTIPLY;
                case "replace" -> Operation.REPLACE;
                default -> throw new IllegalArgumentException("Unknown XP modifier operation");
            };
            boolean perRank = bool(j, "per_rank", false);
            boolean fixed = bool(j, "include_fixed", false);
            if (perRank && (skill.isEmpty() || operation != Operation.ADD)
                    || fixed && kind == Kind.GROWTH)
                throw new IllegalArgumentException("Invalid rank scaling or fixed growth policy");
            int priority = integer(j, "priority", 0);
            if (priority < -1_000_000 || priority > 1_000_000)
                throw new IllegalArgumentException("XP modifier priority outside safety bounds");
            double value = number(j, "value");
            Double min = j.has("minimum") ? number(j, "minimum") : null;
            Double max = j.has("maximum") ? number(j, "maximum") : null;
            if (min != null && (min < XpModifierMath.lower(kind) || min > XpModifierMath.upper(kind))
                    || max != null && (max < XpModifierMath.lower(kind) || max > XpModifierMath.upper(kind)))
                throw new IllegalArgumentException("XP modifier bounds outside kind limits");
            var modifier = new Modifier(id, priority, bool(j, "stack", false),
                    operation, value, min, max);
            result.add(new Rule(kind, system, source, path, skill, minRank, index,
                    achievement, fixed, perRank, modifier));
        }
        return List.copyOf(result);
    }

    private static String id(JsonObject j, String key, String fallback, boolean wildcard) {
        String value = string(j, key, fallback);
        if (value.isEmpty() && "".equals(fallback) || wildcard && value.equals("*")) return value;
        if (value.length() > 240 || !value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid XP modifier identifier " + key);
        return value;
    }
    private static String string(JsonObject j, String key, String fallback) {
        if (!j.has(key)) {
            if (fallback == null) throw new IllegalArgumentException("Missing XP modifier field " + key);
            return fallback;
        }
        var v = j.get(key);
        if (!v.isJsonPrimitive() || !v.getAsJsonPrimitive().isString())
            throw new IllegalArgumentException("Expected string " + key);
        return v.getAsString();
    }
    private static boolean bool(JsonObject j, String key, boolean fallback) {
        if (!j.has(key)) return fallback;
        var v = j.get(key);
        if (!v.isJsonPrimitive() || !v.getAsJsonPrimitive().isBoolean())
            throw new IllegalArgumentException("Expected boolean " + key);
        return v.getAsBoolean();
    }
    private static int integer(JsonObject j, String key, int fallback) {
        if (!j.has(key)) return fallback;
        var v = j.get(key);
        if (!v.isJsonPrimitive() || !v.getAsJsonPrimitive().isNumber())
            throw new IllegalArgumentException("Expected integer " + key);
        return v.getAsBigDecimal().intValueExact();
    }
    private static double number(JsonObject j, String key) {
        if (!j.has(key) || !j.get(key).isJsonPrimitive() || !j.get(key).getAsJsonPrimitive().isNumber())
            throw new IllegalArgumentException("Expected number " + key);
        double value = j.get(key).getAsDouble();
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Nonfinite XP modifier " + key);
        return value;
    }
}
