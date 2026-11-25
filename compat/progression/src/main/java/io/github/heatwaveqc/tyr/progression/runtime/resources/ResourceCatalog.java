package io.github.heatwaveqc.tyr.progression.runtime.resources;

import com.google.gson.*;
import io.github.heatwaveqc.tyr.progression.data.Eligibility;
import io.github.heatwaveqc.tyr.progression.foundation.Condition;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;

/** Optional Path-owned data, validated before installation with the Path registry. */
public final class ResourceCatalog {
    private ResourceCatalog(){}
    public static final int MAX_PER_PATH=16,MAX_DEFINITIONS=256,MAX_RULES=32;
    private static volatile Map<String,Spec> active=Map.of();
    public record Rule(String event,ResourcePolicy.Operation operation,int amount,int perUnit,
                       Condition<ServerPlayer> when){}
    public record Spec(String id,String path,int capacity,int initial,boolean resetOnDeath,
                       String capacitySkill,int capacityPerRank,int intervalTicks,
                       boolean hud,String title,int color,int divisor,List<Rule> rules){
        public Spec{rules=List.copyOf(rules);}
        public int capacity(int rank){
            return (int)Math.min(ResourcePolicy.MAX,(long)capacity+(long)Math.max(0,rank)*capacityPerRank);
        }
    }
    public static Map<String,Spec> current(){return active;}
    public static void install(Map<String,Spec> next){active=Map.copyOf(next);}
    public static Map<String,Spec> parse(Map<String,JsonObject> paths,Map<String,Definition.Path> definitions){
        var out=new TreeMap<String,Spec>();
        for(var entry:paths.entrySet()){
            var raw=entry.getValue();
            if(!raw.has("resources"))continue;
            var list=raw.getAsJsonArray("resources");
            if(list.size()>MAX_PER_PATH)throw new IllegalArgumentException("Too many resources on "+entry.getKey());
            for(var item:list){
                var row=item.getAsJsonObject();
                fields(row,Set.of("id","capacity","initial","reset_on_death","capacity_skill","capacity_per_rank","interval_ticks","hud","rules"));
                String id=string(row,"id",null);
                if(!ResourcePolicy.validId(id))throw new IllegalArgumentException("Invalid resource ID");
                int max=integer(row,"capacity",-1,1,ResourcePolicy.MAX);
                int initial=integer(row,"initial",0,0,max);
                String skill=string(row,"capacity_skill","");
                int perRank=integer(row,"capacity_per_rank",0,0,ResourcePolicy.MAX);
                if((skill.isEmpty())!=(perRank==0)||(!skill.isEmpty()&&definitions.get(entry.getKey()).skill(skill)==null))
                    throw new IllegalArgumentException("Invalid resource capacity skill "+id);
                int interval=integer(row,"interval_ticks",20,1,72000);
                var hud=row.has("hud")?row.getAsJsonObject("hud"):new JsonObject();
                fields(hud,Set.of("visible","title","color","display_divisor"));
                String title=string(hud,"title",id);
                if(title.isBlank()||title.length()>48||title.chars().anyMatch(Character::isISOControl))
                    throw new IllegalArgumentException("Invalid resource HUD title; specify a short title");
                String color=string(hud,"color","d48b43");
                if(!color.matches("[0-9a-fA-F]{6}"))throw new IllegalArgumentException("Invalid HUD color");
                int divisor=integer(hud,"display_divisor",1,1,ResourcePolicy.MAX);
                if(!ResourcePolicy.validDivisor(divisor))throw new IllegalArgumentException("Display divisor must be power of ten");
                var rules=new ArrayList<Rule>();
                var ruleRows=row.has("rules")?row.getAsJsonArray("rules"):new JsonArray();
                if(ruleRows.size()>MAX_RULES)throw new IllegalArgumentException("Too many resource rules");
                for(var rawRule:ruleRows){
                    var rule=rawRule.getAsJsonObject();
                    fields(rule,Set.of("event","operation","amount","amount_per_unit","when"));
                    String event=string(rule,"event",null);
                    if(!"interval".equals(event)&&!ResourcePolicy.validId(event))throw new IllegalArgumentException("Invalid resource event");
                    var op=ResourcePolicy.Operation.valueOf(string(rule,"operation",null).toUpperCase(Locale.ROOT));
                    int min=op==ResourcePolicy.Operation.ADD?-ResourcePolicy.MAX:0;
                    int amount=integer(rule,"amount",0,min,ResourcePolicy.MAX);
                    int scale=integer(rule,"amount_per_unit",0,min,ResourcePolicy.MAX);
                    if("interval".equals(event)&&(scale!=0||op==ResourcePolicy.Operation.SPEND))
                        throw new IllegalArgumentException("Interval supports fixed add/set only");
                    rules.add(new Rule(event,op,amount,scale,Eligibility.parse(rule.get("when"))));
                }
                var spec=new Spec(id,entry.getKey(),max,initial,bool(row,"reset_on_death",false),
                    skill,perRank,interval,bool(hud,"visible",true),title,Integer.parseInt(color,16),divisor,rules);
                if(out.putIfAbsent(id,spec)!=null||out.size()>MAX_DEFINITIONS)
                    throw new IllegalArgumentException("Duplicate or excessive resource ID "+id);
            }
        }
        return Map.copyOf(out);
    }
    private static void fields(JsonObject row,Set<String> allowed){
        for(String key:row.keySet())if(!allowed.contains(key))throw new IllegalArgumentException("Unknown Path resource field "+key);
    }
    private static String string(JsonObject row,String key,String fallback){
        if(!row.has(key)){if(fallback==null)throw new IllegalArgumentException("Missing "+key);return fallback;}
        if(!row.get(key).isJsonPrimitive()||!row.getAsJsonPrimitive(key).isString())throw new IllegalArgumentException("Expected string "+key);
        return row.get(key).getAsString();
    }
    private static int integer(JsonObject row,String key,int fallback,int min,int max){
        int value=fallback;
        if(row.has(key)){
            if(!row.get(key).isJsonPrimitive()||!row.getAsJsonPrimitive(key).isNumber())throw new IllegalArgumentException("Expected integer "+key);
            value=row.get(key).getAsBigDecimal().intValueExact();
        }
        if(value<min||value>max)throw new IllegalArgumentException("Out of range "+key);
        return value;
    }
    private static boolean bool(JsonObject row,String key,boolean fallback){
        if(!row.has(key))return fallback;
        if(!row.get(key).isJsonPrimitive()||!row.getAsJsonPrimitive(key).isBoolean())throw new IllegalArgumentException("Expected boolean "+key);
        return row.get(key).getAsBoolean();
    }
}
