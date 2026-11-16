package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.GildedLootingMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GildedLootingMathTest {
    @Test void baselineAndSeparatePurchases() {
        assertEquals(0,GildedLootingMath.virtualBonus(false,0));
        assertEquals(3,GildedLootingMath.virtualBonus(true,0));
        for(int i=0;i<=7;i++)
            assertEquals(3+i,GildedLootingMath.virtualBonus(true,i));
        assertEquals(10,GildedLootingMath.virtualBonus(true,20));
        assertEquals(0,GildedLootingMath.virtualBonus(false,7));
        assertEquals(3,GildedLootingMath.virtualBonus(true,-2));
        assertEquals(13,GildedLootingMath.effectiveLooting(3,true,7));
        assertEquals(18,GildedLootingMath.effectiveLooting(8,true,7));
    }
}
