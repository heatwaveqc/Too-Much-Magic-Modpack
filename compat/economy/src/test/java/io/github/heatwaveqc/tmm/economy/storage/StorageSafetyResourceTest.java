package io.github.heatwaveqc.tmm.economy.storage;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Lightweight CI contract for tag bridges without a launched Minecraft world.
 * Checks our packaged resources; actual registry binding and Mixin application
 * require separate exact-JAR and game checks.
 */
class StorageSafetyResourceTest {
    private static String resource(String path) throws IOException {
        try (InputStream in = StorageSafetyResourceTest.class.getResourceAsStream(path)) {
            assertNotNull(in, "Economy JAR must package " + path);
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void unsafeSourcesAreCoveredByThePackagedTag() throws IOException {
        String risk = resource("/data/tmm/tags/item/non_nestable_embedded_containers.json");
        for (String id : List.of(
                "minecraft:bundle", "minecraft:shulker_box",
                "ars_elemental:curio_bag", "ars_elemental:caster_bag",
                "occultism:satchel", "occultism:ritual_satchel_t1",
                "occultism:ritual_satchel_t2",
                "malum:soulwoven_pouch", "malum:ravenous_pouch",
                "gaze:spirit_channel_pouch", "extrabotany:mini_handbag")) {
            assertTrue(risk.contains('"' + id + '"'), "Missing embedded-risk source: " + id);
        }
        assertFalse(risk.contains("projecte:"), "Reference-backed ProjectE keys are not recursive payloads");
        assertFalse(risk.contains("occultism:ender_satchel"),
            "Reference-backed Ender Satchel is not a recursive payload");
    }

    @Test
    void arsNativeBlacklistOnlyIncludesTheNarrowRiskTag() throws IOException {
        String ars = resource("/data/ars_elemental/tags/item/blacklist_bag_item.json");
        assertTrue(ars.contains("#tmm:non_nestable_embedded_containers"));
        assertFalse(ars.contains("#tmm:portable_bags"),
            "Legacy broad bridge must not be reintroduced in Economy resources");
    }

    @Test
    void sourceReferenceClassificationIsDistinct() throws IOException {
        String reference = resource("/data/tmm/tags/item/reference_backed_storage.json");
        assertTrue(reference.contains("occultism:ender_satchel"));
        for (String color : List.of("white", "orange", "yellow", "black")) {
            assertTrue(reference.contains("projecte:" + color + "_alchemical_bag"));
        }
        assertFalse(reference.contains("extrabotany:mini_handbag"));
        assertFalse(reference.contains("malum:soulwoven_pouch"));
    }
}
