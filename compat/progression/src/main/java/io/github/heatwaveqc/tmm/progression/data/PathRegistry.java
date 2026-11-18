package io.github.heatwaveqc.tmm.progression.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.foundation.Condition;
import net.minecraft.server.level.ServerPlayer;

/** data/<namespace>/tmm_progression/paths/<name>.json -> <namespace>:<name>. */
public final class PathRegistry extends SimpleJsonResourceReloadListener {
    private static volatile Map<String,Definition.Path> active = Map.of();
    private static volatile Map<String,String> enrollmentModes = Map.of();
    private static volatile Map<String,PotionEnrollmentTrigger> potionTriggers = Map.of();
    private static volatile Map<String,HunterXpPolicy> hunterXpPolicies = Map.of();
    public static Map<String,HunterXpPolicy> computedXp(){return hunterXpPolicies;}
    public static HunterXpPolicy hunterXp(String path) { var c=channel(path);return c==null?null:hunterXpPolicies.get(c.id()); }
    private static volatile Map<String,HunterMutationPolicy> mutationPolicies = Map.of();
    public static HunterMutationPolicy hunterMutationPolicy(String path) { return mutationPolicies.get(path); }
    public static Map<String,HunterMutationPolicy> mutationPolicies(){return mutationPolicies;}
    public static Map<String,PotionEnrollmentTrigger> potionTriggers() { return potionTriggers; }
    public static String enrollment(String path) { return enrollmentModes.getOrDefault(path,"admin"); }

    private static volatile ChannelConfig channelConfig=ChannelConfig.empty();
    public static XpChannel channel(String path) { return channelConfig.paths().get(path); }
    public static XpChannel economy(String id) { return channelConfig.channels().get(id); }
    public static ChannelConfig config(){return channelConfig;}
    public static Map<String,XpChannel> economies(){return channelConfig.channels();}

    private record SourceKey(String system,int index) {}
    private static volatile Map<String,Condition<ServerPlayer>> economyGates=Map.of();
    private static volatile Map<SourceKey,Condition<ServerPlayer>> sourceGates=Map.of();
    public static boolean economyEligible(ServerPlayer player,String id){
        if(XpChannel.MXP.equals(id))return economy(id)!=null;
        var condition=economyGates.get(id);return condition!=null&&condition.test(player);
    }
    public static boolean sourceEligible(ServerPlayer player,String id,int index){
        var condition=sourceGates.get(new SourceKey(id,index));return condition!=null&&condition.test(player);
    }
    private record GateKey(String path,String skill,int index) {}
    private static volatile Map<GateKey,ActivationSpec> activations=Map.of();
    public static ActivationSpec activation(String path,String skill){return activations.get(new GateKey(path,skill,-1));}
    private static volatile Map<GateKey,Condition<ServerPlayer>> gates=Map.of();
    private static boolean test(ServerPlayer player,GateKey key) {
        var condition=gates.get(key);
        return condition!=null && condition.test(player);
    }
    public static boolean eligible(ServerPlayer player,String path) {
        return test(player,new GateKey(path,"",-1));
    }
    public static boolean skillEligible(ServerPlayer player,String path,String skill) {
        return eligible(player,path) && test(player,new GateKey(path,skill,-1));
    }
    public static boolean effectEligible(ServerPlayer player,String path,String skill,int index) {
        return skillEligible(player,path,skill) && test(player,new GateKey(path,skill,index));
    }

    public record FoodRite(FoodRiteSpec spec,Condition<ServerPlayer> conditions){}
    private static volatile Map<String,FoodRite> foodRites=Map.of();
    public static Map<String,FoodRite> foodRites(){return foodRites;}
    private static volatile Map<String,TimedEnrollment> timedEnrollments=Map.of();
    public static Map<String,TimedEnrollment> timedEnrollments(){return timedEnrollments;}

    public PathRegistry() { super(new Gson(), "tmm_progression/paths"); }
    public static Definition.Path get(String id) { return active.get(id); }
    public static Map<String,Definition.Path> all() { return active; }

