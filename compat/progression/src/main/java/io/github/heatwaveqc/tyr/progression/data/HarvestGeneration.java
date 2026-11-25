package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.foundation.HarvestCapabilityGraph;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiFunction;

/** Expands configured tool families into ordinary achievement-backed skills.
 * Pure compiler; the caller supplies a native registry survey after tag binding. */
public final class HarvestGeneration {
    private HarvestGeneration(){}
    public record Result(JsonObject path,Map<String,Set<String>> trials) {
        public Result {trials=Map.copyOf(trials);}
    }
    private static final Set<String> FIELDS=Set.of("id","item_selector","block_selector",
        "count","after","prefer_tools","exclude_tools","training_blocks","lane","unarmored","hud_group");
    private static String id(String value,boolean tag){
        if(!value.matches((tag?"#?":"")+"[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Invalid harvest generation ID/selector "+value);
        return value;
    }
    private static String value(JsonObject row,String key){
        if(!row.has(key)||!row.get(key).isJsonPrimitive()||!row.get(key).getAsJsonPrimitive().isString())
            throw new IllegalArgumentException("Missing/invalid harvest generation "+key);
        return row.get(key).getAsString();
    }
    private static JsonArray array(JsonObject row,String key){
        return row.has(key)?row.getAsJsonArray(key):new JsonArray();
    }
    private static List<String> ids(JsonObject row,String key){
        var list=new ArrayList<String>();
        for(var e:array(row,key)){
            if(!e.isJsonPrimitive()||!e.getAsJsonPrimitive().isString())
                throw new IllegalArgumentException("Expected harvest generation ID");
            list.add(id(e.getAsString(),false));
        }
        if(new HashSet<>(list).size()!=list.size())throw new IllegalArgumentException("Duplicate "+key);
        return List.copyOf(list);
    }
    private static JsonArray strings(Collection<String> values){
        var array=new JsonArray();values.forEach(array::add);return array;
    }
    private static boolean bool(JsonObject row,String key,boolean fallback){
        if(!row.has(key))return fallback;
        if(!row.get(key).isJsonPrimitive()||!row.get(key).getAsJsonPrimitive().isBoolean())
            throw new IllegalArgumentException("Expected boolean "+key);
        return row.get(key).getAsBoolean();
    }
    /** Stable identity per path, family and exact tool ID, not graph order or fingerprint. */
    public static String receipt(String path,String family,String tool){
        return "tyr_progression:harvest/"+UUID.nameUUIDFromBytes(
            (path+"|"+family+"|"+tool).getBytes(StandardCharsets.UTF_8));
    }
    public static Result expand(String pathId,JsonObject input,
            BiFunction<String,String,Map<String,Set<String>>> survey){
        var copy=input.deepCopy();
        if(!copy.has("harvest_generation"))return new Result(copy,Map.of());
        var definitions=copy.remove("harvest_generation").getAsJsonArray();
        if(definitions.size()<1||definitions.size()>8)throw new IllegalArgumentException("Harvest generator count");
        var milestones=array(copy,"milestones");
        var skills=array(copy,"skills");
        var trials=new LinkedHashMap<String,Set<String>>();
        var families=new HashSet<String>();
        var candidateIds=new HashSet<String>();
        for(var element:definitions){
            var row=element.getAsJsonObject();
            if(!FIELDS.containsAll(row.keySet()))throw new IllegalArgumentException("Unknown harvest generation field");
            String family=id(value(row,"id"),false);
            if(!families.add(family))throw new IllegalArgumentException("Duplicate harvest generator "+family);
            String items=id(value(row,"item_selector"),true),blocks=id(value(row,"block_selector"),true);
            int count=row.has("count")?row.get("count").getAsBigDecimal().intValueExact():5;
            if(count<1||count>1000)throw new IllegalArgumentException("Harvest trial count");
            var after=ids(row,"after");
            var preferred=ids(row,"prefer_tools");
            var excluded=ids(row,"exclude_tools");
            var overrides=row.has("training_blocks")?row.getAsJsonObject("training_blocks"):new JsonObject();
            boolean unarmored=bool(row,"unarmored",false);
            String lane=row.has("lane")?value(row,"lane"):"Harvesting";
            String hudGroup=row.has("hud_group")?value(row,"hud_group"):"";
            var fingerprints=new TreeMap<>(survey.apply(items,blocks));
            excluded.forEach(fingerprints::remove);
            // The same exact signal cannot serve unrelated families in one path.
            for(String tool:fingerprints.keySet())if(!candidateIds.add(tool))
                throw new IllegalArgumentException("Representative used by multiple harvest generators "+tool);
            var groups=HarvestCapabilityGraph.plan(fingerprints,preferred);
            if(groups.isEmpty())throw new IllegalArgumentException("No harvest capabilities for "+family);
            var byId=new HashMap<String,HarvestCapabilityGraph.Group>();
            groups.forEach(g->byId.put(g.representative(),g));
            var resolved=new HashMap<String,List<String>>();
            var usedOverrides=new HashSet<String>();
            for(var group:groups){
                var tool=group.representative();
                var parentIds=new TreeSet<String>();
                parentIds.addAll(after);
                for(String lower:group.predecessors())parentIds.addAll(resolved.get(lower));
                var delta=HarvestCapabilityGraph.trainingBlocks(group,byId);
                if(delta.isEmpty()){
                    // Union of already-required branches supplies this whole capability.
                    // No impossible trial; forward its actual dependencies to descendants.
                    resolved.put(tool,List.copyOf(parentIds));
                    continue;
                }
                var selected=new TreeSet<>(delta);
                for(String equivalent:group.equivalents())if(overrides.has(equivalent)){
                    if(!usedOverrides.add(equivalent))throw new IllegalArgumentException("Duplicate training override");
                    var allowed=new TreeSet<String>();
                    for(var b:overrides.getAsJsonArray(equivalent))allowed.add(id(b.getAsString(),false));
                    if(allowed.isEmpty()||!delta.containsAll(allowed))
                        throw new IllegalArgumentException("Training blocks must be a nonempty subset of new capability: "+equivalent);
                    selected.retainAll(allowed);
                }
                if(selected.isEmpty())throw new IllegalArgumentException("Incompatible training block overrides "+tool);
                String receipt=receipt(pathId,family,tool);
                var aliases=group.equivalents().stream().filter(t->!t.equals(tool))
                    .map(t->receipt(pathId,family,t)).sorted().toList();
                var criterion=new JsonObject();
                criterion.addProperty("event","material_harvest_trial");
                criterion.addProperty("target",tool);
                criterion.addProperty("count",count);
                var milestone=new JsonObject();
                milestone.addProperty("id",receipt);
                milestone.addProperty("strict_order",true);
                milestone.addProperty("notify_progress",true);
                milestone.addProperty("title",title("Harvesting: ",tool));
                milestone.add("after",strings(parentIds));
                if(!hudGroup.isEmpty()){
                    var hud=new JsonObject();hud.addProperty("group",hudGroup);
                    var objective=new JsonObject();
                    var examples=selected.stream().limit(2).map(HarvestGeneration::readableId).toList();
                    String message="Break blocks for "+readableId(tool)+": "+String.join(", ",examples);
                    if(selected.size()>examples.size())message+=" or equivalent blocks";
                    objective.addProperty("text",message.length()<=240?message:message.substring(0,240));
                    var objectives=new JsonArray();objectives.add(objective);hud.add("objectives",objectives);
                    milestone.add("hud",hud);
                }
                milestone.add("completion_aliases",strings(aliases));
                var criteria=new JsonArray();criteria.add(criterion);milestone.add("criteria",criteria);
                milestones.add(milestone);
                var effect=new JsonObject();
                effect.addProperty("type","native_harvest");effect.addProperty("target",blocks);
                effect.addProperty("empty_hand",true);effect.addProperty("unarmored",unarmored);
                var params=new JsonObject();params.addProperty("tool",tool);effect.add("params",params);
                var skill=new JsonObject();
                skill.addProperty("id",receipt+"/skill");skill.addProperty("title",title("Harvest: ",tool));
                skill.addProperty("description","Complete the material harvesting challenge. Grants native drop eligibility, not mining speed.");
                skill.addProperty("lane",lane);skill.addProperty("max_rank",1);
                skill.add("auto_achievements",strings(List.of(receipt)));
                var effects=new JsonArray();effects.add(effect);skill.add("effects",effects);
                skills.add(skill);
                if(trials.putIfAbsent(tool,Set.copyOf(selected))!=null)
                    throw new IllegalArgumentException("Duplicate generated tool "+tool);
                resolved.put(tool,List.of(receipt));
            }
            if(!usedOverrides.equals(overrides.keySet()))
                throw new IllegalArgumentException("Training override did not match an active material group");
        }
        // Prevent a manual milestone using the same tool signal from inheriting the generated scope.
        var seen=new HashSet<String>();
        for(var e:milestones)for(var c:array(e.getAsJsonObject(),"criteria")){
            var q=c.getAsJsonObject();
            if(q.has("event")&&q.get("event").getAsString().equals("material_harvest_trial")){
                String target=q.get("target").getAsString();
                if(trials.containsKey(target)&&!seen.add(target))
                    throw new IllegalArgumentException("Manual/generated material criterion collision "+target);
            }
        }
        copy.add("milestones",milestones);copy.add("skills",skills);
        return new Result(copy,trials);
    }
    private static String readableId(String id){return id.replace(':',' ').replace('_',' ').replace('/',' ');}
    private static String title(String prefix,String tool){
        String title=prefix+tool;
        return title.length()<=120?title:title.substring(0,120);
    }
}
