package io.github.heatwaveqc.tmm.monk.rules;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static io.github.heatwaveqc.tmm.monk.rules.DisciplineRules.*;

class DisciplineRulesTest {
    @Test void humanWithoutExistingFactionCanInitiate() {
        assertTrue(mayInitiate(Physiology.HUMAN, false));
        assertFalse(mayInitiate(Physiology.HUMAN, true));
    }

    @Test void advancedTransformationsAndUnknownStatesAreIneligible() {
        for (Physiology state : Physiology.values()) {
            if (state != Physiology.HUMAN) {
                assertTrue(conflicting(state));
                assertFalse(mayInitiate(state, false));
            }
        }
    }

    @Test void conventionalWeaponsAndArmorSuppressDiscipline() {
        assertTrue(equipmentPermitsDiscipline(false, false));
        assertFalse(equipmentPermitsDiscipline(true, false));
        assertFalse(equipmentPermitsDiscipline(false, true));
        assertFalse(equipmentPermitsDiscipline(true, true));
    }
}
