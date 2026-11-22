package io.github.heatwaveqc.tmm.economy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProjectECharmRepairPolicyTest {
    @Test
    void acceptsOnlyDamagedSingleNonInstantCharms() {
        assertTrue(CharmRepairEligibility.permits(true, true, 1, true, false, false));
        assertFalse(CharmRepairEligibility.permits(false, true, 1, true, false, false));
        assertFalse(CharmRepairEligibility.permits(true, false, 1, true, false, false));
        assertFalse(CharmRepairEligibility.permits(true, true, 0, true, false, false));
        assertFalse(CharmRepairEligibility.permits(true, true, 1, false, false, false));
        assertFalse(CharmRepairEligibility.permits(true, true, 1, true, true, false));
        assertFalse(CharmRepairEligibility.permits(true, true, 1, true, false, true));
    }
}
