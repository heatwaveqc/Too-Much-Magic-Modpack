package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Generated recipe/loot provenance, NOT a dietary item-to-nutrition matrix.
 *
 * A provenance producer records how many whole raw input units a product
 * represents, plus virtual item tags and optional per-physiology ingredient
 * fractions. The food policy independently defines every nutrition rule.
 * Evidence is read from datapacks and can be expanded without new Java.
 */
public record FoodSourceEvidence(String item, String provenance,
    double sourceUnits, Set<String> inferredTags, Map<String, Double> preferredShares) {

    public FoodSourceEvidence {
        inferredTags = Set.copyOf(inferredTags);
        preferredShares = Map.copyOf(preferredShares);
    }

    public static FoodSourceEvidence parse(JsonObject raw, Set<String> knownProfiles) {
        if (raw == null || !raw.has("schema") || !raw.has("item") ||
                !raw.has("provenance") || !raw.has("source_units"))
            throw new IllegalArgumentException("Missing source evidence fields");
        for (String key : raw.keySet())
            if (!Set.of("schema", "item", "provenance", "source_units",
                        "inferred_tags", "preferred_shares").contains(key))
                throw new IllegalArgumentException("Unknown source evidence field " + key);
        if (raw.get("schema").getAsInt() != 1)
            throw new IllegalArgumentException("Unsupported food source evidence schema");
        String item = raw.get("item").getAsString();
        String provenance = raw.get("provenance").getAsString();
        if (!item.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                || !provenance.matches("[a-z0-9_.-]+:[a-z0-9_./-]+"))
            throw new IllegalArgumentException("Source evidence needs item and provenance IDs");
        double units = raw.get("source_units").getAsDouble();
        if (!Double.isFinite(units) || units <= 0 || units > 64)
            throw new IllegalArgumentException("Source units must be finite and >0..64");

        Set<String> inferred = new HashSet<>();
        if (raw.has("inferred_tags")) for (var v : raw.getAsJsonArray("inferred_tags")) {
            String tag = v.getAsString();
            if (!tag.matches("#[a-z0-9_.-]+:[a-z0-9_./-]+") || !inferred.add(tag))
                throw new IllegalArgumentException("Invalid/duplicate inferred tag " + tag);
        }

        Map<String, Double> shares = new HashMap<>();
        if (raw.has("preferred_shares"))
            for (var entry : raw.getAsJsonObject("preferred_shares").entrySet()) {
                if (!knownProfiles.contains(entry.getKey()))
                    throw new IllegalArgumentException("Unknown food profile " + entry.getKey());
                double share = entry.getValue().getAsDouble();
                if (!Double.isFinite(share) || share < 0 || share > 1)
                    throw new IllegalArgumentException("Invalid preferred share " + entry.getKey());
                shares.put(entry.getKey(), share);
            }
        return new FoodSourceEvidence(item, provenance,
                units, inferred, shares);
    }
}
