package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.FoodDecisionResolver;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRules;
import io.github.heatwaveqc.tyr.progression.data.FoodSourceEvidence;
import io.github.heatwaveqc.tyr.progression.data.StrictDietPolicy;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Pure data/config-level hybrid policy tests. No installed food census required. */
class FoodDecisionResolverTest {
    private FoodPolicyRules rules() {
        try (var in = getClass().getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_policies/default.json")) {
            assertNotNull(in);
            return FoodPolicyRules.parse(JsonParser.parseReader(
                new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject());
        } catch (java.io.IOException failure) { throw new AssertionError(failure); }
    }

    private StrictDietPolicy dryad() {
        try (var in = getClass().getResourceAsStream(
                "/data/tyr_progression/tyr_progression/food_constraints/dryad.json")) {
            assertNotNull(in);
            return StrictDietPolicy.parse(JsonParser.parseReader(
                new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject());
        } catch (java.io.IOException failure) { throw new AssertionError(failure); }
    }

    private FoodSourceEvidence source(String item, String... tags) {
        var list = java.util.Arrays.stream(tags)
            .map(tag -> "\"" + tag + "\"")
            .collect(java.util.stream.Collectors.joining(","));
        return FoodSourceEvidence.parse(JsonParser.parseString("""
            {"schema":1,"item":"%s","provenance":"example:root",
             "source_units":1,"inferred_tags":[%s]}
            """.formatted(item,list)).getAsJsonObject(),rules().profiles().keySet());
    }

    private FoodDecisionResolver resolver(Map<String, Set<String>> liveTags) {
        var sources = new HashMap<String,FoodSourceEvidence>();
        sources.put("example:carrot",source("example:carrot",
            "#c:foods/vegetable","#tmm:foods/raw"));
        sources.put("example:beef",source("example:beef",
            "#c:foods/raw_meat","#tmm:foods/raw"));
        return new FoodDecisionResolver(rules(),
            Map.of(dryad().profile(),dryad()),sources,Map.of(),
            id -> liveTags.getOrDefault(id,Set.of()));
    }

    @Test void strictDryadVetoBeatsForestAffirmation() {
        var resolver=resolver(Map.of());
        var forest=resolver.decide(Set.of("forest_dragon"),"example:carrot");
        assertEquals(FoodDecisionResolver.Outcome.NUTRITION,forest.outcome());
        assertEquals(6,forest.nutrition().hunger());
        assertEquals(FoodDecisionResolver.Outcome.DENY,
            resolver.decide(Set.of("forest_dragon","dryad"),"example:carrot").outcome());
        assertEquals(FoodDecisionResolver.Outcome.DENY,
            resolver.decide(Set.of("dryad"),"example:beef").outcome());
    }

    @Test void magicIsUniversalButDoesNotFabricateNourishment() {
        var resolver=resolver(Map.of(
            "example:magical",Set.of("#tmm:foods/magical")));
        var hybrid=resolver.decide(Set.of("dryad","forest_dragon"),"example:magical");
        assertEquals(FoodDecisionResolver.Outcome.PERMIT_NATIVE,hybrid.outcome());
        assertNull(hybrid.nutrition());
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            resolver.decide(Set.of("normie"),"example:magical").outcome(),
            "Unrestricted players receive no fabricated permissions");
    }

    @Test void allStrictProfilesMustAllowFood() {
        StrictDietPolicy other = StrictDietPolicy.parse(JsonParser.parseString("""
            {"schema":1,"profile":"strict_other","type":"strict","default":"allow",
             "allow_any":[],"deny_any":["#tmm:foods/drinks"]}
            """).getAsJsonObject());
        var r = new FoodDecisionResolver(rules(),
            Map.of("dryad",dryad(),"strict_other",other),Map.of(),Map.of(),
            id -> Set.of("#tmm:foods/drinks"));
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            r.decide(Set.of("dryad"),"example:tea").outcome(),
            "Allow means no strict veto; it does not fake native permission");
        assertEquals(FoodDecisionResolver.Outcome.DENY,
            r.decide(Set.of("dryad","strict_other"),"example:tea").outcome());
    }

    @Test void unclassifiedAffinityAndUnknownProfilesPreserveNative() {
        var r=resolver(Map.of());
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            r.decide(Set.of("sea_dragon"),"example:unknown").outcome());
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            r.decide(Set.of("unknown"),"example:carrot").outcome());
        assertEquals(FoodDecisionResolver.Outcome.NATIVE,
            r.decide(Set.of(),"example:carrot").outcome());
    }

    @Test void strictProfileRequiresExplicitDeclarativeActivation() {
        var policy = dryad();
        assertNotNull(policy.when());
        assertEquals("path_active",policy.when().get("type").getAsString());
        assertEquals("tyr_progression:dryad",policy.when().get("id").getAsString());
        var dormant = StrictDietPolicy.parse(JsonParser.parseString("""
            {"schema":1,"profile":"unused","type":"strict","default":"deny",
             "allow_any":[],"deny_any":[]}
            """).getAsJsonObject());
        assertNull(dormant.when(), "A definition without a server condition stays dormant");
        assertThrows(IllegalArgumentException.class, () -> StrictDietPolicy.parse(
            JsonParser.parseString("""
                {"schema":1,"profile":"bad","type":"strict","default":"deny",
                 "allow_any":[],"deny_any":[],"when":{}}
                """).getAsJsonObject()));
    }

    @Test void contradictoryStrictDeclarationsFailClosedAtLoad() {
        assertThrows(IllegalArgumentException.class,()->StrictDietPolicy.parse(
            JsonParser.parseString("""
                {"schema":1,"profile":"d","type":"strict","default":"deny",
                 "allow_any":["#tmm:foods/drinks"],"deny_any":["#tmm:foods/drinks"]}
                """).getAsJsonObject()));
        assertThrows(IllegalArgumentException.class,()->new FoodDecisionResolver(
            rules(),Map.of("forest_dragon",dryad()),Map.of(),Map.of(),id -> Set.of()));
    }
}
