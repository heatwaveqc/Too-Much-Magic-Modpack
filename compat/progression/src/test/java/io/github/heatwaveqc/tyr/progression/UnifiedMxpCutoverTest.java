package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.data.MasteryStageProjection;
import io.github.heatwaveqc.tyr.progression.data.UnifiedMxpSource;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Source-model checks; actual NeoForge event attribution requires gameplay QA. */
class UnifiedMxpCutoverTest {
    @Test void stageDependsOnCommittedMonkPointsNotUnspentLevelsOrCorePurchases() {
        var stages=new MasteryStageProjection("tyr_progression:mxp",50,20);
        var paid=new ArrayList<ExperienceEconomy.PaidRank>();
        for(int i=0;i<98;i++)
            paid.add(new ExperienceEconomy.PaidRank("tyr_progression:monk",
                "tyr_progression:test","tyr_progression:mxp",1));
        paid.add(new ExperienceEconomy.PaidRank("tyr_progression:core",
            "tyr_progression:reservoir","tyr_progression:mxp",500));
        var account=new ExperienceEconomy.Ledger(Map.of(),paid,Set.of(),Set.of(),Set.of());
        assertEquals(2,stages.stage(account,"tyr_progression:monk"));
        paid.add(new ExperienceEconomy.PaidRank("tyr_progression:monk",
            "tyr_progression:test","tyr_progression:mxp",2));
        account=new ExperienceEconomy.Ledger(Map.of(),paid,Set.of(),Set.of(),Set.of());
        assertEquals(3,stages.stage(account,"tyr_progression:monk"));
        assertEquals(1,stages.stage(account,"tyr_progression:hunter"));
    }

    @Test void activitySourceValidationForbidsUnverifiedDamageOrBreakHandlers() {
        var spec=JsonParser.parseString("""
          {"unified_mxp_sources":[{"system":"tyr_progression:mxp",
          "source":"tyr_progression:monk_advancement","event":"advancement",
          "target":"*","base_mxp":100,"once_per_target":true}]}
          """).getAsJsonObject();
        var systems=Map.of("tyr_progression:mxp",XpChannel.mxp(1.02));
        var awards=UnifiedMxpSource.parse(spec,systems);
        assertEquals(1,awards.size());
        assertEquals(new BigDecimal("100"),awards.getFirst().nominal());
        assertEquals(42,awards.getFirst().firstCompletionKey("tyr_progression:monk",
            "a".repeat(3000)).length());
        assertEquals(awards.getFirst().firstCompletionKey("tyr_progression:monk","minecraft:story/mine_stone"),
            awards.getFirst().firstCompletionKey("tyr_progression:monk","minecraft:story/mine_stone"));
        assertNotEquals(awards.getFirst().firstCompletionKey("tyr_progression:core","minecraft:story/mine_stone"),
            awards.getFirst().firstCompletionKey("tyr_progression:monk","minecraft:story/mine_stone"));
        spec.getAsJsonArray("unified_mxp_sources").get(0).getAsJsonObject()
            .addProperty("event","kill");
        assertThrows(IllegalArgumentException.class,()->UnifiedMxpSource.parse(spec,systems));
    }

    @Test void firstAdvancementAndTrialReceiptsPersistAcrossAttempts() {
        var system=ExperienceEconomy.SystemSpec.mxp();
        var empty=ExperienceEconomy.initialize(ExperienceEconomy.Ledger.empty(),system);
        var first=ExperienceEconomy.earn(empty,system,"tyr_progression:monk_advancement",
            "first:tyr_progression:monk/minecraft:story/mine_stone",
            "tyr_progression:monk/minecraft:story/mine_stone",
            new BigDecimal("100"),false,1.02);
        assertTrue(first.accepted());
        var again=ExperienceEconomy.earn(first.ledger(),system,"tyr_progression:monk_advancement",
            "first:another-transaction","tyr_progression:monk/minecraft:story/mine_stone",
            new BigDecimal("100"),false,1.02);
        assertFalse(again.accepted());
        var deed=ExperienceEconomy.earn(first.ledger(),system,"tyr_progression:path_trial",
            "trial:tyr_progression:monk/tyr_progression:trial_ten_logs",
            "tyr_progression:monk/tyr_progression:trial_ten_logs",
            new BigDecimal("1000"),true,1.02);
        assertTrue(deed.accepted());
        assertEquals(new BigDecimal("1100"),deed.ledger().currentXp().get(system.id()));
        assertFalse(ExperienceEconomy.earn(deed.ledger(),system,"tyr_progression:path_trial",
            "trial:retry","tyr_progression:monk/tyr_progression:trial_ten_logs",
            new BigDecimal("1000"),true,1.02).accepted());
    }

    @Test void milestoneFixedRewardRejectsNativeDoublePayment() {
        var j=JsonParser.parseString("""
          {"schema":1,"rank_xp":[0,1],"points_per_rank":0,
          "milestones":[{"id":"test:trial","criteria":[{"event":"external","target":"test:done","count":1}],
          "fixed_reward":{"system":"tyr_progression:mxp","xp":1000}}]}
          """).getAsJsonObject();
        var path=PathJson.read("test:monk",j);
        assertEquals("tyr_progression:mxp",path.milestones().getFirst().fixedRewardSystem());
        assertEquals(1000,path.milestones().getFirst().fixedRewardXp());
        j.getAsJsonArray("milestones").get(0).getAsJsonObject().addProperty("points",1);
        assertThrows(IllegalArgumentException.class,()->PathJson.read("test:monk",j));
    }
}
