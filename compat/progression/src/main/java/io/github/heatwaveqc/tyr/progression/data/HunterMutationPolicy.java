package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.MutationPowerPolicy;
import java.util.HashSet;
import java.util.Set;

/** Server-reloaded policy. Vanilla defaults to allow; other namespaces require explicit opt-in. */
public record HunterMutationPolicy(long targetDurationTicks, float exhaustionPerMutationTick,
                                   int maximumAmplifier, Set<String> effectBlacklist,
                                   Set<String> moddedEffectWhitelist) {
    public HunterMutationPolicy {
        effectBlacklist = Set.copyOf(effectBlacklist);
        moddedEffectWhitelist = Set.copyOf(moddedEffectWhitelist);
    }
    /** Source compatibility for old callers and legacy datapacks. */
    public HunterMutationPolicy(long target,float exhaustion,int amplifier,Set<String> blacklist){
        this(target,exhaustion,amplifier,blacklist,Set.of());
    }
    public static HunterMutationPolicy parse(JsonObject path) {
        if (!path.has("hunter_mutations")) return null;
        JsonObject json = path.getAsJsonObject("hunter_mutations");
        Set<String> required = Set.of("target_duration_ticks","exhaustion_per_mutation_tick","maximum_amplifier");
        Set<String> allowed = Set.of("target_duration_ticks","exhaustion_per_mutation_tick","maximum_amplifier",
            "effect_blacklist","vanilla_effect_blacklist","modded_effect_whitelist");
        for (String key : json.keySet())
            if (!allowed.contains(key)) throw new IllegalArgumentException("Unknown hunter_mutations key: " + key);
        for (String key : required)
            if (!json.has(key)) throw new IllegalArgumentException("Missing hunter_mutations key: " + key);
        if(json.has("effect_blacklist")==json.has("vanilla_effect_blacklist"))
            throw new IllegalArgumentException("Supply exactly one of vanilla_effect_blacklist or legacy effect_blacklist");
        long target = json.get("target_duration_ticks").getAsLong();
        float exhaustion = json.get("exhaustion_per_mutation_tick").getAsFloat();
        int amplifier = json.get("maximum_amplifier").getAsInt();
        if (target < 1 || target > 2_147_483_647L
            || !Float.isFinite(exhaustion) || exhaustion < 0 || exhaustion > 0.05f
            || amplifier < 0 || amplifier > 9)
            throw new IllegalArgumentException("Unsafe Hunter mutation target, hunger cost or potency");
        boolean legacy=json.has("effect_blacklist");
        Set<String> denied=readIds(json,legacy?"effect_blacklist":"vanilla_effect_blacklist");
        Set<String> allowedMods=readIds(json,"modded_effect_whitelist");
        if(!legacy&&denied.stream().anyMatch(id->!id.startsWith("minecraft:")))
            throw new IllegalArgumentException("vanilla_effect_blacklist accepts only minecraft IDs");
        if(allowedMods.stream().anyMatch(id->id.startsWith("minecraft:")))
            throw new IllegalArgumentException("modded_effect_whitelist accepts only non-minecraft IDs");
        return new HunterMutationPolicy(target,exhaustion,amplifier,denied,allowedMods);
    }
    private static Set<String> readIds(JsonObject json,String key){
        if(!json.has(key))return Set.of();
        if(!json.get(key).isJsonArray())throw new IllegalArgumentException(key+" must be an array");
        Set<String> ids=new HashSet<>();
        for(JsonElement e:json.getAsJsonArray(key)){
            if(!e.isJsonPrimitive()||!e.getAsJsonPrimitive().isString())
                throw new IllegalArgumentException("Invalid effect ID in "+key);
            String id=e.getAsString();
            if(!MutationPowerPolicy.validId(id)||!ids.add(id))
                throw new IllegalArgumentException("Invalid or duplicate effect ID in "+key+": "+id);
        }
        if(ids.size()>2048)throw new IllegalArgumentException("Oversized "+key);
        return ids;
    }
    public boolean eligible(String effectId, boolean instantaneous) {
        return !instantaneous && MutationPowerPolicy.validId(effectId) && !effectBlacklist.contains(effectId)
            && (effectId.startsWith("minecraft:")||moddedEffectWhitelist.contains(effectId));
    }
}
