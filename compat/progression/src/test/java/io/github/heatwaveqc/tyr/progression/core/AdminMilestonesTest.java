package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminMilestonesTest{
    private static final String PATH="test:path", FIRST="test:first", SECOND="test:second";
    private static Definition.Path path(){
        return new Definition.Path(PATH,"",List.of(0L),0,List.of(),List.of(
            new Definition.Milestone(FIRST,List.of(new Definition.Criterion("external","test:deed",3)),List.of(),0,0,"",false,"tyr_progression:mxp",1000),
            new Definition.Milestone(SECOND,List.of(new Definition.Criterion("external","test:next",1)),List.of(FIRST),0,0)
        ),List.of());
    }
    @Test void counterRepairsDoNotMintXpAndRespectBounds(){
        var old=Ledger.empty().with(PATH,new Track(true,0,1,0,Map.of(),List.of(),Map.of()));
        assertThrows(IllegalArgumentException.class,()->AdminMilestones.counter(old,path(),FIRST,0,4));
        assertThrows(IllegalArgumentException.class,()->AdminMilestones.complete(old,path(),SECOND));
        var adjusted=AdminMilestones.counter(old,path(),FIRST,0,3);
        assertEquals(3,adjusted.get(PATH).criterionCount(ProgressionMath.counterKey(FIRST,0)));
        assertFalse(adjusted.get(PATH).completed(FIRST));
        assertEquals(old.economy(),adjusted.economy());
        var done=AdminMilestones.complete(adjusted,path(),FIRST);
        assertTrue(done.get(PATH).completed(FIRST));
        assertTrue(done.economy().achievements().contains(FIRST));
        assertEquals(old.economy().currentXp(),done.economy().currentXp());
        assertEquals(old.economy().rewardReceipts(),done.economy().rewardReceipts());
        assertThrows(IllegalArgumentException.class,()->AdminMilestones.resetProgress(done,path(),FIRST));
        assertThrows(IllegalArgumentException.class,()->AdminMilestones.counter(done,path(),FIRST,0,0));
        assertTrue(AdminMilestones.complete(done,path(),SECOND).get(PATH).completed(SECOND));
    }
}
