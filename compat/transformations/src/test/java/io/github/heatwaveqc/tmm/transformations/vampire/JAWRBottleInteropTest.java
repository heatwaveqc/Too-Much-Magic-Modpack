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
        assertTrue(JAWRBottleInterop.ordinaryMarkersAbsent(false, false));
    }
    @Test void chickenMarkerIsProtected() {
        assertFalse(JAWRBottleInterop.ordinaryMarkersAbsent(true, false));
    }
    @Test void lilithVampireMarkerIsProtected() {
        assertFalse(JAWRBottleInterop.ordinaryMarkersAbsent(false, true));
    }
    @Test void anyAmbiguousSpecialCombinationFailsClosed() {
        assertFalse(JAWRBottleInterop.ordinaryMarkersAbsent(true, true));
    }
}
