package io.github.heatwaveqc.tmm.progression.core;

/** Pure deterministic XP math; rounds to whole path XP units. */
public final class HunterXpMath {
    private HunterXpMath() {}
    // A single extreme modded health attribute must not overflow or skip the whole progression.
    private static final long MAX_SINGLE_AWARD = 1_000_000L;

    /** HP at death, divided by configured scalar, doubled once for chosen specialty. */
    public static long killXp(double maximumHealth, double divisor, double specialtyMultiplier,
                              long minimumXp, boolean matchesSpecialty) {
        if (!Double.isFinite(maximumHealth) || maximumHealth <= 0 || divisor <= 0) return 0;
        double raw = (maximumHealth / divisor) * (matchesSpecialty ? specialtyMultiplier : 1);
        return Math.min(MAX_SINGLE_AWARD, Math.max(minimumXp, Math.round(raw)));
    }

    /**
     * Potency is measured in amplifier levels; duration is the longest potion effect.
     * Diminishing returns on long potions prevent 8-minute variants dominating the tree.
     * Brewing difficulty is a curated registry score, not player-provided item NBT.
     */
    public static long potionXp(double baseXp, int brewingDifficulty, int highestAmplifier,
                                int longestDurationTicks, double discoveryMultiplier,
                                boolean firstTime) {
        if (baseXp <= 0 || brewingDifficulty < 1 || highestAmplifier < 0
                || longestDurationTicks < 0 || !Double.isFinite(baseXp)) return 0;
        double potency = 1.0 + 0.5 * Math.min(highestAmplifier,10);
        double duration = 1.0 + Math.min(1.5,Math.sqrt(longestDurationTicks / 3600.0) / 2.0);
        double raw = baseXp * brewingDifficulty * potency * duration
            * (firstTime ? discoveryMultiplier : 1.0);
        return Math.min(MAX_SINGLE_AWARD,Math.max(1,Math.round(raw)));
    }
}
