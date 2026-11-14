package io.github.heatwaveqc.tmm.monk.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Checks design inventory integrity; does NOT verify that gameplay adapters exist. */
public class MonkProfileCoverageTest {
    private static JsonObject load(String name) throws IOException {
        try (var reader = Files.newBufferedReader(Path.of("profiles", name))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void profilesAreExplicitlySeparate() throws IOException {
        JsonObject classic = load("original-monk-1.4.json");
        JsonObject pack = load("too-much-magic.json");
        assertNotEquals(classic.get("profile_id").getAsString(), pack.get("profile_id").getAsString());
        assertTrue(classic.get("selected_by_default_in_future_standalone_build").getAsBoolean());
        assertFalse(classic.get("implemented_in_game").getAsBoolean());
        assertEquals(21, classic.getAsJsonArray("milestone_deeds").size());
        assertEquals("too-much-magic", pack.get("profile_id").getAsString());
        assertEquals("watch_sunset", pack.getAsJsonObject("progression").get("initiation").getAsString());
        assertFalse(pack.getAsJsonObject("equipment").get("armor_slots").getAsBoolean());
        assertTrue(pack.getAsJsonObject("equipment").get("curios").getAsBoolean());
        assertTrue(pack.getAsJsonObject("progression").get("attribute_investment_rank_limit").isJsonNull());
    }

    @Test void apotheosisAffixInventoryIsCompleteForPinnedSourceSnapshot() throws IOException {
        JsonObject inventory = load("affix-inventory-apotheosis-8.9.0.json");
        JsonArray rows = inventory.getAsJsonArray("definitions");
        assertEquals(99, rows.size());
        assertEquals(rows.size(), inventory.get("definition_count").getAsInt());
        Set<String> ids = new HashSet<>();
        for (var row : rows) {
            JsonObject item = row.getAsJsonObject();
            String id = item.get("id").getAsString();
            assertTrue(id.startsWith("apotheosis:"));
            assertTrue(ids.add(id), "Duplicate affix: " + id);
            assertEquals("requires_monk_adapter", item.get("status").getAsString());
        }
        JsonObject tmm = load("too-much-magic.json");
        assertEquals(rows.size(), tmm.getAsJsonObject("affix_unlocks").get("count_expected").getAsInt());
    }

    @Test void apothicAttributeInventoryIsCompleteForPinnedSourceSnapshot() throws IOException {
        JsonObject inventory = load("attributes-apothic-2.11.0.json");
        JsonArray rows = inventory.getAsJsonArray("attributes");
        assertEquals(22, rows.size());
        Set<String> ids = new HashSet<>();
        for (var row : rows) {
            JsonObject item = row.getAsJsonObject();
            assertTrue(ids.add(item.get("id").getAsString()));
        }
        assertTrue(ids.contains("apothic_attributes:cooldown_reduction"));
        assertTrue(ids.contains("apothic_attributes:elytra_flight"));
        assertEquals(rows.size(), load("too-much-magic.json")
                .getAsJsonObject("numeric_player_attributes").get("count_apothic_baseline").getAsInt());
    }
}
