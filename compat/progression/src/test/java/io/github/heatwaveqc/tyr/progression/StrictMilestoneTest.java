package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.core.Signal;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StrictMilestoneTest {
    private static final String JSON = """
        {"schema":1,"rank_xp":[0],"milestones":[
          {"id":"test:first","strict_order":true,"criteria":[{"event":"dimension_entered","target":"minecraft:the_nether"}]},
          {"id":"test:second","strict_order":true,"after":["test:first"],"criteria":[{"event":"dimension_entered","target":"minecraft:the_nether"}]},
          {"id":"test:legacy","after":["test:first"],"criteria":[{"event":"dimension_entered","target":"minecraft:the_nether"}]},
          {"id":"test:parallel","criteria":[{"event":"dimension_entered","target":"minecraft:the_nether","count":2}]}
        ]}
        """;
    private static Definition.Path read(String json) {
        return TestPaths.read("test:path",JsonParser.parseString(json).getAsJsonObject());
    }
    private static int count(Track state,String id) {
        return state.criterionCount(ProgressionMath.counterKey("test:"+id,0));
    }
    @Test void strictCollectionUsesSnapshotAndPreservesLegacyParallelCounters() {
        var path=read(JSON);
        var start=Track.empty().joinedAtRankOne(0);
        var signal=new Signal("dimension_entered","minecraft:the_nether",Set.of());
        var first=ProgressionMath.collectMilestoneCriteria(path,start,signal);
        assertEquals(1,count(first,"first"));
        assertEquals(0,count(first,"second"));
        assertEquals(1,count(first,"legacy"));
        assertEquals(1,count(first,"parallel"));
        var second=path.milestones().get(1);
        assertFalse(ProgressionMath.canCollectMilestone(second,start));
        var completed=first.complete("test:first");
        assertTrue(ProgressionMath.canCollectMilestone(second,completed));
        var next=ProgressionMath.collectMilestoneCriteria(path,completed,signal);
        assertEquals(1,count(next,"second"));
        assertEquals(2,count(next,"parallel"));
        assertEquals(1,count(next,"legacy"));
        assertEquals(1,count(next,"first"));
        assertEquals(next,ProgressionMath.collectMilestoneCriteria(path,next,signal));
    }
    @Test void dimensionSignalMatchesDestinationAndJoinedStateOnly() {
        var path=read(JSON);
        var start=Track.empty().joinedAtRankOne(0);
        var wrong=new Signal("dimension_entered","minecraft:overworld",Set.of());
        var correct=new Signal("dimension_entered","minecraft:the_nether",Set.of());
        assertEquals(start,ProgressionMath.collectMilestoneCriteria(path,start,wrong));
        assertEquals(Track.empty(),ProgressionMath.collectMilestoneCriteria(path,Track.empty(),correct));
        assertFalse(correct.matches("advancement","minecraft:story/enter_the_nether"));
    }
    @Test void rejectsMalformedStrictFlagAndCyclicMilestones() {
        assertThrows(IllegalArgumentException.class,()->read(JSON.replace("\"strict_order\":true","\"strict_order\":\"true\"")));
        assertThrows(IllegalArgumentException.class,()->read(JSON.replace("\"id\":\"test:first\",","\"id\":\"test:first\",\"after\":[\"test:second\"],")));
    }
}
