package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Pure shared reward and recent-hit rules; no native entity or damage assumptions. */
public final class BossRewardPolicy {
    private BossRewardPolicy() {}
    public enum Tier { NONE, STANDARD, MINIBOSS, ELITE }
    public static Tier tier(boolean blocked,boolean boss,boolean miniboss,double eliteMultiplier) {
        if(blocked)return Tier.NONE;
        if(boss)return Tier.STANDARD;
        if(miniboss)return Tier.MINIBOSS;
        return eliteMultiplier>0?Tier.ELITE:Tier.NONE;
    }
    public static double eliteMultiplier(boolean invader,boolean elite,String rarity) {
        if(invader)return switch(rarity) {
            case "apotheosis:common","apotheosis:uncommon" -> 1;
            case "apotheosis:rare" -> 2;
            case "apotheosis:epic" -> 3;
            case "apotheosis:mythic" -> 4;
            default -> 0; // unrecognized native rarity: do not guess
        };
        return elite?1:0;
    }
    public static Set<UUID> recent(Map<UUID,Long> hits,long now,int window,UUID finalBlow) {
        var result=new LinkedHashSet<UUID>();
        hits.forEach((id,tick)->{if(tick<=now&&now-tick<=window)result.add(id);});
        if(finalBlow!=null)result.add(finalBlow);
        return Set.copyOf(result);
    }
}
