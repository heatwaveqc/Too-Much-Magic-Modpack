package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.foundation.LichSkillMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class LichSkillMathTest{
 @Test void soulGainAndCapacityDoNotChangeNativeBase(){
    assertEquals(1200,LichSkillMath.capacity(1000,.2));assertEquals(1000,LichSkillMath.capacity(1000,0));
    assertEquals(120,LichSkillMath.gain(100,.2));assertEquals(0,LichSkillMath.gain(0,1));
 }
 @Test void servantLimitNeverBypassesDisabledOrReducesNativeLimit(){
    assertEquals(12,LichSkillMath.servantLimit(10,2.9));
    assertEquals(64,LichSkillMath.servantLimit(60,16));
    assertEquals(80,LichSkillMath.servantLimit(80,16));
    assertEquals(0,LichSkillMath.servantLimit(0,16));
    assertEquals(10,LichSkillMath.servantLimit(10,Double.NaN));
 }
 @Test void rebateNeverFundsItself(){assertEquals(25,LichSkillMath.rebate(100,.25));assertEquals(25,LichSkillMath.rebate(100,1));assertEquals(0,LichSkillMath.rebate(1,1));}
 @Test void combatAndRescueBoundaries(){assertEquals(12,LichSkillMath.damage(10,.2,0),1e-5);assertEquals(5,LichSkillMath.damage(10,0,1),1e-5);
    assertTrue(LichSkillMath.rescue(false,true,true,true,1));assertFalse(LichSkillMath.rescue(true,true,true,true,1));assertFalse(LichSkillMath.rescue(false,true,false,true,1));assertFalse(LichSkillMath.rescue(false,true,true,false,1));}
}
