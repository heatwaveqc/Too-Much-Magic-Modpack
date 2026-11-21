package io.github.heatwaveqc.tmm.progression.mxp.core;
import java.util.UUID;

/** Immutable attribution token; a missing native actor is tolerated only after a verified reload. */
public record PrimalRitualProof(String recipeId,UUID actor,UUID operation,boolean restored) {
    public boolean matches(boolean active,String current,UUID nativeActor){
        return active&&actor!=null&&operation!=null&&recipeId!=null&&recipeId.equals(current)&&
            PrimalRitualCatalog.lookup(recipeId)!=null&&
            (actor.equals(nativeActor)||(restored&&nativeActor==null));
    }
    public RitualRewards.Receipt receipt(boolean installed,boolean cleared){
        return new RitualRewards.Receipt(recipeId,operation,actor,installed&&cleared,false);
    }
}
