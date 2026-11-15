package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Strict server-side experience policy. Missing policy disables dynamic XP rather
 * than awarding unverified loot. The registry contains only registered path data.
 */
public record HunterXpPolicy(double healthDivisor, double specialtyMultiplier,
                             long minKillXp, double potionBaseXp,
                             double potionDiscoveryMultiplier,
                             long potionRepeatCooldownTicks,
                             Map<String,Integer> potionBrewingDifficulty) {
    public HunterXpPolicy {
        potionBrewingDifficulty = Map.copyOf(potionBrewingDifficulty);
    }

    public static HunterXpPolicy parse(JsonObject path) {
        if (!path.has("hunter_xp")) return null;
        if (!path.get("hunter_xp").isJsonObject())
            throw new IllegalArgumentException("hunter_xp must be an object");
        JsonObject json = path.getAsJsonObject("hunter_xp");
        Set<String> allowed = Set.of("health_divisor","specialty_multiplier","min_kill_xp",
            "potion_base_xp","potion_discovery_multiplier","potion_repeat_cooldown_ticks",
            "potion_brewing_difficulty");
        for (String key : json.keySet())
            if (!allowed.contains(key)) throw new IllegalArgumentException("Unknown hunter_xp key: " + key);
        for (String required : allowed)
            if (!json.has(required)) throw new IllegalArgumentException("Missing hunter_xp key: " + required);
        double divisor = json.get("health_divisor").getAsDouble();
        double multiplier = json.get("specialty_multiplier").getAsDouble();
        long min = json.get("min_kill_xp").getAsLong();
        double base = json.get("potion_base_xp").getAsDouble();
        double discovery = json.get("potion_discovery_multiplier").getAsDouble();
        long cooldown = json.get("potion_repeat_cooldown_ticks").getAsLong();
        if (!Double.isFinite(divisor) || divisor < 1 || divisor > 100000
            || !Double.isFinite(multiplier) || multiplier < 1 || multiplier > 20
            || min < 0 || min > 1000 || !Double.isFinite(base) || base < 0 || base > 1000
            || !Double.isFinite(discovery) || discovery < 1 || discovery > 20
            || cooldown < 20 || cooldown > 1728000)
            throw new IllegalArgumentException("Unsafe hunter experience policy");
        if (!json.get("potion_brewing_difficulty").isJsonObject())
            throw new IllegalArgumentException("potion_brewing_difficulty must be an object");
        JsonObject weights = json.getAsJsonObject("potion_brewing_difficulty");
        if (weights.size() > 256) throw new IllegalArgumentException("Too many potion difficulties");
        Map<String,Integer> difficulty = new HashMap<>();
        for (Map.Entry<String,JsonElement> entry : weights.entrySet()) {
            String id = entry.getKey();
            if (!id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
                throw new IllegalArgumentException("Invalid potion id: " + id);
            int value = entry.getValue().getAsInt();
            if (value < 1 || value > 16)
                throw new IllegalArgumentException("Potion brewing difficulty out of range: " + id);
            difficulty.put(id,value);
        }
        return new HunterXpPolicy(divisor,multiplier,min,base,discovery,cooldown,difficulty);
    }

    public int difficulty(String potionId) {
        return potionBrewingDifficulty.getOrDefault(potionId,0);
    }
}
