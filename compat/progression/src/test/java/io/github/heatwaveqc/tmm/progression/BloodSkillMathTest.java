package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.foundation.BloodSkillMath;
import io.github.heatwaveqc.tmm.progression.data.ActivationSpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class BloodSkillMathTest{
 @Test void priceHas25PercentCeilingAndPositiveFloor(){
    assertEquals(75,BloodSkillMath.price(100,.25));assertEquals(75,BloodSkillMath.price(100,1));
    assertEquals(1,BloodSkillMath.price(1,.25));assertEquals(4,BloodSkillMath.price(5,.25));
    assertEquals(100,BloodSkillMath.price(100,Double.NaN));assertEquals(0,BloodSkillMath.price(0,.25));
 }
 @Test void feedingUsesActualGainAndRemainingRoom(){
    assertEquals(20,BloodSkillMath.feedingBonus(100,100,.2));assertEquals(10,BloodSkillMath.feedingBonus(100,10,.2));
    assertEquals(0,BloodSkillMath.feedingBonus(0,100,.2));
 }
 @Test void sunlightNeverNegatesNativeDeathOrPositiveDamage(){
    assertEquals(1.5f,BloodSkillMath.sunDamage(2,.25));assertEquals(Float.MAX_VALUE,BloodSkillMath.sunDamage(Float.MAX_VALUE,.75));
    assertEquals(.5f,BloodSkillMath.sunDamage(2,1));
 }
 @Test void healingRequiresPaidTimedActivation(){
    assertThrows(IllegalArgumentException.class,()->new ActivationSpec("timed",20,20,0,4));
    assertThrows(IllegalArgumentException.class,()->new ActivationSpec("toggle",0,20,20,4));
    assertEquals(4,new ActivationSpec("timed",20,20,20,4).jawrHealOnActivation());
 }
}
