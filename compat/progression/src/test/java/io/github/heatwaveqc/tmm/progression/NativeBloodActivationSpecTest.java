package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.ActivationSpec;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class NativeBloodActivationSpecTest{
    @Test void optionalCostPreservesLegacy(){
        assertEquals(0,new ActivationSpec("toggle",0,20).jawrBloodCost());
        var s=ActivationSpec.read(JsonParser.parseString("{\"activation\":{\"mode\":\"timed\",\"duration_ticks\":200,\"cooldown_ticks\":100,\"jawr_blood_cost\":30}}").getAsJsonObject());
        assertEquals(30,s.jawrBloodCost());
        var active=ActivationSpec.State.empty().request(s,100);
        assertEquals(300,active.until());assertEquals(400,active.ready());
        assertEquals(active,active.request(s,101));
        assertEquals(active,active.request(s,301));
    }
    @Test void rejectNegativeOrPermanentPaidStance(){
        assertThrows(IllegalArgumentException.class,()->new ActivationSpec("toggle",0,20,30));
        assertThrows(IllegalArgumentException.class,()->new ActivationSpec("timed",100,0,-1));
        assertThrows(IllegalArgumentException.class,()->new ActivationSpec("timed",100,0,1000001));
        assertThrows(ArithmeticException.class,()->ActivationSpec.read(JsonParser.parseString("{\"activation\":{\"mode\":\"timed\",\"duration_ticks\":200,\"jawr_blood_cost\":1.5}}").getAsJsonObject()));
    }
}
