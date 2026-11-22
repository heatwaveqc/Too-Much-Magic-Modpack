package io.github.heatwaveqc.tmm.economy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ProjectECharmRepairPolicyTest {
    @Test
    void acceptsOnlyDamagedSingleNonInstantCharms() {
        assertTrue(ProjectECharmRepairPolicy.permits(true, true, 1, true, false, false));
        assertFalse(ProjectECharmRepairPolicy.permits(false, true, 1, true, false, false));
        assertFalse(ProjectECharmRepairPolicy.permits(true, false, 1, true, false, false));
        assertFalse(ProjectECharmRepairPolicy.permits(true, true, 0, true, false, false));
        assertFalse(ProjectECharmRepairPolicy.permits(true, true, 1, false, false, false));
        assertFalse(ProjectECharmRepairPolicy.permits(true, true, 1, true, true, false));
        assertFalse(ProjectECharmRepairPolicy.permits(true, true, 1, true, false, true));
    }
}
