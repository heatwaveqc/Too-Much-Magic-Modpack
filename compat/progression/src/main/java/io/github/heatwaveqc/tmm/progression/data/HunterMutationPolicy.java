package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Set;

/** Server-reloaded, data-driven Hunter mutation rules (not a Pylons dependency). */
public record HunterMutationPolicy(long targetDurationTicks, float exhaustionPerMutationTick,
                                   int maximumAmplifier, Set<String> effectBlacklist) {
    public HunterMutationPolicy {
        effectBlacklist = Set.copyOf(effectBlacklist);
    }

    public static HunterMutationPolicy parse(JsonObject path) {
        if (!path.has("hunter_mutations")) return null;
        JsonObject json = path.getAsJsonObject("hunter_mutations");
        Set<String> allowed = Set.of("target_duration_ticks",
            "exhaustion_per_mutation_tick","maximum_amplifier","effect_blacklist");
        for (String key : json.keySet())
            if (!allowed.contains(key)) throw new IllegalArgumentException("Unknown hunter_mutations key: " + key);
        for (String key : allowed)
            if (!json.has(key)) throw new IllegalArgumentException("Missing hunter_mutations key: " + key);
        long target = json.get("target_duration_ticks").getAsLong();
        float exhaustion = json.get("exhaustion_per_mutation_tick").getAsFloat();
        int amplifier = json.get("maximum_amplifier").getAsInt();
        if (target < 1 || target > 2_147_483_647L
            || !Float.isFinite(exhaustion) || exhaustion < 0 || exhaustion > 0.05f
            || amplifier < 0 || amplifier > 9)
            throw new IllegalArgumentException("Unsafe Hunter mutation target, hunger cost or potency");
        if (!json.get("effect_blacklist").isJsonArray())
            throw new IllegalArgumentException("effect_blacklist must be an array");
        Set<String> denied = new HashSet<>();
        for (JsonElement element : json.getAsJsonArray("effect_blacklist")) {
            String id = element.getAsString();
            if (!id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
                throw new IllegalArgumentException("Invalid blacklisted effect ID: " + id);
            if (!denied.add(id))
                throw new IllegalArgumentException("Duplicate blacklisted effect ID: " + id);
        }
        if (denied.size() > 2048) throw new IllegalArgumentException("Oversized mutation blacklist");
        return new HunterMutationPolicy(target,exhaustion,amplifier,denied);
    }
    public boolean eligible(String effectId, boolean instantaneous) {
        return !instantaneous && !effectBlacklist.contains(effectId);
    }
}
