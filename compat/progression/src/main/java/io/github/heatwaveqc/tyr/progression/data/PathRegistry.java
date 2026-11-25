package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativeCompatibilityDeclarations;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyGateway;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.foundation.Condition;
import net.minecraft.server.level.ServerPlayer;

/** data/<namespace>/tyr_progression/paths/<name>.json -> <namespace>:<name>. */
public final class PathRegistry extends SimpleJsonResourceReloadListener {
    private static volatile Map<String,Definition.Path> active = Map.of();
    private static volatile NativePhysiologyPolicy.Rules nativeCompatibilityRules =
        NativePhysiologyGateway.withApprovedPairData(Map.of());

    /** The last fully validated data reload, shared by join and native-form preflight. */
    public static NativePhysiologyPolicy.Rules nativeCompatibilityRules() {
        return nativeCompatibilityRules;
    }
    private static volatile PathCompatibility.Rules compatibility =
            new PathCompatibility.Rules(Map.of(),Map.of());
    public static boolean canCoexist(String first,String second) {
        return compatibility.canCoexist(active,first,second);
    }
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
    public static ExperienceEconomyConfig.Catalog economyCatalog(){
        return ExperienceEconomyRuntime.current();
    }
    public static Map<String,XpChannel> economies(){return channelConfig.channels();}
    private static volatile Map<String,TrainingXpSpec> training=Map.of();
    public static Map<String,TrainingXpSpec> trainingXp(){return training;}

    private record SourceKey(String system,int index) {}
    private static volatile Map<String,Condition<ServerPlayer>> economyGates=Map.of();
    private static volatile Map<SourceKey,Condition<ServerPlayer>> sourceGates=Map.of();
    public static boolean economyEligible(ServerPlayer player,String id){
        if(ExperienceEconomyRuntime.current().systems().containsKey(id))
            return economy(id)!=null;
        if(XpChannel.MXP.equals(id))return economy(id)!=null;
        var condition=economyGates.get(id);return condition!=null&&condition.test(player);
    }
    public static boolean sourceEligible(ServerPlayer player,String id,int index){
        var condition=sourceGates.get(new SourceKey(id,index));return condition!=null&&condition.test(player);
    }
    private record GateKey(String path,String skill,int index) {}
    private static volatile Map<GateKey,ActivationSpec> activations=Map.of();
    private static volatile Map<GateKey,Condition<ServerPlayer>> purchaseGates=Map.of();
    private static volatile Map<GateKey,PresentationSpec> presentations=Map.of();
    public static boolean purchaseEligible(ServerPlayer p,String path,String skill){
        var gate=purchaseGates.get(new GateKey(path,skill,-1));
        return skillEligible(p,path,skill)&&gate!=null&&gate.test(p);
    }
    public static PresentationSpec presentation(String path,String skill){
        return presentations.getOrDefault(new GateKey(path,skill,-1),PresentationSpec.empty());
    }
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

    public PathRegistry() { super(new Gson(), "tyr_progression/paths"); }
    public static Definition.Path get(String id) { return active.get(id); }
    public static Map<String,Definition.Path> all() { return active; }

    private record Pending(Map<ResourceLocation,JsonElement> data,ResourceManager manager){}
    private static volatile Pending pending;
    private static volatile Map<String,java.util.List<ObservedTrialSpec>> observedTrials=Map.of();
    public static Map<String,java.util.List<ObservedTrialSpec>> observedTrials(){return observedTrials;}
    private static volatile Map<String,java.util.List<HeightFallTrialSpec>> heightFallTrials=Map.of();
    public static Map<String,java.util.List<HeightFallTrialSpec>> heightFallTrials(){return heightFallTrials;}
    private static volatile Map<String,java.util.List<ChallengeHudSpec.Lane>> challengeHuds=Map.of();
    public static Map<String,java.util.List<ChallengeHudSpec.Lane>> challengeHuds(){return challengeHuds;}
    private static volatile Map<String,Map<String,java.util.Set<String>>> generatedTrials=Map.of();

