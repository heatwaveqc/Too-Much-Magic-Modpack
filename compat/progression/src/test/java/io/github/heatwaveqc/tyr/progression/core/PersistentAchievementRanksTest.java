package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.data.ChannelConfig;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PersistentAchievementRanksTest {
    private static final String PATH="test:monk";
    private static final String SKILL="test:automatic_power";
    private static final String FIRST="test:wooden_trial";
    private static final String SECOND="test:stone_trial";

    private static Definition.Path tree() {
        var skill=new Definition.Skill(SKILL,2,1,1,0,List.of(),List.of(),
                List.of(),"",List.of(),List.of());
        return new Definition.Path(PATH,"",List.of(0L,1000L),0,
                List.of(),List.of(),List.of(skill));
    }

    private static ChannelConfig config() {
        var mxp=XpChannel.mxp(1.02);
        var key=new ChannelConfig.SkillKey(PATH,SKILL);
        return new ChannelConfig(Map.of(mxp.id(),mxp),Map.of(PATH,mxp),
                Map.of(key,new ChannelConfig.Binding(mxp,mxp)),
                Map.of(key,List.of(FIRST,SECOND)));
    }

    @Test void freeMilestoneRanksArePathGatedAndReappearWithoutRebuying() {
        var active=ChannelEngine.join(Ledger.empty(),tree(),XpChannel.mxp(1.02));
        assertEquals(0,ChannelEngine.view(active,tree(),config()).skillRank(SKILL));
        var achieved=active.withEconomy(active.economy()
                .withAchievement(FIRST).withAchievement(SECOND));
        assertEquals(2,ChannelEngine.view(achieved,tree(),config()).skillRank(SKILL));
        assertTrue(achieved.economy().paidRanks().isEmpty());
        assertSame(achieved,ChannelEngine.buy(achieved,tree(),config(),SKILL));

        var raw=achieved.get(PATH);
        var left=achieved.with(PATH,new Track(false,0,0,0,Map.of(),
                raw.milestones(),raw.counters()));
        assertEquals(0,ChannelEngine.view(left,tree(),config()).skillRank(SKILL));
        var rejoined=ChannelEngine.join(left,tree(),XpChannel.mxp(1.02));
        assertEquals(2,ChannelEngine.view(rejoined,tree(),config()).skillRank(SKILL));
        assertTrue(rejoined.economy().paidRanks().isEmpty());
    }

    @Test void onlyCompletedDeedsProvideFreeRanks() {
        var joined=ChannelEngine.join(Ledger.empty(),tree(),XpChannel.mxp(1.02));
        joined=joined.withEconomy(joined.economy().withAchievement(FIRST));
        assertEquals(1,ChannelEngine.view(joined,tree(),config()).skillRank(SKILL));
        assertEquals(0,ExperienceEconomy.committed(joined.economy(),XpChannel.MXP));
    }
}
