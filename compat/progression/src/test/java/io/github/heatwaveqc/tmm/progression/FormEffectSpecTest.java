package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.FormEffectSpec;
import io.github.heatwaveqc.tmm.progression.model.Definition;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FormEffectSpecTest{
 private static Definition.Effect fx(String type,String weapon,String damage,Map<String,String> params){
   return new Definition.Effect(type,"","add_value","*",weapon,damage,.1,0,false,false,params);
 }
 @Test void explicitFormContextOnly(){
   assertDoesNotThrow(()->FormEffectSpec.validate(fx("damage_bonus","","minecraft:player_attack",Map.of("native_attack","jawr_wolf_melee"))));
   assertThrows(IllegalArgumentException.class,()->FormEffectSpec.validate(fx("damage_bonus","minecraft:diamond_sword","",Map.of("native_attack","dragon_form_melee"))));
   assertThrows(IllegalArgumentException.class,()->FormEffectSpec.validate(fx("damage_bonus","","*",Map.of("native_attack","dragon_breath"))));
   assertThrows(IllegalArgumentException.class,()->FormEffectSpec.validate(fx("status_on_hit","","minecraft:magic",Map.of("native_attack","jawr_bite"))));
   assertDoesNotThrow(()->FormEffectSpec.validate(fx("break_speed","","",Map.of("native_mining","dragon_claw"))));
   assertThrows(IllegalArgumentException.class,()->FormEffectSpec.validate(fx("harvest_tier","","",Map.of("native_mining","dragon_claw"))));
 }
}
