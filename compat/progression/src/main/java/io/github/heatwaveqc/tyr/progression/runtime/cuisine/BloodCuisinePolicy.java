package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

/** #450: closed, one-bottle blood arithmetic. No potion effects represent JAWR blood. */
public final class BloodCuisinePolicy {
    public enum Product { SAUSAGE, PIE, WINE }
    public static final int VERIFIED_FILLED_BOTTLE_BLOOD = 500;
    private BloodCuisinePolicy() {}

    public static int bloodFor(Product kind, int verifiedInputBlood) {
        if (kind == null || verifiedInputBlood <= 0) return 0;
        int multiplier = kind == Product.WINE ? 5 : 2;
        try { return Math.multiplyExact(verifiedInputBlood, multiplier); }
        catch (ArithmeticException invalid) { return 0; }
    }

    public static boolean validCaskBatch(boolean redWine, boolean alreadyCharged,
                                         int eligibleBottles, int invalidBottles,
                                         int eligibleBottleStackCount) {
        if (eligibleBottles == 0 && invalidBottles == 0) return true; // Native effect-only aging.
        return redWine && !alreadyCharged && eligibleBottles == 1
            && invalidBottles == 0 && eligibleBottleStackCount == 1;
    }

    public static boolean validFoodPayload(Product product, int storedBlood) {
        return (product == Product.SAUSAGE || product == Product.PIE)
            && storedBlood == bloodFor(product, VERIFIED_FILLED_BOTTLE_BLOOD);
    }

    /** Pure global one-use accounting, independent of Minecraft SavedData. */
    public static boolean redeem(java.util.Set<java.util.UUID> spent, java.util.UUID receipt) {
        return spent != null && receipt != null && spent.add(receipt);
    }

    public static boolean validWinePayload(int storedBlood) {
        return storedBlood == bloodFor(Product.WINE, VERIFIED_FILLED_BOTTLE_BLOOD);
    }
}
