package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * Saved on each player, copied on death. Progress keys are effectId + "@" + zero-based
 * amplifier; unlocked keys are just effect IDs, so upgrading a mutation costs no
 * second ongoing hunger tax. Never trust this state from a client packet.
 */
public record HunterMutationState(Map<String,Long> progress, Map<String,Integer> unlocked) {
    public static final Codec<HunterMutationState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.unboundedMap(Codec.STRING,Codec.LONG).fieldOf("progress").forGetter(HunterMutationState::progress),
        Codec.unboundedMap(Codec.STRING,Codec.INT).fieldOf("unlocked").forGetter(HunterMutationState::unlocked)
    ).apply(instance,HunterMutationState::new));

    public HunterMutationState {
        progress = Map.copyOf(progress);
        unlocked = Map.copyOf(unlocked);
    }

    public static HunterMutationState empty() {
        return new HunterMutationState(Map.of(),Map.of());
    }

    public long progress(String effectId, int amplifier) {
        return progress.getOrDefault(effectId + "@" + amplifier,0L);
    }

    public HunterMutationState credit(String effectId, int amplifier, long duration, long threshold) {
        if (duration <= 0 || threshold <= 0 || amplifier < 0) return this;
        String key = effectId + "@" + amplifier;
        long old = Math.max(0, progress.getOrDefault(key,0L));
        long next = Math.min(threshold,old > threshold - Math.min(threshold,duration)
            ? threshold : old + duration);
        if (next == old) return this;
        Map<String,Long> progressNext = new HashMap<>(progress);
        progressNext.put(key,next);
        Map<String,Integer> unlockedNext = new HashMap<>(unlocked);
        if (next == threshold && amplifier > unlockedNext.getOrDefault(effectId,-1))
            unlockedNext.put(effectId,amplifier);
        return new HunterMutationState(progressNext,unlockedNext);
    }
}
