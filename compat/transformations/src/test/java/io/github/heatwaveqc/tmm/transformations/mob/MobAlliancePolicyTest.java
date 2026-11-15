package io.github.heatwaveqc.tmm.transformations.mob;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MobAlliancePolicyTest {
    @Test void ordinaryUndeadDoNotAggroVampiresAndLiches() {
        assertTrue(MobAlliancePolicy.ignoresTarget(true,true,false,false,false,false));
        assertFalse(MobAlliancePolicy.ignoresTarget(true,false,false,false,false,false));
        assertFalse(MobAlliancePolicy.ignoresTarget(false,true,false,false,false,false));
    }

    @Test void provokeAndBossKeepTheirAttacks() {
        assertFalse(MobAlliancePolicy.ignoresTarget(true,true,false,false,true,false));
        assertFalse(MobAlliancePolicy.ignoresTarget(true,true,false,false,false,true));
    }

    @Test void faeAndForestDragonsOnlyAreNeutral() {
        assertTrue(MobAlliancePolicy.ignoresTarget(false,false,true,false,false,false));
        assertFalse(MobAlliancePolicy.ignoresTarget(false,false,false,false,false,false));
        assertFalse(MobAlliancePolicy.ignoresTarget(false,false,false,true,true,false));
    }

    @Test void demonsAndCaveDragonsOnlyAreNeutral() {
        assertTrue(MobAlliancePolicy.ignoresTarget(false,false,false,true,false,false));
        assertFalse(MobAlliancePolicy.ignoresTarget(false,false,false,false,false,false));
    }
}
