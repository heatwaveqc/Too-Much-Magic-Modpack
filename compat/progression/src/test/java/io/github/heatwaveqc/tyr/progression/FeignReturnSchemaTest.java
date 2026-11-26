package io.github.heatwaveqc.tyr.progression;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FeignReturnSchemaTest {
    @Test void feignRequiresToggleAndReturnIsAutomatic(){
        String j="""
          {"schema":1,"rank_xp":[0],"skills":[
            {"id":"test:feign","activation":{"mode":"toggle"},"effects":[{"type":"feign_death","params":{"aggro_radius":64}}]},
            {"id":"test:return","effects":[{"type":"projectile_return","per_rank":1,"params":{"allow_player_projectiles":false}}]}
          ]}
          """;
        assertDoesNotThrow(()->TestPaths.read("test:path",JsonParser.parseString(j).getAsJsonObject()));
        for(String bad:new String[]{j.replace("\"activation\":{\"mode\":\"toggle\"},",""),
            j.replace("\"aggro_radius\":64","\"aggro_radius\":200"),
            j.replace("\"id\":\"test:return\",","\"id\":\"test:return\",\"activation\":{\"mode\":\"toggle\"},"),
            j.replace("\"allow_player_projectiles\":false","\"allow_player_projectiles\":\"bad\"")})
            assertThrows(IllegalArgumentException.class,()->TestPaths.read("test:path",JsonParser.parseString(bad).getAsJsonObject()));
    }
}
