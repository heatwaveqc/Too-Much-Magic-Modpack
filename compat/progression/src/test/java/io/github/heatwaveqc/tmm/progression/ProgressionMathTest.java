package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.ProgressionMath;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.state.Track;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProgressionMathTest {
    private static Definition.Path sample() {
        Definition.Skill baseline = new Definition.Skill("test:baseline",2,1,0,0,List.of(),
            List.of(),List.of(),"",List.of(1,3),List.of());
        Definition.Skill purchased = new Definition.Skill("test:optional",2,2,2,3,List.of(),
            List.of(),List.of(),"",List.of(),List.of());
        return new Definition.Path("test:path","",List.of(0L,20L,55L,100L),
            2,List.of(),List.of(),List.of(baseline,purchased));
    }

    @Test void rankAndBaselineGrantWithoutAnyPurchase() {
        Definition.Path spec = sample();
        Track joined = ProgressionMath.join(spec,Track.empty());
        assertTrue(joined.joined());
        assertEquals(1,joined.rank());
        assertEquals(1,joined.skillRank("test:baseline"));
        assertEquals(2,joined.points());
        Track upgraded = ProgressionMath.grantXp(spec,joined,55);
        assertEquals(3,upgraded.rank());
        assertEquals(6,upgraded.points());
        assertEquals(2,upgraded.skillRank("test:baseline"));
        assertEquals(2,ProgressionMath.grantXp(spec,upgraded,0).skillRank("test:baseline"));
        assertEquals(4,ProgressionMath.grantXp(spec,upgraded,Long.MAX_VALUE).rank());
    }

    @Test void purchasedRanksRespectPriceAndRespecPreservesBaseline() {
        Definition.Path spec = sample();
        Track state = ProgressionMath.grantXp(spec,ProgressionMath.join(spec,Track.empty()),55);
        assertSame(state,ProgressionMath.buy(spec,state,"test:baseline"));
        Track after = ProgressionMath.buy(spec,state,"test:optional");
        assertEquals(1,after.skillRank("test:optional"));
        assertEquals(4,after.points());
        Track second = ProgressionMath.buy(spec,after,"test:optional");
        assertEquals(4,second.points());
        assertEquals(1,second.skillRank("test:optional")); // second rank costs 5
        Track reset = ProgressionMath.refundAll(spec,after);
        assertEquals(0,reset.skillRank("test:optional"));
        assertEquals(2,reset.skillRank("test:baseline"));
        assertEquals(6,reset.points());
    }

    @Test void overflowDoesNotWrapXpOrCounters() {
        Track initial = ProgressionMath.join(sample(),Track.empty());
        Track max = ProgressionMath.grantXp(sample(),initial,Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE,max.xp());
        assertEquals(4,max.rank());
        assertEquals(Long.MAX_VALUE,ProgressionMath.grantXp(sample(),max,10000).xp());
        assertEquals(Integer.MAX_VALUE,
            ProgressionMath.recordCriterion(initial.withCounter("x",Integer.MAX_VALUE),"x",1)
                .criterionCount("x"));
    }
    @Test void branchingChoicesAndOrPrerequisitesAreEnforced() {
        var root = new Definition.Skill("test:root",1,1,1,0,List.of(),List.of(),List.of(),"",List.of(),List.of());
        var swift = new Definition.Skill("test:swift",1,1,1,0,List.of("test:root"),
            List.of(),List.of(),"test:stance",List.of(),List.of());
        var strong = new Definition.Skill("test:strong",1,1,1,0,List.of("test:root"),
            List.of(),List.of(),"test:stance",List.of(),List.of());
        var advanced = new Definition.Skill("test:advanced",1,1,1,0,List.of(),
            List.of("test:swift","test:strong"),List.of(),"",List.of(),List.of());
        var path = new Definition.Path("test:branch","",List.of(0L,10L,20L,30L),
            3,List.of(),List.of(),List.of(root,swift,strong,advanced));
        Track joined = ProgressionMath.join(path,Track.empty());
        assertSame(joined,ProgressionMath.buy(path,joined,"test:swift"));
        Track withRoot = ProgressionMath.buy(path,joined,"test:root");
        Track withSwift = ProgressionMath.buy(path,withRoot,"test:swift");
        assertSame(withSwift,ProgressionMath.buy(path,withSwift,"test:strong"));
        Track withAdvanced = ProgressionMath.buy(path,withSwift,"test:advanced");
        assertEquals(1,withAdvanced.skillRank("test:advanced"));
        assertEquals(0,withAdvanced.skillRank("test:strong"));
        Track reset = ProgressionMath.refundAll(path,withAdvanced);
        assertEquals(0,reset.skillRank("test:swift"));
        assertEquals(0,reset.skillRank("test:advanced"));
        assertEquals(joined.points(),reset.points());
    }

}
