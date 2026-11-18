/*
 * Adapted from HaooooZhang/Epiphany, revision 1cbffb31ef4474fec73d66f1a888b4db27c4a71d.
 * src/main/java/ink/myumoon/epiphany/content/condition/builtin/DimensionCondition.java
 * src/main/java/ink/myumoon/epiphany/content/condition/builtin/BiomeCondition.java
 * src/main/java/ink/myumoon/epiphany/content/condition/builtin/AdvancementCondition.java
 * src/main/java/ink/myumoon/epiphany/content/condition/builtin/EffectCondition.java
 * src/main/java/ink/myumoon/epiphany/content/condition/builtin/AttributeCondition.java
 * Original license: MIT; see third_party_licenses/Epiphany.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tmm.progression.foundation;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import java.util.Objects;

import io.github.heatwaveqc.tmm.progression.foundation.Condition.Result;

/**
 * Typed, side-effect-free player predicates adapted from Epiphany.
 * No registration, evaluation loop or path-schema binding in this source batch.
 * Missing definitions return UNAVAILABLE so negation cannot bypass a missing provider.
 */
public final class PlayerConditions {
    private PlayerConditions() {}

    private static Result truth(boolean value) {
        return value ? Result.PASS : Result.FAIL;
    }

    public static Condition<ServerPlayer> dimension(ResourceLocation id) {
        var key = ResourceKey.create(Registries.DIMENSION, Objects.requireNonNull(id));
        return player -> {
            if (player.getServer() == null || player.getServer().getLevel(key) == null)
                return Result.UNAVAILABLE;
            return truth(player.level().dimension().equals(key));
        };
    }

    public static Condition<ServerPlayer> biome(ResourceLocation id, boolean tag) {
        Objects.requireNonNull(id);
        var key = ResourceKey.create(Registries.BIOME, id);
        var tagKey = TagKey.create(Registries.BIOME, id);
        return player -> {
            var registry = player.registryAccess().registryOrThrow(Registries.BIOME);
            Holder<Biome> current = player.level().getBiome(player.blockPosition());
            if (tag) {
                if (registry.getTag(tagKey).isEmpty()) return Result.UNAVAILABLE;
                return truth(current.is(tagKey));
            }
            if (registry.getHolder(key).isEmpty()) return Result.UNAVAILABLE;
            return truth(current.is(key));
        };
    }

    public static Condition<ServerPlayer> advancement(ResourceLocation id) {
        Objects.requireNonNull(id);
        return player -> {
            if (player.getServer() == null) return Result.UNAVAILABLE;
            AdvancementHolder holder = player.getServer().getAdvancements().get(id);
            if (holder == null) return Result.UNAVAILABLE;
            return truth(player.getAdvancements().getOrStartProgress(holder).isDone());
        };
    }

    public static Condition<ServerPlayer> effect(ResourceLocation id, int minAmplifier) {
        Objects.requireNonNull(id);
        if (minAmplifier < 0 || minAmplifier > 255) throw new IllegalArgumentException("Invalid amplifier");
        return player -> {
            var holder = BuiltInRegistries.MOB_EFFECT.getHolder(id);
            if (holder.isEmpty()) return Result.UNAVAILABLE;
            var active = player.getEffect(holder.get());
            return truth(active != null && active.getAmplifier() >= minAmplifier);
        };
    }

    public static Condition<ServerPlayer> attribute(ResourceLocation id, Comparison comparison, double value) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(comparison);
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Nonfinite attribute threshold");
        return player -> {
            var holder = BuiltInRegistries.ATTRIBUTE.getHolder(id);
            if (holder.isEmpty()) return Result.UNAVAILABLE;
            var attr = player.getAttribute(holder.get());
            if (attr == null || !Double.isFinite(attr.getValue())) return Result.UNAVAILABLE;
            return truth(comparison.test(attr.getValue(), value));
        };
    }
}
