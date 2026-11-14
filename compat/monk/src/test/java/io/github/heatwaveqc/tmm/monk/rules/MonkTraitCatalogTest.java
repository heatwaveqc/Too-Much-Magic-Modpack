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
        assertEquals(1, tiers.getAsJsonArray("flight_unlocks").size());
        var glide = tiers.getAsJsonArray("flight_unlocks").get(0).getAsJsonObject();
        assertEquals("tmm_monk:momentum_gliding", glide.get("id").getAsString());
        assertEquals("post_ender_dragon_pre_chaos_guardian", glide.get("phase").getAsString());
        assertFalse(tiers.toString().contains("tmm_monk:creative_flight"));
        assertEquals("draconic_evolution", tiers.getAsJsonObject("external_creative_flight_provider").get("mod").getAsString());
        assertEquals(5, tiers.getAsJsonArray("world_tier_milestones").size());
        assertEquals("proposed_progression_unlocks_only_not_enforced", tiers.get("kind").getAsString());
    }
    @Test void originalMonkHasEveryRegisteredAbilityIncludingUnusedExtras() throws Exception {
        var catalog = load("original-monk-abilities-1.4.json");
        var entries = catalog.getAsJsonArray("abilities");
        assertEquals(25, catalog.get("original_registered_count").getAsInt());
        assertEquals(25, entries.size());
        var ids = new HashSet<String>();
        for (var entry : entries) {
            var ability = entry.getAsJsonObject();
            assertTrue(ids.add(ability.get("id").getAsString()));
            assertEquals("planned_or_historical_not_implemented", ability.get("status").getAsString());
        }
        for (String critical : new String[]{"blink", "catch_arrows", "glass_touch",
                "kiss_creeper", "tame_animals", "water_walking", "withering_stare",
                "water_breathing_partial", "water_breathing_full", "mining"}) {
            assertTrue(ids.contains(critical), "Missing original ability " + critical);
        }
        var fly = entries.get(8).getAsJsonObject();
        assertEquals("fly", fly.get("id").getAsString());
        assertEquals("retained_historical_only", fly.get("tmm_class").getAsString());
    }

    @Test void harvestTierAndMiningSpeedAreIndependent() throws Exception {
        var tiers = load("world-tier-unlocks.json");
        var mining = tiers.getAsJsonObject("mining");
        var harvest = mining.getAsJsonObject("harvest_tier");
        var speed = mining.getAsJsonObject("harvest_speed");
        assertEquals("finite_unlocks_bounded_by_actual_highest_tool_harvest_requirement",
                harvest.get("upgrade_model").getAsString());
        assertEquals("repeatable_numeric_ranks_increasing_cost",
                speed.get("upgrade_model").getAsString());
        assertTrue(speed.get("hard_cap").isJsonNull());
        assertTrue(harvest.get("independent_of_mining_speed").getAsBoolean());
        assertTrue(speed.get("independent_of_harvest_tier").getAsBoolean());
        assertFalse(load("too-much-magic.json").getAsJsonObject("progression")
                .get("innate_creative_flight").getAsBoolean());
    }

}
