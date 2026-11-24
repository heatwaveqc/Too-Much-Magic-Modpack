package io.github.heatwaveqc.tyr.progression.compat.physiology;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Type-A regression: source-level one-module resources survive packaging. */
class TransformationPackagingTest {
    private InputStream resource(String path) {
        InputStream input = getClass().getClassLoader().getResourceAsStream(path);
        assertNotNull(input, "Missing combined Progression resource: " + path);
        return input;
    }

    private JsonObject json(String path) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(resource(path), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void onlyOneNeoForgeModIsDeclared() throws Exception {
        String metadata;
        try (InputStream input = resource("META-INF/neoforge.mods.toml")) {
            metadata = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
        assertTrue(metadata.contains("modId=\"tyr_progression\""));
        assertFalse(metadata.contains("modId=\"tmm_transformations\""));
        assertEquals(1, metadata.split("\\[\\[mods\\]\\]", -1).length - 1);
    }

    @Test void allNativeAndMagicMixinEntriesArePresent() throws Exception {
        JsonObject config = json("tmm_transformations.mixins.json");
        var common = config.getAsJsonArray("mixins").asList().stream()
            .map(v -> v.getAsString()).collect(Collectors.toSet());
        var client = config.getAsJsonArray("client").asList().stream()
            .map(v -> v.getAsString()).collect(Collectors.toSet());
        assertTrue(common.containsAll(java.util.Set.of(
            "NewbieVampireAcquisitionMixin", "NewbieWerewolfAcquisitionMixin",
            "NewbieDragonAcquisitionMixin", "GoetyDracolichAcquisitionMixin",
            "ForbiddenMagicDracolichAcquisitionMixin", "DragonSurvivalDeathRepeatMixin",
            "BotaniaThermalilyNoCooldownMixin", "BotaniaThermalilyFiftyEndoflamesMixin",
            "MahouCatalystExchangeMixin")));
        assertTrue(client.containsAll(java.util.Set.of(
            "DracolichModelLoadMixin", "DracolichDragonModelMixin",
            "DracolichGlowLayerMixin", "DracolichArmorLayerMixin")));
        assertEquals("io.github.heatwaveqc.tmm.transformations.mixin.DracolichMixinPlugin",
            config.get("plugin").getAsString());
    }

    @Test void allRuntimeDracolichAssetsAndMobTagsSurvive() throws Exception {
        for (String asset : java.util.List.of(
                "assets/tyr_progression/dracolich/cave_center.mesh.json",
                "assets/tyr_progression/dracolich/cave_center.rig.json",
                "assets/tyr_progression/geo/dracolich_cave_center.geo.json",
                "assets/tyr_progression/textures/entity/dracolich/cave_center.png",
                "assets/tyr_progression/textures/entity/dracolich/cave_center_glow.png")) {
            try (InputStream input = resource(asset)) {
                assertTrue(input.read() >= 0, "Empty native visual: " + asset);
            }
        }
        for (String tag : java.util.List.of("boss_exempt", "cave_dragons",
                "demons", "fae", "forest_dragons", "undead_extra")) {
            assertTrue(json("data/tyr_progression/tags/entity_type/" + tag + ".json")
                .getAsJsonArray("values").size() >= 0);
        }
    }

    @Test void faeTagUnionRetainsExistingTaxonomyAndNewMobAllianceEntries() throws Exception {
        var values = json("data/tyr_progression/tags/entity_type/fae.json")
            .getAsJsonArray("values");
        java.util.Set<String> found = new java.util.HashSet<>();
        values.forEach(v -> found.add(v.isJsonPrimitive()
            ? v.getAsString() : v.getAsJsonObject().get("id").getAsString()));
        assertTrue(found.contains("ars_nouveau:bookwyrm"), "Existing Core creature taxonomy lost");
        assertTrue(found.contains("feywild:bee_mount"), "Imported Fae ally lost");
    }
}
