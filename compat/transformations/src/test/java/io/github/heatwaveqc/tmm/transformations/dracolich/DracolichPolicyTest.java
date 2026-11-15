package io.github.heatwaveqc.tmm.transformations.dracolich;

import static io.github.heatwaveqc.tmm.transformations.dracolich.DracolichPolicy.Source.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DracolichPolicyTest {
    @Test void eitherSingleLichSourceCanPowerADracolich() {
        assertEquals(GOETY_ARCA, DracolichPolicy.resolve(true, true, false));
        assertEquals(FORBIDDEN_MAGIC_PHYLACTERY, DracolichPolicy.resolve(true, false, true));
        assertTrue(DracolichPolicy.isDracolich(GOETY_ARCA));
        assertTrue(DracolichPolicy.isDracolich(FORBIDDEN_MAGIC_PHYLACTERY));
    }

    @Test void twoLichesPlusDragonIsConflict() {
        assertEquals(CONFLICT, DracolichPolicy.resolve(true, true, true));
        assertFalse(DracolichPolicy.isDracolich(CONFLICT));
    }

    @Test void pureDragonOrPureLichIsNotDracolich() {
        assertEquals(NONE, DracolichPolicy.resolve(true, false, false));
        assertEquals(NONE, DracolichPolicy.resolve(false, true, false));
        assertEquals(NONE, DracolichPolicy.resolve(false, false, true));
        assertEquals(NONE, DracolichPolicy.resolve(false, true, true));
    }
}
