package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure policy checks; game-engine structure lookup is integration QA. */
final class GolemRocketTargetingTest {
    @Test void exactlyTenBlocksMayLaunch() {
        assertFalse(GolemRocketGeometry.farEnough(99.99999D));
        assertTrue(GolemRocketGeometry.farEnough(100.0D));
        assertTrue(GolemRocketGeometry.farEnough(100.00001D));
    }

    @Test void generatedVillageFootprintIncludesEdgesAndInterveningGaps() {
        assertTrue(GolemRocketGeometry.insideRectangle(-10, 25, -4, 31, -10, -4));
        assertTrue(GolemRocketGeometry.insideRectangle(-10, 25, -4, 31, 25, 31));
        assertTrue(GolemRocketGeometry.insideRectangle(-10, 25, -4, 31, 10, 10));
        assertFalse(GolemRocketGeometry.insideRectangle(-10, 25, -4, 31, 26, 10));
        assertFalse(GolemRocketGeometry.insideRectangle(-10, 25, -4, 31, 10, -5));
    }
}
