package io.github.heatwaveqc.tmm.economy;

/** Pure checked arithmetic, independent of Minecraft/ProjectE. */
public final class GemPricing {
    private GemPricing() {}

    /** Provisional scarcity policy, in Gem Dust units, not a fixed EMC anchor.
     * The largest positive zero-luck weight is used to avoid transient player
     * luck/world context changing the price of the very same stored gem.
     */
    public static int seedDustUnits(int minPurity, int maxWeight, int referenceWeight,
                                    int commonDustUnits, int salvageMaxDust) {
        if (minPurity < 0 || minPurity > 5 || maxWeight <= 0
                || referenceWeight <= 0 || commonDustUnits <= 0 || salvageMaxDust < 0)
            throw new IllegalArgumentException("Invalid gem rarity or purity");
        long rarity = Math.max(1L, (referenceWeight + (long) maxWeight - 1) / maxWeight);
        long seed = Math.multiplyExact(Math.multiplyExact((long) commonDustUnits, 1L << minPurity), rarity);
        return Math.toIntExact(Math.max(seed, salvageMaxDust));
    }

    public static long upgrade(long previous, long dustCost, long reagentCost) {
        if (previous <= 0 || dustCost < 0 || reagentCost < 0)
            throw new IllegalArgumentException("Invalid gem cut costs");
        return Math.addExact(Math.multiplyExact(2L, previous), Math.addExact(dustCost, reagentCost));
    }

    public static int guaranteedDustUnits(int previousUnits, int consumedDust, int salvageMax) {
        int next = Math.addExact(Math.multiplyExact(2, previousUnits), consumedDust);
        if (consumedDust < 0 || salvageMax < 0 || next < salvageMax)
            throw new IllegalArgumentException("Upgrade may undercut salvage output");
        return next;
    }
}
