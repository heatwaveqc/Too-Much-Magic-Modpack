package io.github.heatwaveqc.tyr.progression;
import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.data.PathJson;
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
    @Test void combatStateRules(){
        assertTrue(MechanicalSpec.crossesBelow(.4,.29,.3));
        assertFalse(MechanicalSpec.crossesBelow(.29,.2,.3));
        assertEquals(1,MechanicalSpec.nextStreak(4,5,false));
        assertEquals(5,MechanicalSpec.nextStreak(5,5,true));
        for(String type:new String[]{"hit_streak","melee_reflection","quarry_mark"})
            assertDoesNotThrow(()->parse("{\"type\":\""+type+"\",\"target\":\"test:quarry\",\"per_rank\":0.2}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"hit_streak\",\"target\":\"test:quarry\",\"per_rank\":0.2,\"params\":{\"max_stacks\":21}}"));
    }
    @Test void wearAndDurationMath(){
        assertEquals(.5,MechanicalSpec.unbreakingPrefilter(0,1,false),1e-8);
        assertEquals(.8,MechanicalSpec.unbreakingPrefilter(0,1,true),1e-8);
        assertEquals(.5,MechanicalSpec.unbreakingPrefilter(3,4,false),1e-8);
        assertEquals(50,MechanicalSpec.reducedDuration(100,.5));
        assertEquals(1,MechanicalSpec.reducedDuration(1,.95));
        assertEquals(-1,MechanicalSpec.reducedDuration(-1,.5));
        assertDoesNotThrow(()->parse("{\"type\":\"virtual_unbreaking\",\"target\":\"test:gear\",\"per_rank\":1}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"virtual_unbreaking\",\"per_rank\":1}"));
    }
    @Test void beneficialDurationUsesTheExistingEffectPipelineSafely(){
        assertDoesNotThrow(() -> parse("{\"type\":\"beneficial_duration_bonus\",\"per_rank\":0.02}"));
        assertDoesNotThrow(() -> parse("{\"type\":\"beneficial_duration_bonus\",\"target\":\"minecraft:speed\",\"per_rank\":0.02}"));
        assertThrows(IllegalArgumentException.class, () -> parse("{\"type\":\"beneficial_duration_bonus\",\"per_rank\":0.5}"));
        assertThrows(IllegalArgumentException.class, () -> parse("{\"type\":\"beneficial_duration_bonus\",\"target\":\"#minecraft:speed\",\"per_rank\":0.02}"));
        assertEquals(120, MechanicalSpec.increasedDuration(100,0.2));
        assertEquals(121, MechanicalSpec.increasedDuration(101,0.2));
        assertEquals(1, MechanicalSpec.increasedDuration(1,0.2));
        assertEquals(-1, MechanicalSpec.increasedDuration(-1,0.2));
        assertEquals(100, MechanicalSpec.increasedDuration(100,-0.2));
        assertEquals(195, MechanicalSpec.increasedDuration(100,8));
        assertEquals(Integer.MAX_VALUE, MechanicalSpec.increasedDuration(Integer.MAX_VALUE,0.2));
    }
    @Test void virtualFortuneModeGuardSchema(){
        String effect = "{\"type\":\"virtual_fortune\",\"target\":\"#minecraft:mineable/pickaxe\",\"empty_hand\":true,\"per_rank\":0.3,\"params\":{\"mode_key\":\"tyr_progression:treasure_mode\",\"mode_value\":\"fortune\",\"max_total\":3}}";
        assertDoesNotThrow(() -> parse(effect));
        assertThrows(IllegalArgumentException.class, () -> parse(effect.replace(",\\\"mode_value\\\":\\\"fortune\\\"","")));
        assertThrows(IllegalArgumentException.class, () -> parse(effect.replace("tyr_progression:treasure_mode","unqualified_mode")));
        assertThrows(IllegalArgumentException.class, () -> parse(effect.replace("fortune","silk|fortune")));
    }
    @Test void gildedMechanicsSchema(){
        assertDoesNotThrow(()->parse("{\"type\":\"piglin_kinship\"}"));
        assertDoesNotThrow(()->parse("{\"type\":\"held_harvest_tier\",\"target\":\"#minecraft:mineable/pickaxe\",\"weapon\":\"minecraft:golden_pickaxe\",\"tier\":3}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"held_harvest_tier\",\"target\":\"#minecraft:mineable/pickaxe\",\"tier\":3}"));
        assertDoesNotThrow(()->parse("{\"type\":\"food_restriction\",\"target\":\"#test:foods\",\"params\":{\"gold_recipes\":true}}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"lava_fishing_practice\"}"));
    }
    @Test void miningAndAttractionSchemas(){
        assertDoesNotThrow(()->parse("{\"type\":\"vein_mining\",\"target\":\"#c:ores\",\"weapon\":\"minecraft:diamond_pickaxe\",\"per_rank\":1}"));
        assertDoesNotThrow(()->parse("{\"type\":\"vein_mining\",\"target\":\"#c:ores\",\"empty_hand\":true,\"per_rank\":1}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"vein_mining\",\"target\":\"#c:ores\",\"per_rank\":1}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"ore_sense\",\"target\":\"*\",\"per_rank\":5}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"ore_sense\",\"target\":\"#c:ores\",\"per_rank\":5,\"params\":{\"interval_ticks\":1}}"));
    }
    @Test void conservationSchema(){
        assertDoesNotThrow(()->parse("{\"type\":\"ammo_conservation\",\"target\":\"minecraft:arrow\",\"weapon\":\"minecraft:crossbow\",\"per_rank\":0.02}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"ammo_conservation\",\"target\":\"minecraft:firework_rocket\",\"weapon\":\"minecraft:crossbow\",\"per_rank\":0.02}"));
        assertThrows(IllegalArgumentException.class,()->parse("{\"type\":\"ammo_conservation\",\"target\":\"minecraft:arrow\",\"weapon\":\"minecraft:bow\",\"per_rank\":0.02}"));
    }
    @Test void respectsCaps(){assertEquals(15,MechanicalSpec.heal(.05,3,100,20),1e-8);assertEquals(20,MechanicalSpec.heal(.05,10,100,20),1e-8);assertEquals(0,MechanicalSpec.heal(0,1,100,20),1e-8);}
}
