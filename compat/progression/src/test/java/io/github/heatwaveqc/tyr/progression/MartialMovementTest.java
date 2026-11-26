package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.StunWindow;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MartialMovementTest {
    @Test void recoveryIsTargetWideAndStartsAfterStun(){
        var w=StunWindow.begin(100,20,100);
        assertTrue(w.stunned(100));assertTrue(w.stunned(119));
        assertFalse(w.stunned(120));assertFalse(w.canApply(219));assertTrue(w.canApply(220));
        assertFalse(w.stunned(99));
        assertThrows(IllegalArgumentException.class,()->StunWindow.begin(100,61,100));
        assertThrows(IllegalArgumentException.class,()->StunWindow.begin(100,20,0));
    }
    @Test void ownedClimbToggleAndScopedBarehandStun(){
        String j="""
            {"schema":1,"rank_xp":[0],"skills":[
              {"id":"test:climb","activation":{"mode":"toggle"},"effects":[{"type":"wall_climbing","per_rank":1}]},
              {"id":"test:stun","effects":[{"type":"stunning_strike","target":"#test:hostile","empty_hand":true,
               "per_rank":10,"params":{"recovery_ticks":100,"chance":1}}]}
            ]}
            """;
        assertDoesNotThrow(()->TestPaths.read("test:path",JsonParser.parseString(j).getAsJsonObject()));
        for(String bad:new String[]{j.replace("\"activation\":{\"mode\":\"toggle\"},",""),
                j.replace("\"empty_hand\":true","\"empty_hand\":false"),
                j.replace("\"recovery_ticks\":100","\"recovery_ticks\":0"),
                j.replace("\"target\":\"#test:hostile\"","\"target\":\"*\"")})
            assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(bad).getAsJsonObject()));
    }
}
