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
}
