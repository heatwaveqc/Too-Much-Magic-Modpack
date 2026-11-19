package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Set;

/** Successful consumption enrollment, distinct from ongoing path eligibility. */
public record FoodRiteSpec(String target,JsonElement when,String completionEvent){
    public static FoodRiteSpec parse(JsonObject path){
        boolean food=path.has("enrollment")&&path.get("enrollment").getAsString().equals("food");
        if(!food){
            if(path.has("food_rite"))throw new IllegalArgumentException("food_rite requires enrollment=food");
            return null;
        }
        var j=path.getAsJsonObject("food_rite");
        if(j==null||!j.has("target")||!j.has("when"))throw new IllegalArgumentException("Missing food rite selector/conditions");
        for(var key:j.keySet())if(!Set.of("target","when","completion_event").contains(key))
            throw new IllegalArgumentException("Unknown food rite field "+key);
        String target=j.get("target").getAsString(),event=j.has("completion_event")?j.get("completion_event").getAsString():"";
        if(!target.matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+")||!event.isEmpty()&&!event.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid food rite ID");
        return new FoodRiteSpec(target,j.get("when").deepCopy(),event);
    }
}
