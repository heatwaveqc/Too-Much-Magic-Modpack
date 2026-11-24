package io.github.heatwaveqc.tmm.transformations.vampire;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Defensive policy tests independent of NeoForge runtime / JAWR JAR.
 * The full bottle fluid and vampire consumption flow requires game testing.
 */
class JAWRBottleInteropTest {
    @Test void ordinaryNoSpecialMarkers() {
        assertTrue(BloodBottlePolicy.isOrdinary(false, false));
    }
    @Test void chickenMarkerIsProtected() {
        assertFalse(BloodBottlePolicy.isOrdinary(true, false));
    }
    @Test void lilithVampireMarkerIsProtected() {
        assertFalse(BloodBottlePolicy.isOrdinary(false, true));
    }
    @Test void anyAmbiguousSpecialCombinationFailsClosed() {
        assertFalse(BloodBottlePolicy.isOrdinary(true, true));
    }
}
