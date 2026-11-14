package io.github.heatwaveqc.tmm.transformations.vampire;

/**
 * Pure, saturating conversions between native vampire blood meter sizes.
 * No player state writes. Keeping native levels intact is mandatory; they
 * are NOT interchangeable with a single global vampire rank.
 */
public final class VampireBloodMath {
    private VampireBloodMath() { }

    public static int project(int nativeBlood, int nativeMax, int otherMax) {
        if (nativeMax <= 0 || otherMax <= 0) {
            throw new IllegalArgumentException("Blood capacities must be positive");
        }
        long current = Math.max(0L, Math.min((long) nativeBlood, nativeMax));
        long scaled = (current * otherMax + nativeMax / 2L) / nativeMax;
        return (int) Math.min(otherMax, scaled);
    }

    /** The least native whole units covering an expenditure in the other meter. */
    public static int costRoundUp(int otherCost, int otherMax, int nativeMax) {
        if (otherMax <= 0 || nativeMax <= 0 || otherCost < 0) {
            throw new IllegalArgumentException("Invalid blood cost or capacities");
        }
        long raw = ((long) otherCost * nativeMax + otherMax - 1L) / otherMax;
        return (int) Math.min(nativeMax, raw);
    }
}
