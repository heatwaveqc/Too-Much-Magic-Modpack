package io.github.heatwaveqc.tyr.progression.core;

/** Pure, shared arithmetic for player-owned reductions to ordinary MXP growth. */
public final class GrowthFactorMath {
    private GrowthFactorMath() {}

    /**
     * Reduce future ordinary award growth without touching saved lifetime levels
     * or accumulated effective XP. A reduced growth factor can never be below 1.
     */
    public static double effective(double configured, double reduction) {
        MxpMath.validateGrowth(configured);
        if (!Double.isFinite(reduction) || reduction < 0)
            throw new IllegalArgumentException("MXP growth reduction must be finite and nonnegative");
        return MxpMath.validateGrowth(Math.max(MxpMath.MIN_GROWTH, configured - reduction));
    }
}
