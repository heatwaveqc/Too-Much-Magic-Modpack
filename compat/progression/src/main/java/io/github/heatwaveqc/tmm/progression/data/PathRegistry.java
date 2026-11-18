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
    public static HunterXpPolicy hunterXp(String path) { return hunterXpPolicies.get(path); }
    private static volatile Map<String,HunterMutationPolicy> mutationPolicies = Map.of();
    public static HunterMutationPolicy hunterMutationPolicy(String path) { return mutationPolicies.get(path); }
    public static Map<String,PotionEnrollmentTrigger> potionTriggers() { return potionTriggers; }
    public static String enrollment(String path) { return enrollmentModes.getOrDefault(path,"admin"); }

    private static volatile ChannelConfig channelConfig=ChannelConfig.empty();
    public static XpChannel channel(String path) { return channelConfig.paths().get(path); }
    public static XpChannel economy(String id) { return channelConfig.channels().get(id); }

    private record GateKey(String path,String skill,int index) {}
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
        Map<GateKey,Condition<ServerPlayer>> nextGates=new HashMap<>();
        data.forEach((key,element) -> {
            if(key.toString().equals(ChannelConfig.TABLE)) return;
            try {
                Definition.Path path = PathJson.read(key.toString(), element.getAsJsonObject());
                PotionEnrollmentTrigger trigger = PotionEnrollmentTrigger.parse(element.getAsJsonObject());
                if (trigger != null) triggers.put(path.id(),trigger);
                HunterXpPolicy xpPolicy = HunterXpPolicy.parse(element.getAsJsonObject());
                if (xpPolicy != null) xpPolicies.put(path.id(),xpPolicy);
                HunterMutationPolicy mutationPolicy = HunterMutationPolicy.parse(element.getAsJsonObject());
                if (mutationPolicy != null) mutations.put(path.id(),mutationPolicy);
                var raw=element.getAsJsonObject();
                Map<GateKey,Condition<ServerPlayer>> localGates=new HashMap<>();
                localGates.put(new GateKey(path.id(),"",-1),Eligibility.parse(raw.get("eligibility")));
                if(raw.has("skills")) for(var skillElement:raw.getAsJsonArray("skills")) {
                    var skill=skillElement.getAsJsonObject();
                    String skillId=skill.get("id").getAsString();
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
        var table=data.get(ResourceLocation.parse(ChannelConfig.TABLE));
        ChannelConfig config=ChannelConfig.read(table==null?null:table.getAsJsonObject(),next,rawPaths);
        if(next.size()!=data.size()-1) throw new IllegalArgumentException("Rejected path; retaining previous channel configuration");
        channelConfig=config;
        gates=Map.copyOf(nextGates);
        potionTriggers = Map.copyOf(triggers);
        hunterXpPolicies = Map.copyOf(xpPolicies);
        mutationPolicies = Map.copyOf(mutations);
        enrollmentModes = Map.copyOf(modes);
        active = Map.copyOf(next);
        ProgressionMod.LOGGER.info("Loaded {} progression path definitions",active.size());
    }
}
