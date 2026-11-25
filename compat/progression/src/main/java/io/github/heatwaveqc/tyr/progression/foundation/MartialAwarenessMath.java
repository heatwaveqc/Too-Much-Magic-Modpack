package io.github.heatwaveqc.tyr.progression.foundation;

import java.util.*;
/** Pure time and threat-transition policy, independent of registries and client. */
public final class MartialAwarenessMath {
    private MartialAwarenessMath(){}
    public static boolean idle(long now,long last,int ticks){return now>=last&&now-last>=ticks;}
    public static int strongest(int actual,int conditional){return Math.max(actual,conditional);}
    public record ThreatState(Set<UUID> targets,long lastAlert){
        public ThreatState{targets=Set.copyOf(targets);}
        public static ThreatState empty(){return new ThreatState(Set.of(),Long.MIN_VALUE);}
    }
    public record Transition(ThreatState state,boolean alert){}
    public static Transition observe(ThreatState old,Set<UUID> next,long now,int cooldown){
        boolean fresh=next.stream().anyMatch(id->!old.targets().contains(id));
        boolean alert=fresh&&(old.lastAlert()==Long.MIN_VALUE||now>=old.lastAlert()&&now-old.lastAlert()>=cooldown);
        return new Transition(new ThreatState(next,alert?now:old.lastAlert()),alert);
    }
}
