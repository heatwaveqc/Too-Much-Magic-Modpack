package io.github.heatwaveqc.tmm.monk.rules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SunsetTrialRulesTest {
    @Test void onlySunsetWindowAndNotSunriseNoonMidnight() {
        assertFalse(SunsetTrialRules.duringSunset(0));
        assertFalse(SunsetTrialRules.duringSunset(6000));
        assertFalse(SunsetTrialRules.duringSunset(11499));
        assertTrue(SunsetTrialRules.duringSunset(11500));
        assertTrue(SunsetTrialRules.duringSunset(12000));
        assertTrue(SunsetTrialRules.duringSunset(12500));
        assertFalse(SunsetTrialRules.duringSunset(12501));
        assertFalse(SunsetTrialRules.duringSunset(18000));
        assertTrue(SunsetTrialRules.duringSunset(36000));
        assertTrue(SunsetTrialRules.duringSunset(-12000));
    }

    @Test void requiresNarrowWesternGazeAndRejectsNaN() {
        assertTrue(SunsetTrialRules.facingWesternSun(-1, 0, 0));
        assertTrue(SunsetTrialRules.facingWesternSun(-0.999, 0.04, 0));
        assertFalse(SunsetTrialRules.facingWesternSun(1, 0, 0));
        assertFalse(SunsetTrialRules.facingWesternSun(0, 0, 1));
        assertFalse(SunsetTrialRules.facingWesternSun(-0.9, 0, 0.4));
        assertFalse(SunsetTrialRules.facingWesternSun(Double.NaN, 0, 0));
        assertFalse(SunsetTrialRules.facingWesternSun(0, 0, 0));
    }

    @Test void continuousProgressResetsAndNeverOverflows() {
        int ticks = 0;
        for (int i = 0; i < SunsetTrialRules.REQUIRED_TICKS; ++i) {
            ticks = SunsetTrialRules.consecutiveTicks(ticks, true);
        }
        assertEquals(200, ticks);
        assertEquals(200, SunsetTrialRules.consecutiveTicks(ticks, true));
        assertEquals(0, SunsetTrialRules.consecutiveTicks(ticks, false));
        assertEquals(1, SunsetTrialRules.consecutiveTicks(0, true));
    }
}