    @Override
    protected void apply(Map<ResourceLocation,JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        Map<String,Definition.Path> next = new HashMap<>();
        Map<String,String> modes = new HashMap<>();
        Map<String,PotionEnrollmentTrigger> triggers = new HashMap<>();
        Map<String,HunterXpPolicy> xpPolicies = new HashMap<>();
        Map<String,HunterMutationPolicy> mutations = new HashMap<>();
        Map<String,JsonObject> rawPaths=new HashMap<>();
        Map<String,TimedEnrollment> timed=new HashMap<>();
        Map<String,FoodRite> rites=new HashMap<>();
        Map<GateKey,Condition<ServerPlayer>> nextGates=new HashMap<>();
        Map<GateKey,ActivationSpec> nextActivations=new HashMap<>();
        Map<String,JsonObject> experience=new HashMap<>();
        String prefix="tmm_progression/experience_systems/";
        for(var resource:manager.listResources("tmm_progression/experience_systems",id->id.getPath().endsWith(".json")).entrySet()){
            var key=resource.getKey();String id=key.getNamespace()+":"+key.getPath().substring(prefix.length(),key.getPath().length()-5);
            try(var reader=resource.getValue().openAsReader()){
                experience.put(id,com.google.gson.JsonParser.parseReader(reader).getAsJsonObject());
            }catch(java.io.IOException bad){throw new IllegalArgumentException("Cannot read XP system "+id,bad);}
        }
        var systems=ChannelConfig.definitions(experience,1.02);
        Map<String,Condition<ServerPlayer>> newEconomyGates=new HashMap<>();
        Map<SourceKey,Condition<ServerPlayer>> newSourceGates=new HashMap<>();
        experience.forEach((id,raw)->{
            newEconomyGates.put(id,Eligibility.parse(raw.get("eligibility")));
            var sources=raw.has("sources")?raw.getAsJsonArray("sources"):(raw.has("activities")?raw.getAsJsonArray("activities"):new com.google.gson.JsonArray());
            int i=0;for(var source:sources)newSourceGates.put(new SourceKey(id,i++),Eligibility.parse(source.getAsJsonObject().get("when")));
            var policy=HunterXpPolicy.parse(raw);
            if(policy!=null)xpPolicies.put(id,policy);
        });
        var magicResource=manager.getResource(ResourceLocation.parse("tmm_progression:tmm_progression/magic_stats.json"))
            .orElseThrow(()->new IllegalArgumentException("Missing bundled magic stat catalog"));
        Map<String,MagicCatalog.Binding> magicStats;
        try(var reader=magicResource.openAsReader()){
            magicStats=MagicCatalog.read(com.google.gson.JsonParser.parseReader(reader).getAsJsonObject());
        }catch(java.io.IOException e){throw new IllegalArgumentException("Cannot load magic stat catalog",e);}
        data.forEach((key,element) -> {
            try {
                Definition.Path path = PathJson.read(key.toString(), ChannelConfig.treeInput(MagicCatalog.expand(element.getAsJsonObject(),magicStats),systems));
                PotionEnrollmentTrigger trigger = PotionEnrollmentTrigger.parse(element.getAsJsonObject());
                if (trigger != null) triggers.put(path.id(),trigger);
                if(element.getAsJsonObject().has("hunter_xp")||element.getAsJsonObject().has("computed_xp"))
                    throw new IllegalArgumentException("Computed XP belongs in experience_systems");
                HunterMutationPolicy mutationPolicy = HunterMutationPolicy.parse(element.getAsJsonObject());
                if (mutationPolicy != null) mutations.put(path.id(),mutationPolicy);
                var raw=element.getAsJsonObject();
                var rite=FoodRiteSpec.parse(raw);
                if(rite!=null)rites.put(path.id(),new FoodRite(rite,Eligibility.parse(rite.when())));
                var timer=TimedEnrollment.parse(raw);
                if(timer!=null)timed.put(path.id(),timer);
                Map<GateKey,Condition<ServerPlayer>> localGates=new HashMap<>();
                localGates.put(new GateKey(path.id(),"",-1),Eligibility.parse(raw.get("eligibility")));
                if(raw.has("skills")) for(var skillElement:raw.getAsJsonArray("skills")) {
                    var skill=skillElement.getAsJsonObject();
                    String skillId=skill.get("id").getAsString();
                    var activation=ActivationSpec.read(skill);
                    if(activation!=null)nextActivations.put(new GateKey(path.id(),skillId,-1),activation);
                    localGates.put(new GateKey(path.id(),skillId,-1),Eligibility.parse(skill.get("eligibility")));
                    if(skill.has("effects")) {
                        int index=0;
                        for(var fx:skill.getAsJsonArray("effects"))
                            localGates.put(new GateKey(path.id(),skillId,index++),
                                    Eligibility.parse(fx.getAsJsonObject().get("when")));
                    }
                }
                nextGates.putAll(localGates);
                rawPaths.put(path.id(),raw);
                next.put(path.id(),path);
                modes.put(path.id(),element.getAsJsonObject().has("enrollment") ?
                    element.getAsJsonObject().get("enrollment").getAsString() : "admin");
            } catch (RuntimeException exception) {
                ProgressionMod.LOGGER.error("Rejected path {}: {}",key,exception.getMessage());
            }
        });
        ChannelConfig config=ChannelConfig.bind(systems,next,rawPaths);
        if(next.size()!=data.size()) throw new IllegalArgumentException("Rejected path; retaining previous configuration");
        for(var policy:xpPolicies.values())if(!policy.specialtyPath().isEmpty()&&!next.containsKey(policy.specialtyPath()))
            throw new IllegalArgumentException("Unknown specialty path "+policy.specialtyPath());
        channelConfig=config;
        economyGates=Map.copyOf(newEconomyGates);
        sourceGates=Map.copyOf(newSourceGates);
        timedEnrollments=Map.copyOf(timed);
        foodRites=Map.copyOf(rites);
        gates=Map.copyOf(nextGates);
        activations=Map.copyOf(nextActivations);
        potionTriggers = Map.copyOf(triggers);
        hunterXpPolicies = Map.copyOf(xpPolicies);
        mutationPolicies = Map.copyOf(mutations);
        enrollmentModes = Map.copyOf(modes);
        active = Map.copyOf(next);
        ProgressionMod.LOGGER.info("Loaded {} progression path definitions",active.size());
    }
}
