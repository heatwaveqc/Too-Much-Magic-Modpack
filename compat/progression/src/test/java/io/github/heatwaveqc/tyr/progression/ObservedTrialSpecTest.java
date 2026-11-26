package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.*;
import io.github.heatwaveqc.tyr.progression.data.ObservedTrialSpec;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ObservedTrialSpecTest {
    private static final String JSON="""
      {"schema":1,"rank_xp":[0],"milestones":[
       {"id":"test:water","strict_order":true,"criteria":[{"event":"observed_trial","target":"test:water"}],
        "observed_trial":{"kind":"underwater","ticks":280}},
       {"id":"test:pet","strict_order":true,"after":["test:water"],"criteria":[{"event":"animal_petted_unique","target":"*","count":20}]},
       {"id":"test:arrow","strict_order":true,"after":["test:pet"],"criteria":[{"event":"arrow_dodged","target":"test:arrow","count":5}],
        "observed_trial":{"kind":"arrow_dodge","shooter":"minecraft:skeleton"}}
      ]}
      """;
    @Test void validatesObservedTrialMetadata(){
        var raw=JsonParser.parseString(JSON).getAsJsonObject();
        var path=TestPaths.read("test:path",raw);
        assertEquals(280,ObservedTrialSpec.parse(raw,path).getFirst().ticks());
        raw.getAsJsonArray("milestones").get(0).getAsJsonObject().remove("observed_trial");
        assertThrows(IllegalArgumentException.class,()->ObservedTrialSpec.parse(raw,path));
    }
    @Test void uniqueSubjectsArePersistentAndCannotPrecount(){
        var raw=JsonParser.parseString(JSON).getAsJsonObject();
        var path=TestPaths.read("test:path",raw);
        var track=Track.empty().joinedAtRankOne(0);
        UUID sheep=UUID.randomUUID();
        var pet=new Signal("animal_petted_unique","minecraft:sheep",Set.of(),sheep);
        assertEquals(track,ProgressionMath.collectMilestoneCriteria(path,track,pet));
        track=track.complete("test:water");
        var once=ProgressionMath.collectMilestoneCriteria(path,track,pet);
        assertEquals(1,once.criterionCount("test:pet/0"));
        assertEquals(once,ProgressionMath.collectMilestoneCriteria(path,once,pet));
        assertEquals(once,ProgressionMath.collectMilestoneCriteria(path,once,new Signal("animal_petted_unique","minecraft:sheep",Set.of())));
        var twice=ProgressionMath.collectMilestoneCriteria(path,once,new Signal("animal_petted_unique","minecraft:sheep",Set.of(),UUID.randomUUID()));
        assertEquals(2,twice.criterionCount("test:pet/0"));
    }
    @Test void resetClearsOnlyActiveArrowTrialAndDistinctReceipts(){
        var path=TestPaths.read("test:path",JsonParser.parseString(JSON).getAsJsonObject());
        var start=Track.empty().joinedAtRankOne(0).complete("test:water").complete("test:pet");
        var arrow=new Signal("arrow_dodged","test:arrow",Set.of(),UUID.randomUUID());
        var once=ProgressionMath.collectMilestoneCriteria(path,start,arrow);
        assertEquals(1,once.criterionCount("test:arrow/0"));
        assertEquals(start,ProgressionMath.resetCriterion(once,path.milestones().getLast(),0));
        assertEquals(once.complete("test:arrow"),ProgressionMath.resetCriterion(once.complete("test:arrow"),path.milestones().getLast(),0));
    }
}
