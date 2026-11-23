package io.github.heatwaveqc.tyr.progression.data;

/** Pure policy crossing native food restrictions and provider-owned penalties. */
public final class NativeDietPolicy {
    private NativeDietPolicy() {}

    /** A native owned-effect restriction can veto nutrition, never add it. */
    public static FoodDecisionResolver.Decision intersect(
            FoodDecisionResolver.Decision dietary, boolean ownedRestrictionAllows) {
        if (dietary.outcome() == FoodDecisionResolver.Outcome.DENY
                || !ownedRestrictionAllows)
            return FoodDecisionResolver.Decision.deny();
        return dietary;
    }

    /**
     * Penalty exemption is not permission to consume an otherwise denied
     * item, and not a blood-credit operation. Never infer "safe" merely from
     * FOOD, DRINK animation or the broad positive bloody-food tag.
     */
    public static boolean suppressVampireFoodPenalty(boolean nativeVampire,
            boolean componentFood, boolean denied,
            boolean magical, boolean explicitlySafe,
            boolean recreationalDrink, boolean earnedPermission) {
        return nativeVampire && componentFood && !denied
            && (magical || explicitlySafe
                || (recreationalDrink && earnedPermission));
    }
}
