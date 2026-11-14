package io.github.heatwaveqc.tmm.transformations.vampire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VampireBloodMathTest {
    @Test void proportionateBloodWithHardEndpoints() {
        assertEquals(0, VampireBloodMath.project(0, 40, 1000));
        assertEquals(1000, VampireBloodMath.project(40, 40, 1000));
        assertEquals(500, VampireBloodMath.project(20, 40, 1000));
        assertEquals(1000, VampireBloodMath.project(90, 40, 1000));
        assertEquals(0, VampireBloodMath.project(-1, 40, 1000));
        assertEquals(0, VampireBloodMath.project(9, 20, 0+1)); // rounded 0.45 to 0
    }

    @Test void projectionIsMonotonic() {
        for (int nativeMax : new int[] {20, 26, 30, 34, 40}) {
            for (int otherMax : new int[] {1, 100, 1000, 8192}) {
                int previous = -1;
                for (int current = 0; current <= nativeMax; current++) {
                    int projected = VampireBloodMath.project(current, nativeMax, otherMax);
                    assertTrue(projected >= previous);
                    assertTrue(projected >= 0 && projected <= otherMax);
                    previous = projected;
                }
            }
        }
    }

    @Test void costsNeverBecomeFreeAndInvalidCapacitiesAreRejected() {
        assertEquals(0, VampireBloodMath.costRoundUp(0, 1000, 40));
        assertEquals(1, VampireBloodMath.costRoundUp(1, 1000, 40));
        assertEquals(4, VampireBloodMath.costRoundUp(100, 1000, 40));
        assertEquals(40, VampireBloodMath.costRoundUp(2000, 1000, 40));
        assertThrows(IllegalArgumentException.class, () -> VampireBloodMath.project(3, 0, 500));
        assertThrows(IllegalArgumentException.class, () -> VampireBloodMath.costRoundUp(-1, 500, 40));
    }
}
