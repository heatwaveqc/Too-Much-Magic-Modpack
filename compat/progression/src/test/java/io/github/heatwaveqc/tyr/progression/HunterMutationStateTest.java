package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.state.HunterMutationState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HunterMutationStateTest {
    private static final long TEN_HOURS = 720_000;

    @Test void tinyEffectsCountWithNoMinimumAndTenHoursUnlocks() {
        var initial = HunterMutationState.empty();
        var oneTick = initial.credit("minecraft:speed",0,1,TEN_HOURS);
        assertEquals(1,oneTick.progress("minecraft:speed",0));
        assertTrue(oneTick.unlocked().isEmpty());
        var nearly = oneTick.credit("minecraft:speed",0,TEN_HOURS-2,TEN_HOURS);
        assertEquals(TEN_HOURS-1,nearly.progress("minecraft:speed",0));
        var complete = nearly.credit("minecraft:speed",0,1,TEN_HOURS);
        assertEquals(TEN_HOURS,complete.progress("minecraft:speed",0));
        assertEquals(0,complete.unlocked().get("minecraft:speed"));
        assertSame(complete,complete.credit("minecraft:speed",0,1,TEN_HOURS));
    }

    @Test void potencyUpgradeDoesNotCreateSecondMetabolicMutation() {
        var base = HunterMutationState.empty().credit("minecraft:speed",0,TEN_HOURS,TEN_HOURS);
        var advanced = base.credit("minecraft:speed",1,TEN_HOURS,TEN_HOURS);
        assertEquals(1,advanced.unlocked().size());
        assertEquals(1,advanced.unlocked().get("minecraft:speed"));
        assertEquals(TEN_HOURS,advanced.progress("minecraft:speed",0));
        assertEquals(TEN_HOURS,advanced.progress("minecraft:speed",1));
        var withNightVision = advanced.credit("minecraft:night_vision",0,TEN_HOURS,TEN_HOURS);
        assertEquals(2,withNightVision.unlocked().size());
    }

    @Test void strongerPotencyReplacesLowerAndCannotBeEarnedWithLowerBottles() {
        var speedI = HunterMutationState.empty()
            .credit("minecraft:speed",0,TEN_HOURS,TEN_HOURS);
        var halfSpeedII = speedI.credit("minecraft:speed",1,TEN_HOURS/2,TEN_HOURS);
        assertEquals(0,halfSpeedII.unlocked().get("minecraft:speed"));
        assertEquals(TEN_HOURS/2,halfSpeedII.progress("minecraft:speed",1));
        var withMoreSpeedI = halfSpeedII.credit("minecraft:speed",0,TEN_HOURS,TEN_HOURS);
        assertSame(halfSpeedII,withMoreSpeedI); // no I -> II upgrade
        var speedII = halfSpeedII.credit("minecraft:speed",1,TEN_HOURS/2,TEN_HOURS);
        assertEquals(1,speedII.unlocked().get("minecraft:speed"));
        assertEquals(1,speedII.unlocked().size()); // never simultaneous I and II
        assertSame(speedII,speedII.credit("minecraft:speed",0,TEN_HOURS,TEN_HOURS));
    }

    @Test void accumulationSaturatesWithoutOverflowOrNegativeTime() {
        var s = HunterMutationState.empty();
        assertSame(s,s.credit("test:effect",0,0,TEN_HOURS));
        assertSame(s,s.credit("test:effect",0,-50,TEN_HOURS));
        assertSame(s,s.credit("test:effect",-1,TEN_HOURS,TEN_HOURS));
        var unlocked = s.credit("test:effect",0,Long.MAX_VALUE,TEN_HOURS);
        assertEquals(TEN_HOURS,unlocked.progress("test:effect",0));
    }
}
