package io.github.heatwaveqc.tmm.progression.core;

/** Pure probability calculation for adding virtual Unbreaking BEFORE vanilla wear prevention. */
public final class GildedWearMath {
    private GildedWearMath() {}

    /**
     * Vanilla Unbreaking is still applied after this prefilter.
     * For armor, vanilla protects only 40% of the damage rolls; other 60% bypass enchantment.
     * Probability multiplication yields the expected combined level (real + virtual).
     */
    public static double keepProbability(int actual, int virtual, boolean armor) {
        if (virtual <= 0) return 1.0;
        if (actual < 0) throw new IllegalArgumentException("actual Unbreaking level");
        double before = armor ? 0.6 + 0.4 / (actual + 1.0) : 1.0 / (actual + 1.0);
        double after = armor ? 0.6 + 0.4 / (actual + virtual + 1.0)
                             : 1.0 / (actual + virtual + 1.0);
        return Math.min(1.0,after / before);
    }
}
