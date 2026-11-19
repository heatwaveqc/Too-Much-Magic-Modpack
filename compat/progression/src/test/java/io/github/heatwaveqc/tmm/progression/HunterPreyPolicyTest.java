package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.foundation.HunterPreyPolicy;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class HunterPreyPolicyTest {
    @Test void explicitMembershipAndExclusionWin() {
        assertTrue(HunterPreyPolicy.eligible(true, false, false, false, false));
        assertFalse(HunterPreyPolicy.eligible(false, false, false, false, false));
        assertFalse(HunterPreyPolicy.eligible(true, true, false, false, false));
        assertFalse(HunterPreyPolicy.eligible(true, false, true, false, false));
        assertFalse(HunterPreyPolicy.eligible(true, false, false, true, false));
        assertFalse(HunterPreyPolicy.eligible(true, false, false, false, true));
    }

    @Test void generatedTagsPartitionReviewedIdsAndDoNotUseBlanketSummonDenial() throws Exception {
        Set<String> allow = ids("hunt_eligible");
        Set<String> deny = ids("hunt_excluded");
        assertEquals(475, allow.size());
        assertEquals(1219, deny.size());
        assertTrue(java.util.Collections.disjoint(allow, deny));
        assertTrue(allow.containsAll(Set.of("minecraft:vex", "minecraft:bee", "minecraft:wolf",
            "minecraft:polar_bear", "minecraft:enderman", "minecraft:wither", "alexscaves:tremorsaurus",
            "artifacts:mimic", "occultism:possessed_skeleton", "occultism:marid_unbound")));
        assertTrue(deny.containsAll(Set.of("minecraft:cow", "minecraft:sheep", "minecraft:villager",
            "minecraft:iron_golem", "minecraft:snow_golem", "minecraft:player", "ars_nouveau:ally_vex",
            "neovitae:necromancy_summon", "botania:pink_wither")));
        assertTrue(allow.containsAll(Set.of("minecraft:illusioner", "eternal_starlight:luminaris",
            "eternal_starlight:twilight_gaze", "wardentools:crystal_golem", "wardentools:noctilure")));
        assertTrue(deny.containsAll(Set.of("ars_elemental:firenando_entity", "goety:bear_servant",
            "eternal_starlight:ratlin", "wardentools:pale_wanderer", "farmingforblockheads:merchant")));
        assertFalse(allow.contains("the_bumblezone:cosmic_crystal_entity"));
        assertFalse(deny.contains("the_bumblezone:cosmic_crystal_entity"));
        assertFalse(deny.contains("#c:player_summons"));
    }

    private Set<String> ids(String tag) throws Exception {
        String path = "data/tmm_progression/tags/entity_type/" + tag + ".json";
        try (var stream = getClass().getClassLoader().getResourceAsStream(path)) {
            assertNotNull(stream, path);
            var doc = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            Set<String> result = new HashSet<>();
            for (var value : doc.getAsJsonArray("values")) {
                String id = value.isJsonPrimitive() ? value.getAsString() : value.getAsJsonObject().get("id").getAsString();
                assertFalse(id.startsWith("#"), "exact entity IDs only");
                if (!id.startsWith("minecraft:")) assertFalse(value.getAsJsonObject().get("required").getAsBoolean());
                assertTrue(result.add(id), "duplicate: " + id);
            }
            return result;
        }
    }
}
