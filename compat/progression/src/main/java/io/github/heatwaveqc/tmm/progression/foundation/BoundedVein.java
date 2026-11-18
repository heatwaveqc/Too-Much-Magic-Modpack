package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.*;
import java.util.function.*;

/** Independently implemented bounded flood traversal. Only a committed break expands the frontier. */
public final class BoundedVein {
    private BoundedVein(){}
    public static <T> int walk(T origin,int budget,int maxVisited,Function<T,Iterable<T>> neighbors,Predicate<T> commit){
        if(budget<1||maxVisited<1)return 0;
        var queue=new ArrayDeque<T>();
        var visited=new HashSet<T>();
        visited.add(origin);
        for(T next:neighbors.apply(origin))if(visited.size()<maxVisited&&visited.add(next))queue.addLast(next);
        int broken=0;
        while(!queue.isEmpty()&&broken<budget){
            T next=queue.removeFirst();
            if(!commit.test(next))continue;
            broken++;
            for(T neighbor:neighbors.apply(next))if(visited.size()<maxVisited&&visited.add(neighbor))queue.addLast(neighbor);
        }
        return broken;
    }
}
