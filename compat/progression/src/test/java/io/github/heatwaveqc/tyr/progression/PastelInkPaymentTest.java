package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.mxp.core.InkPaymentReceipt;
import io.github.heatwaveqc.tyr.progression.mxp.core.PastelInkValuation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PastelInkPaymentTest {
    @Test void allSixteenFrozenSurveyAnchors(){
        assertEquals(16,PastelInkValuation.DYE_EMC.size());
        for(var e:PastelInkValuation.DYE_EMC.entrySet()){
            assertEquals(1L,e.getValue().longValue());
            assertEquals(5000,PastelInkValuation.reference(e.getKey(),e.getValue()));
        }
        assertEquals(2500,PastelInkValuation.reference("red",2));
        assertEquals(0,PastelInkValuation.reference("red",0));
        assertEquals(0,PastelInkValuation.reference("unknown",1));
        assertEquals(0,PastelInkValuation.reference("red",Double.NaN));
    }
    @Test void correctMultiReservoirPayment(){
        var r=new InkPaymentReceipt(200);
        r.observe(200,0,-1,-1);
        r.observe(200,50,50,0);
        r.observe(150,150,400,250);
        assertEquals(200,r.paid());
        assertTrue(r.complete(true));
        assertFalse(r.complete(false));
    }
    @Test void rejectPartialFakeExtraAndRepeatedPrice(){
        var partial=new InkPaymentReceipt(200);partial.observe(200,20,20,0);assertFalse(partial.complete(true));
        var fake=new InkPaymentReceipt(200);fake.observe(200,200,1000,1000);assertFalse(fake.complete(true));
        var extra=new InkPaymentReceipt(200);extra.observe(200,201,201,0);assertFalse(extra.complete(true));
        var duplicate=new InkPaymentReceipt(200);duplicate.observe(200,50,50,0);duplicate.observe(200,150,150,0);assertFalse(duplicate.complete(true));
        var free=new InkPaymentReceipt(0);assertFalse(free.complete(true));
        var nested=new InkPaymentReceipt(20);nested.observe(20,20,20,0);nested.invalidate();assertFalse(nested.complete(true));
        var unobservable=new InkPaymentReceipt(10);unobservable.observe(10,10,-1,-1);assertFalse(unobservable.complete(true));
    }
}
