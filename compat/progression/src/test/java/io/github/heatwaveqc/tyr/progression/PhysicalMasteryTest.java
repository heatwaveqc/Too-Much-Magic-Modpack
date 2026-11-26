package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.foundation.PhysicalMasteryMath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PhysicalMasteryTest {
    @Test void protectionSharesNativeCap(){
        assertEquals(16F,PhysicalMasteryMath.protection(0,16));
        assertEquals(22F,PhysicalMasteryMath.protection(6,16));
        assertEquals(36,PhysicalMasteryMath.damageAfterProtection(100,16),1e-9);
        assertEquals(20,PhysicalMasteryMath.damageAfterProtection(100,22),1e-9);
        assertEquals(6F,PhysicalMasteryMath.protection(6,0));
    }
    @Test void nativeSoulSpeedAndTerrainTransition(){
        assertEquals(.0405,PhysicalMasteryMath.soulSpeed(1),1e-9);
        assertEquals(.051,PhysicalMasteryMath.soulSpeed(2),1e-9);
        assertEquals(.0615,PhysicalMasteryMath.soulSpeed(3),1e-9);
        assertEquals(3,PhysicalMasteryMath.soulLevel(8));
        assertTrue(PhysicalMasteryMath.retainSpeed(true,true,false,false));
        assertTrue(PhysicalMasteryMath.retainSpeed(true,false,true,true));
        assertFalse(PhysicalMasteryMath.retainSpeed(true,false,false,true));
        assertFalse(PhysicalMasteryMath.retainSpeed(false,true,true,true));
        assertFalse(PhysicalMasteryMath.retainSpeed(true,false,true,false));
    }
    @Test void bothEffectsAreAutomaticAndBounded(){
        String j="""
          {"schema":1,"rank_xp":[0],"skills":[
            {"id":"test:protection","effects":[{"type":"virtual_protection","per_rank":2,"unarmored":true}]},
            {"id":"test:soul","effects":[{"type":"soul_step","per_rank":1,"unarmored":true}]}
          ]}
          """;
        assertDoesNotThrow(()->TestPaths.read("test:path",JsonParser.parseString(j).getAsJsonObject()));
        for(String bad:new String[]{j.replace("\"per_rank\":2","\"per_rank\":21"),j.replace("\"per_rank\":1","\"per_rank\":4"),j.replace("\"id\":\"test:soul\",","\"id\":\"test:soul\",\"activation\":{\"mode\":\"toggle\"},")})
            assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(bad).getAsJsonObject()));
    }
}
