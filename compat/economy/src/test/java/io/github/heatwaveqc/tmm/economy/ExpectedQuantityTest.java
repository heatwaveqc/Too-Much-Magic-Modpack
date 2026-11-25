package io.github.heatwaveqc.tmm.economy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpectedQuantityTest {
    @Test void probabilityAndScale() {
        var a = ExpectedQuantity.probability(0.25F).times(2);
        var b = ExpectedQuantity.probability(0.2F).times(3);
        assertEquals(10, ExpectedQuantity.scale(a,b));
        assertEquals(5,a.countAt(10));
        assertEquals(6,b.countAt(10));
        assertEquals(11,a.plus(b).countAt(10));
        assertEquals(1,ExpectedQuantity.probability(2F).countAt(1));
        assertThrows(IllegalArgumentException.class,()->ExpectedQuantity.probability(Float.NaN));
    }
    @Test void growth() {
        assertEquals(120,ExpectedQuantity.growthRolls(480,2,8F));
        assertEquals(60,ExpectedQuantity.growthRolls(480,2,16F));
        assertEquals(6,ExpectedQuantity.growthRolls(5,2,2F));
        assertEquals(0,ExpectedQuantity.growthRolls(480,0,8F));
        assertThrows(IllegalArgumentException.class,()->ExpectedQuantity.growthRolls(10,1,0F));
        assertThrows(IllegalArgumentException.class,()->ExpectedQuantity.of(0.0000001F).countAt(1));
    }
}
