package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.ObservedPayment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ObservedPaymentTest{
 @Test void requiresActualFiniteDecrease(){
    assertEquals(10,ObservedPayment.proven(30,20,10,10));
    assertEquals(0,ObservedPayment.proven(30,30,10,10));
    assertEquals(0,ObservedPayment.proven(Double.POSITIVE_INFINITY,20,10,10));
    assertEquals(0,ObservedPayment.proven(30,-1,10,10));
 }
 @Test void wastedMediaIsNotAdditionalExperience(){
    assertEquals(10,ObservedPayment.proven(50000,0,50000,10));
    assertEquals(50000,ObservedPayment.proven(100000,50000,50000,50000));
 }
 @Test void partialAndFreePaymentsDoNotFundNominal(){
    assertFalse(ObservedPayment.funded(10,20));assertFalse(ObservedPayment.funded(0,0));
    assertTrue(ObservedPayment.funded(25,25));assertTrue(ObservedPayment.funded(0.1+0.2,0.3));
    assertFalse(ObservedPayment.funded(25,20));
 }
}
