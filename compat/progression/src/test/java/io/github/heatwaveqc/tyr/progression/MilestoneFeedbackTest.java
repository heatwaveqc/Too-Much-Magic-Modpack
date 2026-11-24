package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.core.MilestoneFeedback;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MilestoneFeedbackTest {
    private static Definition.Milestone logs(int count,String id) {
        return new Definition.Milestone("test:"+id,
            List.of(new Definition.Criterion("barehand_block_break","#minecraft:logs",count)),
            List.of(),0,0,count==10?"Ten Logs":"Steady Hands",true);
    }
    private static Definition.Path path(Definition.Milestone... m){
        return new Definition.Path("test:monk","",List.of(0L),1,
            List.of(),List.of(m),List.of());
    }
    private static Track initial(){
        return new Track(true,0,1,0,Map.of(),List.of(),Map.of());
    }
    @Test void firstActionRevealsChallengeAndUpdatesCounter(){
        var m=logs(10,"short");
        var p=path(m);
        var start=initial();
        var one=start.withCounter(ProgressionMath.counterKey(m.id(),0),1);
        assertEquals("Challenge discovered: Ten Logs (1/10)",
            MilestoneFeedback.changed(p,start,one).message());
        var two=one.withCounter(ProgressionMath.counterKey(m.id(),0),2);
        assertEquals("Challenge: Ten Logs (2/10)",
            MilestoneFeedback.changed(p,one,two).message());
        assertNull(MilestoneFeedback.changed(p,two,two));
        assertEquals(2,MilestoneFeedback.next(p,two).current());
    }
    @Test void completionBeatsSimultaneousProgressWithoutErasingCounters(){
        var shortLogs=logs(10,"short");
        var longLogs=logs(100,"long");
        var p=path(shortLogs,longLogs);
        var start=initial().withCounter(ProgressionMath.counterKey(shortLogs.id(),0),9)
            .withCounter(ProgressionMath.counterKey(longLogs.id(),0),9);
        var done=start.withCounter(ProgressionMath.counterKey(shortLogs.id(),0),10)
            .withCounter(ProgressionMath.counterKey(longLogs.id(),0),10)
            .complete(shortLogs.id());
        assertEquals("Challenge complete: Ten Logs",
            MilestoneFeedback.changed(p,start,done).message());
        assertEquals("Steady Hands",MilestoneFeedback.next(p,done).title());
        assertEquals(10,MilestoneFeedback.next(p,done).current());
    }
    @Test void optInAndPrerequisitesAreIndependent(){
        var hidden=new Definition.Milestone("test:hidden",
            List.of(new Definition.Criterion("kill","minecraft:zombie",4)),List.of(),0,0);
        var gate=logs(10,"short");
        var iron=new Definition.Milestone("test:iron",
            List.of(new Definition.Criterion("barehand_block_break","minecraft:iron_block",4)),
            List.of(gate.id()),0,0,"Iron Palm",true);
        var p=path(gate,hidden,iron);
        var start=initial();
        var changed=start.withCounter(ProgressionMath.counterKey(hidden.id(),0),1)
            .withCounter(ProgressionMath.counterKey(iron.id(),0),2);
        assertEquals("Ten Logs",MilestoneFeedback.next(p,changed).title());
        assertEquals("Iron Palm",MilestoneFeedback.changed(p,start,changed).title());
        assertEquals(2,MilestoneFeedback.next(p,changed.complete(gate.id())).current());
        assertNull(MilestoneFeedback.changed(p,changed,changed));
    }
}
