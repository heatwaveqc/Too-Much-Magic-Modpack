package io.github.heatwaveqc.tmm.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PathJsonTest {
    private static final String VALID = """
        {"schema":1,"rank_xp":[0,20,60],"points_per_rank":1,
         "skills":[{"id":"test:hand","max_rank":2,"auto_ranks":[1,3],
          "effects":[{"type":"harvest_tier","target":"#minecraft:mineable/pickaxe",
           "tier":3,"empty_hand":true}]}]}
        """;

    @Test void baselineParsesAndValidates() {
        var path = PathJson.read("test:monk",JsonParser.parseString(VALID).getAsJsonObject());
        assertEquals(3,path.maxRank());
        assertEquals(2,path.skills().getFirst().autoRanks().size());
        assertEquals("harvest_tier",path.skills().getFirst().effects().getFirst().type());
    }
    @Test void rejectUnknownEffects() {
        String altered = VALID.replace("harvest_tier","unknown_arbitrary_code");
        assertThrows(IllegalArgumentException.class,
            () -> PathJson.read("test:monk",JsonParser.parseString(altered).getAsJsonObject()));
    }
    @Test void rejectNonIncreasingThresholdsAndOversizedSkills() {
        String altered = VALID.replace("[0,20,60]","[0,20,20]");
        assertThrows(IllegalArgumentException.class,
            () -> PathJson.read("test:monk",JsonParser.parseString(altered).getAsJsonObject()));
    }
    @Test void maxLevelIsExplicitAndValidated() {
        String valid = VALID.replace("\"schema\":1", "\"schema\":1,\"max_level\":3");
        assertEquals(3,PathJson.read("test:monk",JsonParser.parseString(valid).getAsJsonObject()).maxRank());
        String mismatch = VALID.replace("\"schema\":1", "\"schema\":1,\"max_level\":5");
        assertThrows(IllegalArgumentException.class,
            () -> PathJson.read("test:monk",JsonParser.parseString(mismatch).getAsJsonObject()));
    }
    @Test void cycleAndUnknownAlternativeFailClosed() {
        String bad = VALID.replace("\"max_rank\":2", "\"max_rank\":2,\"requires_any_skills\":[\"test:missing\"]");
        assertThrows(IllegalArgumentException.class,
            () -> PathJson.read("test:monk",JsonParser.parseString(bad).getAsJsonObject()));
    }

    @Test void branchingAndGearParametersValidate() {
        String graph = """
            {"schema":1,"max_level":2,"rank_xp":[0,10],"skills":[
                {"id":"test:a","choice_group":"test:stance","effects":[]},
                {"id":"test:b","choice_group":"test:stance","requires_any_skills":["test:a"],
                 "effects":[{"type":"gear_durability","target":"*","per_rank":1,
                  "params":{"slot":"all","action":"repair","interval_ticks":"20"}}]}
            ]}
            """;
        assertEquals(2,PathJson.read("test:choice",
            JsonParser.parseString(graph).getAsJsonObject()).skills().size());
        String wrong = graph.replace("repair","explode");
        assertThrows(IllegalArgumentException.class, () -> PathJson.read("test:choice",
            JsonParser.parseString(wrong).getAsJsonObject()));
    }
    @Test void prerequisiteCycleRejected() {
        String cycle = """
            {"schema":1,"rank_xp":[0,10],"skills":[
                {"id":"test:a","requires_skills":["test:b"]},
                {"id":"test:b","requires_any_skills":["test:a"]}
            ]}
            """;
        assertThrows(IllegalArgumentException.class,
            () -> PathJson.read("test:cycle",JsonParser.parseString(cycle).getAsJsonObject()));
    }

}
