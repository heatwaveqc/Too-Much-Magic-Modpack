package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Owner-approved common tags are source-reviewed, additive and optional-safe. */
class CanonicalFoodTagsTest {
    private static JsonObject tag(String path) {
        try (var stream = CanonicalFoodTagsTest.class.getResourceAsStream(
            "/data/" + path + ".json")) {
            assertNotNull(stream, path);
            return JsonParser.parseReader(
                new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (java.io.IOException bad) { throw new AssertionError(bad); }
    }

    private static Set<String> values(JsonObject tag) {
        return tag.getAsJsonArray("values").asList().stream().map(item ->
            item.isJsonObject() ? item.getAsJsonObject().get("id").getAsString()
                                : item.getAsString()).collect(Collectors.toSet());
    }

    @Test void magicalIsExplicitNotEveryGoldenOrPotion() {
        var magic=tag("tmm/tags/item/foods/magical");
        assertFalse(magic.get("replace").getAsBoolean());
        var ids=values(magic);
        assertEquals(Set.of(
            "minecraft:enchanted_golden_apple",
            "spectrum:enchanted_golden_carrot",
            "evilcraft:flesh_rejuvenated",
            "botania:infinite_fruit",
            "botania:mana_cookie",
            "spectrum:enchanted_star_candy",
            "spectrum:magnificent_star_candy"), ids);
        assertFalse(ids.contains("minecraft:golden_carrot"));
        assertFalse(ids.contains("minecraft:golden_apple"));
        assertTrue(values(tag("tmm/tags/item/foods/custom_use"))
            .containsAll(Set.of("evilcraft:flesh_rejuvenated","botania:infinite_fruit")));
        for(var item:magic.getAsJsonArray("values"))
            if(item.isJsonObject()) assertFalse(
                item.getAsJsonObject().get("required").getAsBoolean());
    }

    @Test void canonicalGoldenAndNetherRemainIndependent() {
        var golden=tag("c/tags/item/foods/golden");
        var nether=tag("c/tags/item/foods/nether_only");
        assertFalse(golden.get("replace").getAsBoolean());
        assertFalse(nether.get("replace").getAsBoolean());
        assertTrue(values(golden).containsAll(Set.of("minecraft:golden_apple",
            "minecraft:enchanted_golden_apple","minecraft:golden_carrot",
            "mynethersdelight:golden_egg","mynethersdelight:enchanted_golden_egg")));
        assertEquals(Set.of("minecraft:porkchop","minecraft:cooked_porkchop",
            "minecraft:rotten_flesh","minecraft:mushroom_stew"),values(nether));
        assertTrue(values(tag("tyr_progression/tags/item/jawr_vampire_safe_foods"))
            .isEmpty(),"Paid cuisine must be checked by native receipt, not item whitelist");
    }
}
