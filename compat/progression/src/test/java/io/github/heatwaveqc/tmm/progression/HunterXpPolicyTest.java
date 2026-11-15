package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.data.HunterXpPolicy;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import io.github.heatwaveqc.tmm.progression.core.ProgressionMath;
import io.github.heatwaveqc.tmm.progression.state.Track;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HunterXpPolicyTest {
    @Test void shippedSpecialtiesAreExclusiveAndFree() throws Exception {
        String resource = "data/tmm_progression/tmm_progression/paths/hunter.json";
        try (var stream = HunterXpPolicyTest.class.getClassLoader().getResourceAsStream(resource)) {
            assertNotNull(stream);
            var raw = JsonParser.parseReader(new java.io.InputStreamReader(
                stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            var path = PathJson.read("tmm_progression:hunter",raw);
            var policy = HunterXpPolicy.parse(raw);
            assertNotNull(policy);
            assertEquals(4,policy.healthDivisor());
            assertEquals(2,policy.specialtyMultiplier());
            assertEquals(0,policy.difficulty("minecraft:water"));
            assertEquals(3,policy.difficulty("minecraft:poison"));
            assertEquals(7,policy.difficulty("minecraft:strong_regeneration"));
            assertTrue(path.activities().stream().noneMatch(a -> "kill".equals(a.event())));
            var specialties = path.skills().stream()
                .filter(s -> "tmm_progression:hunter_favored_enemy".equals(s.choiceGroup())).toList();
            assertEquals(16,specialties.size());
            for (var skill : specialties) {
                assertEquals(0,skill.baseCost());
                assertEquals(1,skill.maxRank());
                assertEquals("hunter_specialty",skill.effects().getFirst().type());
                assertTrue(skill.effects().getFirst().selector().startsWith("#"));
            }
            Track joined = ProgressionMath.join(path,Track.empty());
            Track first = ProgressionMath.buy(path,joined,"tmm_progression:specialty_undead");
            assertEquals(joined.points(),first.points()); // free selection
            assertEquals(1,first.skillRank("tmm_progression:specialty_undead"));
            assertSame(first,ProgressionMath.buy(path,first,"tmm_progression:specialty_dragon"));
            var reset = ProgressionMath.refundAll(path,first);
            assertEquals(0,reset.skillRank("tmm_progression:specialty_undead"));
        }
    }

    @Test void unsafeOrUnsupportedXpPolicyRejected() {
        String base = """
            {"schema":1,"rank_xp":[0,10],
             "hunter_xp":{"health_divisor":4,"specialty_multiplier":2,"min_kill_xp":1,
              "potion_base_xp":2,"potion_discovery_multiplier":2,
              "potion_repeat_cooldown_ticks":12000,
              "potion_brewing_difficulty":{"minecraft:healing":3}}}
            """;
        var good = JsonParser.parseString(base).getAsJsonObject();
        assertNotNull(HunterXpPolicy.parse(good));
        assertEquals(2,PathJson.read("test:hunter",good).maxRank());
        for (String bad : new String[] {
            base.replace("\"health_divisor\":4","\"health_divisor\":0"),
            base.replace("\"specialty_multiplier\":2","\"specialty_multiplier\":-1"),
            base.replace("\"minecraft:healing\":3","\"minecraft:healing\":0"),
            base.replace("\"potion_repeat_cooldown_ticks\":12000",
                "\"potion_repeat_cooldown_ticks\":0")
        }) assertThrows(IllegalArgumentException.class,
            () -> PathJson.read("test:hunter",JsonParser.parseString(bad).getAsJsonObject()));
    }
}
