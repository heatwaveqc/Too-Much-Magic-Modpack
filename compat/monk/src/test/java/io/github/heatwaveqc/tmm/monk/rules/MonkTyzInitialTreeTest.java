package io.github.heatwaveqc.tmm.monk.rules;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Validates the staged Tyz Monk layout; MUST NOT activate prior to central MXP/gates. */
class MonkTyzInitialTreeTest {
    private static final Path BASE = Path.of("tyzs-prototypes", "config", "tyzs_skills", "custom");

    private static JsonObject load(Path p) throws Exception {
        try (var reader = Files.newBufferedReader(p)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        }
    }

    @Test void globalCategoriesPreserveDefaultTabsAndAllReservedFormTabs() throws Exception {
        JsonObject cats = load(BASE.resolve("data/categories.json"));
        for (String id : Set.of("abilities", "combat", "misc", "nature", "tmm_magic_core",
                "tmm_vampire", "tmm_werewolf", "tmm_monk", "tmm_dragon", "tmm_lich")) {
            assertTrue(cats.has(id), "missing global Tyz tab: " + id);
        }
        assertEquals(10, cats.size());
    }

    @Test void onlyStagedMonkSkillsAndCorrectExclusiveAttributes() throws Exception {
        verify("tmm_monk_iron_palm_training", "tmm_monk:unarmed_training");
        verify("tmm_monk_barehand_harvest_training", "tmm_monk:hand_harvest_training");
    }

    private static void verify(String id, String attribute) throws Exception {
        JsonObject obj = load(BASE.resolve("skills/" + id + ".json"));
        assertEquals(id, obj.get("id").getAsString());
        assertFalse(obj.get("active").getAsBoolean(), "no Tyz XP/gate backend is live yet");
        assertEquals("tmm_monk", obj.get("category").getAsString());
        assertEquals("CUSTOM", obj.get("type").getAsString());
        assertEquals(10, obj.get("maximumLevel").getAsInt());
        assertEquals(10, obj.getAsJsonArray("prices").size());
        var mods = obj.getAsJsonArray("modifiers");
        assertEquals(1, mods.size());
        var mod = mods.get(0).getAsJsonObject();
        assertEquals(attribute, mod.get("attribute").getAsString());
        assertEquals(10, mod.getAsJsonArray("values").size());
    }
}
