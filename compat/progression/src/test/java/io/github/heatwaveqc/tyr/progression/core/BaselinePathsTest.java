package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaselinePathsTest {
    @Test void firstLoginIsNewbieAndReconciliationIsIdempotent() {
        var empty=Ledger.empty();
        var newbie=BaselinePaths.reconcile(empty,true);
        assertTrue(newbie.get(BaselinePaths.NEWBIE).joined());
        assertFalse(newbie.get(BaselinePaths.NORMIE).joined());
        assertFalse(BaselinePaths.hasSpecialist(newbie));
        assertSame(newbie,BaselinePaths.reconcile(newbie,true));
    }

    @Test void graduationMakesNormieFallbackWithoutChangingEconomy() {
        var initial=Ledger.empty().withEconomy(
            new ExperienceEconomy.Ledger(Map.of(XpChannel.MXP,new java.math.BigDecimal("3000")),
                List.of(),Set.of("test:earned"),Set.of("test:first_claim"),Set.of()));
        var newbie=BaselinePaths.reconcile(initial,true);
        var graduate=BaselinePaths.reconcile(newbie,false);
        assertFalse(graduate.get(BaselinePaths.NEWBIE).joined());
        assertTrue(graduate.get(BaselinePaths.NORMIE).joined());
        assertFalse(BaselinePaths.hasSpecialist(graduate));
        assertEquals(newbie.economy(),graduate.economy());
        assertSame(graduate,BaselinePaths.reconcile(graduate,false));
    }

    @Test void specialistReplacesNormieAndLeavingRestoresFallbackWithoutBurningCore() {
        var graduate=BaselinePaths.reconcile(Ledger.empty(),false);
        var core=new Track(true,0,1,0,Map.of("test:core_power",3),List.of(),Map.of());
        var specialist=new Track(true,0,1,0,Map.of("test:special_power",2),
            List.of("test:completed_trial"),Map.of("test:trial_counter/0",4));
        var trained=BaselinePaths.reconcile(graduate.with(BaselinePaths.CORE,core)
            .with("test:monk",specialist),false);
        assertFalse(trained.get(BaselinePaths.NORMIE).joined());
        assertTrue(BaselinePaths.hasSpecialist(trained));
        assertEquals(3,trained.get(BaselinePaths.CORE).skillRank("test:core_power"));

        var left=trained.with("test:monk",new Track(false,0,0,0,Map.of(),
            specialist.milestones(),specialist.counters()));
        var normie=BaselinePaths.reconcile(left,false);
        assertTrue(normie.get(BaselinePaths.NORMIE).joined());
        assertEquals(3,normie.get(BaselinePaths.CORE).skillRank("test:core_power"));
        assertTrue(normie.get("test:monk").completed("test:completed_trial"));
        assertSame(normie,BaselinePaths.reconcile(normie,false));
    }

    @Test void pathRejoinCannotResetMilestonesOrCounters() {
        var path=new Definition.Path("test:monk","",List.of(0L,1000L),0,
            List.of(),List.of(),List.of());
        var old=new Track(false,0,0,0,Map.of(),List.of("test:trial_completed"),
            Map.of("test:trial_completed/0",10));
        var rejoined=ChannelEngine.join(Ledger.empty().with(path.id(),old),path,XpChannel.mxp(1.02));
        assertTrue(rejoined.get(path.id()).joined());
        assertTrue(rejoined.get(path.id()).completed("test:trial_completed"));
        assertEquals(10,rejoined.get(path.id()).criterionCount("test:trial_completed/0"));
    }

    @Test void baselineJsonIsRegisteredAsDataOnlyAndHasNoPurchasableSkills() {
        for (String id:List.of("newbie","normie")) {
            var resource="/data/tyr_progression/tyr_progression/paths/"+id+".json";
            try (var stream=BaselinePathsTest.class.getResourceAsStream(resource)) {
                assertNotNull(stream,resource);
                var j=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8))
                    .getAsJsonObject();
                assertEquals(1,j.get("schema").getAsInt());
                assertEquals("tyr_progression:mxp",j.get("xp_channel").getAsString());
                assertTrue(j.getAsJsonArray("skills").isEmpty());
                assertEquals("admin",j.get("enrollment").getAsString());
            } catch (java.io.IOException e) {
                fail(e);
            }
        }
    }
}
