package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.mxp.core.GoetyRitualCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.GoetyRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoetyRitualProofTest {
    @Test void exactSingularCensusAndAliases(){
        assertEquals(407,GoetyRitualCatalog.entries().size());
        assertEquals(2,GoetyRitualCatalog.excluded().size());
        assertEquals(358,GoetyRitualCatalog.entries().keySet().stream().filter(id->id.startsWith("goety:")).count());
        assertEquals(49,GoetyRitualCatalog.entries().keySet().stream().filter(id->id.startsWith("goety_cataclysm:")).count());
        assertEquals(407,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("goety:")||id.startsWith("goety_cataclysm:")).count());
        assertEquals(386,GoetyRitualCatalog.entries().values().stream().map(e->e.family()).distinct().count());
        assertEquals(RitualRewards.lookup("goety:focus/blossoming_focus").family(),RitualRewards.lookup("goety:focus/blossoming_focus_bomd").family());
        assertEquals(RitualRewards.lookup("goety:plinth_dark").family(),RitualRewards.lookup("goety:plinth_stone").family());
        assertNotEquals(RitualRewards.lookup("goety:convert_hoglin_to_hoglin_servant").family(),RitualRewards.lookup("goety:thrall/summon_hoglin_servant_thrall").family());
        assertEquals(5000,RitualRewards.lookup("goety:undeath_potion").band().first());
        assertNull(RitualRewards.lookup("goety:summon_apostle"));
        assertEquals(1000,RitualRewards.lookup("goety:summon_storm").band().first());
        assertNull(RitualRewards.lookup("goety:unknown"));
        var e=GoetyRitualCatalog.lookup("goety:animation_core");
        assertTrue(e.matches("goety:craft","goety:animation_core",2,"animation",1,10,4,"","","",0));
        assertFalse(e.matches("goety:craft","goety:animation_core",1,"animation",1,10,4,"","","",0));
    }
    @Test void receiptsRequireOriginalOwnerAndSuccessfulClear(){
        UUID actor=UUID.randomUUID(),operation=UUID.randomUUID();
        String id="goety:arca";
        var op=new GoetyRitualProof(id,actor,operation);
        assertTrue(op.matches(id,actor));
        assertFalse(op.matches(id,null));
        assertFalse(op.matches(id,UUID.randomUUID()));
        assertFalse(op.matches("goety:undeath_potion",actor));
        assertNull(op.receipt(false,true).rule());assertNull(op.receipt(true,false).rule());
        assertEquals("ritual:"+operation,op.receipt(true,true).transaction());
    }
}
