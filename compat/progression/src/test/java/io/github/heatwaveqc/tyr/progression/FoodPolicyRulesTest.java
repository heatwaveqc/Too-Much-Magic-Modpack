package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRules;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FoodPolicyRulesTest {
    private static FoodPolicyRules rules() {
        try (var stream = FoodPolicyRulesTest.class.getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_policies/default.json")) {
            assertNotNull(stream, "The configurable default policy must ship in the module");
            return FoodPolicyRules.parse(JsonParser.parseReader(new InputStreamReader(
                stream, StandardCharsets.UTF_8)).getAsJsonObject());
        } catch (java.io.IOException e) { throw new AssertionError(e); }
    }
    private static Set<String> cave(String stage) {
        return Set.of("#c:foods/raw_meat", "#tmm:foods/" + stage);
    }
    private static void check(FoodPolicyRules.Nutrition n, int hunger, double saturationPoints) {
        assertEquals(hunger, n.hunger());
        assertEquals(saturationPoints, n.theoreticalSaturationPoints(), 0.00001);
        assertEquals(saturationPoints, 2 * hunger * n.saturationModifier(), 0.00001);
    }
    @Test void rawCookedCharredHalveHungerAndActualSaturationPoints() {
        var r = rules();
        check(r.evaluate("cave_dragon", cave("raw"), 1, null).orElseThrow(), 6, 24);
        check(r.evaluate("cave_dragon", cave("cooked"), 1, null).orElseThrow(), 3, 12);
        // 1.5 theoretical hunger rounds to 2; saturation remains exactly 6 points.
        check(r.evaluate("cave_dragon", cave("charred"), 1, null).orElseThrow(), 2, 6);
        assertEquals(1.5f,r.evaluate("cave_dragon",cave("charred"),1,null).orElseThrow().saturationModifier());
    }
    @Test void sourcePortionsAndColdCombinationKeepRecipeBudget() {
        var r=rules();
        check(r.evaluate("sea_dragon",Set.of("#c:foods/raw_fish"),0.5,null).orElseThrow(),3,12);
        check(r.evaluate("cave_dragon",cave("raw"),2.0/3,null).orElseThrow(),4,16);
        check(r.evaluate("forest_dragon",Set.of("#c:foods/fruit"),2,null).orElseThrow(),12,48);
        assertTrue(r.evaluate("sea_dragon",Set.of("#c:foods/raw_fish"),1,null).isPresent(),
            "Nether fish analogues must not be rejected by name or dimension");
    }
    @Test void offDietAndMixedFractionsAreConfigurable() {
        var r=rules();
        check(r.evaluate("sea_dragon",Set.of("#c:foods/fruit"),1,null).orElseThrow(),3,12);
        check(r.evaluate("sea_dragon",Set.of("#c:foods/fruit","#c:foods/raw_fish"),2,0.5).orElseThrow(),9,36);
        assertTrue(r.evaluate("sea_dragon",Set.of("#c:foods/fruit","#c:foods/raw_fish"),2,null).isEmpty(),
            "Ambiguous dish without provenance must keep native outcome");
        check(r.evaluate("sea_dragon",Set.of("#c:foods/fruit","#c:foods/raw_fish","#tmm:foods/cooked"),2,0.5)
            .orElseThrow(),5,18);
    }
    @Test void universalMagicalIsPermissionOnlyAndPreservesNativeFoodSideEffects() {
        var r=rules();var tags=Set.of("#tmm:foods/magical","#c:foods/fruit");
        assertTrue(r.universallyPermitted(tags));
        assertTrue(r.evaluate("sea_dragon",tags,1,null).isEmpty(),
            "The magical exemption must never create an extra food payment");
        assertFalse(r.universallyPermitted(Set.of("#c:foods/golden")));
    }
    @Test void unknownTagsAndProfilesDoNotGenerateNutrition() {
        var r=rules();
        assertTrue(r.evaluate("unknown",cave("raw"),1,null).isEmpty());
        assertTrue(r.evaluate("cave_dragon",Set.of("#tmm:foods/raw"),1,null).isEmpty());
        assertTrue(r.evaluate("cave_dragon",cave("raw"),-1,null).isEmpty());
        assertTrue(r.evaluate("cave_dragon",cave("raw"),1,1.2).isEmpty());
    }
    @Test void threeStagesOnlyAndStrictSchema() {
        var root=JsonParser.parseString("""
          {"schema":1,"preferred_raw":{"nutrition":6,"saturation_modifier":2},
           "stage_multipliers":{"raw":1,"cooked":0.5,"charred":0.25,"heavily_cooked":0.1},
           "mixed":{"off_diet_contribution":0.5},
           "family_tags":{"plant":["#c:foods/fruit"]},"profiles":{"forest_dragon":["plant"]},
           "preparation_tags":{"raw":["#c:foods/fruit"],"cooked":["#tmm:foods/cooked"],"charred":["#tmm:foods/charred"]},
           "magical_tags":["#tmm:foods/magical"]}
          """).getAsJsonObject();
        assertThrows(IllegalArgumentException.class,()->FoodPolicyRules.parse(root));
        root.getAsJsonObject("stage_multipliers").remove("heavily_cooked");
        root.getAsJsonObject("mixed").addProperty("off_diet_contribution",0.25);
        root.getAsJsonObject("stage_multipliers").addProperty("cooked",0.75);
        var adjustable=FoodPolicyRules.parse(root);
        check(adjustable.evaluate("forest_dragon",Set.of("#c:foods/fruit","#tmm:foods/cooked"),1,null)
            .orElseThrow(),5,18);
        check(adjustable.evaluate("forest_dragon",Set.of("#c:foods/fruit"),1,0).orElseThrow(),2,6);
    }
}
