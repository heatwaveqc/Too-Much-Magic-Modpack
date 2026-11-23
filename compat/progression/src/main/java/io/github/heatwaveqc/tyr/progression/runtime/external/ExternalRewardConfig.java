package io.github.heatwaveqc.tyr.progression.runtime.external;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

/** Reloadable, strict declarative external-reward offers; no client price input. */
public final class ExternalRewardConfig {
    private static volatile Map<String, ExternalRewardPolicy.Offer> active = Map.of();
    private static final String DIR = "tyr_progression/external_rewards/";
    private ExternalRewardConfig() {}

    public static Map<String, ExternalRewardPolicy.Offer> current() { return active; }
    public static ExternalRewardPolicy.Offer get(String id) { return active.get(id); }

    public static void install(Map<String, ExternalRewardPolicy.Offer> verified) {
        active = Map.copyOf(verified);
    }

    public static Map<String, ExternalRewardPolicy.Offer> parse(ResourceManager manager) {
        Map<String, ExternalRewardPolicy.Offer> result = new HashMap<>();
        var resources = manager.listResources("tyr_progression/external_rewards",
                path -> path.getPath().endsWith(".json"));
        for (var entry : resources.entrySet()) {
            ResourceLocation key = entry.getKey();
            String path = key.getPath();
            if (!path.startsWith(DIR)) throw new IllegalArgumentException("Invalid external-reward resource path " + key);
            String id = key.getNamespace() + ":" + path.substring(DIR.length(), path.length() - 5);
            try (var reader = entry.getValue().openAsReader()) {
                JsonElement parsed = JsonParser.parseReader(reader);
                if (!parsed.isJsonObject()) throw new IllegalArgumentException("Expected external-reward object " + id);
                JsonObject row = parsed.getAsJsonObject();
                for (String field : row.keySet()) if (!Set.of("kind","currency","base_points",
                        "step_points","max_purchases").contains(field))
                    throw new IllegalArgumentException("Unknown external-reward field " + field);
                var offer = new ExternalRewardPolicy.Offer(
                        id, requiredString(row,"kind"), requiredString(row,"currency"),
                        requiredInt(row,"base_points"), requiredInt(row,"step_points"),
                        requiredInt(row,"max_purchases"));
                if (result.putIfAbsent(id, offer) != null)
                    throw new IllegalArgumentException("Duplicate external reward " + id);
            } catch (IOException io) {
                throw new IllegalArgumentException("Cannot read external reward " + id, io);
            }
        }
        return Map.copyOf(result);
    }

    private static String requiredString(JsonObject row, String field) {
        if (!row.has(field) || !row.get(field).isJsonPrimitive())
            throw new IllegalArgumentException("Missing external reward field " + field);
        return row.get(field).getAsString();
    }
    private static int requiredInt(JsonObject row, String field) {
        if (!row.has(field) || !row.get(field).isJsonPrimitive())
            throw new IllegalArgumentException("Missing external reward field " + field);
        // getAsInt truncates JSON fractional numbers: reject instead.
        return row.get(field).getAsBigDecimal().intValueExact();
    }
}
