package io.github.heatwaveqc.tyr.progression.foundation;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SkillGraphTest {
    @Test void explicitEdgesAndAlternativeBranchesRemainDistinct(){
        var graph=SkillGraph.arrange(List.of(new SkillGraph.Skill("c",1,List.of("a"),List.of("b")),
            new SkillGraph.Skill("b",1,List.of(),List.of()),new SkillGraph.Skill("a",1,List.of(),List.of())),2,98,42,18,22);
        var map=graph.layout().positions();
        assertTrue(map.get("c").y()>map.get("a").y());assertTrue(map.get("c").y()>map.get("b").y());
        assertEquals(2,graph.connections().size());assertEquals(1,graph.connections().stream().filter(SkillGraph.Connection::alternative).count());
    }
    @Test void cyclesRejected(){
        assertThrows(IllegalArgumentException.class,()->SkillGraph.arrange(List.of(new SkillGraph.Skill("a",1,List.of("b"),List.of()),
            new SkillGraph.Skill("b",1,List.of("a"),List.of())),4,98,42,18,22));
    }
}
