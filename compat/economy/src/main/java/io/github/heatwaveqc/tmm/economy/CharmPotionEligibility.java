package io.github.heatwaveqc.tmm.economy;

/** Source-independent charm eligibility: no effect identity, amplifier,
 * provenance or registered base-potion tag can exclude a valid effect. */
public final class CharmPotionEligibility {
    private CharmPotionEligibility() {}

    public static boolean permits(int effectiveEffects, boolean instantaneous, boolean registered) {
        return effectiveEffects == 1 && !instantaneous && registered;
    }

    public static boolean matchingBottles(int count) {
        return count == 3;
    }
}
