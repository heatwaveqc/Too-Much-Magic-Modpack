package io.github.heatwaveqc.tmm.transformations.vampire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VampireBloodCarryTest {
    @Test void microFeedDoesNotDisappear() {
        VampireBloodCarry remainder = VampireBloodCarry.empty();
        int whole = 0;
        for (int i = 0; i < 300; i++) {
            var result = remainder.gain(1, 300, 20);
            whole += result.wholeUnits();
            remainder = result.remainder();
        }
        assertEquals(20, whole);
        assertEquals(0, remainder.numerator());
    }

    @Test void denominatorsCanChangeWithJAWRLevel() {
        var starting = new VampireBloodCarry(300, 150);
        var transformed = starting.gain(0, 400, 40);
        assertEquals(0, transformed.wholeUnits());
        assertEquals(new VampireBloodCarry(400, 200), transformed.remainder());
    }

    @Test void inputValidationRejectsInvalidFractions() {
        assertThrows(IllegalArgumentException.class, () -> new VampireBloodCarry(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new VampireBloodCarry(1, 1));
        assertThrows(IllegalArgumentException.class, () -> new VampireBloodCarry(2, -1));
        assertThrows(IllegalArgumentException.class, () -> VampireBloodCarry.empty().gain(-1, 40, 30));
    }
}
