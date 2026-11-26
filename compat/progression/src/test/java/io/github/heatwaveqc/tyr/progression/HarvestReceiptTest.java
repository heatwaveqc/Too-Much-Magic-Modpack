package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HarvestReceiptTest {
    private static Definition.Path path(String aliases) {
        return TestPaths.read("test:path",JsonParser.parseString("""
          {"schema":1,"rank_xp":[0],"milestones":[{"id":"test:a","strict_order":true,
           "criteria":[{"event":"material_harvest_trial","target":"minecraft:wooden_pickaxe","count":5}],
           "completion_aliases":%s}]}
          """.formatted(aliases)).getAsJsonObject());
    }
    @Test void genuineCompletionPersistsAllEquivalentReceipts(){
        var p=path("[\"test:b\"]");
        var raw=Track.empty().joinedAtRankOne(0);
        var completed=ProgressionMath.completeMilestone(raw,p.milestones().getFirst());
        assertTrue(completed.completed("test:a"));
        assertTrue(completed.completed("test:b"));
        assertEquals(completed.milestones(),ProgressionMath.persistentMilestones(p,raw,completed));
    }
    @Test void mergedGraphProjectsButNeverMintsReceiptForNewlyEquivalentTool(){
        var p=path("[\"test:b\"]");
        var raw=Track.empty().joinedAtRankOne(0).complete("test:b");
        var view=ProgressionMath.projectCompletionAliases(p,raw);
        assertTrue(view.completed("test:a"));
        assertEquals(raw.milestones(),ProgressionMath.persistentMilestones(p,raw,view));
        assertFalse(ProgressionMath.projectCompletionAliases(path("[]"),raw).completed("test:a"));
    }
    @Test void aliasesCannotCarryRewardsOrCollide(){
        assertThrows(IllegalArgumentException.class,()->path("[\"test:a\"]"));
        assertThrows(IllegalArgumentException.class,()->path("[\"test:b\",\"test:b\"]"));
    }
}
