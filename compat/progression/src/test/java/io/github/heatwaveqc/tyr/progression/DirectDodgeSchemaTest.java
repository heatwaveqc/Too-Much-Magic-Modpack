package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectDodgeSchemaTest {
    @Test void passiveBoundedDodge(){
        String j="""
          {"schema":1,"rank_xp":[0],"skills":[
            {"id":"test:dodge","max_rank":3,"effects":[{"type":"direct_dodge","unarmored":true,"per_rank":0.1}]}
          ]}
          """;
        assertDoesNotThrow(()->TestPaths.read("test:path",JsonParser.parseString(j).getAsJsonObject()));
        assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(j.replace("0.1","0.8")).getAsJsonObject()));
        assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(j.replace("\"max_rank\":3","\"max_rank\":3,\"activation\":{\"mode\":\"toggle\"}")).getAsJsonObject()));
    }
}
