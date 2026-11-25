package io.github.heatwaveqc.tyr.progression.runtime.taming;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.runtime.resources.ResourcePolicy;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/** Pure permanent ownership/eligibility rules; never a pet-count registry. */
public final class TamePolicy {
    private TamePolicy(){}
    public static final int THREAT_TICKS=200;
    private static final Set<String> SUPPORTED=Set.of("minecraft:pillager","minecraft:vindicator");
    public static boolean supported(String id){return SUPPORTED.contains(id);}
    public record State(UUID owner,UUID entity,boolean waiting){
        public State {
            if(owner==null||entity==null||owner.equals(entity))throw new IllegalArgumentException("Invalid companion identity");
        }
        public State toggle(){return new State(owner,entity,!waiting);}
        public String encode(){return owner+"/"+entity+"/"+(waiting?"wait":"follow");}
        public boolean belongsTo(UUID id){return owner.equals(id);}
    }
    public static Optional<State> decode(String stored,UUID actualEntity){
        if(stored==null||stored.length()>90||actualEntity==null)return Optional.empty();
        String[] parts=stored.split("/",-1);
        if(parts.length!=3||(!parts[2].equals("wait")&&!parts[2].equals("follow")))return Optional.empty();
        try{
            var state=new State(UUID.fromString(parts[0]),UUID.fromString(parts[1]),parts[2].equals("wait"));
            // Refuse copied receipts on different entity identities, including spawn-egg NBT.
            return state.entity().equals(actualEntity)&&state.encode().equals(stored)?Optional.of(state):Optional.empty();
        }catch(IllegalArgumentException bad){return Optional.empty();}
    }
    public record Spec(String resource,int cost){
        public Spec{
            if(resource==null||cost<0||cost>ResourcePolicy.MAX||(cost==0)!=resource.isEmpty()
                ||(!resource.isEmpty()&&!ResourcePolicy.validId(resource)))
                throw new IllegalArgumentException("Invalid tame cost");
        }
    }
    public static Spec validate(Definition.Effect fx){
        if(!fx.selector().matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+")||fx.amountPerRank()!=0
            ||!fx.attribute().isEmpty()||!fx.weapon().isEmpty()||!fx.damageTag().isEmpty())
            throw new IllegalArgumentException("Tame requires explicit entity ID/tag and no numeric rank effect");
        var p=fx.params();
        for(String key:p.keySet())if(!Set.of("resource","resource_cost","requires_milestone",
            "requires_sprinting","passive").contains(key))
            throw new IllegalArgumentException("Unknown tame parameter "+key);
        return new Spec(p.getOrDefault("resource",""),Integer.parseInt(p.getOrDefault("resource_cost","0")));
    }
    public static boolean threatFresh(long now,long until){
        return now>=0&&until>now&&until-now<=THREAT_TICKS;
    }
    public static boolean combatAllowed(boolean waiting,boolean ownerAvailable,boolean hostile,
            boolean protectedTarget,boolean authorizedThreat){
        return !waiting&&ownerAvailable&&hostile&&!protectedTarget&&authorizedThreat;
    }
}
