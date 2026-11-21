package io.github.heatwaveqc.tyr.progression.mxp.core;
import java.util.UUID;

/** Stable native activation identity; never recover ownership from nearby players. */
public record GoetyRitualProof(String recipeId,UUID actor,UUID operation) {
    public boolean matches(String recipe,UUID owner){
        return recipeId!=null&&recipeId.equals(recipe)&&actor!=null&&actor.equals(owner)&&operation!=null&&GoetyRitualCatalog.lookup(recipeId)!=null;
    }
    public RitualRewards.Receipt receipt(boolean outcome,boolean cleared){
        return new RitualRewards.Receipt(recipeId,operation,actor,outcome&&cleared,false);
    }
}
