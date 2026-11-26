package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.util.*;

/** Server-observed environmental or projectile trial configuration. */
public record ObservedTrialSpec(String milestone,String kind,String dimension,int ticks,
                                double distance,double lookCosine,double radius,String shooter) {
    private static String str(JsonObject j,String k,String def){return j.has(k)?j.get(k).getAsString():def;}
    private static double num(JsonObject j,String k,double def){
        if(!j.has(k))return def;
        var v=j.get(k);
        if(!v.isJsonPrimitive()||!v.getAsJsonPrimitive().isNumber())throw new IllegalArgumentException("Expected numeric "+k);
        return v.getAsDouble();
    }
    private static boolean id(String s){return s.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");}
    public ObservedTrialSpec{
        if(!id(milestone)||!id(dimension)||!Set.of("underwater","moon_boat","fire_walk","arrow_dodge").contains(kind)
            ||ticks<1||ticks>72000||!Double.isFinite(distance)||distance<1||distance>1000
            ||!Double.isFinite(lookCosine)||lookCosine<0||lookCosine>1||!Double.isFinite(radius)||radius<1||radius>4
            ||!id(shooter))throw new IllegalArgumentException("Invalid observed trial");
    }
    public static List<ObservedTrialSpec> parse(JsonObject raw,Definition.Path path){
        if(!raw.has("milestones"))return List.of();
        var out=new ArrayList<ObservedTrialSpec>();
        for(var e:raw.getAsJsonArray("milestones")){
            var j=e.getAsJsonObject();String mid=str(j,"id","");
            var m=path.milestones().stream().filter(x->x.id().equals(mid)).findFirst().orElseThrow();
            boolean observed=m.criteria().stream().anyMatch(c->Set.of("observed_trial","arrow_dodged").contains(c.event()));
            if(observed!=j.has("observed_trial"))throw new IllegalArgumentException("Observed criterion/metadata mismatch");
            if(!observed)continue;
            var o=j.getAsJsonObject("observed_trial");
            if(!Set.of("kind","dimension","ticks","distance","look_cosine","radius","shooter").containsAll(o.keySet()))
                throw new IllegalArgumentException("Unknown observed trial field");
            String kind=str(o,"kind","");
            boolean arrow=kind.equals("arrow_dodge");
            if(!m.strictOrder()||m.criteria().size()!=1||!m.criteria().getFirst().selector().equals(mid)
                ||!m.criteria().getFirst().event().equals(arrow?"arrow_dodged":"observed_trial")
                ||!arrow&&m.criteria().getFirst().count()!=1)
                throw new IllegalArgumentException("Observed trial requires a strict single criterion targeting itself");
            double ticks=num(o,"ticks",1);
            if(ticks!=Math.rint(ticks)||ticks<1||ticks>72000)throw new IllegalArgumentException("Invalid ticks");
            if((kind.equals("underwater")||kind.equals("moon_boat"))&&!o.has("ticks")
                ||kind.equals("fire_walk")&&!o.has("distance"))
                throw new IllegalArgumentException("Observed trial threshold missing");
            out.add(new ObservedTrialSpec(mid,kind,str(o,"dimension","minecraft:overworld"),(int)ticks,
                num(o,"distance",10),num(o,"look_cosine",0.92),num(o,"radius",2.5),str(o,"shooter","minecraft:skeleton")));
        }
        return List.copyOf(out);
    }
}
