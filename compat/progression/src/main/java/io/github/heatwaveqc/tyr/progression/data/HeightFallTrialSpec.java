package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.util.*;

/** Validated, reusable server landing trial. Bounds are coordinates, not an immunity timer. */
public record HeightFallTrialSpec(String milestone,String dimension,double startY,double endY,
                                  double distance,String advancement,String advancementCriterion) {
    public HeightFallTrialSpec {
        exactId(milestone);exactId(dimension);
        if(!Double.isFinite(startY)||!Double.isFinite(endY)||!Double.isFinite(distance)
                ||Math.abs(startY)>2048||Math.abs(endY)>2048||startY<=endY
                ||distance<10||distance>4096)
            throw new IllegalArgumentException("Invalid height fall trial bounds");
        if(advancement==null||advancementCriterion==null
                ||advancement.isEmpty()!=advancementCriterion.isEmpty())
            throw new IllegalArgumentException("Advancement and criterion must be paired");
        if(!advancement.isEmpty()){
            exactId(advancement);
            if(!advancementCriterion.matches("[a-zA-Z0-9_.:/-]{1,128}"))
                throw new IllegalArgumentException("Invalid advancement criterion");
        }
    }
    private static String exactId(String value){
        if(value==null||!value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Expected exact namespaced ID");
        return value;
    }
    private static String string(JsonObject j,String key,String fallback){
        if(!j.has(key))return fallback;
        var e=j.get(key);
        if(!e.isJsonPrimitive()||!e.getAsJsonPrimitive().isString())
            throw new IllegalArgumentException("Expected string "+key);
        return e.getAsString();
    }
    private static double number(JsonObject j,String key){
        var e=j.get(key);
        if(e==null||!e.isJsonPrimitive()||!e.getAsJsonPrimitive().isNumber())
            throw new IllegalArgumentException("Expected number "+key);
        return e.getAsDouble();
    }
    public static List<HeightFallTrialSpec> parse(JsonObject raw,Definition.Path path){
        if(!raw.has("milestones"))return List.of();
        var result=new ArrayList<HeightFallTrialSpec>();
        var byId=new HashMap<String,Definition.Milestone>();
        path.milestones().forEach(m->byId.put(m.id(),m));
        var signals=new HashSet<String>();
        for(var element:raw.getAsJsonArray("milestones")){
            var j=element.getAsJsonObject();
            var m=byId.get(string(j,"id",""));
            if(m==null)throw new IllegalArgumentException("Unknown fall milestone");
            boolean signal=m.criteria().stream().anyMatch(c->c.event().equals("height_fall_trial"));
            if(signal!=j.has("fall_trial"))throw new IllegalArgumentException("Height fall trial requires both criterion and fall_trial");
            if(!signal)continue;
            if(!m.strictOrder()||m.criteria().size()!=1||m.criteria().getFirst().count()!=1
                    ||!m.criteria().getFirst().selector().equals(m.id())||!signals.add(m.id()))
                throw new IllegalArgumentException("Height fall trial must be a unique strict single-deed milestone");
            var f=j.getAsJsonObject("fall_trial");
            if(!Set.of("dimension","start_y","end_y","distance","advancement","advancement_criterion").containsAll(f.keySet()))
                throw new IllegalArgumentException("Unknown fall trial field");
            result.add(new HeightFallTrialSpec(m.id(),string(f,"dimension",""),
                number(f,"start_y"),number(f,"end_y"),number(f,"distance"),
                string(f,"advancement",""),string(f,"advancement_criterion","")));
        }
        return List.copyOf(result);
    }

    /** Native start/end must satisfy exact bounds. Accumulated fall distance plus
     * the final downward step verifies continuous descent; small tolerance only
     * accommodates floating point and native fall-start sampling. */
    public boolean qualifies(String actualDimension,double start,double end,double accumulated,double lastStep){
        if(!dimension.equals(actualDimension)||!Double.isFinite(start)||!Double.isFinite(end)
                ||!Double.isFinite(accumulated)||!Double.isFinite(lastStep)||accumulated<=0
                ||start<startY||end>endY||start-end<distance||lastStep>0)return false;
        return accumulated+Math.max(0,-lastStep)>=start-end-1.5;
    }
}
