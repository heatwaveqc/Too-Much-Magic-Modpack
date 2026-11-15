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

/** data/<namespace>/tmm_progression/paths/<name>.json -> <namespace>:<name>. */
public final class PathRegistry extends SimpleJsonResourceReloadListener {
    private static volatile Map<String,Definition.Path> active = Map.of();
    private static volatile Map<String,String> enrollmentModes = Map.of();
    private static volatile Map<String,PotionEnrollmentTrigger> potionTriggers = Map.of();
    private static volatile Map<String,HunterXpPolicy> hunterXpPolicies = Map.of();
    public static HunterXpPolicy hunterXp(String path) { return hunterXpPolicies.get(path); }
    public static Map<String,PotionEnrollmentTrigger> potionTriggers() { return potionTriggers; }
    public static String enrollment(String path) { return enrollmentModes.getOrDefault(path,"admin"); }

    public PathRegistry() { super(new Gson(), "tmm_progression/paths"); }
    public static Definition.Path get(String id) { return active.get(id); }
    public static Map<String,Definition.Path> all() { return active; }

    @Override
    protected void apply(Map<ResourceLocation,JsonElement> data, ResourceManager manager, ProfilerFiller profiler) {
        Map<String,Definition.Path> next = new HashMap<>();
        Map<String,String> modes = new HashMap<>();
        Map<String,PotionEnrollmentTrigger> triggers = new HashMap<>();
        Map<String,HunterXpPolicy> xpPolicies = new HashMap<>();
        data.forEach((key,element) -> {
            try {
                Definition.Path path = PathJson.read(key.toString(), element.getAsJsonObject());
                PotionEnrollmentTrigger trigger = PotionEnrollmentTrigger.parse(element.getAsJsonObject());
                if (trigger != null) triggers.put(path.id(),trigger);
                HunterXpPolicy xpPolicy = HunterXpPolicy.parse(element.getAsJsonObject());
                if (xpPolicy != null) xpPolicies.put(path.id(),xpPolicy);
                next.put(path.id(),path);
                modes.put(path.id(),element.getAsJsonObject().has("enrollment") ?
                    element.getAsJsonObject().get("enrollment").getAsString() : "admin");
            } catch (RuntimeException exception) {
                ProgressionMod.LOGGER.error("Rejected path {}: {}",key,exception.getMessage());
            }
        });
        potionTriggers = Map.copyOf(triggers);
        hunterXpPolicies = Map.copyOf(xpPolicies);
        enrollmentModes = Map.copyOf(modes);
        active = Map.copyOf(next);
        ProgressionMod.LOGGER.info("Loaded {} progression path definitions",active.size());
    }
}
