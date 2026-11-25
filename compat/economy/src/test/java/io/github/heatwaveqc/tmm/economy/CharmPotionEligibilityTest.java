package io.github.heatwaveqc.tmm.economy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CharmPotionEligibilityTest {
    @Test void anyRegisteredSingleNonInstantEffectAllowed() {
        assertTrue(CharmPotionEligibility.permits(1, false, true));
    }

    @Test void noOrMultipleEffectsDenied() {
        assertFalse(CharmPotionEligibility.permits(0, false, true));
        assertFalse(CharmPotionEligibility.permits(2, false, true));
        assertFalse(CharmPotionEligibility.permits(9, false, true));
    }

    @Test void instantAndUnregisteredEffectsDenied() {
        assertFalse(CharmPotionEligibility.permits(1, true, true));
        assertFalse(CharmPotionEligibility.permits(1, false, false));
    }

    @Test void exactThreeBottleCountRequired() {
        for (int count = 0; count <= 9; count++) {
            assertEquals(count == 3, CharmPotionEligibility.matchingBottles(count));
        }
    }
}
