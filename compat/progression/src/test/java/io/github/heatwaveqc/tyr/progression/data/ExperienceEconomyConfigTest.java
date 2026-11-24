package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExperienceEconomyConfigTest {
    private static ExperienceEconomyConfig.Catalog parse(String json) {
        return ExperienceEconomyConfig.read(JsonParser.parseString(json).getAsJsonObject());
    }

    @Test void arbitraryCurrenciesPerSkillAndIndependentCaps() {
        var config = parse("""
            {
              "schema": 1,
              "systems": [
                {"id":"test:mxp","xp_per_point":1000,"growth_factor":1.02},
                {"id":"test:cultivation","xp_per_point":100,"growth_factor":0.001,"max_level":100000},
                {"id":"test:hunting","xp_per_point":500,"growth_factor":100}
              ],
              "default_path_change": {
                "on_leave":"forfeit","on_respec":"refund","wipe_unallocated":true
              },
              "path_changes": [{
                "path":"test:monk","system":"test:cultivation",
                "on_leave":"retain","wipe_unallocated":false
              }],
              "skills": [{
                "path":"test:monk","skill":"test:wellspring","total_rank_cap":8,
                "purchases":[
                  {"system":"test:mxp","max_ranks":5,"points_per_rank":1},
                  {"system":"test:cultivation","max_ranks":3,"points_per_rank":2}
                ]
              }]
            }
            """);
        assertEquals(3, config.systems().size());
        assertEquals(0.001, config.growth("test:cultivation"));
        assertEquals(100, config.growth("test:hunting"));
        assertEquals(1000, config.systems().get("test:mxp").xpPerPoint().intValueExact());
        assertEquals(100, config.systems().get("test:cultivation").xpPerPoint().intValueExact());
        var skill = config.skills().get(new ExperienceEconomyConfig.SkillKey("test:monk", "test:wellspring"));
        assertEquals(8, skill.totalRankCap());
        assertEquals(2, skill.sources().size());
        assertEquals(5, skill.sources().get(0).maxRanks());
        assertEquals(2, skill.sources().get(1).pointsPerRank());
        assertEquals(ExperienceEconomy.DeparturePolicy.FORFEIT,
                config.policy("test:monk", "test:mxp").onLeave());
        assertTrue(config.policy("test:monk", "test:mxp").wipeUnallocated());
        assertEquals(ExperienceEconomy.DeparturePolicy.RETAIN,
                config.policy("test:monk", "test:cultivation").onLeave());
        assertFalse(config.policy("test:monk", "test:cultivation").wipeUnallocated());
        assertEquals(ExperienceEconomy.DeparturePolicy.REFUND,
                config.policy("test:monk", "test:cultivation").onRespec());
    }

    @Test void defaultEconomyHasNoMagicOnlySpecialCase() {
        var c = parse("""
            {"systems":[
              {"id":"test:experience","xp_per_point":7},
              {"id":"test:skill_points","xp_per_point":50}
            ]}
            """);
        assertEquals(2, c.systems().size());
        assertEquals(7, c.systems().get("test:experience").xpPerPoint().intValueExact());
        assertEquals(ExperienceEconomy.DeparturePolicy.RETAIN,
                c.policy("test:path", "test:experience").onLeave());
        assertFalse(c.defaultTransition().wipeUnallocated());
    }

    @Test void rejectUnknownCurrencyDuplicateSourceAndTypo() {
        assertThrows(IllegalArgumentException.class, () -> parse("""
            {"systems":[{"id":"test:a"},{"id":"test:a"}]}
            """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
            {"systems":[{"id":"test:a"}],
             "skills":[{"path":"test:path","skill":"test:skill","total_rank_cap":3,
             "purchases":[{"system":"test:a","max_ranks":1},
                          {"system":"test:a","max_ranks":2}]}]}
            """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
            {"systems":[{"id":"test:a"}],
             "skills":[{"path":"test:path","skill":"test:skill","total_rank_cap":3,
             "purchases":[{"system":"test:missing","max_ranks":1}]}]}
            """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
            {"systems":[{"id":"test:a","growth_factor":101}]}
            """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
            {"systems":[{"id":"test:a","xp_per_points":1000}]}
            """));
        assertThrows(IllegalArgumentException.class, () -> parse("""
            {"systems":[{"id":"test:a"}],
             "default_path_change":{"on_leave":"duplicate"}}
            """));
    }
}
