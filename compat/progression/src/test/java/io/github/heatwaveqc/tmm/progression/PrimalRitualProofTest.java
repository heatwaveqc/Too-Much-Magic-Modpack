package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.mxp.core.PrimalRitualCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.PrimalRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class PrimalRitualProofTest {
    @Test void nativeCatalogIsExactAndFamiliesUseNativeExpertiseGroups(){
        assertEquals(76,PrimalRitualCatalog.entries().size());
        assertEquals(76,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("primalmagick:")).count());
        assertEquals(25,PrimalRitualCatalog.entries().values().stream().map(e->e.rule().family()).distinct().count());
        assertEquals(RitualRewards.lookup("primalmagick:pixie_basic_blood").family(),RitualRewards.lookup("primalmagick:pixie_basic_earth").family());
        assertNotEquals(RitualRewards.lookup("primalmagick:pixie_basic_blood").family(),RitualRewards.lookup("primalmagick:pixie_grand_blood").family());
        assertEquals(5000,RitualRewards.lookup("primalmagick:pure_primal_wand_core_item").band().first());
        assertNull(RitualRewards.lookup("primalmagick:ritual_altar"));
        assertNull(RitualRewards.lookup("primalmagick:unknown"));
        var e=PrimalRitualCatalog.lookup("primalmagick:blood_rose");
        assertTrue(e.matches("primalmagick:blood_rose",2,4,2,5));
        assertFalse(e.matches("primalmagick:blood_rose",1,4,2,5));
        assertFalse(e.matches("primalmagick:blood_rose",2,0,2,5));
    }
    @Test void exactActorIdentityAndTerminalProof(){
        String id="primalmagick:manafruit";UUID actor=UUID.randomUUID(),transaction=UUID.randomUUID(),other=UUID.randomUUID();
        var op=new PrimalRitualProof(id,actor,transaction,false);
        assertTrue(op.matches(true,id,actor));
        assertFalse(op.matches(true,id,null));
        assertFalse(op.matches(true,id,other));
        assertFalse(op.matches(false,id,actor));
        assertFalse(op.matches(true,"primalmagick:blood_rose",actor));
        var restored=new PrimalRitualProof(id,actor,transaction,true);
        assertTrue(restored.matches(true,id,null));
        assertFalse(restored.matches(true,id,other));
        assertNull(restored.receipt(false,true).rule());
        assertNull(restored.receipt(true,false).rule());
        assertNotNull(restored.receipt(true,true).rule());
        assertEquals(op.receipt(true,true).transaction(),restored.receipt(true,true).transaction());
    }
}
