package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.*;

/** Opt-in presentation only. No client or HUD code may award milestones. */
public final class ChallengeHudSpec {
    private ChallengeHudSpec(){}
    public static final int MAX_ROWS=4;
    public record Text(String text,String key){
        public Text{
            if(text==null||text.isBlank()||text.length()>240||text.chars().anyMatch(Character::isISOControl)
                    ||key==null||key.length()>160||!key.isEmpty()&&!key.matches("[a-zA-Z0-9_./:-]+"))
                throw new IllegalArgumentException("Invalid challenge HUD text");
        }
    }
    public record Objective(String milestone,List<Text> criteria){
        public Objective{criteria=List.copyOf(criteria);}
    }
    public record Lane(String id,Text title,List<Objective> objectives){
        public Lane{objectives=List.copyOf(objectives);}
    }
    public record Row(Text title,Text objective,int current,int total){
        public Row{
            Objects.requireNonNull(title);Objects.requireNonNull(objective);
            if(total<1||total>1000000||current<0||current>=total)
                throw new IllegalArgumentException("Invalid challenge HUD progress");
        }
    }
    private static String string(JsonObject j,String name,String fallback){
        if(!j.has(name))return fallback;
        var v=j.get(name);
        if(!v.isJsonPrimitive()||!v.getAsJsonPrimitive().isString())
            throw new IllegalArgumentException("Expected string "+name);
        return v.getAsString();
    }
    private static String laneId(String id){
        if(!id.matches("[a-z0-9_.-]{1,48}"))throw new IllegalArgumentException("Invalid HUD lane "+id);
        return id;
    }
    private static JsonArray array(JsonObject j,String key){
        return j.has(key)?j.getAsJsonArray(key):new JsonArray();
    }
    public static List<Lane> parse(JsonObject raw,Definition.Path path){
        var titles=new LinkedHashMap<String,Text>();
        var members=new LinkedHashMap<String,List<Objective>>();
        for(var element:array(raw,"challenge_hud")){
            var j=element.getAsJsonObject();
            if(!Set.of("id","title","title_key").containsAll(j.keySet()))
                throw new IllegalArgumentException("Unknown challenge HUD lane field");
            String id=laneId(string(j,"id",""));
            if(titles.putIfAbsent(id,new Text(string(j,"title",""),string(j,"title_key","")))!=null)
                throw new IllegalArgumentException("Duplicate challenge HUD lane "+id);
            members.put(id,new ArrayList<>());
            if(titles.size()>MAX_ROWS)throw new IllegalArgumentException("Too many challenge HUD lanes");
        }
        var milestones=new HashMap<String,Definition.Milestone>();
        path.milestones().forEach(m->milestones.put(m.id(),m));
        for(var element:array(raw,"milestones")){
            var j=element.getAsJsonObject();
            if(!j.has("hud"))continue;
            var hud=j.getAsJsonObject("hud");
            if(!Set.of("group","objectives").containsAll(hud.keySet()))
                throw new IllegalArgumentException("Unknown milestone HUD field");
            String group=laneId(string(hud,"group",""));
            var list=members.get(group);
            if(list==null)throw new IllegalArgumentException("Unknown challenge HUD lane "+group);
            String id=string(j,"id","");
            var milestone=milestones.get(id);
            if(milestone==null)throw new IllegalArgumentException("Unknown HUD milestone "+id);
            var objectives=new ArrayList<Text>();
            for(var e:array(hud,"objectives")){
                var o=e.getAsJsonObject();
                if(!Set.of("text","key").containsAll(o.keySet()))
                    throw new IllegalArgumentException("Unknown challenge objective field");
                objectives.add(new Text(string(o,"text",""),string(o,"key","")));
            }
            if(objectives.size()!=milestone.criteria().size())
                throw new IllegalArgumentException("HUD objectives must match criteria count: "+id);
            list.add(new Objective(id,objectives));
        }
        return titles.entrySet().stream().map(e->new Lane(e.getKey(),e.getValue(),members.get(e.getKey()))).toList();
    }
    /** In definition order, one currently available incomplete objective per lane. */
    public static List<Row> select(Definition.Path path,Track current,List<Lane> lanes){
        if(!current.joined())return List.of();
        var result=new ArrayList<Row>();
        var projected=ProgressionMath.projectCompletionAliases(path,current);
        var byId=new HashMap<String,Definition.Milestone>();
        path.milestones().forEach(m->byId.put(m.id(),m));
        for(var lane:lanes){
            for(var objective:lane.objectives()){
                var m=byId.get(objective.milestone());
                if(m==null||projected.completed(m.id())||!projected.milestones().containsAll(m.after()))continue;
                Row row=null;
                for(int i=0;i<m.criteria().size();i++){
                    int total=m.criteria().get(i).count();
                    int count=Math.min(total,projected.criterionCount(ProgressionMath.counterKey(m.id(),i)));
                    if(count<total){
                        row=new Row(lane.title(),objective.criteria().get(i),count,total);
                        break;
                    }
                }
                if(row!=null){result.add(row);break;}
            }
            if(result.size()==MAX_ROWS)break;
        }
        return List.copyOf(result);
    }
}
