package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.MultiSourcePayment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MultiSourcePaymentTest {
 @Test void aggregateOnlyFullyPaidSources(){
   Object earth=new Object(),sea=new Object();var p=new MultiSourcePayment();
   p.expect(earth,300);p.expect(sea,200);
   p.observe(earth,250,0,250);
   assertFalse(p.complete());
   p.observe(sea,150,0,150);
   assertTrue(p.complete());assertEquals(500,p.nominal());assertEquals(400,p.actual());
 }
 @Test void partialInfiniteAndDoubleDebitFailClosed(){
   Object source=new Object();
   var partial=new MultiSourcePayment();partial.expect(source,100);partial.observe(source,100,30,70);assertFalse(partial.complete());
   var infinite=new MultiSourcePayment();infinite.expect(source,100);assertFalse(infinite.complete());
   var duplicate=new MultiSourcePayment();duplicate.expect(source,100);duplicate.observe(source,80,0,80);duplicate.observe(source,80,0,80);assertFalse(duplicate.complete());
 }
 @Test void zeroCostSourceIsNotExtraNominal(){
   Object zero=new Object(),paid=new Object();var p=new MultiSourcePayment();p.expect(zero,0);p.expect(paid,50);p.observe(paid,25,0,25);
   assertTrue(p.complete());assertEquals(50,p.nominal());assertEquals(25,p.actual());
 }
}
