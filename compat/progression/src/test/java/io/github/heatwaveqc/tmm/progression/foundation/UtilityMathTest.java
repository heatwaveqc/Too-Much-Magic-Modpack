package io.github.heatwaveqc.tmm.progression.foundation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class UtilityMathTest{
    @Test void bonusUsesUnbiasedFractionAndBound(){assertEquals(1,UtilityMath.bonus(3,.25,.2,20));assertEquals(0,UtilityMath.bonus(3,.25,.8,20));assertEquals(20,UtilityMath.bonus(20,2,0,20));assertEquals(0,UtilityMath.bonus(-5,2,0,20));}
    @Test void virtualLevelsBounded(){assertEquals(0,UtilityMath.virtualLevel(-1));assertEquals(3,UtilityMath.virtualLevel(3.99));assertEquals(8,UtilityMath.virtualLevel(500));}
}
