package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.AbominationPilotPolicy.Verdict.*;
import static io.github.heatwaveqc.tmm.transformations.core.Observation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AbominationPilotPolicyTest {
    @Test void disabledPilotNeverApproves() {
        assertEquals(DISABLED, AbominationPilotPolicy.assess(false, PRESENT, PRESENT, ABSENT));
    }

    @Test void bothIndependentlyObservedJAWRStatesRequired() {
        assertEquals(READY, AbominationPilotPolicy.assess(true, PRESENT, PRESENT, ABSENT));
        assertEquals(MISSING_DUAL_AFFLICTION,
                AbominationPilotPolicy.assess(true, PRESENT, ABSENT, ABSENT));
        assertEquals(MISSING_DUAL_AFFLICTION,
                AbominationPilotPolicy.assess(true, ABSENT, PRESENT, ABSENT));
        assertEquals(MISSING_DUAL_AFFLICTION,
                AbominationPilotPolicy.assess(true, ABSENT, ABSENT, ABSENT));
        assertEquals(UNKNOWN_AFFLICTION,
                AbominationPilotPolicy.assess(true, UNKNOWN, PRESENT, ABSENT));
        assertEquals(UNKNOWN_AFFLICTION,
                AbominationPilotPolicy.assess(true, PRESENT, UNKNOWN, ABSENT));
    }

    @Test void hunterExplicitlyBlocksThisExperiment() {
        assertEquals(HUNTER_CONFLICT,
                AbominationPilotPolicy.assess(true, PRESENT, PRESENT, PRESENT));
        assertEquals(HUNTER_CONFLICT,
                AbominationPilotPolicy.assess(true, ABSENT, ABSENT, PRESENT));
    }

    @Test void pilotCooldownBoundedToOneMinute() {
        assertEquals(420L, AbominationPilotPolicy.nextReadyTick(400, -1));
        assertEquals(420L, AbominationPilotPolicy.nextReadyTick(400, 0));
        assertEquals(440L, AbominationPilotPolicy.nextReadyTick(400, 40));
        assertEquals(1600L, AbominationPilotPolicy.nextReadyTick(400, Integer.MAX_VALUE));
    }
}
