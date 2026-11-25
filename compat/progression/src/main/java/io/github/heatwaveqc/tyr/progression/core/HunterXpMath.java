package io.github.heatwaveqc.tyr.progression.core;

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

    /** Unified rate, exact fractional health contribution; central XP system owns the award cap. */
    public static java.math.BigDecimal combatMxp(double hp,double perHp,double multiplier,boolean favored){
        if(!Double.isFinite(hp)||hp<=0||!Double.isFinite(perHp)||perHp<=0||
            !Double.isFinite(multiplier)||multiplier<1)return java.math.BigDecimal.ZERO;
        return java.math.BigDecimal.valueOf(hp).multiply(java.math.BigDecimal.valueOf(perHp))
            .multiply(java.math.BigDecimal.valueOf(favored?multiplier:1));
    }
    public static java.math.BigDecimal alchemyMxp(double baseXp,int difficulty,int amplifier,int ticks,
            double discoveryMultiplier,boolean first){
        if(!Double.isFinite(baseXp)||baseXp<=0||difficulty<1||amplifier<0||ticks<0||
            !Double.isFinite(discoveryMultiplier)||discoveryMultiplier<1)return java.math.BigDecimal.ZERO;
        double potency=1+.5*Math.min(amplifier,10);
        double duration=1+Math.min(1.5,Math.sqrt(ticks/3600.0)/2);
        return java.math.BigDecimal.valueOf(baseXp).multiply(java.math.BigDecimal.valueOf(difficulty))
            .multiply(java.math.BigDecimal.valueOf(potency)).multiply(java.math.BigDecimal.valueOf(duration))
            .multiply(java.math.BigDecimal.valueOf(first?discoveryMultiplier:1))
            .setScale(0,java.math.RoundingMode.HALF_UP);
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
