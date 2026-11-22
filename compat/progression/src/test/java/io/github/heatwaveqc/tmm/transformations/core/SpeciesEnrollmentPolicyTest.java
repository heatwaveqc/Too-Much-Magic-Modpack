package io.github.heatwaveqc.tmm.transformations.core;

import static io.github.heatwaveqc.tmm.transformations.core.ProviderState.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SpeciesEnrollmentPolicyTest {
    private static StateSnapshot states(ProviderState... active) {
        var observed = new EnumMap<ProviderState, Observation>(ProviderState.class);
        for (ProviderState state : ProviderState.values()) observed.put(state, Observation.ABSENT);
        for (ProviderState state : active) observed.put(state, Observation.PRESENT);
        return new StateSnapshot(observed);
    }

    @Test void newbieOrAnyProgressionPathBlocksNewNonHumanForm() {
        assertFalse(SpeciesEnrollmentPolicy.canAcquire(false, true, false));
        assertFalse(SpeciesEnrollmentPolicy.canAcquire(false, false, true));
        assertFalse(SpeciesEnrollmentPolicy.canAcquire(false, true, true));
        assertTrue(SpeciesEnrollmentPolicy.canAcquire(false, false, false));
    }

    @Test void existingFormsCanAdvanceAndCureWithoutSilentReset() {
        assertTrue(SpeciesEnrollmentPolicy.canAcquire(true, true, true));
        assertTrue(SpeciesEnrollmentPolicy.canAcquire(true, false, true));
    }

    @Test void anyNativeFormBlocksJoiningAHumanClass() {
        assertTrue(SpeciesEnrollmentPolicy.canJoinHumanPath(states()));
        for (ProviderState species : ProviderState.values())
            assertFalse(SpeciesEnrollmentPolicy.canJoinHumanPath(states(species)), species.toString());
        assertFalse(SpeciesEnrollmentPolicy.canJoinHumanPath(new StateSnapshot(Map.of())));
        assertFalse(SpeciesEnrollmentPolicy.canJoinHumanPath(null));
    }
}
