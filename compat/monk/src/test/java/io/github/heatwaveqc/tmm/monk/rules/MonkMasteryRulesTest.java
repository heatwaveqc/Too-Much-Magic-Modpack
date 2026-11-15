package io.github.heatwaveqc.tmm.monk.rules;

import io.github.heatwaveqc.tmm.monk.progression.MonkMasteryRules;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonkMasteryRulesTest {
    @Test void cumulativeThresholdsCanSkipSeveralLevels() {
        assertEquals(1, MonkMasteryRules.eligibleLevel(1, 19));
        assertEquals(2, MonkMasteryRules.eligibleLevel(1, 20));
        assertEquals(2, MonkMasteryRules.eligibleLevel(1, 54));
        assertEquals(3, MonkMasteryRules.eligibleLevel(1, 55));
        assertEquals(4, MonkMasteryRules.eligibleLevel(1, 105));
        assertEquals(4, MonkMasteryRules.eligibleLevel(1, 1_000_000));
        assertEquals(4, MonkMasteryRules.eligibleLevel(4, 20));
    }
    @Test void boundedSafeAwardAndNoNegativeRewards() {
        assertEquals(11, MonkMasteryRules.safeAdd(8, 3));
        assertEquals(Integer.MAX_VALUE, MonkMasteryRules.safeAdd(Integer.MAX_VALUE, 17));
        assertThrows(IllegalArgumentException.class, () -> MonkMasteryRules.safeAdd(0, -1));
    }
}
