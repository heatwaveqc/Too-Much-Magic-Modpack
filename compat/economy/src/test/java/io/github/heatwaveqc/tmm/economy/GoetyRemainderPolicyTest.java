package io.github.heatwaveqc.tmm.economy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoetyRemainderPolicyTest {
    @Test void consumedRootsTotemHasNoRemainderToSubtract() {
        assertFalse(GoetyRemainderPolicy.hasActualRemainder(true, true, () -> true));
    }

    @Test void chargedRootsTotemStillUsesNativeRemainder() {
        assertTrue(GoetyRemainderPolicy.hasActualRemainder(true, true, () -> false));
    }

    @Test void otherItemsAndUnadvertisedRemaindersNeverProbe() {
        assertTrue(GoetyRemainderPolicy.hasActualRemainder(true, false,
            () -> { throw new AssertionError("unrelated item probed"); }));
        assertFalse(GoetyRemainderPolicy.hasActualRemainder(false, true,
            () -> { throw new AssertionError("unadvertised item probed"); }));
    }

    @Test void realNativeFailureIsNotSilentlySwallowed() {
        assertThrows(IllegalStateException.class,
            () -> GoetyRemainderPolicy.hasActualRemainder(true, true,
                () -> { throw new IllegalStateException("native failure"); }));
    }
}
