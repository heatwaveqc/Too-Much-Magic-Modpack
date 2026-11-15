package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Monk-only Tyz design complements, never replaces, the one primary MXP wallet. */
final class MonkPrimaryTyzSkeletonTest {
    private static JsonObject json(String path) throws Exception {
        try (var reader = Files.newBufferedReader(Path.of(path))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void everyMonkOnlyRankedSourceIsAccountedForExactlyOnce() throws Exception {
        JsonObject source = json("profiles/skill-placement-no-pufferfish-v2.json");
        JsonObject tyz = json("design/primary-tyz-monk-only-skeleton-v1.json");
        assertEquals("PLAN_ONLY_NOT_ACTIVE", tyz.get("state").getAsString());
        assertEquals("monk_specific_portion_of_PRIMARY_tyz_mxp_tree", tyz.get("kind").getAsString());
        assertEquals(47, tyz.getAsJsonArray("entries").size());

        Map<String, JsonObject> sourceById = new HashMap<>();
        for (var el : source.getAsJsonArray("source_records")) {
            var entry = el.getAsJsonObject();
            sourceById.put(entry.get("source_id").getAsString(), entry);
        }
        Set<String> used = new HashSet<>();
        int licenses = 0, bounded = 0;
        for (var el : tyz.getAsJsonArray("entries")) {
            var entry = el.getAsJsonObject();
            String sourceId = entry.get("source_id").getAsString();
            assertTrue(used.add(sourceId), "Duplicate Tyz source: " + sourceId);
            assertFalse(entry.get("active").getAsBoolean(), "No inactive design should become a paid skill");
            JsonObject original = sourceById.get(sourceId);
            assertNotNull(original, "Tyz endpoint missing from canonical v2 catalog");
            String disposition = original.get("disposition").getAsString();
            if (disposition.equals("Monk_licensed_Tyz_ranks")) {
                licenses++;
                assertEquals("monk_specific_tyz_trait", entry.get("kind").getAsString());
                assertFalse(entry.get("native_license_node").isJsonNull(), "Tyz Monk trait needs one native license");
                assertTrue(entry.get("native_license_earliest_rank_hint").getAsInt() >= 1);
                if (sourceId.equals("tmm_monk:harvest/fortune_hands")) {
                    assertEquals("tmm_monk:harvest_touch",
                            entry.get("native_license_node").getAsString(),
                            "Fortune ranks share one native toggle with Silk Touch");
                    assertEquals(10, entry.get("native_license_earliest_rank_hint").getAsInt());
                }
            } else {
                assertEquals("unique_bounded_Tyz_Monk_affix", disposition);
                assertEquals("bounded_source_affix", entry.get("kind").getAsString());
                assertEquals(original.getAsJsonArray("distinct_rarity_milestones").size(),
                        entry.get("proposed_max_purchases").getAsInt(), "Source rarity tiers, not fabricated 99 ranks");
                assertTrue(entry.get("native_license_node").isJsonNull(), "40 bounded affixes must not generate 40 duplicate native licenses");
                bounded++;
            }
        }
        assertEquals(7, licenses);
        assertEquals(40, bounded);
        for (var entry : sourceById.values()) {
            String disposition = entry.get("disposition").getAsString();
            if (disposition.equals("Monk_licensed_Tyz_ranks")
                    || disposition.equals("unique_bounded_Tyz_Monk_affix")) {
                assertTrue(used.contains(entry.get("source_id").getAsString()));
            }
        }
    }
}
