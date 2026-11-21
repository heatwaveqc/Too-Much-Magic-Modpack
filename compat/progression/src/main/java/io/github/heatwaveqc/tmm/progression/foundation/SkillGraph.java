package io.github.heatwaveqc.tmm.progression.foundation;
import java.util.*;

/** Stable, bounded presentation-only DAG layout. Actual prerequisites remain server authoritative. */
public final class SkillGraph {
    private SkillGraph(){}
    public record Skill(String id,int rank,List<String> required,List<String> alternatives){
        public Skill{required=List.copyOf(required);alternatives=List.copyOf(alternatives);}
    }
    public record Connection(String parent,String child,boolean alternative){}
    public record Graph(TreeLayout.Layout layout,List<Connection> connections){}
    public static Graph arrange(List<Skill> skills,int columns,int w,int h,int gapX,int gapY){
        if(skills.size()>4096||columns<1||columns>32)throw new IllegalArgumentException("Graph too large");
        var remaining=new LinkedHashMap<String,Skill>();var placed=new HashSet<String>();
        for(var skill:skills)if(remaining.putIfAbsent(skill.id(),skill)!=null)throw new IllegalArgumentException("Duplicate skill");
        var nodes=new ArrayList<TreeLayout.Node>();var connections=new ArrayList<Connection>();
        for(var skill:skills){
            var parents=new LinkedHashSet<>(skill.required());parents.addAll(skill.alternatives());
            for(String parent:parents){
                if(!remaining.containsKey(parent))throw new IllegalArgumentException("Unknown graph edge");
                connections.add(new Connection(parent,skill.id(),!skill.required().contains(parent)));
            }
        }
        int depth=0;
        while(!remaining.isEmpty()){
            var ready=remaining.values().stream().filter(skill->placed.containsAll(skill.required())&&placed.containsAll(skill.alternatives()))
                .sorted(Comparator.comparingInt(Skill::rank).thenComparing(Skill::id)).toList();
            if(ready.isEmpty())throw new IllegalArgumentException("Cyclic graph");
            int firstRank=ready.getFirst().rank();
            var row=ready.stream().filter(skill->skill.rank()==firstRank).limit(columns).toList();
            for(var skill:row){
                nodes.add(new TreeLayout.Node(skill.id(),depth));
                placed.add(skill.id());remaining.remove(skill.id());
            }
            depth++;
        }
        var edges=connections.stream().map(c->new TreeLayout.Edge(c.parent(),c.child())).toList();
        return new Graph(TreeLayout.arrange(nodes,edges,w,h,gapX,gapY),List.copyOf(connections));
    }
}
