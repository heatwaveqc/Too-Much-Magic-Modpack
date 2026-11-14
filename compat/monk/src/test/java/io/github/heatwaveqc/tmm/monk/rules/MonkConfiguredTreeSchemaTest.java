package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/** Pinned Vampirism 1.10 NeoForge configured skill tree requires children, including for a root-only tree. */
final class MonkConfiguredTreeSchemaTest {
    @Test void branchingConfiguredTreeContainsAllRegisteredSkills() throws Exception {
        Path path = Path.of("src", "main", "resources", "data", "tmm_monk",
                "vampirism", "configured_skill_tree", "monk_level.json");
        try (var reader = Files.newBufferedReader(path)) {
            var tree = JsonParser.parseReader(reader).getAsJsonObject();
            assertEquals("tmm_monk:monk/level", tree.get("skill_tree").getAsString());
            assertEquals("tmm_monk:monk/level_root", tree.get("node").getAsString());
            assertTrue(tree.has("children"), "Vampirism 1.10 codec requires children");
            var children = tree.getAsJsonArray("children");
            assertEquals(2, children.size());
            assertEquals("tmm_monk:monk/martial_1", children.get(0).getAsJsonObject().get("node").getAsString());
            assertEquals("tmm_monk:monk/martial_2", children.get(0).getAsJsonObject()
                    .getAsJsonArray("children").get(0).getAsJsonObject().get("node").getAsString());
            assertEquals("tmm_monk:monk/mobility_1", children.get(1).getAsJsonObject().get("node").getAsString());
        }
    }
}
