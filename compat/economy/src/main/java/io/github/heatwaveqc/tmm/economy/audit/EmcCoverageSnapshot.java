package io.github.heatwaveqc.tmm.economy.audit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.SortedSet;

/**
 * Pure deterministic encoder for the existing #47 Python registry/EMC auditor.
 *
 * The values must come from ProjectE's *post-mapping* map, not recipe
 * estimates, custom_emc.json, per-stack charge, or inventory contents.
 * Explicitly distinguish a mapped zero from an absent entry. This class
 * never assigns EMC or changes player/item state.
 */
public final class EmcCoverageSnapshot {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public record Payload(String itemsJson, String emcJson, String statusJson,
                          int total, int positive, int unmapped, int mappedZero) {}

    private EmcCoverageSnapshot() {}

    public static Payload encode(SortedSet<String> registered, Map<String, Long> mapped) {
        if (registered == null || registered.isEmpty() || mapped == null) {
            throw new IllegalArgumentException("Registry and mapped EMC data are required");
        }

        JsonArray itemIds = new JsonArray();
        JsonArray missingIds = new JsonArray();
        JsonArray zeroIds = new JsonArray();
        JsonObject entries = new JsonObject();
        int positive = 0;
        for (String id : registered) {
            if (id == null || id.isBlank() || id.indexOf(':') < 1) {
                throw new IllegalArgumentException("Invalid registry ID: " + id);
            }
            itemIds.add(id);
            Long value = mapped.get(id);
            if (value == null) {
                missingIds.add(id);
                continue;
            }
            if (value < 0) {
                throw new IllegalArgumentException("Negative EMC is invalid for " + id);
            }
            JsonObject entry = new JsonObject();
            entry.addProperty("emc", value);
            entries.add(id, entry);
            if (value == 0) {
                zeroIds.add(id);
            } else {
                positive++;
            }
        }
        for (String id : mapped.keySet()) {
            if (!registered.contains(id)) {
                throw new IllegalArgumentException("Mapped item not in frozen registry: " + id);
            }
        }

        JsonObject itemsRoot = new JsonObject();
        itemsRoot.add("items", itemIds);
        JsonObject emcRoot = new JsonObject();
        emcRoot.add("entries", entries);
        JsonObject statusRoot = new JsonObject();
        statusRoot.addProperty("schema_version", 1);
        statusRoot.addProperty("registry_items", registered.size());
        statusRoot.addProperty("mapped_positive", positive);
        statusRoot.addProperty("mapped_zero", zeroIds.size());
        statusRoot.addProperty("unmapped", missingIds.size());
        statusRoot.add("unmapped_ids", missingIds);
        statusRoot.add("mapped_zero_ids", zeroIds);
        return new Payload(GSON.toJson(itemsRoot) + "\n", GSON.toJson(emcRoot) + "\n",
                GSON.toJson(statusRoot) + "\n",
                registered.size(), positive, missingIds.size(), zeroIds.size());
    }
}
