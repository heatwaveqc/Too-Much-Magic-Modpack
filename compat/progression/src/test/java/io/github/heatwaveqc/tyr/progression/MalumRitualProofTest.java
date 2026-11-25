package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.MalumRitualCatalog;
import io.github.heatwaveqc.tyr.progression.mxp.core.MalumRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards;
import java.util.UUID;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MalumRitualProofTest {
    @Test void exactCatalogAndFirstOnly(){
        assertEquals(52,MalumRitualCatalog.rites().size());
        assertEquals(26,MalumRitualCatalog.rites().stream().filter(x->x.startsWith("malum:gaze_")||x.startsWith("malum:corrupt_gaze_")).count());
        assertEquals(38,MalumRitualCatalog.geas().size());
        assertEquals(36,MalumRitualCatalog.geas().keySet().stream().filter(x->x.startsWith("malum:")).count());
        assertEquals(2,MalumRitualCatalog.geas().keySet().stream().filter(x->x.startsWith("gaze:")).count());
        assertNull(MalumRitualCatalog.lookupGeas("malum:creed_of_the_blight_eater"));
        assertNull(MalumRitualCatalog.riteFamily("gaze:gaze_lesser_arcane_rite"));
        assertNull(RitualRewards.lookup("malum:spirit_infusion"));
        for(String rite:MalumRitualCatalog.rites()){var rule=RitualRewards.lookup(MalumRitualCatalog.riteFamily(rite));assertEquals(500,rule.band().first());assertEquals(0,rule.band().repeat());}
        for(var geas:MalumRitualCatalog.geas().values()){var rule=RitualRewards.lookup(geas.family());assertEquals(1500,rule.band().first());assertEquals(0,rule.band().repeat());}
    }
    @Test void pinnedGazeRegistryHasAllTwentySixDistinctRites() {
        var expected=Set.of(
            "malum:gaze_lesser_arcane_rite",
            "malum:gaze_greater_arcane_rite",
            "malum:gaze_lesser_sacred_rite",
            "malum:corrupt_gaze_lesser_sacred_rite",
            "malum:gaze_greater_sacred_rite",
            "malum:corrupt_gaze_greater_sacred_rite",
            "malum:gaze_lesser_aerial_rite",
            "malum:corrupt_gaze_lesser_aerial_rite",
            "malum:gaze_greater_aerial_rite",
            "malum:corrupt_gaze_greater_aerial_rite",
            "malum:gaze_lesser_aqueous_rite",
            "malum:corrupt_gaze_lesser_aqueous_rite",
            "malum:gaze_greater_aqueous_rite",
            "malum:corrupt_gaze_greater_aqueous_rite",
            "malum:gaze_lesser_earthen_rite",
            "malum:corrupt_gaze_lesser_earthen_rite",
            "malum:gaze_greater_earthen_rite",
            "malum:corrupt_gaze_greater_earthen_rite",
            "malum:gaze_lesser_infernal_rite",
            "malum:corrupt_gaze_lesser_infernal_rite",
            "malum:gaze_greater_infernal_rite",
            "malum:corrupt_gaze_greater_infernal_rite",
            "malum:gaze_lesser_wicked_rite",
            "malum:corrupt_gaze_lesser_wicked_rite",
            "malum:gaze_greater_wicked_rite",
            "malum:corrupt_gaze_greater_wicked_rite"
        );
        var actual=MalumRitualCatalog.rites().stream()
            .filter(x->x.startsWith("malum:gaze_")||x.startsWith("malum:corrupt_gaze_"))
            .collect(java.util.stream.Collectors.toUnmodifiableSet());
        assertEquals(expected,actual);
        assertEquals(26,actual.size());
        for(String id:expected){
            String family="malum:totem/"+id.substring("malum:".length());
            assertEquals(family,MalumRitualCatalog.riteFamily(id));
            var rule=RitualRewards.lookup(family);
            assertNotNull(rule,id);
            assertEquals(RitualRewards.Band.MALUM_TOTEM,rule.band());
            assertEquals(500,rule.band().first());
            assertEquals(0,rule.band().repeat());
        }
        assertNull(MalumRitualCatalog.riteFamily("gaze:gaze_lesser_arcane_rite"));
        assertNull(MalumRitualCatalog.riteFamily("malum:corrupt_gaze_lesser_arcane_rite"));
        assertNull(MalumRitualCatalog.riteFamily("malum:gaze_typo_rite"));
    }
    @Test void gazeReceiptUsesNativeIdentityAndOnlySuccessfulCommission(){
        var actor=UUID.randomUUID();
        var op=UUID.randomUUID();
        var proof=new MalumRitualProof.Commission(actor,op);
        var receipt=proof.receipt("malum:gaze_lesser_arcane_rite",true);
        assertNotNull(receipt);
        assertEquals("malum:totem/gaze_lesser_arcane_rite",receipt.recipeId());
        assertEquals("ritual:"+op,receipt.transaction());
        assertNotNull(receipt.rule());
        assertEquals(0,receipt.rule().band().repeat());
        assertNull(proof.receipt("malum:gaze_lesser_arcane_rite",false));
        assertNull(proof.receipt("gaze:gaze_lesser_arcane_rite",true));
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
