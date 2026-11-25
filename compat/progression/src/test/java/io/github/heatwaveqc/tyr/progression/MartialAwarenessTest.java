package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.foundation.MartialAwarenessMath;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class MartialAwarenessTest {
    @Test void idleBoundaryAndStrongestResistance(){
        assertFalse(MartialAwarenessMath.idle(139,100,40));
        assertTrue(MartialAwarenessMath.idle(140,100,40));
        assertFalse(MartialAwarenessMath.idle(99,100,40));
        assertEquals(2,MartialAwarenessMath.strongest(2,0));
        assertEquals(0,MartialAwarenessMath.strongest(-1,0));
    }
    @Test void threatTransitionsRespectSharedCooldownAndNeverReplaySustainedAggro(){
        UUID a=UUID.randomUUID(),b=UUID.randomUUID(),c=UUID.randomUUID();
        var first=MartialAwarenessMath.observe(MartialAwarenessMath.ThreatState.empty(),Set.of(a),10,100);
        assertTrue(first.alert());
        var second=MartialAwarenessMath.observe(first.state(),Set.of(a,b),50,100);
        assertFalse(second.alert());
        var sustained=MartialAwarenessMath.observe(second.state(),Set.of(a,b),110,100);
        assertFalse(sustained.alert());
        var fresh=MartialAwarenessMath.observe(sustained.state(),Set.of(b,c),110,100);
        assertTrue(fresh.alert());
        assertFalse(MartialAwarenessMath.observe(fresh.state(),Set.of(),210,100).alert());
    }
    @Test void validSchemaAndInvalidBounds(){
        String raw="""
          {"schema":1,"rank_xp":[0],"skills":[{"id":"test:awareness","effects":[
            {"type":"attack_idle_resistance","params":{"idle_ticks":40,"amplifier":0}},
            {"type":"danger_sense","target":"#test:hostile","per_rank":8,"params":{"alert_cooldown_ticks":100}},
            {"type":"detection_range","target":"#test:hostile","per_rank":0.1,"params":{"native_range":true}}
          ]}]}
          """;
        assertDoesNotThrow(()->TestPaths.read("test:path",JsonParser.parseString(raw).getAsJsonObject()));
        for(String bad:List.of(raw.replace("idle_ticks\":40","idle_ticks\":0"),raw.replace("alert_cooldown_ticks\":100","alert_cooldown_ticks\":0"),
            raw.replace("native_range\":true","native_range\":\"invalid\"")))
            assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(bad).getAsJsonObject()));
    }
}
