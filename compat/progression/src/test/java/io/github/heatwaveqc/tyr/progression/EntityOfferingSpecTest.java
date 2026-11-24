package io.github.heatwaveqc.tyr.progression;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.EntityOfferingSpec;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class EntityOfferingSpecTest {
    private static EntityOfferingSpec parse(String json) {
        return EntityOfferingSpec.parse(JsonParser.parseString(json).getAsJsonObject());
    }
    @Test void requiresRealDiamondMaterialAndExactEntityAndItem() {
        var spec=parse("""
            {"enrollment":"entity_offering","entity_offering":{
              "entity":"witchery:lilith","item":"irons_jewelry:ring",
              "jewelry_material":"irons_jewelry:diamond",
              "completion_event":"tyr_progression:sexy_demon_initiation"}}
            """);
        assertTrue(spec.matches("witchery:lilith","irons_jewelry:ring",Set.of("irons_jewelry:diamond")));
        assertFalse(spec.matches("witchery:lilith","irons_jewelry:ring",Set.of()));
        assertFalse(spec.matches("witchery:lilith","irons_jewelry:ring",Set.of("irons_jewelry:ruby")));
        assertFalse(spec.matches("witchery:lilith","irons_jewelry:necklace",Set.of("irons_jewelry:diamond")));
        assertFalse(spec.matches("minecraft:villager","irons_jewelry:ring",Set.of("irons_jewelry:diamond")));
    }
    @Test void malformedAndMisplacedSpecsFailClosed() {
        assertNull(parse("{}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"enrollment\":\"entity_offering\"}"));
        assertThrows(IllegalArgumentException.class,()->parse("""
          {"enrollment":"manual","entity_offering":{}}
          """));
        assertThrows(IllegalArgumentException.class,()->parse("""
          {"enrollment":"entity_offering","entity_offering":{"entity":"*","item":"minecraft:diamond"}}
          """));
        assertThrows(IllegalArgumentException.class,()->parse("""
          {"enrollment":"entity_offering","entity_offering":{"entity":"witchery:lilith","item":"minecraft:diamond","jewelry_material":"irons_jewelry:diamond"}}
          """));
        assertThrows(IllegalArgumentException.class,()->parse("""
          {"enrollment":"entity_offering","entity_offering":{"entity":"witchery:lilith","item":"irons_jewelry:ring","typo":true}}
          """));
    }
}
