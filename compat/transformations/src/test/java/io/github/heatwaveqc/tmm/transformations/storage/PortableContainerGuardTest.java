package io.github.heatwaveqc.tmm.transformations.storage;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Pure no-game checks: no recursive bag, no accidental ordinary-item ban. */
class PortableContainerGuardTest {
    @Test void ordinaryStackAllowed() {
        assertFalse(PortableBagNestingRules.rejects(false, false, false));
    }
    @Test void taggedCrossModBagRejected() {
        assertTrue(PortableBagNestingRules.rejects(false, true, false));
    }
    @Test void shulkerBoxRejectedInsideOtherPortableContainer() {
        assertTrue(PortableBagNestingRules.rejects(false, false, true));
    }
    @Test void emptyStackNotRejected() {
        assertFalse(PortableBagNestingRules.rejects(true, true, true));
    }
}
