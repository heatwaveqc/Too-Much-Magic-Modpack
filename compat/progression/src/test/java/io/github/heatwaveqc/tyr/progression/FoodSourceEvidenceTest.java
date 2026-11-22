package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRules;
import io.github.heatwaveqc.tyr.progression.data.FoodSourceEvidence;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Offline producer contract: all per-food facts are generated evidence, not rules. */
class FoodSourceEvidenceTest {
    private static FoodPolicyRules rules() {
        try (var stream = FoodSourceEvidenceTest.class.getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_policies/default.json")) {
            assertNotNull(stream);
            return FoodPolicyRules.parse(JsonParser.parseReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject());
        } catch (java.io.IOException bad) { throw new AssertionError(bad); }
    }

    private static FoodSourceEvidence evidence(String json) {
        return FoodSourceEvidence.parse(JsonParser.parseString(json).getAsJsonObject(),
            rules().profiles().keySet());
    }

    @Test void inferredVirtualTagsMakeVerifiedHalfFishSliceSourced() {
        var source = evidence("""
            {
              "schema": 1, "item": "example:raw_cod_slice",
              "provenance": "example:cutting/raw_cod",
              "source_units": 0.5,
              "inferred_tags": ["#c:foods/raw_fish", "#tmm:foods/raw"]
            }""");
        Set<String> inferred = new HashSet<>(source.inferredTags());
        inferred.add("#example:unrelated_mod_tag");
        var n = rules().evaluate("sea_dragon", inferred, source.sourceUnits(),
            source.preferredShares().get("sea_dragon")).orElseThrow();
        assertEquals(3, n.hunger());
        assertEquals(12.0, n.theoreticalSaturationPoints(), 0.00001);
        assertEquals("example:cutting/raw_cod", source.provenance());
    }

    @Test void oneDoubleIngredientJuicePreservesNonthermalSourceBudget() {
        var source = evidence("""
            {"schema":1,"item":"example:orange_juice",
             "provenance":"example:juicing/two_oranges",
             "source_units":2,"inferred_tags":["#c:foods/fruit","#tmm:foods/raw"]}
            """);
        var n = rules().evaluate("forest_dragon", source.inferredTags(),
            source.sourceUnits(), null).orElseThrow();
        assertEquals(12, n.hunger());
        assertEquals(48, n.theoreticalSaturationPoints(), 0.00001);
    }

    @Test void mixedDishUsesSourceProvidedProfileFractionNotNaiveTagIntersection() {
        var source = evidence("""
            {"schema":1,"item":"example:fish_with_vegetables",
             "provenance":"example:recipe/fish_and_veg",
             "source_units":2,
             "inferred_tags":["#c:foods/raw_fish","#c:foods/vegetable","#tmm:foods/cooked"],
             "preferred_shares":{"sea_dragon":0.5,"forest_dragon":0.5,"cave_dragon":0.0}}
            """);
        assertTrue(rules().evaluate("sea_dragon", source.inferredTags(),
            source.sourceUnits(), null).isEmpty(),
            "A mixed dish needs source-provided fractions; tags alone cannot establish ratios");
        var result = rules().evaluate("sea_dragon", source.inferredTags(),
            source.sourceUnits(), source.preferredShares().get("sea_dragon")).orElseThrow();
        assertEquals(5, result.hunger());
        assertEquals(18, result.theoreticalSaturationPoints(), 0.00001);
    }

    @Test void rejectsUnverifiedMalformedOrUnsafelyHugeEvidence() {
        for (String raw : new String[]{
            """
              {"schema":1,"item":"example:fish","source_units":1}
              """,
            """
              {"schema":1,"item":"example:fish","provenance":"example:cut","source_units":0}
              """,
            """
              {"schema":1,"item":"example:fish","provenance":"example:cut","source_units":100}
              """,
            """
              {"schema":1,"item":"example:fish","provenance":"example:cut","source_units":1,
               "preferred_shares":{"unregistered":0.5}}
              """,
            """
              {"schema":1,"item":"example:fish","provenance":"example:cut","source_units":1,
               "preferred_shares":{"sea_dragon":-0.25}}
              """,
            """
              {"schema":1,"item":"example:fish","provenance":"example:cut","source_units":1,
               "inferred_tags":["#tmm:foods/raw","#tmm:foods/raw"]}
              """,
            """
              {"schema":1,"item":"example:fish","provenance":"example:cut","source_units":1,
               "nutrition":999}
              """
        }) assertThrows(IllegalArgumentException.class, () -> evidence(raw));
    }

    @Test void evidenceCannotSmuggleNutritionOrInventMagicalEffects() {
        var source = evidence("""
            {"schema":1,"item":"example:magical_candy",
             "provenance":"example:loot/rare",
             "source_units":1,
             "inferred_tags":["#c:foods/fruit","#tmm:foods/magical"]}
            """);
        assertTrue(rules().universallyPermitted(source.inferredTags()));
        assertTrue(rules().evaluate("forest_dragon", source.inferredTags(),
            source.sourceUnits(), null).isEmpty(),
            "Magical is a permission exemption, never a general nourishment override");
    }
}
