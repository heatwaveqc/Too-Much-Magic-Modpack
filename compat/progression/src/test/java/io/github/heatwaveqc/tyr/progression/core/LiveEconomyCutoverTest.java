package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.data.ChannelConfig;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests the wired ChannelEngine, not merely the independent pure Economy. */
class LiveEconomyCutoverTest {
    private static final XpChannel MXP = XpChannel.mxp(1.0);
    private static final String CORE = "test:core";
    private static final String MONK = "test:monk";
    private static final String CORE_SKILL = "test:core_stat";
    private static final String MONK_SKILL = "test:monk_stat";

    private static Definition.Path path(String id,String skill) {
        var paid = new Definition.Skill(skill,30,1,1,0,
            List.of(),List.of(),List.of(),"",List.of(),List.of());
        return new Definition.Path(id,"",List.of(0L,1000L),0,
            List.of(),List.of(),List.of(paid));
    }
    private static final Definition.Path CORE_PATH = path(CORE,CORE_SKILL);
    private static final Definition.Path MONK_PATH = path(MONK,MONK_SKILL);
    private static final ChannelConfig CONFIG = new ChannelConfig(
        Map.of(MXP.id(),MXP),
        Map.of(CORE,MXP, MONK,MXP),
        Map.of(new ChannelConfig.SkillKey(CORE,CORE_SKILL),
                   new ChannelConfig.Binding(MXP,MXP),
               new ChannelConfig.SkillKey(MONK,MONK_SKILL),
                   new ChannelConfig.Binding(MXP,MXP))
    );

    private static Ledger pay(Ledger ledger, Definition.Path tree,String skill,int count) {
        for (int i=0;i<count;i++) {
            var next=ChannelEngine.buy(ledger,tree,CONFIG,skill);
            assertNotSame(ledger,next,"Rank "+i+" should be purchasable");
            ledger=next;
        }
        return ledger;
    }

    @Test void currentMxpProducesDerivedLevelAndPurchasesDoNotSpendXp() {
        Ledger l=ChannelEngine.join(Ledger.empty(),CORE_PATH,MXP);
        l=ChannelEngine.magic(l,MXP,"tyr_progression:boss","boss:test:one",
            "boss:test:one",1000,1000,true,true);
        assertEquals(1000,l.economy().currentXp().get(MXP.id()).intValueExact());
        assertEquals(1,l.wallet(MXP.id()).points());
        assertEquals(2,l.wallet(MXP.id()).rank());
        var paid=ChannelEngine.buy(l,CORE_PATH,CONFIG,CORE_SKILL);
        assertEquals(1000,paid.economy().currentXp().get(MXP.id()).intValueExact());
        assertEquals(0,paid.wallet(MXP.id()).points());
        assertEquals(1,paid.get(CORE).skillRank(CORE_SKILL));
        assertEquals(1,paid.economy().paidRanks().size());
        assertSame(paid,ChannelEngine.buy(paid,CORE_PATH,CONFIG,CORE_SKILL));
    }

    @Test void sourceCategoriesCanEachPayOneCommittedAction() {
        Ledger l=ChannelEngine.join(Ledger.empty(),CORE_PATH,MXP);
        l=ChannelEngine.magic(l,MXP,"test:mana","spell:test:one",null,0,100,false,true);
        Ledger other=ChannelEngine.magic(l,MXP,"test:block","spell:test:one",null,0,200,false,true);
        assertEquals(300,other.economy().currentXp().get(MXP.id()).intValueExact());
        assertSame(other,ChannelEngine.magic(other,MXP,"test:block",
            "spell:test:one",null,0,200,false,true));
    }

    @Test void monkLossForfeitsPurchasesAndResetsGrowthWithoutErasingCore() {
        Ledger l=ChannelEngine.join(Ledger.empty(),CORE_PATH,MXP);
        l=ChannelEngine.join(l,MONK_PATH,MXP);
        l=ChannelEngine.magic(l,MXP,"test:boss","boss:test:epic",
            "boss:test:epic",37400,37400,true,true);
        l=pay(l,CORE_PATH,CORE_SKILL,15);
        l=pay(l,MONK_PATH,MONK_SKILL,20);
        l=l.withEconomy(l.economy().withAchievement("test:stone_ordeal"));
        assertEquals(37,l.wallet(MXP.id()).magicLevels());
        assertEquals(2,l.wallet(MXP.id()).points());
        assertTrue(l.economy().rewardReceipts().stream().anyMatch(x -> x.endsWith("boss:test:epic")));
        var before=l.get(MONK);
        Ledger departed=l.with(MONK,new Track(false,0,0,0,Map.of(),
            before.milestones(),before.counters())).clearAllReceipts(MONK);
        departed=ChannelEngine.pathChange(departed,Set.of(MONK));
        assertEquals(15000,departed.economy().currentXp().get(MXP.id()).intValueExact());
        assertEquals(15,departed.wallet(MXP.id()).magicLevels());
        assertEquals(0,departed.wallet(MXP.id()).points());
        assertEquals(0,ExperienceEconomy.effectiveRank(departed.economy(),MONK,MONK_SKILL));
        assertEquals(15,ExperienceEconomy.effectiveRank(departed.economy(),CORE,CORE_SKILL));
        assertTrue(departed.economy().achievements().contains("test:stone_ordeal"));
        assertTrue(departed.economy().rewardReceipts().stream().anyMatch(x -> x.endsWith("boss:test:epic")));
        var replay=ChannelEngine.magic(departed,MXP,"test:boss","boss:test:another",
            "boss:test:epic",37400,0,true,false);
        assertSame(departed,replay,"A first-only boss cannot be replayed after Path forfeiture");
    }

    @Test void refundableRespecReleasesCommitmentsButNeverMintsXp() {
        Ledger l=ChannelEngine.join(Ledger.empty(),CORE_PATH,MXP);
        l=ChannelEngine.magic(l,MXP,"test:practice","test:practice_1",null,0,3000,false,true);
        l=pay(l,CORE_PATH,CORE_SKILL,2);
        assertEquals(1,l.wallet(MXP.id()).points());
        Ledger after=ChannelEngine.refund(l,CORE_PATH);
        assertEquals(3,after.wallet(MXP.id()).points());
        assertEquals(3000,after.economy().currentXp().get(MXP.id()).intValueExact());
        assertEquals(0,ExperienceEconomy.effectiveRank(after.economy(),CORE,CORE_SKILL));
    }
}
