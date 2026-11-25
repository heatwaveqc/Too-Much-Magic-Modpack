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
    public static List<Group> plan(Map<String,Set<String>> fingerprints){return plan(fingerprints,List.of());}
    public static List<Group> plan(Map<String,Set<String>> fingerprints,List<String> preferred){
        if(fingerprints.size()>256)throw new IllegalArgumentException("Too many harvest representatives");
        // Canonical ordering makes results independent of registry iteration order.
        var byBlocks=new LinkedHashMap<Set<String>,List<String>>();
        fingerprints.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry->{
            var blocks=Set.copyOf(entry.getValue());
            if(!blocks.isEmpty())byBlocks.computeIfAbsent(blocks,key->new ArrayList<>()).add(entry.getKey());
        });
        var groups=new ArrayList<Group>();
        byBlocks.forEach((blocks,tools)->{
            String representative=preferred.stream().filter(tools::contains).findFirst().orElse(tools.getFirst());
            groups.add(new Group(representative,tools,blocks,List.of()));
        });
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

    /** Unique training blocks relative to all direct predecessors. An upper
     * capability covered by the union is redundant, not a new trial. */
    public static Set<String> trainingBlocks(Group group,Map<String,Group> byId){
        var delta=new TreeSet<>(group.blocks());
        for(String predecessor:group.predecessors())delta.removeAll(byId.get(predecessor).blocks());
        return Set.copyOf(delta);
    }
}
