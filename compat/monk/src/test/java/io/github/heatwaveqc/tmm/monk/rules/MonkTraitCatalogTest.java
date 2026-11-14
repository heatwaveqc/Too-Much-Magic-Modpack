package io.github.heatwaveqc.tmm.monk.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

/** Checks the offline research catalog. Runtime player attributes are separate. */
final class MonkTraitCatalogTest {
    private static JsonObject load(String name) throws Exception {
        try (var reader = Files.newBufferedReader(Path.of("profiles", name))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void staticAttributeCandidatesHaveUniqueIdsAndProvenance() throws Exception {
        var inventory = load("attribute-candidates-1.21.1.json");
        var entries = inventory.getAsJsonArray("attributes");
        assertEquals(142, inventory.get("candidate_count").getAsInt());
        assertEquals(142, entries.size());
        assertFalse(inventory.get("complete_player_registry").getAsBoolean());
        var ids = new HashSet<String>();
        for (var e : entries) {
            var row = e.getAsJsonObject();
            String id = row.get("id").getAsString();
            assertTrue(id.contains(":"), id);
            assertTrue(ids.add(id), "Duplicate candidate " + id);
            assertEquals("not_yet_live_dumped", row.get("status").getAsString());
        }
        assertFalse(ids.contains("ars_nouveau:ars_nouveau.perk.toughness"),
                "Commented-out Ars attribute must not be treated as registered");
        assertTrue(ids.contains("apothic_attributes:cooldown_reduction"));
        assertTrue(ids.contains("goety:soul_discount"));
        assertTrue(ids.contains("irons_spellbooks:max_mana"));
    }

    @Test void worldTierPlanKeepsCreativeFlightAfterGuardian() throws Exception {
        var tiers = load("world-tier-unlocks.json");
        assertEquals(2, tiers.getAsJsonArray("flight_unlocks").size());
        var stages = tiers.getAsJsonArray("flight_unlocks");
        var glide = stages.get(0).getAsJsonObject();
        var creative = stages.get(1).getAsJsonObject();
        assertEquals("tmm_monk:momentum_gliding", glide.get("id").getAsString());
        assertEquals("post_ender_dragon_pre_chaos_guardian", glide.get("phase").getAsString());
        assertEquals("tmm_monk:creative_flight", creative.get("id").getAsString());
        assertEquals("post_chaos_guardian", creative.get("phase").getAsString());
        assertEquals(5, tiers.getAsJsonArray("world_tier_milestones").size());
        assertEquals("proposed_progression_unlocks_only_not_enforced", tiers.get("kind").getAsString());
    }
}
