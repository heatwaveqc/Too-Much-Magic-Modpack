package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tmm.progression.data.PathJson;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MechanicalSpecTest {
    private static void parse(String fx){
        PathJson.read("test:effects",JsonParser.parseString("{\"schema\":1,\"rank_xp\":[0],\"skills\":[{\"id\":\"test:ability\",\"effects\":["+fx+"]}]}").getAsJsonObject());
    }
    @Test void standaloneEffectsParse(){
        for(String fx:new String[]{
            "{\"type\":\"magnet\",\"per_rank\":2,\"params\":{\"move_xp\":true}}",
            "{\"type\":\"status_pulse\",\"per_rank\":4,\"params\":{\"effect\":\"minecraft:speed\",\"scope\":\"allied\"}}",
            "{\"type\":\"status_on_hit\",\"per_rank\":0.1,\"params\":{\"effect\":\"minecraft:poison\"}}",
            "{\"type\":\"heal_on_kill\",\"per_rank\":0.05}",
            "{\"type\":\"heal_on_hit\",\"per_rank\":0.05}",
            "{\"type\":\"critical_hit\",\"per_rank\":0.05}",
            "{\"type\":\"damage_at_low_health\",\"per_rank\":0.2}",
            "{\"type\":\"enhanced_night_vision\"}",
            "{\"type\":\"lava_vision\",\"per_rank\":20}"})assertDoesNotThrow(()->parse(fx));
    }
    @Test void rejectsInvalidOrUnboundedParameters(){
        for(String fx:new String[]{
            "{\"type\":\"magnet\",\"per_rank\":100}",
            "{\"type\":\"magnet\",\"per_rank\":2,\"params\":{\"max_targets\":999}}",
            "{\"type\":\"status_pulse\",\"per_rank\":3,\"params\":{\"effect\":\"minecraft:speed\",\"scope\":\"everyone\"}}",
            "{\"type\":\"status_on_hit\",\"per_rank\":0.2,\"params\":{\"effect\":\"bad\"}}",
            "{\"type\":\"critical_hit\",\"per_rank\":0.2,\"params\":{\"chance\":\"NaN\"}}",
            "{\"type\":\"heal_on_kill\",\"per_rank\":0.1,\"params\":{\"basis\":\"damage\"}}"})assertThrows(IllegalArgumentException.class,()->parse(fx));
    }
    @Test void growthAndDefenseSchema(){
        for(String type:new String[]{"breeding_cooldown","baby_growth","bonemeal_growth","projectile_deflection"})
            assertDoesNotThrow(()->parse("{\"type\":\""+type+"\",\"target\":\"test:target\",\"per_rank\":0.2}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"baby_growth\",\"per_rank\":1}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"bonemeal_growth\",\"target\":\"test:crop\",\"per_rank\":0.2,\"params\":{\"extra_stages\":4}}"));
        assertEquals(3000,MechanicalSpec.fasterTimer(6000,1));
        assertEquals(1200,MechanicalSpec.fasterTimer(6000,99));
        assertEquals(1,MechanicalSpec.fasterTimer(1,4));
    }
    @Test void respectsCaps(){assertEquals(15,MechanicalSpec.heal(.05,3,100,20),1e-8);assertEquals(20,MechanicalSpec.heal(.05,10,100,20),1e-8);assertEquals(0,MechanicalSpec.heal(0,1,100,20),1e-8);}
}
