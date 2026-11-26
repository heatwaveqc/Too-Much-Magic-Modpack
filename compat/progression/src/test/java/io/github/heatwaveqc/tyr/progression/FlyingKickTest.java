package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.foundation.FlyingKickMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FlyingKickTest {
    @Test void naturalCritFallingAndClearanceAllRequired(){
        assertTrue(FlyingKickMath.eligible(true,true,-0.1,true));
        assertFalse(FlyingKickMath.eligible(false,true,-0.1,true));
        assertFalse(FlyingKickMath.eligible(true,false,-0.1,true));
        assertFalse(FlyingKickMath.eligible(true,true,0.1,true));
        assertFalse(FlyingKickMath.eligible(true,true,-0.1,false));
    }
    @Test void radiusIsAdditiveAndBounded(){
        assertEquals(3.0,FlyingKickMath.radius(2,1,1));
        assertEquals(4.0,FlyingKickMath.radius(2,1,2));
        assertEquals(5.0,FlyingKickMath.radius(2,1,3));
        assertEquals(0.0,FlyingKickMath.radius(2,1,0));
        assertEquals(16.0,FlyingKickMath.radius(2,1,100));
        assertTrue(FlyingKickMath.inRadius(25,5));
        assertFalse(FlyingKickMath.inRadius(25.01,5));
    }
    @Test void automaticScopedEffectParses(){
        String j="""
          {"schema":1,"rank_xp":[0],"skills":[
            {"id":"test:kick","effects":[{"type":"flying_kick","target":"#test:hostiles","empty_hand":true,"per_rank":1,"params":{"base_radius":2,"min_clearance":1}}]}
          ]}
          """;
        assertDoesNotThrow(()->TestPaths.read("test:path",JsonParser.parseString(j).getAsJsonObject()));
        for(String bad:new String[]{j.replace("#test:hostiles","*"),j.replace("\"empty_hand\":true","\"empty_hand\":false"),
            j.replace("\"min_clearance\":1","\"min_clearance\":0"),j.replace("\"id\":\"test:kick\",","\"id\":\"test:kick\",\"activation\":{\"mode\":\"toggle\"},")})
            assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(bad).getAsJsonObject()));
    }
}
