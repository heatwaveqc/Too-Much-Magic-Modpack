package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.HunterXpMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HunterXpMathTest {
    @Test void vanillaHealthQuarterAndSingleSpecialtyDouble() {
        assertEquals(5,HunterXpMath.killXp(20,4,2,1,false));
        assertEquals(10,HunterXpMath.killXp(20,4,2,1,true));
        assertEquals(125,HunterXpMath.killXp(500,4,2,1,false));
        assertEquals(250,HunterXpMath.killXp(500,4,2,1,true));
        assertEquals(1,HunterXpMath.killXp(1,4,2,1,false));
        assertEquals(0,HunterXpMath.killXp(Double.NaN,4,2,1,false));
        assertEquals(1_000_000L,HunterXpMath.killXp(100_000_000,4,2,1,true));
    }

    @Test void potionWeightPotencyDurationAndNoveltyScaleWithoutOverflow() {
        long simple = HunterXpMath.potionXp(2,2,0,1200,2,false);
        long stronger = HunterXpMath.potionXp(2,2,1,1200,2,false);
        long longer = HunterXpMath.potionXp(2,2,0,7200,2,false);
        long rarer = HunterXpMath.potionXp(2,5,0,1200,2,false);
        assertTrue(stronger > simple);
        assertTrue(longer > simple);
        assertTrue(rarer > simple);
        assertEquals(simple * 2,HunterXpMath.potionXp(2,2,0,1200,2,true));
        assertEquals(0,HunterXpMath.potionXp(2,0,0,1200,2,true));
        assertEquals(0,HunterXpMath.potionXp(2,2,-1,1200,2,true));
        assertEquals(1_000_000L,HunterXpMath.potionXp(1000,16,10,720000,20,true));
    }
}
