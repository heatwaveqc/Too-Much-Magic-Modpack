package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class BossDeathPolicyTest {
    @Test void encounterSpecificAndDerivativeIdsNeverSettleGenericDeath() throws Exception {
        try (var in = getClass().getClassLoader().getResourceAsStream(
                "data/tyr_progression/tags/entity_type/boss_generic_death_excluded.json")) {
            assertNotNull(in);
            var root = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
            Set<String> ids = new HashSet<>();
            for (var value : root.getAsJsonArray("values")) {
                String id = value.isJsonPrimitive() ? value.getAsString() : value.getAsJsonObject().get("id").getAsString();
                assertTrue(ids.add(id));
            }
            assertEquals(58, ids.size());
            for (String id : new String[] {"botania:pink_wither", "neovitae:daemonium_doloris",
                    "the_bumblezone:cosmic_crystal_entity", "twilightforest:knight_phantom",
                    "voidscape:null_servant", "goety:vizier_clone", "twilightforest:plateau_boss"}) {
                assertTrue(ids.contains(id), id);
            }
            assertFalse(ids.contains("minecraft:wither"));
            assertTrue(ids.contains("minecraft:ender_dragon"));
            assertTrue(ids.contains("draconicevolution:draconic_guardian"));
        }
    }
}
