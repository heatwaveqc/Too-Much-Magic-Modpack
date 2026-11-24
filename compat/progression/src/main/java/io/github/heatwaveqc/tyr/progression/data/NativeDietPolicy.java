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
     * A custom-use consumable has no FOOD component to rewrite; only its
     * permission can be governed. Never invent nutrition or bypass native
     * item use, charges, cooldown, blood/mana cost or potion effects.
     */
    public static FoodDecisionResolver.Decision specialUse(
            boolean markedCustomUse, boolean magical, boolean deniedByStrict) {
        if (!markedCustomUse) return FoodDecisionResolver.Decision.nativeResult();
        if (magical) return FoodDecisionResolver.Decision.permitNative();
        return deniedByStrict ? FoodDecisionResolver.Decision.deny()
                              : FoodDecisionResolver.Decision.nativeResult();
    }

    /**
     * A beverage unlock is permission, not food. Only known native/Path
     * nourishment can override this: the unlock itself never adds hunger,
     * saturation, vampire blood, mana or exhaustion.
     */
    public static boolean permissionOnly(boolean hasFood, boolean drink,
            boolean hasPermission, boolean separatelyNourishing,
            boolean magical, boolean strictDenied) {
        return hasFood && drink && hasPermission && !separatelyNourishing
            && !magical && !strictDenied;
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
