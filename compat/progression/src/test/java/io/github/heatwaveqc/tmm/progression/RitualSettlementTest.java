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
        assertEquals(43,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("rootsclassic:")).count());
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
    @Test void occultProofUsesExactBookAndActualOutcomeFamily(){
        var item="occultism:ritual/craft_miner_marid_master";
        var summon="occultism:ritual/summon_foliot_crusher";
        assertTrue(io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.matchesBook(item,"occultism:book_of_binding_bound_marid"));
        assertFalse(io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.matchesBook(item,"occultism:book_of_binding_bound_foliot"));
        assertEquals(io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.Kind.ITEM,io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.kind(item));
        assertEquals(io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.Kind.SUMMON,io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.kind(summon));
        for(String held:java.util.List.of("occultism:ritual/craft_upgrade_ritual_satchel","occultism:ritual/familiar_parrot","occultism:ritual/repair_chalks"))
            assertEquals(io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.Kind.HELD,io.github.heatwaveqc.tmm.progression.mxp.core.OccultRitualProof.kind(held));
    }
    @Test void e6bIndependentNativeRecipes(){
        assertEquals(14,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("totemic:")).count());
        assertEquals(34,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("witchery:")).count());
        assertEquals(1200,RitualRewards.lookup("totemic:war_dance").band().first());
        assertEquals(2200,RitualRewards.lookup("totemic:rain").band().first());
        assertEquals(3500,RitualRewards.lookup("totemic:baykok_summon").band().first());
        assertEquals("witchery:ritual/summon_demon",RitualRewards.lookup("witchery:ritual/summon_demon").family());
        assertNull(RitualRewards.lookup("witchery:ritual/unknown"));
        assertEquals("witchery:undead",io.github.heatwaveqc.tmm.progression.mxp.core.E6bRitualCatalog.jawr("witchery:ritual/curse_of_the_undead").output());
        assertEquals(io.github.heatwaveqc.tmm.progression.mxp.core.E6bRitualCatalog.Kind.INFUSION,io.github.heatwaveqc.tmm.progression.mxp.core.E6bRitualCatalog.jawr("witchery:ritual/infuse_light").kind());
        assertNull(RitualRewards.lookup("witchery:ritual/blocks_below_iron"));
        assertEquals(1000,RitualRewards.lookup("witchery:ritual/summon_lightning").band().first());
        assertEquals(2000,RitualRewards.lookup("witchery:ritual/set_midnight").band().first());
    }
    @Test void enchantedAndEidolonUseDistinctNativeCatalogs(){
        assertEquals(6,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("enchanted:")).count());
        assertEquals(22,RitualRewards.rules().keySet().stream().filter(id->id.startsWith("eidolon_repraised:")).count());
        assertNotEquals(RitualRewards.lookup("enchanted:infuse_broom").family(),RitualRewards.lookup("witchery:ritual/apply_ointment").family());
        assertEquals(3,io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog.eidolon("eidolon_repraised:rituals/summon_drowned").count());
        assertEquals(3500,RitualRewards.lookup("eidolon_repraised:rituals/brazier_craft_sanguine_amulet").band().first());
        assertNull(RitualRewards.lookup("eidolon_repraised:rituals/recharging_chill"));
        assertNull(RitualRewards.lookup("eidolon_repraised:rituals/recharging_soulfire"));
        for(String id:java.util.List.of("allure","repelling","deceit"))
            assertTrue(io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog.continuous(io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog.eidolon("eidolon_repraised:rituals/"+id)));
        assertFalse(io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog.continuous(io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog.eidolon("eidolon_repraised:rituals/purify")));
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
