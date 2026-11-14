package io.github.heatwaveqc.tmm.monk.rules;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Static design integrity; does NOT test in-game skill behavior, costs, or progression.
 * The intended backend remains undecided.
 */
final class MonkSkillPlanTest {
    private static JsonObject load(String filename) throws Exception {
        try (var reader = Files.newBufferedReader(Path.of("profiles", filename))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void skillPlanCoversEveryCandidateAndOriginalAbility() throws Exception {
        JsonObject plan = load("skill-catalog-v1.json");
        JsonArray skills = plan.getAsJsonArray("skills");
        JsonArray references = plan.getAsJsonArray("attribute_dispositions");
        JsonArray original = plan.getAsJsonArray("original_crosswalk");
        assertEquals(172, skills.size());
        assertEquals(144, references.size());
        assertEquals(25, original.size());
        assertTrue(plan.get("backend_decision").getAsString().startsWith("DEFERRED"));
        assertTrue(plan.get("placement_decision").getAsString().startsWith("DEFERRED"));

        Set<String> ids = new HashSet<>();
        for (var entry : skills) {
            JsonObject item = entry.getAsJsonObject();
            assertTrue(ids.add(item.get("id").getAsString()), "Duplicate skill: " + item);
            assertEquals("design_only", item.get("implementation_status").getAsString());
            String rankModel = item.get("rank_model").getAsString();
            assertTrue(Set.of("repeatable", "bounded", "once", "staged").contains(rankModel));
            if (rankModel.equals("repeatable")) {
                assertTrue(item.get("max_ranks").isJsonNull(), "Repeatable must have no arbitrary rank cap");
            } else if (rankModel.equals("once")) {
                assertEquals(1, item.get("max_ranks").getAsInt());
            }
        }
        assertFalse(ids.contains("tmm_monk:creative_flight"));
        assertFalse(ids.contains("tmm_monk:trait/neoforge/creative_flight"));

        Set<String> referencedAttrs = new HashSet<>();
        int mapped = 0;
        int excluded = 0;
        for (var ref : references) {
            JsonObject item = ref.getAsJsonObject();
            String attribute = item.get("attribute").getAsString();
            assertTrue(referencedAttrs.add(attribute), "Duplicate source attribute: " + attribute);
            String decision = item.get("decision").getAsString();
            if (decision.equals("mapped")) {
                mapped++;
                assertTrue(item.getAsJsonArray("skill_ids").size() > 0);
                for (var id : item.getAsJsonArray("skill_ids")) {
                    assertTrue(ids.contains(id.getAsString()), "Unresolved skill " + id);
                }
            } else {
                assertEquals("excluded", decision);
                excluded++;
                assertEquals(0, item.getAsJsonArray("skill_ids").size());
                assertTrue(item.has("reason"));
            }
        }
        assertEquals(141, mapped);
        assertEquals(3, excluded);
        assertTrue(referencedAttrs.contains("neoforge:swim_speed"));
        assertTrue(referencedAttrs.contains("neoforge:creative_flight"));
        assertTrue(referencedAttrs.contains("minecraft:player.mining_efficiency"));

        Set<String> originalIds = new HashSet<>();
        for (var entry : original) {
            JsonObject row = entry.getAsJsonObject();
            assertTrue(originalIds.add(row.get("ability").getAsString()));
            for (var mappedSkill : row.getAsJsonArray("skill_ids")) {
                assertTrue(ids.contains(mappedSkill.getAsString()),
                        "Original ability references an undefined Monk skill");
            }
        }
        assertTrue(originalIds.contains("glass_touch"));
        assertTrue(originalIds.contains("water_breathing_full"));
        assertTrue(originalIds.contains("fly"));
    }

    @Test void harvestTiersAreFiniteAndSpeedIsRepeatable() throws Exception {
        var entries = load("skill-catalog-v1.json").getAsJsonArray("skills");
        var skillById = new HashMap<String, JsonObject>();
        for (var e : entries) {
            var row = e.getAsJsonObject();
            skillById.put(row.get("id").getAsString(), row);
        }
        var harvest = skillById.get("tmm_monk:harvest/barehand_tier");
        var speed = skillById.get("tmm_monk:harvest/barehand_speed");
        assertNotNull(harvest);
        assertNotNull(speed);
        assertEquals("staged", harvest.get("rank_model").getAsString());
        assertEquals("runtime_pack_tool_tiers", harvest.get("max_ranks").getAsString());
        assertEquals("repeatable", speed.get("rank_model").getAsString());
        assertTrue(speed.get("max_ranks").isJsonNull());
        assertEquals(3, speed.getAsJsonArray("source_attributes").size());
    }

    @Test void affixTechniquesCoverAllNinetyNineWithSourceSupportedRanks() throws Exception {
        var source = load("affix-inventory-apotheosis-8.9.0.json");
        var plan = load("skill-affix-techniques-v1.json");
        var originalAffixes = source.getAsJsonArray("definitions");
        var techniques = plan.getAsJsonArray("skills");
        assertEquals(99, originalAffixes.size());
        assertEquals(99, techniques.size());
        Set<String> affixIds = new HashSet<>();
        for (var entry : originalAffixes) affixIds.add(entry.getAsJsonObject().get("id").getAsString());
        Set<String> skillIds = new HashSet<>();
        for (var entry : techniques) {
            JsonObject skill = entry.getAsJsonObject();
            String id = skill.get("id").getAsString();
            String sourceId = skill.get("source_affix").getAsString();
            assertTrue(skillIds.add(id), "Duplicate affix skill: " + id);
            assertTrue(affixIds.remove(sourceId), "Missing/unrecognized source affix: " + sourceId);
            int levels = skill.get("max_ranks").getAsInt();
            int tiers = skill.getAsJsonArray("allowed_source_rarities").size();
            assertTrue(levels >= 1 && levels <= 5);
            assertEquals(levels, tiers == 0 ? 1 : tiers);
            assertEquals(tiers == 0 ? "once" : "finite_source_rarity",
                    skill.get("rank_model").getAsString());
            assertFalse(id.equals("tmm_monk:creative_flight"));
            assertEquals("design_only", skill.get("implementation_status").getAsString());
        }
        assertTrue(affixIds.isEmpty());

        var unbound = techniques.get(13).getAsJsonObject();
        assertEquals("apotheosis:armor/attribute/unbound", unbound.get("source_affix").getAsString());
        assertEquals("neoforge:creative_flight", unbound.get("source_attribute").getAsString());
        assertTrue(unbound.get("effect_contract").getAsString().contains("NEVER grant Creative Flight"));
    }

    @Test void dragonSurvivalRosterIsApprovedButNotInstalled() throws Exception {
        try (var reader = Files.newBufferedReader(Path.of("..", "..", "manifest", "mods.json"))) {
            var manifest = JsonParser.parseReader(reader).getAsJsonObject();
            var mods = manifest.getAsJsonArray("mods");
            JsonObject dragon = null;
            for (var mod : mods) {
                var row = mod.getAsJsonObject();
                if ("dragon-survival".equals(row.get("id").getAsString())) dragon = row;
            }
            assertNotNull(dragon);
            assertTrue(dragon.get("desired").getAsBoolean());
            assertFalse(dragon.get("enabled_in_pack").getAsBoolean());
            assertEquals("verified_release", dragon.get("release_status").getAsString());
            assertEquals(9539454, dragon.get("candidate_file_id").getAsInt());
        }
    }
}
