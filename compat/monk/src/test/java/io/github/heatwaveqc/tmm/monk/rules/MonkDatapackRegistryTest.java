package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Guard the Vampirism 1.10 NeoForge datapack paths against confusing them
 * with the incompatible 2.x FactionApi paths or singular folder names.
 */
final class MonkDatapackRegistryTest {
    private static final Path DATA = Path.of("src", "main", "resources", "data", "tmm_monk", "vampirism");

    @Test void levelTreeIsLoadableForTheMonkFaction() throws Exception {
        Path path = DATA.resolve("skill_trees/monk/level.json");
        assertTrue(Files.isRegularFile(path), "1.10 expects plural skill_trees");
        try (var reader = Files.newBufferedReader(path)) {
            var tree = JsonParser.parseReader(reader).getAsJsonObject();
            assertEquals("tmm_monk:monk",
                    tree.getAsJsonObject("faction").get("id").getAsString());
            var predicate = tree.getAsJsonObject("unlock_predicate").getAsJsonObject("type_specific");
            assertEquals("vampirism:faction", predicate.get("type").getAsString());
            assertEquals("tmm_monk:monk", predicate.getAsJsonObject("faction").get("id").getAsString());
            assertEquals(1, predicate.get("level").getAsInt());
        }
        assertFalse(Files.exists(DATA.resolve("skill_tree/monk/level.json")),
                "Singular folder is not a Vampirism 1.10 registry");
    }

    @Test void everyConfiguredNodeExistsUnderPluralRegistryPath() throws Exception {
        String[] nodes = {"level_root", "martial_1", "martial_2", "mobility_1"};
        String[] skills = {"tmm_monk:monk_root", "tmm_monk:open_palm",
                "tmm_monk:unarmored_poise", "tmm_monk:fleet_footwork"};
        for (int i = 0; i < nodes.length; i++) {
            Path path = DATA.resolve("skill_nodes/monk/" + nodes[i] + ".json");
            assertTrue(Files.isRegularFile(path), "Missing skill node: " + nodes[i]);
            try (var reader = Files.newBufferedReader(path)) {
                var node = JsonParser.parseReader(reader).getAsJsonObject();
                assertEquals(skills[i], node.getAsJsonArray("skills").get(0).getAsString());
            }
            assertFalse(Files.exists(DATA.resolve("skill_node/monk/" + nodes[i] + ".json")));
        }
    }
}
