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
