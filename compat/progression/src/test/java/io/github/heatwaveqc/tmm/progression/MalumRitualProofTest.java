package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.mxp.core.MalumRitualCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.MalumRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import java.util.UUID;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MalumRitualProofTest {
    @Test void exactCatalogAndFirstOnly(){
        assertEquals(26,MalumRitualCatalog.rites().size());
        assertEquals(38,MalumRitualCatalog.geas().size());
        assertEquals(36,MalumRitualCatalog.geas().keySet().stream().filter(x->x.startsWith("malum:")).count());
        assertEquals(2,MalumRitualCatalog.geas().keySet().stream().filter(x->x.startsWith("gaze:")).count());
        assertNull(MalumRitualCatalog.lookupGeas("malum:creed_of_the_blight_eater"));
        assertNull(MalumRitualCatalog.riteFamily("gaze:gaze_lesser_arcane_rite"));
        assertNull(RitualRewards.lookup("malum:spirit_infusion"));
        for(String rite:MalumRitualCatalog.rites()){var rule=RitualRewards.lookup(MalumRitualCatalog.riteFamily(rite));assertEquals(500,rule.band().first());assertEquals(0,rule.band().repeat());}
        for(var geas:MalumRitualCatalog.geas().values()){var rule=RitualRewards.lookup(geas.family());assertEquals(1500,rule.band().first());assertEquals(0,rule.band().repeat());}
    }
    @Test void commissionedRiteNotPulseOrFailedAssembly(){
        UUID actor=UUID.randomUUID(),op=UUID.randomUUID();
        var c=new MalumRitualProof.Commission(actor,op);
        assertNull(c.receipt("malum:rite_of_healing",false));
        assertNull(c.receipt("malum:unknown",true));
        assertEquals("ritual:"+op,c.receipt("malum:rite_of_healing",true).transaction());
    }
    @Test void onlyActualVoluntaryRecipientsAndNativeSuccess(){
        UUID actor=UUID.randomUUID(),target=UUID.randomUUID(),other=UUID.randomUUID(),op=UUID.randomUUID();
        var b=new MalumRitualProof.Binding(actor,op,"gaze:domain_of_swords");
        var nativeIds=Set.of(actor,target,other);
        assertNotNull(b.receipt(actor,nativeIds,Set.of(),true,true));
        assertNotNull(b.receipt(target,nativeIds,Set.of(target),true,true));
        assertNull(b.receipt(other,nativeIds,Set.of(target),true,true));
        assertNull(b.receipt(target,Set.of(),Set.of(target),true,true));
        assertNull(b.receipt(target,nativeIds,Set.of(target),false,true));
        assertNull(b.receipt(target,nativeIds,Set.of(target),true,false));
        assertFalse(b.matches("gaze:pact_of_encroaching"));
        assertEquals("ritual:"+op,b.receipt(target,nativeIds,Set.of(target),true,true).transaction());
    }
}
