package dev.heatwaveqc.tmm.hexpilot;

/** Pure, overflow-safe arithmetic for the opt-in Hex Casting pilot. */
public final class MediaDiscountMath {
    private MediaDiscountMath() {}

    /**
     * Apply a floor-rounded, bounded percentage reduction to the requested media debt.
     * No negative debts or free one-unit casts can be produced by this routine.
     */
    public static long remainingCost(long cost, int percent) {
        if (percent < 0 || percent > 90) {
            throw new IllegalArgumentException("media discount must be between 0 and 90 percent");
        }
        if (cost <= 0) {
            return 0;
        }
        // Avoid cost * percent overflow when cost is close to Long.MAX_VALUE.
        long saved = (cost / 100) * percent + ((cost % 100) * percent) / 100;
        return cost - saved;
    }
}
