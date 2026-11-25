package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.core.HunterXpMath;
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

    @Test void unifiedHunterCalibration(){
        assertEquals(4000.0,HunterXpMath.combatMxp(20,200,2,false).doubleValue());
        assertEquals(8000.0,HunterXpMath.combatMxp(20,200,2,true).doubleValue());
        assertEquals(850.0,HunterXpMath.combatMxp(4.25,200,2,false).doubleValue());
        assertEquals(4000000.0,HunterXpMath.combatMxp(10000,200,2,true).doubleValue());
        assertEquals(300,HunterXpMath.alchemyMxp(100,2,0,3600,2,false).intValueExact());
        assertEquals(600,HunterXpMath.alchemyMxp(100,2,0,3600,2,true).intValueExact());
        assertEquals(1218,HunterXpMath.alchemyMxp(100,6,1,1800,2,false).intValueExact());
    }
}
