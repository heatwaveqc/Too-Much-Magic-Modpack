package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.GildedFortuneMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GildedFortuneMathTest {
    @Test void baselineAppliesImmediatelyAfterGildedEnrollment() {
        assertEquals(0,GildedFortuneMath.virtualBonus(false,0));
        assertEquals(3,GildedFortuneMath.virtualBonus(true,0));
        assertEquals(6,GildedFortuneMath.totalEffectiveFortune(3,true,0));
    }
    @Test void sevenTyzRanksReachTenWithoutCappingRealEnchantments() {
        for(int i=0;i<=7;i++)
            assertEquals(3+i,GildedFortuneMath.virtualBonus(true,i));
        assertEquals(10,GildedFortuneMath.virtualBonus(true,99));
        assertEquals(10,GildedFortuneMath.virtualBonus(true,7));
        assertEquals(13,GildedFortuneMath.totalEffectiveFortune(3,true,7));
        assertEquals(28,GildedFortuneMath.totalEffectiveFortune(18,true,7));
        assertEquals(0,GildedFortuneMath.virtualBonus(false,7));
        assertEquals(3,GildedFortuneMath.virtualBonus(true,-1));
    }
}
