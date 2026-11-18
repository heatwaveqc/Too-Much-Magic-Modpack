package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArmedGolemPolicyTest {
    @Test void onlyRecordedVillagerSpawnIsArmed() {
        assertTrue(ArmedGolemPolicy.shouldArm(true, false));
        assertFalse(ArmedGolemPolicy.shouldArm(true, true));
        assertFalse(ArmedGolemPolicy.shouldArm(false, false));
        assertFalse(ArmedGolemPolicy.shouldArm(false, true));
    }
    @Test void spinRampsAndStopsWithoutOutOfRangeSpeed() {
        var spin = new MinigunSpin();
        for (int i = 0; i < 20; i++) spin.tick(true);
        assertFalse(spin.ready());
        for (int i = 0; i < 30; i++) spin.tick(true);
        assertTrue(spin.ready());
        assertEquals(MinigunSpin.MAX_SPEED, spin.speed(), 0.0001);
        for (int i = 0; i < 150; i++) spin.tick(false);
        assertFalse(spin.ready());
        assertEquals(0F, spin.speed(), 0.0001);
        assertTrue(spin.angle() >= 0F && spin.angle() < 2 * Math.PI);
    }
}
