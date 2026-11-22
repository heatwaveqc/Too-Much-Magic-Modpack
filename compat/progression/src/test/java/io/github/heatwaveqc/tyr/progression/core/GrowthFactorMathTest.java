package io.github.heatwaveqc.tyr.progression.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GrowthFactorMathTest {
    @Test void enlightenmentRankOneAndEighteen() {
        assertEquals(1.019, GrowthFactorMath.effective(1.020, 0.001), 1e-12);
        assertEquals(1.002, GrowthFactorMath.effective(1.020, 18 * 0.001), 1e-12);
    }

    @Test void clampsAtOneWithoutMintingExperience() {
        assertEquals(1.0, GrowthFactorMath.effective(1.005, 0.018), 1e-12);
        assertEquals(1.020, GrowthFactorMath.effective(1.020, 0), 1e-12);
        assertThrows(IllegalArgumentException.class, () -> GrowthFactorMath.effective(1.020, -0.01));
        assertThrows(IllegalArgumentException.class, () -> GrowthFactorMath.effective(1.020, Double.NaN));
    }

    @Test void futureOrdinaryAwardsBenefitButExistingLifetimeCountDoesNotRewind() {
        MxpMath.Counter saved = new MxpMath.Counter(100, 300);
        MxpMath.Award without = MxpMath.apply(saved, 1750, false, 1.020);
        MxpMath.Award with = MxpMath.apply(saved, 1750, false,
            GrowthFactorMath.effective(1.020, 0.018));
        assertTrue(with.effectiveXP() > without.effectiveXP());
        assertEquals(100, saved.lifetimeLevels());
        assertEquals(300, saved.progress(), 1e-12);
    }

    @Test void fixedFirstTimeRewardsIgnoreGrowthModifier() {
        MxpMath.Counter saved = new MxpMath.Counter(200, 100);
        MxpMath.Award without = MxpMath.apply(saved, 1000, true, 1.020);
        MxpMath.Award with = MxpMath.apply(saved, 1000, true,
            GrowthFactorMath.effective(1.020, 0.018));
        assertEquals(without.state(), with.state());
        assertEquals(without.effectiveXP(), with.effectiveXP(), 1e-12);
    }
}
