package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRules;
import io.github.heatwaveqc.tyr.progression.data.FoodRecipeEvidence;
import io.github.heatwaveqc.tyr.progression.data.FoodRecipeEvaluator;
import io.github.heatwaveqc.tyr.progression.data.FoodSourceEvidence;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure, source-only regression; no client, item NBT or gameplay acceptance. */
class FoodRecipeEvaluatorTest {
    private static FoodPolicyRules rules() {
        try (var in = FoodRecipeEvaluatorTest.class.getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_policies/default.json")) {
            assertNotNull(in);
            return FoodPolicyRules.parse(JsonParser.parseReader(
                new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject());
        } catch (java.io.IOException e) { throw new AssertionError(e); }
    }

    private static FoodSourceEvidence root(String item, String... tags) {
        String tagJson = java.util.Arrays.stream(tags)
            .map(t -> "\"" + t + "\"").collect(java.util.stream.Collectors.joining(","));
        return FoodSourceEvidence.parse(JsonParser.parseString("""
            {"schema":1,"item":"%s","provenance":"example:verified_root",
             "source_units":1,"inferred_tags":[%s]}
            """.formatted(item, tagJson)).getAsJsonObject(), rules().profiles().keySet());
    }

    private static FoodRecipeEvidence recipe(String raw) {
        return FoodRecipeEvidence.parse(JsonParser.parseString(raw).getAsJsonObject());
    }

    private static Map<String, FoodSourceEvidence> roots() {
        var roots = new HashMap<String, FoodSourceEvidence>();
        var beef = root("minecraft:beef", "#c:foods/raw_meat", "#tmm:foods/raw");
        var cod = root("minecraft:cod", "#c:foods/raw_fish", "#tmm:foods/raw");
        var carrot = root("minecraft:carrot", "#c:foods/vegetable", "#tmm:foods/raw");
        var orange = root("pamhc2trees:orangeitem", "#c:foods/fruit", "#tmm:foods/raw");
        var seaPlant = root("minecraft:seagrass", "#tmm:foods/sea_plants", "#tmm:foods/raw");
        var rice = root("example:rice", "#tmm:foods/grains", "#tmm:foods/raw");
        var fillet = root("aquaculture:fish_fillet_raw",
            "#c:foods/raw_fish", "#tmm:foods/raw");
        for (var v : new FoodSourceEvidence[]{beef,cod,carrot,orange,seaPlant,rice,fillet})
            roots.put(v.item(),v);
        return roots;
    }

    private static FoodRecipeEvaluator evaluator(Map<String, FoodSourceEvidence> roots,
            FoodRecipeEvidence... recipes) {
        var routes = new HashMap<String,FoodRecipeEvidence>();
        for (FoodRecipeEvidence recipe : recipes) routes.put(recipe.item(), recipe);
        return new FoodRecipeEvaluator(rules(), roots, routes, item -> Set.of());
    }

    private static void budget(FoodPolicyRules.Nutrition n,int hunger,double sat) {
        assertEquals(hunger,n.hunger());
        assertEquals(sat,n.theoreticalSaturationPoints(),0.00001);
        assertEquals(sat,2*n.hunger()*n.saturationModifier(),0.00001);
    }

    @Test void basicDietProfilesAndUnalteredHumans() {
        var calculator=evaluator(roots());
        budget(calculator.evaluate("cave_dragon","minecraft:beef").orElseThrow(),6,24);
        budget(calculator.evaluate("sea_dragon","minecraft:cod").orElseThrow(),6,24);
        budget(calculator.evaluate("forest_dragon","minecraft:carrot").orElseThrow(),6,24);
        assertTrue(calculator.evaluate("normie","minecraft:beef").isEmpty(),
            "No native-player nutrition rewrite; only configured physiological profiles");
    }

    @Test void compatibleJuiceConservesBothOrangesAndToolIsNeutral() {
        var juice=recipe("""
            {"schema":1,"item":"example:orange_juice","complete":true,
             "identity":"ordinary","routes":[
               {"receipt":"example:two_oranges_and_juicer","method":"nonthermal",
                "result_count":1,"ingredients":[
                  {"item":"pamhc2trees:orangeitem","count":2},
                  {"item":"example:juicer","count":1,"role":"neutral"}]}]}
            """);
        var calculator=evaluator(roots(),juice);
        budget(calculator.evaluate("forest_dragon",juice.item()).orElseThrow(),12,48);
        // The recipe does not become a nutritious processing loophole for Sea.
        assertTrue(calculator.evaluate("sea_dragon",juice.item()).orElseThrow().hunger() <= 2);
    }

    @Test void seaSushiSupportsCompatibleIngredientsAndMixedDishCeiling() {
        var plantSushi=recipe("""
            {"schema":1,"item":"example:seagrass_sushi","complete":true,
             "identity":"ordinary","routes":[
               {"receipt":"example:sushi_fish_seagrass","method":"nonthermal",
                "result_count":1,"ingredients":[
                  {"item":"minecraft:cod","count":1},
                  {"item":"minecraft:seagrass","count":1}]}]}
            """);
        var riceSushi=recipe("""
            {"schema":1,"item":"example:rice_sushi","complete":true,
             "identity":"ordinary","routes":[
               {"receipt":"example:sushi_fish_rice","method":"nonthermal",
                "result_count":1,"ingredients":[
                  {"item":"minecraft:cod","count":1},
                  {"item":"example:rice","count":1}]}]}
            """);
        var calculator=evaluator(roots(),plantSushi,riceSushi);
        budget(calculator.evaluate("sea_dragon",plantSushi.item()).orElseThrow(),12,48);
        budget(calculator.evaluate("sea_dragon",riceSushi.item()).orElseThrow(),3,12);
    }

    @Test void sameItemUsesWorstDietaryRouteAndNeverTracksCraftingHistory() {
        var sushi=recipe("""
            {"schema":1,"item":"example:sushi","complete":true,"identity":"ordinary",
             "routes":[
               {"receipt":"example:sea_sushi","method":"nonthermal","result_count":1,
                "ingredients":[{"item":"minecraft:cod","count":1},
                               {"item":"minecraft:seagrass","count":1}]},
               {"receipt":"example:rice_sushi","method":"nonthermal","result_count":1,
                "ingredients":[{"item":"minecraft:cod","count":1},
                               {"item":"example:rice","count":1}]}]}
            """);
        budget(evaluator(roots(),sushi).evaluate("sea_dragon","example:sushi").orElseThrow(),3,12);
        assertEquals(2,sushi.routes().size());
        assertEquals("example:sushi",sushi.item()); // no per-craft NBT variant
    }

    @Test void meatballsRemainPoorForAllDragonsEvenWhenFishIsIngredient() {
        var meatballs=recipe("""
            {"schema":1,"item":"example:meatballs","complete":true,"identity":"unfavored_all",
             "routes":[
               {"receipt":"example:beef_meatballs","method":"nonthermal","result_count":1,
                "ingredients":[{"item":"minecraft:beef","count":1}]},
               {"receipt":"example:fish_meatballs","method":"nonthermal","result_count":1,
                "ingredients":[{"item":"minecraft:cod","count":1}]}]}
            """);
        var calculator=evaluator(roots(),meatballs);
        for (String profile : Set.of("cave_dragon","sea_dragon","forest_dragon")) {
            var outcome=calculator.evaluate(profile,meatballs.item()).orElseThrow();
            assertTrue(outcome.hunger()<=3,profile+" must not upgrade fish meatballs");
            assertTrue(outcome.theoreticalSaturationPoints()<=12,profile);
        }
    }

    @Test void cookingAndCharringHalveOnEachStepBeforeRounding() {
        var steak=recipe("""
            {"schema":1,"item":"example:steak","complete":true,"identity":"ordinary",
             "routes":[{"receipt":"example:beef_smelting","method":"heat","result_count":1,
             "ingredients":[{"item":"minecraft:beef","count":1}]}]}
            """);
        var charred=recipe("""
            {"schema":1,"item":"example:charred_meat","complete":true,"identity":"ordinary",
             "routes":[{"receipt":"example:steak_charring","method":"char","result_count":1,
             "ingredients":[{"item":"example:steak","count":1}]}]}
            """);
        var calculator=evaluator(roots(),steak,charred);
        budget(calculator.evaluate("cave_dragon",steak.item()).orElseThrow(),3,12);
        budget(calculator.evaluate("cave_dragon",charred.item()).orElseThrow(),2,6);
        assertEquals(1.5f,calculator.evaluate("cave_dragon",charred.item())
            .orElseThrow().saturationModifier());
    }

    @Test void cuttingRespectsNativeYieldAndEfficiencies() {
        var codSlice=recipe("""
            {"schema":1,"item":"example:cod_slice","complete":true,"identity":"ordinary",
             "routes":[{"receipt":"example:cod_cutting","method":"nonthermal","result_count":2,
             "ingredients":[{"item":"minecraft:cod","count":1}]}]}
            """);
        var mince=recipe("""
            {"schema":1,"item":"example:minced_beef","complete":true,"identity":"ordinary",
             "routes":[{"receipt":"example:beef_cutting","method":"nonthermal","result_count":2,
             "efficiency":1.3333333333333333,
             "ingredients":[{"item":"minecraft:beef","count":1}]}]}
            """);
        var calculator=evaluator(roots(),codSlice,mince);
        budget(calculator.evaluate("sea_dragon",codSlice.item()).orElseThrow(),3,12);
        budget(calculator.evaluate("cave_dragon",mince.item()).orElseThrow(),4,16);
    }

    @Test void installedAquacultureSushiUsesRealVerifiedFishAndSeagrassRecipe() {
        try (var in = FoodRecipeEvaluatorTest.class.getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_recipe_routes/aquaculture/sushi.json")) {
            assertNotNull(in);
            var sushi = FoodRecipeEvidence.parse(JsonParser.parseReader(
                new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject());
            assertEquals("aquaculture:sushi",sushi.item());
            budget(evaluator(roots(),sushi).evaluate("sea_dragon",sushi.item()).orElseThrow(),12,48);
        } catch (java.io.IOException bad) { throw new AssertionError(bad); }
    }

    @Test void brokenAlternativesCyclesAndInvalidSchemasFailClosed() {
        var unresolved=recipe("""
            {"schema":1,"item":"example:food","complete":true,"identity":"ordinary",
             "routes":[
                {"receipt":"example:good","method":"nonthermal","result_count":1,
                 "ingredients":[{"item":"minecraft:cod","count":1}]},
                {"receipt":"example:unknown","method":"nonthermal","result_count":1,
                 "ingredients":[{"item":"example:unknown_root","count":1}]}]}
            """);
        assertTrue(evaluator(roots(),unresolved).evaluate("sea_dragon",unresolved.item()).isEmpty());
        var cycle=recipe("""
            {"schema":1,"item":"example:cycle","complete":true,"identity":"ordinary",
             "routes":[{"receipt":"example:self_cycle","method":"nonthermal",
             "result_count":1,"ingredients":[{"item":"example:cycle","count":1}]}]}
            """);
        assertTrue(evaluator(roots(),cycle).evaluate("sea_dragon",cycle.item()).isEmpty());
        assertThrows(IllegalArgumentException.class,()->recipe("""
            {"schema":1,"item":"example:unsafe","complete":false,"identity":"ordinary",
             "routes":[{"receipt":"example:a","method":"nonthermal","result_count":1,
             "ingredients":[{"item":"minecraft:cod","count":1}]}]}
            """));
        assertThrows(IllegalArgumentException.class,()->recipe("""
            {"schema":1,"item":"example:unsafe","complete":true,"identity":"ordinary",
             "routes":[{"receipt":"example:a","method":"heat","result_count":1,
             "efficiency":2,"ingredients":[{"item":"minecraft:cod","count":1}]}]}
            """));
    }

    @Test void shippedCutAndCookedRouteEvidenceHasExpectedStructure() {
        for (String item : new String[]{"farmersdelight/cod_slice","farmersdelight/salmon_slice",
            "farmersdelight/cabbage_leaf","farmersdelight/minced_beef",
            "minecraft/cooked_beef","minecraft/cooked_cod","aquaculture/sushi"}) {
            try (var in = FoodRecipeEvaluatorTest.class.getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_recipe_routes/" + item + ".json")) {
                assertNotNull(in,item);
                var doc=FoodRecipeEvidence.parse(JsonParser.parseReader(
                    new InputStreamReader(in,StandardCharsets.UTF_8)).getAsJsonObject());
                assertEquals(item.replaceFirst("/",":"),doc.item());
                assertEquals(1,doc.routes().size());
            } catch (java.io.IOException error) { throw new AssertionError(error); }
        }
    }
}
