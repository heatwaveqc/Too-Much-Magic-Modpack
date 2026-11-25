package io.github.heatwaveqc.tyr.progression;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/** Registry snapshot from misode/mcmeta 1.21.1-registries: attribute/data.json. */
class VanillaAttributeRegistryTest {
    private static JsonElement resource(String path) throws Exception {
        try(var in=VanillaAttributeRegistryTest.class.getClassLoader().getResourceAsStream(path)){
            assertNotNull(in,path);
            return JsonParser.parseReader(new InputStreamReader(in,StandardCharsets.UTF_8));
        }
    }
    private static void check(JsonElement element,Set<String> registry,String source){
        if(element.isJsonArray())for(var child:element.getAsJsonArray())check(child,registry,source);
        if(element.isJsonObject()){
            var object=element.getAsJsonObject();
            if(object.has("attribute")&&object.get("attribute").isJsonPrimitive()){
                String id=object.get("attribute").getAsString();
                if(id.startsWith("minecraft:"))assertTrue(registry.contains(id),source+": "+id);
            }
            for(var value:object.entrySet())check(value.getValue(),registry,source);
        }
    }
    @Test void shippedAttributeIdsExistInMinecraft1211() throws Exception {
        var registry=new HashSet<String>();
        for(var value:resource("fixtures/minecraft-1.21.1-attributes.json").getAsJsonArray())
            registry.add(value.getAsString());
        assertTrue(registry.contains("minecraft:generic.movement_speed"));
        assertFalse(registry.contains("minecraft:movement_speed"));
        for(String path:new String[]{
            "data/tyr_progression/tyr_progression/economy.json",
            "data/tyr_progression/tyr_progression/experience_systems/hunter.json",
            "data/tyr_progression/tyr_progression/experience_systems/monk.json",
            "data/tyr_progression/tyr_progression/magic_stats.json",
            "data/tyr_progression/tyr_progression/paths/core.json",
            "data/tyr_progression/tyr_progression/paths/hunter.json",
            "data/tyr_progression/tyr_progression/paths/monk.json",
            "data/tyr_progression/tyr_progression/paths/newbie.json",
            "data/tyr_progression/tyr_progression/paths/normie.json"
        }) check(resource(path),registry,path);
    }
}
