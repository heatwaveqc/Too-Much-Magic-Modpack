package io.github.heatwaveqc.tmm.mxp.core;

/**
 * Pure progression arithmetic, independent of Minecraft and Tyz.
 * 1000 effective XP buys one level; routine BASE XP is divided by 1.2^P.
 * P is the player's lifetime levels earned through MXP, not spent skill ranks.
 */
public final class MxpMath {
    public static final double GOAL = 1000.0;
    public static final double GROWTH = 1.2;
    public static final double MAX_EVENT_BASE = 1.0e12;
    private static final int MAX_LEVELS_PER_AWARD = 100_000;

    private MxpMath() {}

    public record Counter(long lifetimeLevels, double progress) {
        public Counter {
            if (lifetimeLevels < 0 || !Double.isFinite(progress)
                    || progress < 0 || progress >= GOAL) {
                throw new IllegalArgumentException("Invalid MXP state");
            }
        }
    }

    public record Award(Counter state, double effectiveXP) {}

    public static double normalizedBase(double nominalCost, double firstAccessiblePool) {
        requirePositive(nominalCost);
        requirePositive(firstAccessiblePool);
        double base = GOAL * (nominalCost / firstAccessiblePool);
        requirePositive(base);
        return base;
    }

    public static Award apply(Counter original, double base, boolean fixed) {
        if (original == null) throw new IllegalArgumentException("Missing MXP state");
        requirePositive(base);
        if (base > MAX_EVENT_BASE) throw new IllegalArgumentException("MXP award too large");
        return fixed ? fixed(original, base) : ordinary(original, base);
    }

    private static Award fixed(Counter previous, double amount) {
        // O(1) even for a fixed first-time victory worth many levels.
        double combined = previous.progress + amount;
        long gained = (long) Math.floor(combined / GOAL);
        if (gained > Long.MAX_VALUE - previous.lifetimeLevels)
            throw new IllegalArgumentException("MXP level overflow");
        double remaining = combined - (gained * GOAL);
        if (remaining >= GOAL) { gained++; remaining -= GOAL; }
        if (remaining < 0 && remaining > -1e-7) remaining = 0;
        return new Award(new Counter(previous.lifetimeLevels + gained, remaining), amount);
    }

    private static Award ordinary(Counter previous, double base) {
        long levels = previous.lifetimeLevels;
        double progress = previous.progress;
        double effective = 0;
        double unspentBase = base;
        int cycles = 0;

        while (unspentBase > 0) {
            if (++cycles > MAX_LEVELS_PER_AWARD)
                throw new IllegalArgumentException("Too many MXP levels in one event");
            double divisor = Math.pow(GROWTH, levels);
            // Once 1.2^P exceeds floating-point range, ordinary progress
            // approaches zero. Fixed exceptions still work.
            if (!Double.isFinite(divisor)) break;
            double effectiveRemaining = GOAL - progress;
            double baseToNext = effectiveRemaining * divisor;
            if (Double.isFinite(baseToNext) && unspentBase >= baseToNext) {
                // Exactly fill one level at the OLD divisor. Recalculate for
                // the leftover base at the NEW P after this boundary.
                unspentBase -= baseToNext;
                effective += effectiveRemaining;
                progress = 0;
                if (levels == Long.MAX_VALUE)
                    throw new IllegalArgumentException("MXP level overflow");
                levels++;
            } else {
                double gain = unspentBase / divisor;
                // If the double accumulator cannot represent this quantity,
                // do not fabricate progress; later fixed bonuses remain valid.
                if (gain > 0 && progress + gain > progress) {
                    progress += gain;
                    effective += gain;
                    if (progress >= GOAL) {
                        // Floating point at the boundary can round up.
                        progress -= GOAL;
                        if (levels == Long.MAX_VALUE)
                            throw new IllegalArgumentException("MXP level overflow");
                        levels++;
                    }
                }
                break;
            }
        }
        return new Award(new Counter(levels, progress), effective);
    }

    private static void requirePositive(double n) {
        if (!Double.isFinite(n) || n <= 0)
            throw new IllegalArgumentException("MXP cost / reference must be finite and positive");
    }
}
