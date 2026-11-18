package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.JsonObject;
import java.util.*;

/** Data-defined alias to one native player attribute. Expansion keeps effect indices stable. */
public final class MagicCatalog {
    private MagicCatalog(){}
    public record Binding(String attribute,String operation,double scale){}
    public static Map<String,Binding> read(JsonObject root){
        if(root.get("schema").getAsInt()!=1)throw new IllegalArgumentException("Magic catalog schema");
        var bindings=new HashMap<String,Binding>();
        for(var row:root.getAsJsonObject("stats").entrySet()){
            var j=row.getValue().getAsJsonObject();String id=row.getKey(),attr=j.get("attribute").getAsString();
            String op=j.has("operation")?j.get("operation").getAsString():"add_value";
            double scale=j.has("scale")?j.get("scale").getAsDouble():1;
            if(!id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||!attr.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||
                !Set.of("add_value","add_multiplied_base","add_multiplied_total").contains(op)||
                !Double.isFinite(scale)||scale<=0||scale>1000000)throw new IllegalArgumentException("Bad magic stat "+id);
            bindings.put(id,new Binding(attr,op,scale));
        }
        return Map.copyOf(bindings);
    }
    public static JsonObject expand(JsonObject input,Map<String,Binding> bindings){
        var copy=input.deepCopy();
        if(!copy.has("skills"))return copy;
        for(var skill:copy.getAsJsonArray("skills")){
            var row=skill.getAsJsonObject();if(!row.has("effects"))continue;
            for(var elem:row.getAsJsonArray("effects")){
                var fx=elem.getAsJsonObject();
                if(!fx.has("type")||!fx.get("type").getAsString().equals("magic_stat"))continue;
                String id=fx.get("target").getAsString();var binding=bindings.get(id);
                if(binding==null)throw new IllegalArgumentException("Unknown magic stat "+id);
                if(fx.has("attribute")||fx.has("operation"))throw new IllegalArgumentException("Magic stat binding owns native attribute and operation");
                double value=fx.get("per_rank").getAsDouble()*binding.scale();
                if(!Double.isFinite(value)||Math.abs(value)>10000)throw new IllegalArgumentException("Magic stat contribution outside native parser bounds");
                fx.addProperty("type","attribute");fx.remove("target");fx.addProperty("attribute",binding.attribute());
                fx.addProperty("operation",binding.operation());fx.addProperty("per_rank",value);
            }
        }
        return copy;
    }
}
