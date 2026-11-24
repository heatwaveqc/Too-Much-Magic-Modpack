package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Boundary and XZ projection checks. Structure lookup is server QA. */
final class GolemRocketTargetingTest {
    @Test void exactlyTenBlocksMayLaunch() {
        assertFalse(GolemRocketTargeting.farEnough(99.99999D));
        assertTrue(GolemRocketTargeting.farEnough(100.0D));
        assertTrue(GolemRocketTargeting.farEnough(100.00001D));
    }

    @Test void structureFootprintIncludesRoofsAndGapsIrrespectiveOfY() {
        var box = new BoundingBox(-10, 55, -4, 25, 72, 31);
        assertTrue(GolemRocketTargeting.withinFootprint(box, -10, -4));
        assertTrue(GolemRocketTargeting.withinFootprint(box, 25, 31));
        assertTrue(GolemRocketTargeting.withinFootprint(box, 10, 10));
        assertFalse(GolemRocketTargeting.withinFootprint(box, 26, 10));
        assertFalse(GolemRocketTargeting.withinFootprint(box, 10, -5));
    }
}
