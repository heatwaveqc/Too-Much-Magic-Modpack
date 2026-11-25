package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.FoodLiveRecipeValidator;
import io.github.heatwaveqc.tyr.progression.data.FoodRecipeEvidence;
import io.github.heatwaveqc.tyr.progression.data.FoodRecipeEvaluator;
import io.github.heatwaveqc.tyr.progression.data.FoodSourceEvidence;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRules;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Snapshot-level tests: no Minecraft world or mod JAR required. */
class FoodLiveRecipeValidatorTest {
    private static FoodRecipeEvidence recipe() {
        return FoodRecipeEvidence.parse(JsonParser.parseString("""
            {"schema":1,"item":"example:meal","complete":true,"identity":"ordinary",
             "routes":[
              {"receipt":"example:shapeless","method":"nonthermal","result_count":2,
               "ingredients":[{"item":"example:carrot","count":1}]},
              {"receipt":"example:shapeless","method":"nonthermal","result_count":2,
               "ingredients":[{"item":"example:fish","count":1}]},
              {"receipt":"example:oven","method":"heat","result_count":2,
               "ingredients":[{"item":"example:carrot","count":1}]}]}
            """).getAsJsonObject());
    }
    private static FoodRecipeEvidence.Ingredient in(String item) {
        return new FoodRecipeEvidence.Ingredient(item, 1, false);
    }
    private static FoodLiveRecipeValidator.Observed originalTag() {
        return new FoodLiveRecipeValidator.Observed("example:shapeless","example:meal",2,
            FoodRecipeEvidence.Method.NONTHERMAL,
            List.of(List.of(in("example:carrot")), List.of(in("example:fish"))),true);
    }
    private static FoodLiveRecipeValidator.Observed originalOven() {
        return new FoodLiveRecipeValidator.Observed("example:oven","example:meal",2,
            FoodRecipeEvidence.Method.HEAT,List.of(List.of(in("example:carrot"))),true);
    }
    private static FoodLiveRecipeValidator.Validation check(
            FoodLiveRecipeValidator.Observed... actual) {
        return FoodLiveRecipeValidator.validate(Map.of("example:meal",recipe()),List.of(actual));
    }

    @Test void exactEveryActiveRecipeAndTagAlternativeAccepted() {
        var result=check(originalTag(),originalOven());
        assertEquals(Set.of("example:meal"),result.approved());
        assertTrue(result.blocked().isEmpty());
    }

    @Test void cannotTrustCompleteWhenAlternativeChanges() {
        var newAlternative = new FoodLiveRecipeValidator.Observed(
            "example:shapeless","example:meal",2,FoodRecipeEvidence.Method.NONTHERMAL,
            List.of(List.of(in("example:carrot")), List.of(in("example:fish")),
                List.of(in("example:beef"))),true);
        assertEquals(Set.of("example:meal"),
            check(newAlternative,originalOven()).blocked());
        assertEquals("active_routes_differ_from_evidence",
            check(newAlternative,originalOven()).reasons().get("example:meal"));
    }

    @Test void missingExtraOrRetargetedRecipeBlocksFood() {
        assertEquals(Set.of("example:meal"),check(originalTag()).blocked());
        var extra = new FoodLiveRecipeValidator.Observed(
            "newpack:third","example:meal",2,FoodRecipeEvidence.Method.HEAT,
            List.of(List.of(in("example:carrot"))),true);
        assertEquals(Set.of("example:meal"),
            check(originalTag(),originalOven(),extra).blocked());
        var renamed = new FoodLiveRecipeValidator.Observed(
            "example:oven","example:other",2,FoodRecipeEvidence.Method.HEAT,
            List.of(List.of(in("example:carrot"))),true);
        assertEquals(Set.of("example:meal"),
            check(originalTag(),renamed).blocked());
        assertEquals(Set.of("example:meal"),
            check(originalTag(),FoodLiveRecipeValidator.Observed.unsupported(
                "example:oven",null)).blocked());
    }

    @Test void countProcessingAndIngredientMutationBlocksFood() {
        var wrongCount = new FoodLiveRecipeValidator.Observed(
            "example:oven","example:meal",1,FoodRecipeEvidence.Method.HEAT,
            List.of(List.of(in("example:carrot"))),true);
        assertEquals(Set.of("example:meal"),check(originalTag(),wrongCount).blocked());
        var wrongMethod = new FoodLiveRecipeValidator.Observed(
            "example:oven","example:meal",2,FoodRecipeEvidence.Method.NONTHERMAL,
            List.of(List.of(in("example:carrot"))),true);
        assertEquals(Set.of("example:meal"),check(originalTag(),wrongMethod).blocked());
        var unsupported = FoodLiveRecipeValidator.Observed.unsupported("example:oven","example:meal");
        assertEquals(Set.of("example:meal"),check(originalTag(),unsupported).blocked());
    }

    @Test void rejectedRecipeCannotFallThroughToRootOrParentRecipe() {
        FoodPolicyRules rules;
        try (var in = getClass().getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_policies/default.json")) {
            assertNotNull(in);
            rules=FoodPolicyRules.parse(JsonParser.parseReader(
                new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject());
        } catch (java.io.IOException bad) { throw new AssertionError(bad); }
        FoodSourceEvidence root = FoodSourceEvidence.parse(JsonParser.parseString("""
            {"schema":1,"item":"example:meal","provenance":"example:source",
             "source_units":1,
             "inferred_tags":["#c:foods/vegetable","#tmm:foods/raw"]}
            """).getAsJsonObject(),rules.profiles().keySet());
        var calculator = new FoodRecipeEvaluator(rules,
            Map.of("example:meal",root),Map.of(),
            item -> Set.of(),Set.of("example:meal"));
        assertTrue(calculator.evaluate("forest_dragon","example:meal").isEmpty());
    }
}
