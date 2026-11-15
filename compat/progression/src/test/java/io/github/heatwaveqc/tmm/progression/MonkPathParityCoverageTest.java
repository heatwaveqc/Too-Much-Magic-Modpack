package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JSON-level parity crosswalk. Valid parser records != verified in-world effects.
 * Keeps missing Java behaviors explicit without adding deceptively purchasable no-op nodes.
 */
final class MonkPathParityCoverageTest {
    private static Definition.Path monkPath() throws Exception {
        String resource = "data/tmm_progression/tmm_progression/paths/monk.json";
        try (InputStream input = MonkPathParityCoverageTest.class.getClassLoader()
                .getResourceAsStream(resource)) {
            assertNotNull(input, "Loadable Monk datapack missing: " + resource);
            JsonObject source = JsonParser.parseReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8)).getAsJsonObject();
            return PathJson.read("tmm_progression:monk", source);
        }
    }

    private static JsonObject coverage() throws Exception {
        return JsonParser.parseString(Files.readString(
                Path.of("design", "monk-classic-coverage-v1.json"))).getAsJsonObject();
    }

    @Test void installedPathParsesAndRemainsTwentyRanksWithoutTyz() throws Exception {
        Definition.Path path = monkPath();
        assertEquals(20, path.maxRank());
        assertTrue(path.skills().size() >= 28);
        assertEquals(0L, path.rankXp().getFirst().longValue());
        assertEquals(20L, path.rankXp().get(1).longValue());
        assertEquals(55L, path.rankXp().get(2).longValue());
        assertEquals(105L, path.rankXp().get(3).longValue());
        for (Definition.Skill skill : path.skills()) {
            if (skill.autoRanks().isEmpty()) continue;
            assertEquals(0, skill.baseCost(), "Classic automatically unlocked talents are free: " + skill.id());
            assertEquals(0, skill.stepCost(), "Auto-grants cannot spend Monk or Tyz points");
            assertEquals("", skill.choiceGroup(), "Never make classic baseline mutually exclusive");
            assertTrue(skill.requiresSkills().isEmpty());
            assertTrue(skill.requiresAnySkills().isEmpty());
            assertTrue(skill.requiresMilestones().isEmpty());
            assertTrue(skill.autoRanks().size() <= skill.maxRank());
        }
    }

    @Test void everyClassicSourcePowerIsAuditedAndOnlyLavaAwaitingARealHandler() throws Exception {
        Definition.Path path = monkPath();
        JsonObject manifest = coverage();
        assertEquals("CONFIGURED_PATH_NOT_RUNTIME_PARITY_COMPLETE",
                manifest.get("status").getAsString());
        assertEquals(25, manifest.get("all_original_classic_abilities").getAsInt());
        assertTrue(manifest.get("no_tyz_dependency").getAsBoolean());
        var rows = manifest.getAsJsonArray("classic_ability_mapping");
        assertEquals(25, rows.size());

        Set<String> sourceIds = new HashSet<>();
        Set<String> originalOffDefault = new HashSet<>();
        int unavailable = 0;
        for (var element : rows) {
            JsonObject ability = element.getAsJsonObject();
            String source = ability.get("classic_id").getAsString();
            assertTrue(sourceIds.add(source), "Duplicate classic ability " + source);
            if (!ability.get("original_default").getAsBoolean()) originalOffDefault.add(source);
            assertTrue(ability.get("native_only").getAsBoolean());
            assertFalse(ability.get("tyz_required").getAsBoolean());
            var targets = ability.getAsJsonArray("native_skill_ids");
            if (source.equals("lava_protection")) {
                unavailable++;
                assertTrue(targets.size() == 0, "Distinct contextual lava handler is NOT built");
                assertEquals("missing_effect_handler",
                        ability.get("status").getAsString());
            } else {
                assertTrue(targets.size() > 0, "Classic ability lacks a configured native skill: " + source);
                for (var target : targets) {
                    Definition.Skill skill = path.skill(target.getAsString());
                    assertNotNull(skill, "Native capability absent from loadable Monk JSON: " + target);
                    assertFalse(skill.autoRanks().isEmpty(), "Classic baseline must auto-unlock: " + target);
                    assertFalse(skill.effects().isEmpty(), "Never use paid or empty no-op classics: " + target);
                }
            }
            assertFalse(ability.get("code_or_qa_gap").getAsString().isBlank(),
                    "Missing factual QA status: " + source);
        }
        assertEquals(1, unavailable, "New missing abilities need explicit owner-visible accounting");
        assertEquals(Set.of("armor_toughness", "health", "glass_touch", "water_breathing_full"),
                originalOffDefault);
        assertEquals(25, sourceIds.size());
    }

    @Test void handMiningWorksAcrossToolsWithNativeEndgameSpeedNotRequiringTyz() throws Exception {
        Definition.Path path = monkPath();
        Definition.Skill speed = path.skill("tmm_progression:hand_mining");
        assertNotNull(speed);
        assertEquals(5, speed.autoRanks().size());
        assertEquals(17, speed.autoRanks().getLast().intValue());
        assertEquals(1, speed.effects().size());
        var bonus = speed.effects().getFirst();
        assertEquals("break_speed", bonus.type());
        assertEquals("*", bonus.selector(), "All valid barehand blocks, not pickaxe-only");
        assertEquals(2.4, bonus.amountPerRank(), 0.0001);
        assertTrue(bonus.emptyHand());
        assertTrue(bonus.unarmored());
        assertEquals(13.0, 1.0 + speed.maxRank() * bonus.amountPerRank(), 0.0001);

        Definition.Skill harvest = path.skill("tmm_progression:hand_harvest");
        assertNotNull(harvest);
        assertEquals(java.util.List.of(4, 9, 13), harvest.autoRanks());
        Set<String> selectors = new HashSet<>();
        for (var effect : harvest.effects()) {
            assertEquals("harvest_tier", effect.type());
            assertEquals(3, effect.tier());
            assertTrue(effect.emptyHand());
            assertTrue(effect.unarmored());
            selectors.add(effect.selector());
        }
        assertEquals(Set.of("#minecraft:mineable/pickaxe",
                "#minecraft:mineable/shovel", "#minecraft:mineable/axe"), selectors);
    }

    @Test void explicitExceptionsRemainUnambiguousInLoadableConfig() throws Exception {
        Definition.Path path = monkPath();
        assertNull(path.skill("tmm_progression:spirit_projectile"));
        assertNull(path.skill("tmm_progression:creative_flight"));
        assertNull(path.skill("tmm_progression:lava_protection"),
                "Until an actual distinct lava handler exists, do NOT ship a no-op or double-dipping skill");
        var glide = path.skill("tmm_progression:momentum_glide");
        assertNotNull(glide);
        assertEquals("tmm_progression:dragon_defeated",
                glide.effects().getFirst().params().get("requires_milestone"));

        var treasure = path.skill("tmm_progression:treasure_hand");
        assertNotNull(treasure);
        assertEquals(1, treasure.effects().size());
        assertEquals("treasure_hand", treasure.effects().getFirst().type());
        assertEquals(0, treasure.baseCost());
        assertEquals(java.util.List.of(12), treasure.autoRanks());
        assertEquals(java.util.List.of(1), path.skill("tmm_progression:reserved_fist").autoRanks());
        assertEquals(java.util.List.of(1), path.skill("tmm_progression:armor_lock").autoRanks());
    }
}
