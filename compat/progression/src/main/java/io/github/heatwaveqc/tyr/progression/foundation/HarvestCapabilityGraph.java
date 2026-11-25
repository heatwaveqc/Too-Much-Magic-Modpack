package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/** Pure partial-order planner over native correct-for-drops capability fingerprints.
 * Equal tools collapse; incomparable tool materials remain independent branches. */
public final class HarvestCapabilityGraph {
    private HarvestCapabilityGraph(){}
    public record Group(String representative,List<String> equivalents,Set<String> blocks,
                        List<String> predecessors) {
        public Group {
            equivalents=List.copyOf(equivalents);
            blocks=Set.copyOf(blocks);
            predecessors=List.copyOf(predecessors);
        }
    }
    private static boolean strictlyBelow(Set<String> lower,Set<String> upper){
        return lower.size()<upper.size()&&upper.containsAll(lower);
    }
    public static List<Group> plan(Map<String,Set<String>> fingerprints){
        if(fingerprints.size()>256)throw new IllegalArgumentException("Too many harvest representatives");
        // Canonical ordering makes results independent of registry iteration order.
        var byBlocks=new LinkedHashMap<Set<String>,List<String>>();
        fingerprints.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry->{
            var blocks=Set.copyOf(entry.getValue());
            if(!blocks.isEmpty())byBlocks.computeIfAbsent(blocks,key->new ArrayList<>()).add(entry.getKey());
        });
        var groups=new ArrayList<Group>();
        byBlocks.forEach((blocks,tools)->groups.add(new Group(tools.getFirst(),tools,blocks,List.of())));
        var result=new ArrayList<Group>();
        for(var upper:groups){
            var direct=new TreeSet<String>();
            for(var lower:groups){
                if(!strictlyBelow(lower.blocks(),upper.blocks()))continue;
                // Hasse edges: remove redundant transitive prerequisites.
                boolean intermediate=groups.stream().anyMatch(mid->
                    strictlyBelow(lower.blocks(),mid.blocks())&&strictlyBelow(mid.blocks(),upper.blocks()));
                if(!intermediate)direct.add(lower.representative());
            }
            result.add(new Group(upper.representative(),upper.equivalents(),upper.blocks(),List.copyOf(direct)));
        }
        result.sort(Comparator.comparingInt((Group g)->g.blocks().size()).thenComparing(Group::representative));
        return List.copyOf(result);
    }
}
