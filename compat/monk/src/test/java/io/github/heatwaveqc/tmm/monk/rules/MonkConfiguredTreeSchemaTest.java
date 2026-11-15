package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/** The root opens three early choices; movement is later and requires martial study. */
final class MonkConfiguredTreeSchemaTest {
    @Test void rankOneChoicesAndLaterMobility() throws Exception {
        Path file = Path.of("src", "main", "resources", "data", "tmm_monk",
                "vampirism", "configured_skill_tree", "monk_level.json");
        try (var reader = Files.newBufferedReader(file)) {
            var tree = JsonParser.parseReader(reader).getAsJsonObject();
            assertEquals("tmm_monk:monk/level", tree.get("skill_tree").getAsString());
            assertEquals("tmm_monk:monk/level_root", tree.get("node").getAsString());
            var choices = tree.getAsJsonArray("children");
            assertEquals(3, choices.size(), "Open Palm, Poise and Stone Fist must be available before Step Assist");
            assertEquals("tmm_monk:monk/martial_1", choices.get(0).getAsJsonObject().get("node").getAsString());
            assertEquals("tmm_monk:monk/defense_1", choices.get(1).getAsJsonObject().get("node").getAsString());
            assertEquals("tmm_monk:monk/harvest_1", choices.get(2).getAsJsonObject().get("node").getAsString());
            var fleet = choices.get(0).getAsJsonObject().getAsJsonArray("children").get(0).getAsJsonObject();
            assertEquals("tmm_monk:monk/mobility_1", fleet.get("node").getAsString());
            assertEquals("tmm_monk:monk/mobility_2",
                    fleet.getAsJsonArray("children").get(0).getAsJsonObject().get("node").getAsString());
        }
    }
}
