package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tyr.progression.core.ChannelEngine;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RootsRitualProofTest {
    private static final String PREFIX="rootsclassic:ritual/";
    @Test void actualStartNotInteractionResult(){
        assertTrue(RootsRitualProof.started(0,200));assertFalse(RootsRitualProof.started(0,0));assertFalse(RootsRitualProof.started(200,200));assertFalse(RootsRitualProof.started(1,0));
    }
    @Test void nativeMetadataAndExactOutcome(){
        var blade=RootsRitualProof.rule(PREFIX+"crafting/engraved_crafting");
        assertEquals(Kind.ITEM,blade.kind());
        assertTrue(blade.matches(2,"rootsclassic:engraved_crafting","elucent.rootsclassic.ritual.rituals.RitualEngravedSword"));
        assertFalse(blade.matches(0,"rootsclassic:engraved_crafting","elucent.rootsclassic.ritual.rituals.RitualEngravedSword"));
        assertFalse(blade.matches(2,"rootsclassic:crafting","elucent.rootsclassic.ritual.rituals.RitualCrafting"));
        assertTrue(blade.resultMatches("rootsclassic:engraved_blade",1));assertFalse(blade.resultMatches("rootsclassic:engraved_crafting",1));assertFalse(blade.resultMatches("rootsclassic:engraved_blade",0));
        var cow=RootsRitualProof.rule(PREFIX+"summoning/cow");assertEquals(1,cow.level());assertEquals(Kind.SUMMON,cow.kind());assertTrue(cow.resultMatches("minecraft:cow",1));assertFalse(cow.resultMatches("minecraft:pig",1));
    }
    @Test void noEffectAndReceiptIdentity(){
        assertTrue(RootsRitualProof.weatherChanged(Kind.RAIN_ON,false,true));assertFalse(RootsRitualProof.weatherChanged(Kind.RAIN_ON,true,true));assertFalse(RootsRitualProof.weatherChanged(Kind.RAIN_ON,true,false));assertTrue(RootsRitualProof.weatherChanged(Kind.RAIN_OFF,true,false));
        assertFalse(RootsRitualProof.timeAdvanced(1000,1000));assertTrue(RootsRitualProof.timeAdvanced(1000,2000));
        assertFalse(RootsRitualProof.damageObserved(false,10,1));assertFalse(RootsRitualProof.damageObserved(true,10,10));assertTrue(RootsRitualProof.damageObserved(true,10,1));
        var actor=UUID.randomUUID();var transaction=UUID.randomUUID();var op=new RootsRitualProof.Operation(PREFIX+"grow",actor,transaction);
        assertEquals(actor,op.receipt().actor());assertEquals("ritual:"+transaction,op.receipt().transaction());assertThrows(IllegalArgumentException.class,()->new RootsRitualProof.Operation(PREFIX+"grow",null,transaction));
    }
    @Test void exactCatalogAndCanonicalGearFamilies(){
        var roots=RitualRewards.rules().keySet().stream().filter(id->id.startsWith(PREFIX)).toList();assertEquals(43,roots.size());
        for(String id:roots)assertNotNull(RootsRitualProof.rule(id),id);
        for(String held:java.util.List.of("imbuer","sacrifice","crafting/aesthetic_standing_stone","unknown"))assertNull(RootsRitualProof.rule(PREFIX+held));
        var a=RitualRewards.lookup(PREFIX+"crafting/living_axe");var b=RitualRewards.lookup(PREFIX+"crafting/living_sword");assertEquals(a.family(),b.family());
        var mxp=XpChannel.mxp(1.02);var ledger=ChannelEngine.magic(Ledger.empty(),mxp,"ritual:a","ritual:"+a.family(),a.band().first(),a.band().repeat(),true,false);
        var after=ChannelEngine.magic(ledger,mxp,"ritual:b","ritual:"+b.family(),b.band().first(),b.band().repeat(),true,false);
        assertEquals(120,after.wallet(mxp.id()).magicProgress(),1e-8);assertEquals(1,after.wallet(mxp.id()).firstCompletions().size());
    }
}
