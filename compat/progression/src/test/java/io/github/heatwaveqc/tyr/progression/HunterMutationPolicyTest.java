package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.HunterMutationPolicy;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HunterMutationPolicyTest {
    private static final String CONFIG = """
        {"schema":1,"rank_xp":[0,20],
          "hunter_mutations":{"target_duration_ticks":720000,
            "exhaustion_per_mutation_tick":0.0005,
            "maximum_amplifier":3,
            "effect_blacklist":["minecraft:absorption","minecraft:saturation"]}}
        """;

    @Test void defaultPilotHasTenHourTargetAndBlacklist() {
        var source = JsonParser.parseString(CONFIG).getAsJsonObject();
        HunterMutationPolicy p = HunterMutationPolicy.parse(source);
        assertNotNull(p);
        assertEquals(720000,p.targetDurationTicks());
        assertEquals(0.0005f,p.exhaustionPerMutationTick());
        assertEquals(3,p.maximumAmplifier());
        assertTrue(p.eligible("minecraft:speed",false));
        assertFalse(p.eligible("minecraft:absorption",false));
        assertFalse(p.eligible("modded:instant_effect",true));
        assertEquals(2,TestPaths.read("test:mutagenic_hunter",source).maxRank());
    }

    @Test void invalidConfigurationFailsClosed() {
        for (String broken : new String[]{
            CONFIG.replace("720000","0"),
            CONFIG.replace("0.0005","-0.01"),
            CONFIG.replace("0.0005","1000"),
            CONFIG.replace("maximum_amplifier\":3","maximum_amplifier\":100"),
            CONFIG.replace("minecraft:saturation","invalid_effect"),
            CONFIG.replace("\"minecraft:saturation\"","\"minecraft:absorption\"")
        }) assertThrows(IllegalArgumentException.class,() -> TestPaths.read(
            "test:bad_mutation",JsonParser.parseString(broken).getAsJsonObject()));
    }

    @Test void shippedHunterSpecLoadsAndPreservesNoSlotCap() throws Exception {
        String file = "data/tyr_progression/tyr_progression/paths/hunter.json";
        try(var input = HunterMutationPolicyTest.class.getClassLoader().getResourceAsStream(file)) {
            assertNotNull(input);
            var json = JsonParser.parseReader(new java.io.InputStreamReader(input,
                java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            TestPaths.read("tyr_progression:hunter",json);
            var p = HunterMutationPolicy.parse(json);
            assertEquals(720000,p.targetDurationTicks());
            assertEquals(0.0005f,p.exhaustionPerMutationTick());
            assertFalse(p.eligible("minecraft:saturation",false));
            assertTrue(p.eligible("minecraft:night_vision",false));
        }
    }
}
