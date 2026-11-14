package io.github.heatwaveqc.tmm.transformations.vampire;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Pure contract tests independent of gameplay runtime. */
class VampireChassisContractTest {
    @Test void bloodConvertRoundTripIsBounded() {
        for (int a=1; a<=1000; a+=23) {
            int converted = VampireBloodMath.project(a, 1000, 40);
            assertTrue(converted >= 0 && converted <= 40);
            assertTrue(VampireBloodMath.costRoundUp(a, 1000, 40) >= converted);
        }
    }

    @Test void smallJAWRCostsRoundUpRatherThanBecomeFree() {
        assertEquals(1, VampireBloodMath.costRoundUp(1, 300, 20));
        assertEquals(1, VampireBloodMath.costRoundUp(4, 300, 20));
        assertEquals(20, VampireBloodMath.costRoundUp(300, 300, 20));
    }
}
