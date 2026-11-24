package io.github.heatwaveqc.tmm.economy;

/**
 * Source-independent admission rule for Apotheosis's three-potion charm recipe.
 * Registered base potions retain their native eligibility; only custom-only
 * modded effects require affirmative data-tag vetting.
 */
public final class CharmPotionEligibility {
    private CharmPotionEligibility() {}

    /** Vanilla Resistance V+ (amplifier >=4) is effectively invulnerable. */
    public static boolean privilegedAmplifier(String effectId, int amplifier) {
        return "minecraft:resistance".equals(effectId) && amplifier >= 4;
    }

    public static boolean permits(
            int effectiveEffects,
            boolean instantaneous,
            boolean nativeBaseAllowed,
            boolean customOnly,
            boolean knownOrdinaryEffect,
            boolean deniedEffect,
            boolean ordinaryPotionCarrier,
            boolean safeComponents) {
        return effectiveEffects == 1
                && !instantaneous
                && nativeBaseAllowed
                && (!customOnly || knownOrdinaryEffect)
                && !deniedEffect
                && ordinaryPotionCarrier
                && safeComponents;
    }
}
