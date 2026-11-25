package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.*;
import java.util.function.*;

/** Independently implemented bounded flood traversal. Only a committed break expands the frontier. */
public final class BoundedVein {
    private BoundedVein(){}
    public enum Axis { X,Y,Z }
    public record Offset(int x,int y,int z){}
    /** Centered, one-layer square perpendicular to the captured face; excludes the origin. */
    public static List<Offset> plane(Axis normal,int radius){
        if(radius<1||radius>3)throw new IllegalArgumentException("Radial radius outside 1..3");
        var result=new ArrayList<Offset>();
        for(int u=-radius;u<=radius;u++)for(int v=-radius;v<=radius;v++){
            if(u==0&&v==0)continue;
            result.add(switch(normal){
                case X -> new Offset(0,u,v);
                case Y -> new Offset(u,0,v);
                case Z -> new Offset(u,v,0);
            });
        }
        return List.copyOf(result);
    }
    public static boolean radialHardness(double origin,double target,double ratio){
        return Double.isFinite(origin)&&Double.isFinite(target)&&Double.isFinite(ratio)
            &&origin>0&&target>0&&ratio>=1&&ratio<=3&&target<=origin*ratio;
    }
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
