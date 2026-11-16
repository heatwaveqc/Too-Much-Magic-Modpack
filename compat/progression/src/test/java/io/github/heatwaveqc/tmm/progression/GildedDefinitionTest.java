package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.core.GildedWearMath;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GildedDefinitionTest {
    @Test void virtualUnbreakingIsAdditiveInExpectedWearProbability() {
        assertEquals(1.0,GildedWearMath.keepProbability(0,0,false),1.0e-8);
        // Real III, virtual III: prefilter 4/7 then vanilla 1/4 = 1/7.
        assertEquals(4.0/7.0,GildedWearMath.keepProbability(3,3,false),1.0e-8);
        assertEquals(1.0/7.0,(1.0/4.0)*GildedWearMath.keepProbability(3,3,false),1.0e-8);
        // Armor keeps the same 60% vanilla bypass behavior.
        assertEquals((0.6+0.4/7.0)/(0.6+0.4/4.0),
            GildedWearMath.keepProbability(3,3,true),1.0e-8);
    }
    @Test void jsonTargetsGoldWeaponsAndGrantsThreeLootingImmediately() throws Exception {
        var loader = getClass().getClassLoader();
        var json = loader.getResourceAsStream(
            "data/tmm_progression/tmm_progression/paths/gilded.json");
        assertNotNull(json, "Gilded path must be compiled into the module");
        try (json) {
            var root = JsonParser.parseReader(new java.io.InputStreamReader(
                json,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("admin",root.get("enrollment").getAsString());
            boolean lootingFound=false;
            for (var el : root.getAsJsonArray("skills")) {
                var skill=el.getAsJsonObject();
                if (!skill.get("id").getAsString().equals("tmm_progression:gilded_looting")) continue;
                assertEquals(1,skill.get("max_rank").getAsInt());
                assertEquals(1,skill.getAsJsonArray("auto_ranks").size());
                assertEquals(1,skill.getAsJsonArray("auto_ranks").get(0).getAsInt());
                var effect=skill.getAsJsonArray("effects").get(0).getAsJsonObject();
                assertEquals("tmm_progression:virtual_looting",effect.get("type").getAsString());
                assertEquals("#tmm_progression:gilded_gold_weapons",effect.get("target").getAsString());
                assertEquals(3,effect.get("per_rank").getAsInt());
                assertEquals("tmm_gilded_looting_mastery",
                    effect.getAsJsonObject("params").get("tyz_skill_id").getAsString());
                assertEquals("10",effect.getAsJsonObject("params").get("maximum_virtual_bonus").getAsString());
                lootingFound=true;
            }
            assertTrue(lootingFound,"Additive gilded gold-weapon Looting skill is required");
        }
    }
    @Test void gildedFortuneStartsAtThreeRatherThanAtNativeRankThree() throws Exception {
        try (var stream = getClass().getClassLoader().getResourceAsStream(
                "data/tmm_progression/tmm_progression/paths/gilded.json")) {
            assertNotNull(stream);
            var root=JsonParser.parseReader(new java.io.InputStreamReader(
                stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            var skills=root.getAsJsonArray("skills");
            boolean found=false;
            for (var el : skills) {
                var skill=el.getAsJsonObject();
                if (!"tmm_progression:gilded_fortunes_favor".equals(skill.get("id").getAsString()))
                    continue;
                assertEquals(1,skill.get("max_rank").getAsInt());
                assertEquals(1,skill.getAsJsonArray("auto_ranks").get(0).getAsInt());
                var effect=skill.getAsJsonArray("effects").get(0).getAsJsonObject();
                assertEquals(3,effect.get("per_rank").getAsInt());
                assertEquals("10",effect.getAsJsonObject("params")
                    .get("maximum_virtual_bonus").getAsString());
                found=true;
            }
            assertTrue(found,"Gilded auto-granted base Fortune must be registered");
        }
    }

    @Test void moltenSovereigntyUnlocksOnlyAtRankEighteen() throws Exception {
        try(var stream = getClass().getClassLoader().getResourceAsStream(
                "data/tmm_progression/tmm_progression/paths/gilded.json")) {
            assertNotNull(stream);
            var root = JsonParser.parseReader(new java.io.InputStreamReader(
                stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            boolean found = false;
            for (var entry : root.getAsJsonArray("skills")) {
                var s=entry.getAsJsonObject();
                if (!"tmm_progression:gilded_molten_sovereignty".equals(s.get("id").getAsString())) continue;
                assertEquals(18,s.get("required_rank").getAsInt());
                assertEquals(18,s.getAsJsonArray("auto_ranks").get(0).getAsInt());
                assertEquals("tmm_progression:lava_immunity",s.getAsJsonArray("effects")
                    .get(0).getAsJsonObject().get("type").getAsString());
                found = true;
            }
            assertTrue(found,"High-level lava immunity is required");
        }
    }
}
