package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BloodCuisinePolicyTest {
    @Test void exactNativePinnedDoseAndOwnerMultipliers() {
        assertEquals(500, BloodCuisinePolicy.VERIFIED_FILLED_BOTTLE_BLOOD);
        assertEquals(2500, BloodCuisinePolicy.bloodFor(BloodCuisinePolicy.Product.WINE, 500));
        assertEquals(1000, BloodCuisinePolicy.bloodFor(BloodCuisinePolicy.Product.SAUSAGE, 500));
        assertEquals(1000, BloodCuisinePolicy.bloodFor(BloodCuisinePolicy.Product.PIE, 500));
    }

    @Test void invalidOrUnfilledVesselsNeverGenerateCredit() {
        for (var kind : BloodCuisinePolicy.Product.values()) {
            assertEquals(0, BloodCuisinePolicy.bloodFor(kind, 0));
            assertEquals(0, BloodCuisinePolicy.bloodFor(kind, -1));
            assertEquals(0, BloodCuisinePolicy.bloodFor(kind, Integer.MAX_VALUE));
        }
        assertEquals(0, BloodCuisinePolicy.bloodFor(null, 500));
    }

    @Test void nativeWineCanAgeWithoutNewBloodButCannotRecharge() {
        assertTrue(BloodCuisinePolicy.validCaskBatch(true, false, 1, 0, 1));
        assertTrue(BloodCuisinePolicy.validCaskBatch(false, false, 0, 0, 0));
        assertTrue(BloodCuisinePolicy.validCaskBatch(true, true, 0, 0, 0));
        assertFalse(BloodCuisinePolicy.validCaskBatch(false, false, 1, 0, 1));
        assertFalse(BloodCuisinePolicy.validCaskBatch(true, true, 1, 0, 1));
        assertFalse(BloodCuisinePolicy.validCaskBatch(true, false, 2, 0, 1));
        assertFalse(BloodCuisinePolicy.validCaskBatch(true, false, 1, 1, 1));
        assertFalse(BloodCuisinePolicy.validCaskBatch(true, false, 1, 0, 2));
    }

    @Test void wrongProductPayloadIsNotAValidCredit() {
        assertTrue(BloodCuisinePolicy.validFoodPayload(BloodCuisinePolicy.Product.SAUSAGE, 1000));
        assertTrue(BloodCuisinePolicy.validFoodPayload(BloodCuisinePolicy.Product.PIE, 1000));
        assertFalse(BloodCuisinePolicy.validFoodPayload(BloodCuisinePolicy.Product.WINE, 1000));
        assertFalse(BloodCuisinePolicy.validFoodPayload(BloodCuisinePolicy.Product.SAUSAGE, 2500));
        assertFalse(BloodCuisinePolicy.validFoodPayload(BloodCuisinePolicy.Product.PIE, 0));
        assertTrue(BloodCuisinePolicy.validWinePayload(2500));
        assertFalse(BloodCuisinePolicy.validWinePayload(1000));
        assertFalse(BloodCuisinePolicy.validWinePayload(5000));
    }
}
