package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.MahouPracticeReceipt;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MahouPracticeReceiptTest {
 @Test void mixedPersonalGemCircuitPayment(){
    var r=new MahouPracticeReceipt(100);
    r.observe(20,0);r.observe(100,70);r.observe(1000,950);
    assertEquals(100,r.accepted(100));
    assertEquals(0,r.accepted(99));
 }
 @Test void partialChargingPracticeCountsOnlyWhatWasActuallySpent(){
    var r=new MahouPracticeReceipt(100);
    r.observe(30,0);
    assertEquals(30,r.accepted(30));
    assertEquals(0,r.accepted(100));
 }
 @Test void rejectFreeNegativeOverDebitAndAmbiguousReceipt(){
    var free=new MahouPracticeReceipt(100);assertEquals(0,free.accepted(100));
    var negative=new MahouPracticeReceipt(10);negative.observe(-1,0);assertEquals(0,negative.accepted(1));
    var increase=new MahouPracticeReceipt(10);increase.observe(1,2);increase.add(10);assertEquals(0,increase.accepted(10));
    var excessive=new MahouPracticeReceipt(10);excessive.observe(11,0);assertEquals(0,excessive.accepted(11));
    var nested=new MahouPracticeReceipt(10);nested.add(10);nested.invalidate();assertEquals(0,nested.accepted(10));
 }
}
