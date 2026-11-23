package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.ChannelConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Exercises actual ChannelEngine multi-wallet purchases, not only pure ledger math. */
class ConfiguredEconomyRuntimeTest {
    private static final String PATH="test:discipline";
    private static final String SKILL="test:wellspring";
    private static final String CULT="test:cultivation";

    private static ExperienceEconomyConfig.Catalog catalog() {
        return ExperienceEconomyConfig.read(JsonParser.parseString("""
            {"schema":1,
             "systems":[
               {"id":"tyr_progression:mxp","xp_per_point":1000,"growth_factor":1.02},
               {"id":"test:cultivation","xp_per_point":100,"growth_factor":0.001}
             ],
             "default_path_change":{"on_leave":"forfeit","on_respec":"refund","wipe_unallocated":true},
             "skills":[{"path":"test:discipline","skill":"test:wellspring",
               "total_rank_cap":8,
               "purchases":[
                 {"system":"tyr_progression:mxp","max_ranks":5,"points_per_rank":1},
                 {"system":"test:cultivation","max_ranks":3,"points_per_rank":1}
               ]
             }]}
            """).getAsJsonObject());
    }

    private static Definition.Path tree() {
        var skill=new Definition.Skill(SKILL,8,1,1,0,
                List.of(),List.of(),List.of(),"",List.of(),List.of());
        return new Definition.Path(PATH,"",List.of(0L,1000L),0,
                List.of(),List.of(),List.of(skill));
    }

    private static ChannelConfig config() {
        XpChannel mxp=XpChannel.mxp(1.02);
        return new ChannelConfig(Map.of(mxp.id(),mxp),
                Map.of(PATH,mxp),
                Map.of(new ChannelConfig.SkillKey(PATH,SKILL),
                        new ChannelConfig.Binding(mxp,mxp)));
    }

    @Test void twoCurrenciesPurchaseAndStackRanksWithIndependentCaps() {
        var previous=ExperienceEconomyRuntime.current();
        try {
            var catalog=catalog();
            ExperienceEconomyRuntime.install(catalog);
            var initial=ExperienceEconomy.Ledger.empty();
            initial=ChannelEngine.awardConfigured(Ledger.empty(),catalog,
                XpChannel.MXP,"test:mana","test:mana_debit",null,
                new BigDecimal("5000"),true,1.02).economy();
            Ledger ledger=Ledger.empty().withEconomy(initial);
            ledger=ChannelEngine.awardConfigured(ledger,catalog,CULT,
                "test:practice","test:practice_one",null,
                new BigDecimal("300"),true,0.001);
            ledger=ChannelEngine.join(ledger,tree(),XpChannel.mxp(1.02));

            var before=ledger;
            for(int i=0;i<5;i++)ledger=ChannelEngine.buy(ledger,tree(),config(),SKILL,XpChannel.MXP);
            assertEquals(5,ChannelEngine.view(ledger,tree(),config()).skillRank(SKILL));
            assertEquals(0,ledger.wallet(XpChannel.MXP).points());
            assertSame(ledger,ChannelEngine.buy(ledger,tree(),config(),SKILL,XpChannel.MXP));
            for(int i=0;i<3;i++)ledger=ChannelEngine.buy(ledger,tree(),config(),SKILL,CULT);
            assertEquals(8,ChannelEngine.view(ledger,tree(),config()).skillRank(SKILL));
            assertEquals(3,ExperienceEconomy.contributedRanks(ledger.economy(),PATH,SKILL,CULT));
            assertEquals(5,ExperienceEconomy.contributedRanks(ledger.economy(),PATH,SKILL,XpChannel.MXP));
            assertSame(ledger,ChannelEngine.buy(ledger,tree(),config(),SKILL,CULT));
            assertEquals(5000,ledger.economy().currentXp().get(XpChannel.MXP).intValueExact());
            assertEquals(300,ledger.economy().currentXp().get(CULT).intValueExact());
            assertEquals(4,ledger.wallet(CULT).rank());
        } finally {
            ExperienceEconomyRuntime.install(previous);
        }
    }

