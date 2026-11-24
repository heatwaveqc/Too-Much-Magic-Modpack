package io.github.heatwaveqc.tyr.progression.data;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.util.*;
/** XP definitions are independent resources. Trees bind a default and optional per-skill currencies. */
public record ChannelConfig(Map<String,XpChannel> channels,Map<String,XpChannel> paths,
                            Map<SkillKey,Binding> skills,Map<SkillKey,List<String>> achievements,
                             Map<String,MasteryStageProjection> masteryStages) {
    public ChannelConfig(Map<String,XpChannel> channels,Map<String,XpChannel> paths,Map<SkillKey,Binding> skills){
        this(channels,paths,skills,Map.of(),Map.of());
    }
    public ChannelConfig(Map<String,XpChannel> channels,Map<String,XpChannel> paths,
                         Map<SkillKey,Binding> skills,Map<SkillKey,List<String>> achievements){
        this(channels,paths,skills,achievements,Map.of());
    }
    public record SkillKey(String path,String skill) {}
    public record Binding(XpChannel currency,XpChannel levelSystem,boolean permanent,XpChannel awardCurrency,int awardPoints) {
        public Binding(XpChannel currency,XpChannel levelSystem){this(currency,levelSystem,false,null,0);}
    }
    public static ChannelConfig empty(){return new ChannelConfig(Map.of(),Map.of(),Map.of());}
    public ChannelConfig {
        channels=Map.copyOf(channels);paths=Map.copyOf(paths);skills=Map.copyOf(skills);
        var copy=new HashMap<SkillKey,List<String>>();
        achievements.forEach((key,value)->copy.put(key,List.copyOf(value)));
        achievements=Map.copyOf(copy);
        masteryStages=Map.copyOf(masteryStages);
    }
    public static Map<String,XpChannel> definitions(Map<String,JsonObject> raw,double growth){
        var result=new HashMap<String,XpChannel>();result.put(XpChannel.MXP,XpChannel.mxp(growth));
        for(var entry:raw.entrySet()){
            String id=entry.getKey();var value=entry.getValue();
            if(result.containsKey(id))throw new IllegalArgumentException("Reserved XP ID "+id);
            var spec=value.deepCopy();spec.remove("skills");spec.remove("milestones");
            if(spec.has("sources"))spec.add("activities",spec.remove("sources"));
            var curve=PathJson.read(id,spec);
            result.put(id,new XpChannel(id,curve,1.0,value.has("requires_path")?value.get("requires_path").getAsString():"",
                value.has("name")?value.get("name").getAsString():id,value.has("point_name")?value.get("point_name").getAsString():"Points"));
        }
        return Map.copyOf(result);
    }
    public static XpChannel resolve(Map<String,XpChannel> systems,String id){
        var c=systems.get(id);if(c==null)throw new IllegalArgumentException("Unknown XP system "+id);return c;
    }
    public static JsonObject treeInput(JsonObject raw,Map<String,XpChannel> systems){
        var j=raw.deepCopy();var def=resolve(systems,j.has("xp_channel")?j.get("xp_channel").getAsString():XpChannel.MXP);
        if(j.has("rank_xp")||j.has("activities")||j.has("points_per_rank")||j.has("max_level"))
            throw new IllegalArgumentException("Curve/activity fields belong in experience_systems");
        int max=def.magic()?200:def.maxRank();
        if(j.has("skills"))for(var el:j.getAsJsonArray("skills")){
            var skill=el.getAsJsonObject();var currency=resolve(systems,skill.has("xp_channel")?skill.get("xp_channel").getAsString():def.id());
            var level=resolve(systems,skill.has("level_system")?skill.get("level_system").getAsString():currency.id());
            max=Math.max(max,level.magic()?200:level.maxRank());
        }
        var rank=new JsonArray();for(int i=0;i<max;i++)rank.add(i);
        j.add("rank_xp",rank);j.addProperty("points_per_rank",0);return j;
    }
    public static ChannelConfig bind(Map<String,XpChannel> systems,Map<String,Definition.Path> trees,Map<String,JsonObject> raw){
        var paths=new HashMap<String,XpChannel>();
        var skills=new HashMap<SkillKey,Binding>();
        var achievements=new HashMap<SkillKey,List<String>>();
        var stages=new HashMap<String,MasteryStageProjection>();
        for(var sys:systems.values())if(!sys.earningPath().isEmpty()&&!trees.containsKey(sys.earningPath()))
            throw new IllegalArgumentException("Unknown required earning path "+sys.earningPath());
        for(var tree:trees.values()){
            var j=raw.get(tree.id());var def=resolve(systems,j.has("xp_channel")?j.get("xp_channel").getAsString():XpChannel.MXP);
            paths.put(tree.id(),def);
            var projected=MasteryStageProjection.parse(j,systems);
            if(projected!=null)stages.put(tree.id(),projected);
            if(def.magic()&&tree.milestones().stream().anyMatch(m->m.xp()!=0||m.points()!=0))
                throw new IllegalArgumentException("Direct milestone XP requires a native wallet");
            if(j.has("skills"))for(var el:j.getAsJsonArray("skills")){
                var q=el.getAsJsonObject();String id=q.get("id").getAsString();
                var currency=resolve(systems,q.has("xp_channel")?q.get("xp_channel").getAsString():def.id());
                var level=resolve(systems,q.has("level_system")?q.get("level_system").getAsString():currency.id());
                var skill=tree.skill(id);
                if(skill.requiredRank()>level.maxRank()||skill.autoRanks().stream().anyMatch(n->n>level.maxRank())||skill.rankRequirements().stream().anyMatch(n->n>level.maxRank()))
                    throw new IllegalArgumentException("Level gate exceeds curve "+id);
                if(q.has("auto_achievements")){
                    if(!skill.autoRanks().isEmpty())
                        throw new IllegalArgumentException("Automatic ranks may use milestones OR legacy level gates, not both");
                    var unlocks=new ArrayList<String>();
                    var seen=new HashSet<String>();
                    for(var entry:q.getAsJsonArray("auto_achievements")){
                        String achievement=entry.getAsString();
                        if(!achievement.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")||!seen.add(achievement))
                            throw new IllegalArgumentException("Invalid or duplicate skill achievement "+achievement);
                        unlocks.add(achievement);
                    }
                    if(unlocks.isEmpty()||unlocks.size()>skill.maxRank())
                        throw new IllegalArgumentException("Invalid automatic achievement rank count "+id);
                    achievements.put(new SkillKey(tree.id(),id),List.copyOf(unlocks));
                }
                boolean permanent=q.has("refundable")&&!q.get("refundable").getAsBoolean();
                if(permanent&&(!skill.autoRanks().isEmpty()||!skill.requiresSkills().isEmpty()||!skill.requiresAnySkills().isEmpty()))
                    throw new IllegalArgumentException("Permanent purchase must not depend on refundable skill ranks "+id);
                XpChannel award=null;int points=0;
                if(q.has("rank_point_award")){
                    var receipt=q.getAsJsonObject("rank_point_award");
                    if(!receipt.keySet().equals(Set.of("system","points")))throw new IllegalArgumentException("Invalid rank point award fields");
                    award=resolve(systems,receipt.get("system").getAsString());points=receipt.get("points").getAsBigDecimal().intValueExact();
                    if(!permanent||!currency.magic()||award.magic()||points<1||points>100)
                        throw new IllegalArgumentException("Rank award requires irreversible positive-cost MXP purchase into native wallet");
                    for(int rank=0;rank<skill.maxRank();rank++)if(skill.price(rank)<=0)
                        throw new IllegalArgumentException("Rank point award cannot be free");
                }
                skills.put(new SkillKey(tree.id(),id),new Binding(currency,level,permanent,award,points));
            }
        }
        return new ChannelConfig(systems,paths,skills,achievements,stages);
    }
}
