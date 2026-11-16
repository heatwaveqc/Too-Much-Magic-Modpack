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
    @Test void jsonTargetsOnlyGoldWeaponsAndUsesThreeAdditiveRanks() throws Exception {
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
                assertEquals(3,skill.get("max_rank").getAsInt());
                assertEquals(3,skill.getAsJsonArray("auto_ranks").size());
                var effect=skill.getAsJsonArray("effects").get(0).getAsJsonObject();
                assertEquals("tmm_progression:virtual_looting",effect.get("type").getAsString());
                assertEquals("#tmm_progression:gilded_gold_weapons",effect.get("target").getAsString());
                assertEquals(1,effect.get("per_rank").getAsInt());
                lootingFound=true;
            }
            assertTrue(lootingFound,"Additive gilded gold-weapon Looting skill is required");
        }
    }
}
