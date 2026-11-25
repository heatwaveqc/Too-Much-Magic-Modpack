package io.github.heatwaveqc.tmm.transformations.lich;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ArchlichResurrectionPolicyTest {

    @Test void genuineChargedDualLichTakesPhylacteryFirst() {
        assertTrue(ArchlichResurrectionPolicy.prioritizePhylactery(
                true, true, 2, 1, false));
        assertTrue(ArchlichResurrectionPolicy.prioritizePhylactery(
                true, true, 10, 3, false));
    }

    @Test void noSoulLeavesGoetyToHandleDeath() {
        assertFalse(ArchlichResurrectionPolicy.prioritizePhylactery(
                true, true, 10, 0, false));
    }

    @Test void standaloneLichesAreNeverIntercepted() {
        assertFalse(ArchlichResurrectionPolicy.prioritizePhylactery(
                true, false, 10, 3, false));
        assertFalse(ArchlichResurrectionPolicy.prioritizePhylactery(
                false, true, 10, 3, false));
    }

    @Test void prePhylacteryLevelNeverIntercepts() {
        assertFalse(ArchlichResurrectionPolicy.prioritizePhylactery(
                true, true, 1, 1, false));
    }

    @Test void alreadyCanceledEventIsNeverRedispatched() {
        assertFalse(ArchlichResurrectionPolicy.prioritizePhylactery(
                true, true, 10, 2, true));
    }
}
