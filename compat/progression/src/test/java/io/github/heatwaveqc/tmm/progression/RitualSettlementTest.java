package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.core.ChannelEngine;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RitualSettlementTest {
    @Test void explicitForgeRulesUseRequiredTier(){
        assertEquals(22,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("forbidden_arcanus:")).count());
        assertEquals(1000,RitualRewards.lookup("forbidden_arcanus:upgrade_tier_2").band().first());
        assertEquals(3500,RitualRewards.lookup("forbidden_arcanus:upgrade_tier_5").band().first());
        assertEquals(480,RitualRewards.lookup("forbidden_arcanus:eternal_stella").band().repeat());
        assertNull(RitualRewards.lookup("forbidden_arcanus:unknown"));
        assertNull(RitualRewards.lookup("neovitae:ritual"));
    }
    @Test void pinnedCatalogAliasesAndHeldRecipes(){
        assertEquals(113,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("occultism:")).count());
        assertEquals(36,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("rootsclassic:")).count());
        assertEquals(RitualRewards.lookup("occultism:ritual/craft_stabilizer_tier1").family(),
            RitualRewards.lookup("occultism:ritual/craft_stabilizer_tier1_dark").family());
        assertEquals(RitualRewards.lookup("rootsclassic:ritual/crafting/living_pickaxe").family(),
            RitualRewards.lookup("rootsclassic:ritual/crafting/living_sword").family());
        assertNull(RitualRewards.lookup("occultism:ritual/repair_armors"));
        assertNull(RitualRewards.lookup("rootsclassic:ritual/sacrifice"));
    }
    @Test void forgeProofRejectsUnchangedTierWrongItemOrWrongRequirement(){
        var id="forbidden_arcanus:eternal_stella";
        assertTrue(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.matchesTier(id,3));
        assertFalse(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.matchesTier(id,5));
        assertTrue(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.itemResult(id,id,1));
        assertFalse(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.itemResult(id,"minecraft:diamond",1));
        assertFalse(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.itemResult(id,id,0));
        var upgrade="forbidden_arcanus:upgrade_tier_5";
        assertTrue(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.actualUpgrade(upgrade,4,5));
        assertFalse(io.github.heatwaveqc.tmm.progression.mxp.core.ForgeRitualProof.actualUpgrade(upgrade,5,5));
    }
    @Test void receiptsFailClosed(){
        UUID operation=UUID.randomUUID(), actor=UUID.randomUUID();
        String id="forbidden_arcanus:eternal_stella";
        var valid=new RitualRewards.Receipt(id,operation,actor,true,false);
        assertNotNull(valid.rule());
        assertEquals("ritual:"+operation,valid.transaction());
        assertNull(new RitualRewards.Receipt(id,operation,actor,false,false).rule());
        assertNull(new RitualRewards.Receipt(id,operation,actor,true,true).rule());
        assertNull(new RitualRewards.Receipt(id,null,actor,true,false).rule());
        assertNull(new RitualRewards.Receipt(id,operation,null,true,false).rule());
        assertNull(new RitualRewards.Receipt("forbidden_arcanus:unlisted",operation,actor,true,false).rule());
    }
    @Test void firstFixedRepeatDividedAndDuplicate(){
        var mxp=XpChannel.mxp(1.02);
        var band=RitualRewards.Band.FORGE_1;
        var ledger=ChannelEngine.magic(Ledger.empty(),mxp,"ritual:first","ritual:forge:one",band.first(),band.repeat(),true,false);
        assertEquals(1,ledger.wallet(mxp.id()).magicLevels());
        assertSame(ledger,ChannelEngine.magic(ledger,mxp,"ritual:first","ritual:forge:one",band.first(),band.repeat(),true,false));
        var repeat=ChannelEngine.magic(ledger,mxp,"ritual:second","ritual:forge:one",band.first(),band.repeat(),true,false);
        assertEquals(200/1.02,repeat.wallet(mxp.id()).magicProgress(),1e-8);
    }
    @Test void firstOnlyDoesNotThrowOrAwardAgain(){
        var mxp=XpChannel.mxp(1.02);
        var b=RitualRewards.Band.ASTRAL_ATTUNEMENT;
        var ledger=ChannelEngine.magic(Ledger.empty(),mxp,"ritual:first","ritual:astral:personal",b.first(),b.repeat(),true,false);
        assertEquals(3,ledger.wallet(mxp.id()).magicLevels());
        assertSame(ledger,ChannelEngine.magic(ledger,mxp,"ritual:later","ritual:astral:personal",b.first(),b.repeat(),true,false));
        assertEquals(1,ledger.wallet(mxp.id()).firstCompletions().size());
    }
}
