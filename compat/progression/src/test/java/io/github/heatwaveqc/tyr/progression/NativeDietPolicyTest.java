package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.data.FoodDecisionResolver;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRules;
import io.github.heatwaveqc.tyr.progression.data.NativeDietPolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure source regressions: no JAWR runtime, inventory or user world needed. */
class NativeDietPolicyTest {
    @Test void gildedVetoBeatsDragonNutritionButNotNativeApproval() {
        var nutrition = FoodDecisionResolver.Decision.nutrition(
            new FoodPolicyRules.Nutrition(6, 2f, 24));
        assertEquals(FoodDecisionResolver.Outcome.DENY,
            NativeDietPolicy.intersect(nutrition,false).outcome());
        assertEquals(nutrition, NativeDietPolicy.intersect(nutrition,true));
        assertEquals(FoodDecisionResolver.Outcome.DENY,
            NativeDietPolicy.intersect(FoodDecisionResolver.Decision.deny(),true).outcome());
        assertEquals(FoodDecisionResolver.Outcome.PERMIT_NATIVE,
            NativeDietPolicy.intersect(FoodDecisionResolver.Decision.permitNative(),true).outcome());
    }

    @Test void customUseNeverFabricatesNutritionOrGeneralPotionRules() {
        assertEquals(FoodDecisionResolver.Outcome.PERMIT_NATIVE,
            NativeDietPolicy.specialUse(true,true,true).outcome(),
            "Magical native item retains its own effects and costs");
        assertEquals(FoodDecisionResolver.Outcome.DENY,
            NativeDietPolicy.specialUse(true,false,true).outcome());
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            NativeDietPolicy.specialUse(true,false,false).outcome());
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            NativeDietPolicy.specialUse(false,false,true).outcome(),
            "Unrelated items are not classified as edible");
    }

    @Test void acquiredTasteIsPermissionNotNourishment() {
        assertTrue(NativeDietPolicy.permissionOnly(true,true,true,false,false,false));
        assertFalse(NativeDietPolicy.permissionOnly(true,true,true,true,false,false),
            "Recognized native dietary nutrition remains");
        assertFalse(NativeDietPolicy.permissionOnly(true,true,true,false,true,false),
            "Magical food keeps native nourishment");
        assertFalse(NativeDietPolicy.permissionOnly(true,true,true,false,false,true),
            "Strict Dryad still denies");
        assertFalse(NativeDietPolicy.permissionOnly(true,false,true,false,false,false),
            "Ordinary solid food cannot become a beverage");
        assertFalse(NativeDietPolicy.permissionOnly(false,true,true,false,false,false),
            "Non-FOOD potion is not assigned fictional hunger");
        assertFalse(NativeDietPolicy.permissionOnly(true,true,false,false,false,false),
            "Cannot drink without earned permission");
    }

    @Test void jawrSicknessBypassRequiresNativeVampireAndVerifiedExemption() {
        assertTrue(NativeDietPolicy.suppressVampireFoodPenalty(
            true,true,false,true,false,false,false), "Magical native exemption");
        assertTrue(NativeDietPolicy.suppressVampireFoodPenalty(
            true,true,false,false,true,false,false), "Explicit safe food");
        assertTrue(NativeDietPolicy.suppressVampireFoodPenalty(
            true,true,false,false,false,true,true), "Earned food-backed drink exception");
        assertFalse(NativeDietPolicy.suppressVampireFoodPenalty(
            true,true,false,false,false,true,false), "No free acquired taste");
        assertFalse(NativeDietPolicy.suppressVampireFoodPenalty(
            true,true,false,false,false,false,true), "No free permission for ordinary solids");
        assertFalse(NativeDietPolicy.suppressVampireFoodPenalty(
            true,true,true,true,true,true,true), "Another Path still vetoes");
        assertFalse(NativeDietPolicy.suppressVampireFoodPenalty(
            false,true,false,true,true,true,true), "No werewolf/human repair");
        assertFalse(NativeDietPolicy.suppressVampireFoodPenalty(
            true,false,false,true,true,true,true), "Non-food potion remains native");
    }
}
