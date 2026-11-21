package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Source-level contract for the owner-approved 3x3 shapeless Bio-Mutagen recipe. */
class BioMutagenRecipeTest {
    @Test void nineIngredientShapelessRecipeMatchesSpecification() throws Exception {
        String name = "data/tyr_progression/recipe/bio_mutagen.json";
        try (var input = BioMutagenRecipeTest.class.getClassLoader().getResourceAsStream(name)) {
            assertNotNull(input,"Missing Bio-Mutagen recipe");
            var source = JsonParser.parseReader(new java.io.InputStreamReader(input,
                java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("minecraft:crafting_shapeless",source.get("type").getAsString());
            var ingredients = source.getAsJsonArray("ingredients");
            assertEquals(9,ingredients.size());
            java.util.Map<String,Integer> amounts = new java.util.HashMap<>();
            for (var part : ingredients) {
                // A raw string is rejected by the Minecraft 1.21.1 RecipeManager codec.
                assertTrue(part.isJsonObject(), "Ingredient must be a JSON object");
                var ingredient = part.getAsJsonObject();
                assertEquals(java.util.Set.of("item"), ingredient.keySet(),
                    "Exactly one registered item key is required");
                String itemId = ingredient.get("item").getAsString();
                amounts.merge(itemId,1,Integer::sum);
            }
            assertEquals(java.util.Map.of(
                "minecraft:moss_block",4,
                "minecraft:rotten_flesh",4,
                "minecraft:spider_eye",1),amounts);
            var result = source.getAsJsonObject("result");
            assertEquals("tyr_progression:bio_mutagen",result.get("id").getAsString());
            assertEquals(1,result.get("count").getAsInt());
        }
    }
}
