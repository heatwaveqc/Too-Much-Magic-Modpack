package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The complete Monk design belongs in a blueprint, not in the live first-six
 * configured tree until server-side gates and effect adapters are implemented.
 */
final class MonkFullTreeSkeletonTest {
    private static final Path PLAN = Path.of("design", "native-full-tree-skeleton-v1.json");
    private static final Path CATALOG = Path.of("profiles", "skill-placement-no-pufferfish-v2.json");
    private static final Path LIVE = Path.of("src", "main", "resources", "data", "tmm_monk", "vampirism");
    private static final Set<String> LIVE_SKILLS = Set.of(
            "tmm_monk:monk_root", "tmm_monk:open_palm", "tmm_monk:unarmored_poise",
            "tmm_monk:stone_fist", "tmm_monk:fleet_footwork", "tmm_monk:step_assist");

    private static JsonObject read(Path path) throws IOException {
        try (var reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void allFiftyNodesHaveValidParentsRanksAndStatuses() throws Exception {
        JsonObject blueprint = read(PLAN);
        assertEquals("DESIGN_ONLY_NOT_RUNTIME_LOADED", blueprint.get("state").getAsString());
        assertEquals(20, blueprint.get("intended_max_faction_rank").getAsInt());
        JsonArray nodes = blueprint.getAsJsonArray("nodes");
        assertEquals(50, nodes.size(), "Changing the skeleton needs explicit review");
        Map<String, JsonObject> byId = new HashMap<>();
        Set<String> live = new HashSet<>();
        for (var element : nodes) {
            JsonObject node = element.getAsJsonObject();
            String id = node.get("id").getAsString();
            assertTrue(byId.putIfAbsent(id, node) == null, "Duplicate node " + id);
            assertTrue(id.startsWith("tmm_monk:"), "Only Monk-specific native nodes belong here");
            assertFalse(id.toLowerCase().contains("creative_flight"), "Creative flight is supplied by Curios, not Monk");
            assertFalse(id.toLowerCase().contains("unbound"), "Apotheosis Unbound is intentionally excluded");
            int rank = node.get("earliest_rank_design").getAsInt();
            assertTrue(rank >= 1 && rank <= 20, "Out of 1-20 design range: " + id);
            String state = node.get("implementation").getAsString();
            assertTrue(state.equals("live") || state.equals("blueprint_only"));
            if (state.equals("live")) live.add(id);
            assertTrue(node.get("price").getAsInt() >= 0);
            assertEquals("native_monk_skill_point", node.get("currency").getAsString());
        }
        assertEquals(LIVE_SKILLS, live, "No unimplemented nodes become paid before tests");
        for (JsonObject node : byId.values()) {
            String id = node.get("id").getAsString();
            if (node.get("parent").isJsonNull()) {
                assertEquals("tmm_monk:monk_root", id, "Only one canonical root");
                continue;
            }
            String parent = node.get("parent").getAsString();
            assertTrue(byId.containsKey(parent), "Unknown parent " + parent + " on " + id);
            assertTrue(node.get("earliest_rank_design").getAsInt()
                    >= byId.get(parent).get("earliest_rank_design").getAsInt(), "Child before parent: " + id);
            Set<String> visited = new HashSet<>();
            String cursor = id;
            while (cursor != null) {
                assertTrue(visited.add(cursor), "Cycle in Monk native plan at " + cursor);
                JsonObject cursorNode = byId.get(cursor);
                assertNotNull(cursorNode, "Missing ancestor " + cursor);
                cursor = cursorNode.get("parent").isJsonNull()
                        ? null : cursorNode.get("parent").getAsString();
            }
        }
    }

    @Test void nativeSourceInventoryHasCompleteNonduplicatingCoverage() throws Exception {
        JsonObject blueprint = read(PLAN);
        JsonObject catalog = read(CATALOG);
        Set<String> allSourceIds = new HashSet<>();
        Set<String> uniqueAffixes = new HashSet<>();
        Set<String> nativeSources = new HashSet<>();
        Set<String> monkTyzLicenses = new HashSet<>();
        for (var el : catalog.getAsJsonArray("source_records")) {
            JsonObject source = el.getAsJsonObject();
            String sourceId = source.get("source_id").getAsString();
            allSourceIds.add(sourceId);
            switch (source.get("disposition").getAsString()) {
                case "native_Monk_faction_unlock" -> nativeSources.add(sourceId);
                case "Monk_licensed_Tyz_ranks" -> monkTyzLicenses.add(sourceId);
                case "unique_one_time_Monk_affix" -> uniqueAffixes.add(sourceId);
            }
        }
        Set<String> covered = new HashSet<>();
        Map<String, Integer> affixUse = new HashMap<>();
        for (var el : blueprint.getAsJsonArray("nodes")) {
            JsonObject node = el.getAsJsonObject();
            if (node.get("source_record").isJsonNull()) continue;
            String source = node.get("source_record").getAsString();
            assertTrue(allSourceIds.contains(source), "Not in approved catalog: " + source);
            covered.add(source);
            if (uniqueAffixes.contains(source)) affixUse.merge(source, 1, Integer::sum);
            if (monkTyzLicenses.contains(source)) {
                assertEquals("tyz_license_only", node.get("backend_role").getAsString());
            }
        }
        assertTrue(covered.containsAll(nativeSources), "Every native Monk source must be represented");
        assertTrue(covered.containsAll(monkTyzLicenses), "Every Monk-only Tyz license must be represented");
        assertTrue(covered.containsAll(uniqueAffixes), "All 15 one-time affix techniques need a place");
        assertEquals(15, uniqueAffixes.size(), "Changes to affix source inventory require review");
        for (String source : uniqueAffixes) assertEquals(1, affixUse.getOrDefault(source, 0));
    }

    @Test void liveConfiguredTreeContainsOnlyWorkingPilotSkills() throws Exception {
        JsonObject blueprint = read(PLAN);
        Set<String> planned = new HashSet<>();
        for (var el : blueprint.getAsJsonArray("nodes")) {
            JsonObject node = el.getAsJsonObject();
            if (node.get("implementation").getAsString().equals("blueprint_only")) {
                planned.add(node.get("id").getAsString());
            }
        }
        assertEquals(44, planned.size());
        JsonObject root = read(LIVE.resolve("configured_skill_tree/monk_level.json"));
        Set<String> actual = new HashSet<>();
        traverse(root, actual);
        assertEquals(LIVE_SKILLS, actual,
                "Do not activate future nodes while initial GUI/purchase behavior is unverified");
        assertTrue(actual.stream().noneMatch(planned::contains));
    }

    private static void traverse(JsonObject node, Set<String> out) throws Exception {
        String nodeId = node.get("node").getAsString();
        assertTrue(nodeId.startsWith("tmm_monk:monk/"));
        String name = nodeId.substring("tmm_monk:monk/".length());
        JsonObject file = read(LIVE.resolve("skill_nodes/monk/" + name + ".json"));
        for (var skill : file.getAsJsonArray("skills")) {
            String skillId = skill.getAsString();
            assertTrue(out.add(skillId), "Repeated live skill: " + skillId);
        }
        if (node.has("children")) {
            for (var child : node.getAsJsonArray("children")) traverse(child.getAsJsonObject(), out);
        }
    }

    @Test void worldGatesRemainExplicitlyStaged() throws Exception {
        JsonObject blueprint = read(PLAN);
        Map<String, JsonObject> nodes = new HashMap<>();
        for (var el : blueprint.getAsJsonArray("nodes")) {
            var node = el.getAsJsonObject();
            nodes.put(node.get("id").getAsString(), node);
        }
        var glide = nodes.get("tmm_monk:momentum_gliding");
        assertEquals("blueprint_only", glide.get("implementation").getAsString());
        assertEquals("personal_or_credited_ender_dragon_defeat_verified_server_side",
                glide.getAsJsonArray("requirements").get(0).getAsString());
        for (String capstone : Set.of("tmm_monk:empty_hand_paragon", "tmm_monk:stillness_paragon")) {
            var node = nodes.get(capstone);
            assertEquals(20, node.get("earliest_rank_design").getAsInt());
            assertEquals("blueprint_only", node.get("implementation").getAsString());
            assertEquals("verified_chaos_guardian_milestone",
                    node.getAsJsonArray("requirements").get(0).getAsString());
        }
    }
}
