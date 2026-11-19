package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PistolCycleTest {
    @Test void verifiedM1911Balance() {
        assertEquals(7F, PistolCycle.DAMAGE);
        assertEquals(35F, PistolCycle.RANGE);
        assertEquals(420, PistolCycle.RPM);
        assertEquals(42, PistolCycle.RELOAD_TICKS);
        assertEquals(7, PistolCycle.MAGAZINE);
    }
    @Test void firesOnlyWhenReadyAndReloads() {
        var cycle = new PistolCycle();
        for (int i = 0; i < 120; i++) assertEquals(PistolCycle.Action.NONE, cycle.tick(false));
        assertEquals(8, cycle.rounds());
        int shots = 0;
        int tick = 0;
        while (shots < 8 && tick < 40) {
            if (cycle.tick(true) == PistolCycle.Action.SHOT) shots++;
            tick++;
        }
        assertEquals(8, shots);
        assertEquals(0, cycle.rounds());
        assertTrue(tick >= 20 && tick <= 23);
        boolean started = false;
        for (int i = 0; i < 4; i++) {
            if (cycle.tick(true) == PistolCycle.Action.MAG_OUT) { started = true; break; }
        }
        assertTrue(started);
        assertEquals(42, cycle.reloadRemaining());
        boolean magIn = false, slide = false;
        for (int i = 0; i < 42; i++) {
            var action = cycle.tick(true);
            assertNotEquals(PistolCycle.Action.SHOT, action);
            magIn |= action == PistolCycle.Action.MAG_IN;
            slide |= action == PistolCycle.Action.SLIDE;
        }
        assertTrue(magIn && slide);
        assertEquals(7, cycle.rounds());
        assertEquals(PistolCycle.Action.SHOT, cycle.tick(true));
    }
}
