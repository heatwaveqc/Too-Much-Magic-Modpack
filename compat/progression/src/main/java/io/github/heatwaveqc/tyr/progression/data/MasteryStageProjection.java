package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.util.Map;
import java.util.Set;

/** Non-spendable Path stage projected from invested points in a shared currency. */
public record MasteryStageProjection(String systemId,int pointsPerStage,int maxStage) {
    public MasteryStageProjection {
        if(systemId==null||!systemId.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                ||pointsPerStage<1||pointsPerStage>100000||maxStage<1||maxStage>200)
            throw new IllegalArgumentException("Invalid mastery stage projection");
    }
    public static MasteryStageProjection parse(JsonObject path,Map<String,XpChannel> systems) {
        if(!path.has("mastery_stages"))return null;
        var value=path.getAsJsonObject("mastery_stages");
        if(!value.keySet().equals(Set.of("system","points_per_stage","max_stage")))
            throw new IllegalArgumentException("Invalid mastery_stages fields");
        var rule=new MasteryStageProjection(value.get("system").getAsString(),
                value.get("points_per_stage").getAsInt(),value.get("max_stage").getAsInt());
        var system=systems.get(rule.systemId());
        if(system==null||!system.magic())
            throw new IllegalArgumentException("Mastery stages require shared XP");
        return rule;
    }
    public int stage(ExperienceEconomy.Ledger state,String path){
        long spent=0;
        for(var purchase:state.paidRanks())
            if(path.equals(purchase.path())&&systemId.equals(purchase.system()))
                spent+=purchase.pointCost();
        return (int)Math.min(maxStage,1L+spent/pointsPerStage);
    }
}