    /** Null means an explicitly authored material criterion, not a generated one. */
    public static java.util.Set<String> generatedTrainingBlocks(String path,String tool){
        return generatedTrials.getOrDefault(path,Map.of()).get(tool);
    }

    @Override
    protected void apply(Map<ResourceLocation,JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        pending=null;
        // Minecraft binds registry tags after ordinary resource listeners.
        // Opt-in paths must use the NEW tags, not the previous world's tags.
        if(data.values().stream().anyMatch(e->e.isJsonObject()&&e.getAsJsonObject().has("harvest_generation"))){
            var copied=new HashMap<ResourceLocation,JsonElement>();
            data.forEach((k,v)->copied.put(k,v.deepCopy()));
            pending=new Pending(Map.copyOf(copied),manager);
            return;
        }
        install(data,manager);
    }

    public static void tagsUpdated(net.neoforged.neoforge.event.TagsUpdatedEvent event){
        if(event.getUpdateCause()!=net.neoforged.neoforge.event.TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD)return;
        var ready=pending;pending=null;
        if(ready!=null)install(ready.data(),ready.manager());
    }
    public static void serverStopped(net.neoforged.neoforge.event.server.ServerStoppedEvent event){pending=null;}

    private static void install(Map<ResourceLocation,JsonElement> data, ResourceManager manager) {
        Map<String,Map<String,java.util.Set<String>>> nextGenerated=new HashMap<>();
        Map<String,java.util.List<ChallengeHudSpec.Lane>> nextHuds=new HashMap<>();
        Map<String,java.util.List<HeightFallTrialSpec>> nextFalls=new HashMap<>();
        Map<String,java.util.List<ObservedTrialSpec>> nextObserved=new HashMap<>();
        Map<String,Definition.Path> next = new HashMap<>();
        Map<String,String> modes = new HashMap<>();
        Map<String,PotionEnrollmentTrigger> triggers = new HashMap<>();
        Map<String,HunterXpPolicy> xpPolicies = new HashMap<>();
        Map<String,HunterMutationPolicy> mutations = new HashMap<>();
        Map<String,JsonObject> rawPaths=new HashMap<>();
        Map<String,java.util.Set<Form>> nativePairs=new HashMap<>();
        Map<String,TrainingXpSpec> newTraining=new HashMap<>();
        Map<String,TimedEnrollment> timed=new HashMap<>();
        Map<String,FoodRite> rites=new HashMap<>();
        Map<GateKey,Condition<ServerPlayer>> nextGates=new HashMap<>();
        Map<GateKey,ActivationSpec> nextActivations=new HashMap<>();
        Map<GateKey,Condition<ServerPlayer>> nextPurchase=new HashMap<>();
        Map<GateKey,PresentationSpec> nextPresentation=new HashMap<>();
        Map<String,JsonObject> experience=new HashMap<>();
        String prefix="tyr_progression/experience_systems/";
        for(var resource:manager.listResources("tyr_progression/experience_systems",id->id.getPath().endsWith(".json")).entrySet()){
            var key=resource.getKey();String id=key.getNamespace()+":"+key.getPath().substring(prefix.length(),key.getPath().length()-5);
            try(var reader=resource.getValue().openAsReader()){
                experience.put(id,com.google.gson.JsonParser.parseReader(reader).getAsJsonObject());
            }catch(java.io.IOException bad){throw new IllegalArgumentException("Cannot read XP system "+id,bad);}
        }
        var economyResource=manager.getResource(ResourceLocation.parse("tyr_progression:tyr_progression/economy.json"))
                .orElseThrow(()->new IllegalArgumentException("Missing bundled progression economy.json"));
        ExperienceEconomyConfig.Catalog economy;
        try(var reader=economyResource.openAsReader()){
            economy=ExperienceEconomyConfig.read(
                com.google.gson.JsonParser.parseReader(reader).getAsJsonObject());
        }catch(java.io.IOException error){
            throw new IllegalArgumentException("Cannot load Progression economy configuration",error);
        }
        // Parse once with this same datapack reload; installation is atomic
        // with the validated Path and XP catalogs below.
        var externalOffers=io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardConfig.parse(manager);
        for(var offer:externalOffers.values())if(!economy.systems().containsKey(offer.currency()))
            throw new IllegalArgumentException("Unknown external reward currency "+offer.currency());
        var registeredSystems=new HashMap<>(ChannelConfig.definitions(experience,1.02));
        // Any XP ledger may be bound by a tree, even without a legacy
        // experience_systems native curve or a hard-coded MXP channel.
        for(var entry:economy.systems().entrySet())
            registeredSystems.putIfAbsent(entry.getKey(),
                XpChannel.generic(entry.getKey(),economy.growth(entry.getKey()),
                    entry.getKey(),"Points"));
        var systems=Map.copyOf(registeredSystems);
        Map<String,Condition<ServerPlayer>> newEconomyGates=new HashMap<>();
        Map<SourceKey,Condition<ServerPlayer>> newSourceGates=new HashMap<>();
        experience.forEach((id,raw)->{
            newEconomyGates.put(id,Eligibility.parse(raw.get("eligibility")));
            var sources=raw.has("sources")?raw.getAsJsonArray("sources"):(raw.has("activities")?raw.getAsJsonArray("activities"):new com.google.gson.JsonArray());
            int i=0;for(var source:sources)newSourceGates.put(new SourceKey(id,i++),Eligibility.parse(source.getAsJsonObject().get("when")));
            var policy=HunterXpPolicy.parse(raw);
            if(policy!=null)xpPolicies.put(id,policy);
        });
        var magicResource=manager.getResource(ResourceLocation.parse("tyr_progression:tyr_progression/magic_stats.json"))
            .orElseThrow(()->new IllegalArgumentException("Missing bundled magic stat catalog"));
        Map<String,MagicCatalog.Binding> magicStats;
        try(var reader=magicResource.openAsReader()){
            magicStats=MagicCatalog.read(com.google.gson.JsonParser.parseReader(reader).getAsJsonObject());
        }catch(java.io.IOException e){throw new IllegalArgumentException("Cannot load magic stat catalog",e);}
        data.forEach((key,element) -> {
            try {
                var generated=HarvestGeneration.expand(key.toString(),element.getAsJsonObject(),
                    io.github.heatwaveqc.tyr.progression.runtime.NativeHarvest::fingerprints);
                var raw=generated.path();
                Definition.Path path = PathJson.read(key.toString(), ChannelConfig.treeInput(MagicCatalog.expand(raw,magicStats),systems));
                nextGenerated.put(path.id(),generated.trials());
                var hud=ChallengeHudSpec.parse(raw,path);
                if(!hud.isEmpty())nextHuds.put(path.id(),hud);
                var falls=HeightFallTrialSpec.parse(raw,path);
                if(!falls.isEmpty())nextFalls.put(path.id(),falls);
                var observed=ObservedTrialSpec.parse(raw,path);
                if(!observed.isEmpty())nextObserved.put(path.id(),observed);
                PotionEnrollmentTrigger trigger = PotionEnrollmentTrigger.parse(element.getAsJsonObject());
                if (trigger != null) triggers.put(path.id(),trigger);
                if(element.getAsJsonObject().has("hunter_xp")||element.getAsJsonObject().has("computed_xp"))
                    throw new IllegalArgumentException("Computed XP belongs in experience_systems");
                HunterMutationPolicy mutationPolicy = HunterMutationPolicy.parse(element.getAsJsonObject());
                if (mutationPolicy != null) mutations.put(path.id(),mutationPolicy);
                nextPresentation.put(new GateKey(path.id(),"",-1),PresentationSpec.parse(raw));
                var rite=FoodRiteSpec.parse(raw);
                if(rite!=null)rites.put(path.id(),new FoodRite(rite,Eligibility.parse(rite.when())));
                var timer=TimedEnrollment.parse(raw);
                if(timer!=null)timed.put(path.id(),timer);
                Map<GateKey,Condition<ServerPlayer>> localGates=new HashMap<>();
                localGates.put(new GateKey(path.id(),"",-1),Eligibility.parse(raw.get("eligibility")));
                if(raw.has("skills")) for(var skillElement:raw.getAsJsonArray("skills")) {
                    var skill=skillElement.getAsJsonObject();
                    String skillId=skill.get("id").getAsString();
                    nextPresentation.put(new GateKey(path.id(),skillId,-1),PresentationSpec.parse(skill));
                    nextPurchase.put(new GateKey(path.id(),skillId,-1),Eligibility.parse(skill.get("purchase_when")));
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
                var permittedForms = NativeCompatibilityDeclarations.read(raw);
                if (!permittedForms.isEmpty()) nativePairs.put(path.id(), permittedForms);
                var physical=TrainingXpSpec.parse(raw,systems);
                if(physical!=null)newTraining.put(path.id(),physical);
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
        ExperienceEconomyConfig.validateReferences(economy,next,experience.keySet());
        if(!economy.systems().containsKey(XpChannel.MXP))
            throw new IllegalArgumentException("Current TMM live legacy adapters require configured MXP");
        var resourceCatalog=io.github.heatwaveqc.tyr.progression.runtime.resources.ResourceCatalog.parse(rawPaths,next);
        // Validate optional Charm/Taming costs against the same pending resource catalog.
        for(var path:next.values())for(var skill:path.skills())for(var effect:skill.effects()){
            if(!effect.type().equals("charm")&&!effect.type().equals("tame"))continue;
            String resource=effect.params().getOrDefault("resource","");
            if(!resource.isEmpty()&&!resourceCatalog.containsKey(resource))
                throw new IllegalArgumentException("Unknown creature interaction resource "+resource);
        }
        var nextCompatibility=PathCompatibility.parse(rawPaths,next);
        for(var policy:xpPolicies.values())if(!policy.specialtyPath().isEmpty()&&!next.containsKey(policy.specialtyPath()))
            throw new IllegalArgumentException("Unknown specialty path "+policy.specialtyPath());
        channelConfig=config;
        training=Map.copyOf(newTraining);
        compatibility=nextCompatibility;
        ExperienceEconomyRuntime.install(economy);
        io.github.heatwaveqc.tyr.progression.runtime.external.ExternalRewardConfig.install(externalOffers);
        economyGates=Map.copyOf(newEconomyGates);
        sourceGates=Map.copyOf(newSourceGates);
        timedEnrollments=Map.copyOf(timed);
        foodRites=Map.copyOf(rites);
        gates=Map.copyOf(nextGates);
        activations=Map.copyOf(nextActivations);
        purchaseGates=Map.copyOf(nextPurchase);
        presentations=Map.copyOf(nextPresentation);
        potionTriggers = Map.copyOf(triggers);
        hunterXpPolicies = Map.copyOf(xpPolicies);
        mutationPolicies = Map.copyOf(mutations);
        enrollmentModes = Map.copyOf(modes);
        nativeCompatibilityRules = NativePhysiologyGateway.withApprovedPairData(nativePairs);
        generatedTrials=Map.copyOf(nextGenerated);
        challengeHuds=Map.copyOf(nextHuds);
        heightFallTrials=Map.copyOf(nextFalls);
        observedTrials=Map.copyOf(nextObserved);
        active = Map.copyOf(next);
        io.github.heatwaveqc.tyr.progression.runtime.resources.ResourceCatalog.install(resourceCatalog);
        io.github.heatwaveqc.tyr.progression.runtime.GildedMechanics.clearFoodCache();
        ProgressionMod.LOGGER.info("Loaded {} progression path definitions",active.size());
    }
}
