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

    @Test void allFortyEightNodesHaveValidParentsRanksAndStatuses() throws Exception {
        JsonObject blueprint = read(PLAN);
        assertEquals("DESIGN_ONLY_NOT_RUNTIME_LOADED", blueprint.get("state").getAsString());
        assertEquals(20, blueprint.get("intended_max_faction_rank").getAsInt());
        JsonArray nodes = blueprint.getAsJsonArray("nodes");
        assertEquals(48, nodes.size(), "Changing the skeleton needs explicit review");
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

    @Test void nativeSourceInventoryRespectsExplicitOwnerOverrides() throws Exception {
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
        // The source audit is historical; the owner explicitly removed the old
        // independent spirit projectile in favor of Ars Nouveau and Iron's.
        Set<String> explicitRejections = new HashSet<>();
        for (var el : blueprint.getAsJsonArray("explicit_owner_overrides")) {
            var override = el.getAsJsonObject();
            if (override.get("decision").getAsString().equals("excluded")) {
                explicitRejections.add(override.get("source_record").getAsString());
            }
        }
        assertEquals(Set.of("tmm_monk:martial/spirit_projectile"), explicitRejections);
        assertTrue(nativeSources.removeAll(explicitRejections));

        Set<String> covered = new HashSet<>();
        Map<String, Integer> affixUse = new HashMap<>();
        for (var el : blueprint.getAsJsonArray("nodes")) {
            JsonObject node = el.getAsJsonObject();
            Set<String> assigned = new HashSet<>();
            if (!node.get("source_record").isJsonNull()) {
                assigned.add(node.get("source_record").getAsString());
            }
            if (node.has("source_records")) {
                for (var sr : node.getAsJsonArray("source_records")) assigned.add(sr.getAsString());
            }
            for (String source : assigned) {
                assertTrue(allSourceIds.contains(source), "Not in audited inventory: " + source);
                assertFalse(explicitRejections.contains(source), "Rejected innate projectile cannot be revived");
                covered.add(source);
                if (uniqueAffixes.contains(source)) affixUse.merge(source, 1, Integer::sum);
                if (monkTyzLicenses.contains(source)) {
                    String role = node.get("backend_role").getAsString();
                    assertTrue(role.equals("tyz_license_only")
                                    || role.equals("one_time_toggle_and_tyz_license"),
                            "Monk-only Tyz source requires a native license: " + node.get("id"));
                }
            }
        }
        assertTrue(covered.containsAll(nativeSources), "Every retained native source must be represented");
        assertTrue(covered.containsAll(monkTyzLicenses), "Every Monk-only Tyz license must be represented");
        assertTrue(covered.containsAll(uniqueAffixes), "All 15 one-time affix techniques need a place");
        assertEquals(15, uniqueAffixes.size(), "Changes to affix source inventory require review");
        for (String source : uniqueAffixes) assertEquals(1, affixUse.getOrDefault(source, 0));
    }

    @Test void spellCombatFortuneSilkAndFistSlotMatchOwnerDirection() throws Exception {
        JsonObject blueprint = read(PLAN);
        Map<String, JsonObject> byId = new HashMap<>();
        for (var el : blueprint.getAsJsonArray("nodes")) {
            var node = el.getAsJsonObject();
            byId.put(node.get("id").getAsString(), node);
        }
        assertFalse(byId.containsKey("tmm_monk:spirit_projectile"));
        assertFalse(byId.containsKey("tmm_monk:delicate_hands"));
        assertFalse(byId.containsKey("tmm_monk:fortunate_hands_license"));
        for (String id : Set.of("tmm_monk:magical_casting",
                "tmm_monk:acidic_casting", "tmm_monk:deathbound_casting")) {
            JsonObject node = byId.get(id);
            assertNotNull(node);
            assertEquals("arcane", node.get("lane").getAsString());
            assertEquals("blueprint_only", node.get("implementation").getAsString());
            assertTrue(node.get("effect_contract").getAsString().contains("Ars"));
        }

        JsonObject touch = byId.get("tmm_monk:harvest_touch");
        assertNotNull(touch);
        assertEquals("blueprint_only", touch.get("implementation").getAsString());
        assertEquals("one_time_toggle_and_tyz_license", touch.get("backend_role").getAsString());
        assertEquals(2, touch.getAsJsonArray("source_records").size());
        JsonObject toggle = touch.getAsJsonObject("toggle");
        assertEquals("fortune", toggle.get("initial_mode").getAsString());
        assertTrue(toggle.get("exclusive").getAsBoolean());
        assertEquals(2, toggle.getAsJsonArray("available_modes").size());
        assertEquals("silk_touch", toggle.getAsJsonArray("available_modes").get(1).getAsString());

        JsonObject slot = read(Path.of("design", "fist-slot-lock-contract-v1.json"));
        assertEquals("DESIGN_ONLY_NOT_RUNTIME_ENABLED", slot.get("state").getAsString());
        JsonObject position = slot.getAsJsonObject("default_slot");
        assertEquals(0, position.get("zero_based_hotbar_index").getAsInt());
        assertEquals(1, position.get("user_visible_hotbar_position").getAsInt());
        assertTrue(position.get("not_item").getAsBoolean());
        assertEquals("automatic_on_valid_monk_faction_join_no_skill_point_purchase",
                slot.get("unlock").getAsString());
        assertTrue(slot.getAsJsonArray("prelaunch_tests").size() >= 10);

        JsonObject armor = read(Path.of("design", "armor-lockout-trait-v1.json"));
        assertEquals("DESIGN_ONLY_NOT_RUNTIME_ENABLED", armor.get("state").getAsString());
        assertTrue(armor.get("default_enabled_for_new_monks").getAsBoolean());
        assertEquals(0, armor.get("native_skill_point_cost").getAsInt());
        assertEquals(4, armor.getAsJsonObject("slot_policy")
                .getAsJsonArray("armor_equipment_slots").size());
        assertEquals("tmm_monk:reserved_fist_slot", armor.get("independent_from").getAsString());

        var passiveFeatures = blueprint.getAsJsonArray("passive_features");
        assertEquals(2, passiveFeatures.size(), "Free fist and armor lock are passives, not paid nodes");
        assertEquals("tmm_monk:reserved_fist_slot",
                passiveFeatures.get(0).getAsJsonObject().get("id").getAsString());
        assertEquals(0, passiveFeatures.get(0).getAsJsonObject().get("default_hotbar_index").getAsInt());
        assertEquals("tmm_monk:armor_lock",
                passiveFeatures.get(1).getAsJsonObject().get("id").getAsString());
        assertTrue(passiveFeatures.get(1).getAsJsonObject().get("default_enabled").getAsBoolean());
        assertEquals(0, passiveFeatures.get(1).getAsJsonObject().get("native_skill_point_cost").getAsInt());
        assertEquals("design_only_not_runtime_enabled",
                passiveFeatures.get(1).getAsJsonObject().get("state").getAsString());

        String preview = Files.readString(Path.of("design", "preview",
                "monk_level.configured_tree.PREVIEW.json"));
        assertFalse(preview.contains("spirit_projectile"), "Never restore native Spirit Projectile");
        assertTrue(preview.contains("tmm_monk:monk/harvest_touch"));
        assertTrue(preview.contains("tmm_monk:monk/magical_casting"));
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
        assertEquals(42, planned.size());
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
