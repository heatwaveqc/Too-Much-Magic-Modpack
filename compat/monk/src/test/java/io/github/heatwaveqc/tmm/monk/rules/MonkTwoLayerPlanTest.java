package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Design-only architecture consistency test. Not an in-game Pufferfish integration test. */
final class MonkTwoLayerPlanTest {
    private static JsonObject load(String name) throws Exception {
        try (var reader = Files.newBufferedReader(Path.of("profiles", name))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void allSourceAttributesHaveOneOwnerWhenUnified() throws Exception {
        var plan = load("skill-unification-proposal.json");
        assertEquals("architecture_smoke_test_not_runtime_or_final_skill_roster",
                plan.get("status").getAsString());
        var groups = plan.getAsJsonArray("groups");
        assertEquals(24, groups.size());
        var seen = new HashSet<String>();
        var ids = new HashSet<String>();
        for (var entry : groups) {
            var group = entry.getAsJsonObject();
            assertTrue(ids.add(group.get("canonical_skill_id").getAsString()));
            assertEquals("source_backed_aggregate_not_implemented",
                    group.get("adapter_status").getAsString());
            for (var member : group.getAsJsonArray("source_members")) {
                var source = member.getAsJsonObject();
                assertTrue(seen.add(source.get("source_attribute").getAsString()),
                        "Source attribute owned by more than one unified skill");
            }
        }
        assertEquals(43, seen.size());
        assertTrue(ids.contains("tmm_shared:magic_capacity"));
        assertTrue(ids.contains("tmm_shared:mining_speed"));
        assertTrue(ids.contains("tmm_shared:spell_cooldown"));
        assertTrue(seen.contains("neoforge:swim_speed"));
        assertTrue(seen.contains("irons_spellbooks:max_mana"));
    }

    @Test void stepAssistAndGlideAreFactionOneTimeUnlocks() throws Exception {
        var catalog = load("skill-catalog-v1.json");
        var selected = new HashMap<String, JsonObject>();
        for (var entry : catalog.getAsJsonArray("skills")) {
            var row = entry.getAsJsonObject();
            selected.put(row.get("id").getAsString(), row);
            assertTrue(row.has("tentative_owner"), "Every proposed skill needs tentative ownership");
            String owner = row.get("tentative_owner").getAsString();
            if ("once".equals(row.get("rank_model").getAsString())) {
                assertEquals("monk_faction_unlock_and_effect", owner);
                assertEquals(1, row.get("max_ranks").getAsInt());
            } else {
                assertEquals("monk_faction_unlock_then_tyzs_investment", owner);
            }
        }
        var step = selected.get("tmm_monk:trait/minecraft/generic_step_height");
        assertNotNull(step);
        assertEquals("Step Assist", step.get("name").getAsString());
        assertEquals("once", step.get("rank_model").getAsString());
        var glide = selected.get("tmm_monk:mobility/momentum_glide");
        assertNotNull(glide);
        assertEquals("once", glide.get("rank_model").getAsString());
        assertFalse(selected.containsKey("tmm_monk:creative_flight"));
    }

    @Test void unboundIsExcludedFromEveryPaidMonkAffix() throws Exception {
        var affix = load("skill-affix-techniques-v1.json");
        var source = load("affix-inventory-apotheosis-8.9.0.json");
        var profile = load("too-much-magic.json");
        assertEquals(99, source.getAsJsonArray("definitions").size());
        assertEquals(99, affix.get("source_total").getAsInt());
        assertEquals(98, affix.get("total").getAsInt());
        assertEquals(98, affix.getAsJsonArray("skills").size());
        assertEquals(98, profile.getAsJsonObject("affix_unlocks").get("count_expected").getAsInt());
        assertEquals("apotheosis:armor/attribute/unbound",
                affix.getAsJsonArray("excluded_source_affixes").get(0)
                     .getAsJsonObject().get("id").getAsString());
        for (var element : affix.getAsJsonArray("skills")) {
            var row = element.getAsJsonObject();
            assertFalse(row.get("source_affix").getAsString().endsWith("/unbound"));
            assertFalse(row.get("id").getAsString().contains("creative_flight"));
        }
    }
}
