package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.Map;
import java.util.Set;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HarvestCapabilityGraphTest {
    @Test void collapsesEqualCapabilitiesAndPreservesPartialOrder(){
        var graph=HarvestCapabilityGraph.plan(Map.of(
            "test:wood",Set.of("stone"),
            "test:iron",Set.of("stone","iron"),
            "test:alternate",Set.of("stone","crystal"),
            "test:diamond",Set.of("stone","iron","crystal","obsidian"),
            "test:netherite",Set.of("stone","iron","crystal","obsidian"),
            "test:empty",Set.of()));
        assertEquals(4,graph.size());
        var wood=graph.stream().filter(g->g.representative().equals("test:wood")).findFirst().orElseThrow();
        var iron=graph.stream().filter(g->g.representative().equals("test:iron")).findFirst().orElseThrow();
        var alt=graph.stream().filter(g->g.representative().equals("test:alternate")).findFirst().orElseThrow();
        var diamond=graph.stream().filter(g->g.representative().equals("test:diamond")).findFirst().orElseThrow();
        assertEquals(List.of(),wood.predecessors());
        assertEquals(List.of("test:wood"),iron.predecessors());
        assertEquals(List.of("test:wood"),alt.predecessors());
        assertEquals(List.of("test:alternate","test:iron"),diamond.predecessors());
        assertEquals(List.of("test:diamond","test:netherite"),diamond.equivalents());
    }
    @Test void emptyAndIdenticalSetsDoNotInventExtraTiers(){
        assertTrue(HarvestCapabilityGraph.plan(Map.of("test:empty",Set.of())).isEmpty());
        var graph=HarvestCapabilityGraph.plan(Map.of("test:b",Set.of("ore"),"test:a",Set.of("ore")));
        assertEquals(1,graph.size());
        assertEquals("test:a",graph.getFirst().representative());
    }
}
