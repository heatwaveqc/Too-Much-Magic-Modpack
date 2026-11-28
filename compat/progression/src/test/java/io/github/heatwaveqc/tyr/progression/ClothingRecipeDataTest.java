package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClothingRecipeDataTest {
    private JsonObject resource(String path) throws Exception {
        try (var stream = getClass().getResourceAsStream("/data/tyr_progression/" + path)) {
            assertNotNull(stream, path);
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }
    @Test void whiteCapHasFourWhiteWoolAndNativeMirroring() throws Exception {
        var recipe = resource("recipe/white_cap.json");
        assertEquals("minecraft:crafting_shaped", recipe.get("type").getAsString());
        var pattern = recipe.getAsJsonArray("pattern");
        assertEquals(2, pattern.size());
        assertEquals("WWW", pattern.get(0).getAsString());
        assertEquals("W  ", pattern.get(1).getAsString());
        assertEquals("minecraft:white_wool", recipe.getAsJsonObject("key").getAsJsonObject("W").get("item").getAsString());
        assertEquals("tyr_progression:white_cap", recipe.getAsJsonObject("result").get("id").getAsString());
    }
    @Test void loinclothUsesOnlyTwoStringAndOneLeather() throws Exception {
        var recipe = resource("recipe/loincloth.json");
        assertEquals("minecraft:crafting_shapeless", recipe.get("type").getAsString());
        var ingredients = recipe.getAsJsonArray("ingredients");
        assertEquals(3, ingredients.size());
        int string = 0, leather = 0;
        for (var ingredient : ingredients) {
            switch (ingredient.getAsJsonObject().get("item").getAsString()) {
                case "minecraft:string" -> string++;
                case "minecraft:leather" -> leather++;
                default -> fail("Unexpected loincloth ingredient");
            }
        }
        assertEquals(2, string);
        assertEquals(1, leather);
    }
    @Test void garmentsAllowlistedAndKiltUsesDynamicSerializer() throws Exception {
        Set<String> values = new HashSet<>();
        resource("tags/item/non_protective_clothing.json").getAsJsonArray("values")
                .forEach(e -> values.add(e.getAsString()));
        assertEquals(Set.of("tyr_progression:tartan_kilt", "tyr_progression:white_cap", "tyr_progression:loincloth"), values);
        assertEquals("tyr_progression:tartan_kilt", resource("recipe/tartan_kilt.json").get("type").getAsString());
    }
}
