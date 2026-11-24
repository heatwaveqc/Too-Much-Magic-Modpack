package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Protect the reviewed four-node Core candidate against obsolete namespaced imports. */
class CoreMagicPilotTest {
    @Test void sourceBackedCoreFundamentalsArePlayableAndCostOnePerRank() throws Exception {
        String path = "/data/tyr_progression/tyr_progression/paths/core.json";
        try (var raw = CoreMagicPilotTest.class.getResourceAsStream(path)) {
            assertNotNull(raw, "Core datapack must be bundled with the Progression jar");
            var parsed = JsonParser.parseReader(
                    new InputStreamReader(raw, StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("automatic", parsed.get("enrollment").getAsString());
            assertEquals("tyr_progression:mxp", parsed.get("xp_channel").getAsString());
            var skills = parsed.getAsJsonArray("skills");
            assertEquals(4, skills.size(), "Remaining 23 Core nodes are not yet reviewed live");
            var ids = new HashSet<String>();
            for (var el : skills) {
                JsonObject skill = el.getAsJsonObject();
                String id = skill.get("id").getAsString();
                assertTrue(id.startsWith("tyr_progression:"), id);
                assertTrue(ids.add(id), "Duplicate Core skill: " + id);
                assertEquals(5, skill.get("max_rank").getAsInt());
                assertEquals(1, skill.get("required_rank").getAsInt());
                assertEquals("tyr_progression:mxp", skill.get("xp_channel").getAsString());
                var costs = skill.getAsJsonArray("rank_costs");
                assertEquals(5, costs.size());
                for (var cost : costs) assertEquals(1, cost.getAsInt());
                assertFalse(skill.getAsJsonArray("effects").isEmpty());
                for (var fxEl : skill.getAsJsonArray("effects")) {
                    var fx = fxEl.getAsJsonObject();
                    assertEquals("attribute", fx.get("type").getAsString());
                    assertEquals(5, fx.getAsJsonArray("rank_values").size());
                }
            }
            assertTrue(ids.contains("tyr_progression:tmm_magic_reservoir_1"));
            assertTrue(ids.contains("tyr_progression:tmm_magic_wellspring_1"));
            assertTrue(ids.contains("tyr_progression:tmm_magic_thrift_1"));
            assertTrue(ids.contains("tyr_progression:tmm_magic_potency_1"));
        }
    }
}