    @Test void configuredDepartureCanForfeitBothCurrenciesWithoutRepeatingRewards() {
        var catalog=catalog();
        var source=ExperienceEconomy.Ledger.empty()
            .withAchievement("test:milestone");
        source=ExperienceEconomy.earn(source,catalog.systems().get(XpChannel.MXP),
            "test:boss","test:boss_tx","test:boss_first",
            new BigDecimal("2500"),true,1.02).ledger();
        source=ExperienceEconomy.earn(source,catalog.systems().get(CULT),
            "test:manual","test:manual_tx",null,
            new BigDecimal("200"),true,1.02).ledger();
        source=ExperienceEconomy.buy(source,catalog.systems().get(XpChannel.MXP),
            PATH,SKILL,1,5,8);
        source=ExperienceEconomy.buy(source,catalog.systems().get(CULT),
            PATH,SKILL,1,3,8);
        var departed=ChannelEngine.pathChange(Ledger.empty().withEconomy(source),
            Set.of(PATH),catalog);
        assertEquals(0,departed.economy().currentXp().get(XpChannel.MXP).signum());
        assertEquals(0,departed.economy().currentXp().get(CULT).signum());
        assertEquals(0,departed.economy().paidRanks().size());
        assertTrue(departed.economy().achievements().contains("test:milestone"));
        assertTrue(departed.economy().rewardReceipts().stream().anyMatch(x->x.endsWith("test:boss_first")));
    }

    @Test void departureMayForfeitOneCurrencyAndRetainAnotherWithoutRestoringLostPoints() {
        var previous=ExperienceEconomyRuntime.current();
        try {
            var policy=ExperienceEconomyConfig.read(JsonParser.parseString("""
                {"systems":[
                  {"id":"tyr_progression:mxp","xp_per_point":1000},
                  {"id":"test:cultivation","xp_per_point":100}
                ],
                "default_path_change":{"on_leave":"forfeit","on_respec":"refund","wipe_unallocated":true},
                "path_changes":[{"path":"test:discipline","system":"test:cultivation",
                  "on_leave":"retain","wipe_unallocated":false}],
                "skills":[{"path":"test:discipline","skill":"test:wellspring",
                  "total_rank_cap":8,
                  "purchases":[
                    {"system":"tyr_progression:mxp","max_ranks":5},
                    {"system":"test:cultivation","max_ranks":3}
                  ]}]}
                """).getAsJsonObject());
            ExperienceEconomyRuntime.install(policy);
            Ledger l=Ledger.empty();
            l=ChannelEngine.awardConfigured(l,policy,XpChannel.MXP,"test:mana",
                "test:mana_one",null,new BigDecimal("1500"),true,1.02);
            l=ChannelEngine.awardConfigured(l,policy,CULT,"test:practice",
                "test:practice_one",null,new BigDecimal("200"),true,1.02);
            l=ChannelEngine.join(l,tree(),XpChannel.mxp(1.02));
            l=ChannelEngine.buy(l,tree(),config(),SKILL,XpChannel.MXP);
            l=ChannelEngine.buy(l,tree(),config(),SKILL,CULT);
            assertEquals(2,ChannelEngine.view(l,tree(),config()).skillRank(SKILL));
            var left=ChannelEngine.depart(l,tree(),policy);
            assertFalse(left.get(PATH).joined());
            assertEquals(0,left.economy().currentXp().get(XpChannel.MXP).intValueExact());
            assertEquals(200,left.economy().currentXp().get(CULT).intValueExact());
            assertEquals(1,ExperienceEconomy.effectiveRank(left.economy(),PATH,SKILL));
            assertEquals(1,left.payments().get(PATH+"|"+SKILL).size());
            var returned=ChannelEngine.join(left,tree(),XpChannel.mxp(1.02));
            assertEquals(1,ChannelEngine.view(returned,tree(),config()).skillRank(SKILL));
            assertEquals(0,ExperienceEconomy.contributedRanks(returned.economy(),PATH,SKILL,XpChannel.MXP));
        } finally {
            ExperienceEconomyRuntime.install(previous);
        }
    }

    @Test void shippedUnifiedPresetIsAnActualPackResource() throws Exception {
        try(var reader=new java.io.InputStreamReader(
                getClass().getResourceAsStream(
                    "/data/tyr_progression/tyr_progression/economy.json"),
                java.nio.charset.StandardCharsets.UTF_8)){
            var catalog=ExperienceEconomyConfig.read(
                    JsonParser.parseReader(reader).getAsJsonObject());
            assertEquals(1,catalog.systems().size());
            assertEquals(1000,catalog.systems().get(XpChannel.MXP).xpPerPoint().intValueExact());
            assertEquals(ExperienceEconomy.DeparturePolicy.FORFEIT,
                    catalog.defaultTransition().onLeave());
            assertTrue(catalog.defaultTransition().wipeUnallocated());
        }
    }
}
