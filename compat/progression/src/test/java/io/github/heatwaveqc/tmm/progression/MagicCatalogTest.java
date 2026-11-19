package io.github.heatwaveqc.tmm.progression;
import io.github.heatwaveqc.tmm.progression.data.MagicCatalog;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MagicCatalogTest{
 @Test void expandsOneToOneAndPreservesCondition(){
   var defs=MagicCatalog.read(JsonParser.parseString("""
   {"schema":1,"stats":{"test:cost":{"attribute":"test:percent","scale":100}}}
   """).getAsJsonObject());
   var raw=JsonParser.parseString("""
   {"skills":[{"effects":[{"type":"magic_stat","target":"test:cost","per_rank":0.05,"when":{"type":"always"}}]}]}
   """).getAsJsonObject();
   var fx=MagicCatalog.expand(raw,defs).getAsJsonArray("skills").get(0).getAsJsonObject().getAsJsonArray("effects").get(0).getAsJsonObject();
   assertEquals("attribute",fx.get("type").getAsString());assertEquals("test:percent",fx.get("attribute").getAsString());
   assertEquals(5,fx.get("per_rank").getAsDouble());assertTrue(fx.has("when"));assertThrows(IllegalArgumentException.class,()->MagicCatalog.expand(raw,java.util.Map.of()));
 }
 @Test void packagedNativeCatalogCoversAdditionalProviders() throws java.io.IOException {
   var path="/data/tmm_progression/tmm_progression/magic_stats.json";
   try(var stream=MagicCatalogTest.class.getResourceAsStream(path)){
     assertNotNull(stream, "Packaged magic stat catalog");
     var raw=JsonParser.parseReader(new java.io.InputStreamReader(stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
     var defs=MagicCatalog.read(raw);
     assertEquals(80,defs.size());
     assertEquals(80,defs.values().stream().map(MagicCatalog.Binding::attribute).distinct().count());
     assertEquals(new MagicCatalog.Binding("goety:spell_duration","add_value",1),defs.get("tmm_progression:goety_duration"));
     assertEquals(new MagicCatalog.Binding("goety:spell_range","add_value",1),defs.get("tmm_progression:goety_range"));
     assertEquals(new MagicCatalog.Binding("goety:spell_radius","add_value",1),defs.get("tmm_progression:goety_radius"));
     assertEquals(new MagicCatalog.Binding("goety:spell_burning","add_value",1),defs.get("tmm_progression:goety_burning"));
     assertEquals(new MagicCatalog.Binding("goety:spell_velocity","add_value",1),defs.get("tmm_progression:goety_velocity"));
     assertEquals(new MagicCatalog.Binding("goety:casting_speed","add_value",1),defs.get("tmm_progression:goety_cast_speed"));
     assertEquals(new MagicCatalog.Binding("goety:cooldown_discount","add_value",1),defs.get("tmm_progression:goety_cooldown_reduction"));
     assertEquals(new MagicCatalog.Binding("goety:abyss_potency","add_value",1),defs.get("tmm_progression:goety_abyss_potency"));
     assertEquals(new MagicCatalog.Binding("goety:abyss_discount","add_value",1),defs.get("tmm_progression:goety_abyss_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:frost_potency","add_value",1),defs.get("tmm_progression:goety_frost_potency"));
     assertEquals(new MagicCatalog.Binding("goety:frost_discount","add_value",1),defs.get("tmm_progression:goety_frost_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:geomancy_potency","add_value",1),defs.get("tmm_progression:goety_geomancy_potency"));
     assertEquals(new MagicCatalog.Binding("goety:geomancy_discount","add_value",1),defs.get("tmm_progression:goety_geomancy_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:necromancy_potency","add_value",1),defs.get("tmm_progression:goety_necromancy_potency"));
     assertEquals(new MagicCatalog.Binding("goety:necromancy_discount","add_value",1),defs.get("tmm_progression:goety_necromancy_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:nether_potency","add_value",1),defs.get("tmm_progression:goety_nether_potency"));
     assertEquals(new MagicCatalog.Binding("goety:nether_discount","add_value",1),defs.get("tmm_progression:goety_nether_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:storm_potency","add_value",1),defs.get("tmm_progression:goety_storm_potency"));
     assertEquals(new MagicCatalog.Binding("goety:storm_discount","add_value",1),defs.get("tmm_progression:goety_storm_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:void_potency","add_value",1),defs.get("tmm_progression:goety_void_potency"));
     assertEquals(new MagicCatalog.Binding("goety:void_discount","add_value",1),defs.get("tmm_progression:goety_void_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:wild_potency","add_value",1),defs.get("tmm_progression:goety_wild_potency"));
     assertEquals(new MagicCatalog.Binding("goety:wild_discount","add_value",1),defs.get("tmm_progression:goety_wild_cost_discount"));
     assertEquals(new MagicCatalog.Binding("goety:wind_potency","add_value",1),defs.get("tmm_progression:goety_wind_potency"));
     assertEquals(new MagicCatalog.Binding("goety:wind_discount","add_value",1),defs.get("tmm_progression:goety_wind_cost_discount"));
     assertEquals(new MagicCatalog.Binding("malum:scythe_proficiency","add_value",1),defs.get("tmm_progression:malum_scythe_proficiency"));
     assertEquals(new MagicCatalog.Binding("malum:spirit_spoils","add_value",1),defs.get("tmm_progression:malum_spirit_spoils"));
     assertEquals(new MagicCatalog.Binding("malum:arcane_resonance","add_value",1),defs.get("tmm_progression:malum_arcane_resonance"));
     assertEquals(new MagicCatalog.Binding("malum:soul_ward_integrity","add_value",1),defs.get("tmm_progression:malum_soul_ward_integrity"));
     assertEquals(new MagicCatalog.Binding("malum:soul_ward_recovery_rate","add_value",1),defs.get("tmm_progression:malum_soul_ward_recovery_rate"));
     assertEquals(new MagicCatalog.Binding("malum:soul_ward_recovery_gain","add_value",1),defs.get("tmm_progression:malum_soul_ward_recovery_gain"));
     assertEquals(new MagicCatalog.Binding("malum:soul_ward_capacity","add_value",1),defs.get("tmm_progression:malum_soul_ward_capacity"));
     assertEquals(new MagicCatalog.Binding("malum:charge_duration","add_value",1),defs.get("tmm_progression:malum_charge_duration"));
     assertEquals(new MagicCatalog.Binding("malum:charge_capacity","add_value",1),defs.get("tmm_progression:malum_charge_capacity"));
     assertEquals(new MagicCatalog.Binding("malum:charge_recovery_rate","add_value",1),defs.get("tmm_progression:malum_charge_recovery_rate"));
     assertEquals(new MagicCatalog.Binding("malum:geas_limit","add_value",1),defs.get("tmm_progression:malum_geas_limit"));
     assertEquals(new MagicCatalog.Binding("malum:malignant_conversion","add_value",1),defs.get("tmm_progression:malum_malignant_conversion"));
     assertEquals(new MagicCatalog.Binding("malum:malignant_aegis_recovery_rate","add_value",1),defs.get("tmm_progression:malum_malignant_aegis_recovery_rate"));
     assertEquals(new MagicCatalog.Binding("malum:malignant_aegis_recovery_gain","add_value",1),defs.get("tmm_progression:malum_malignant_aegis_recovery_gain"));
     assertEquals(new MagicCatalog.Binding("malum:malignant_aegis_capacity","add_value",1),defs.get("tmm_progression:malum_malignant_aegis_capacity"));
     assertEquals(new MagicCatalog.Binding("psitweaks:spell_damage_factor","add_value",1),defs.get("tmm_progression:psitweaks_potency"));
     assertEquals(new MagicCatalog.Binding("enigmaticlegacyplus:etherium_shield","add_value",1),defs.get("tmm_progression:enigmatic_etherium_shield"));
     assertEquals(new MagicCatalog.Binding("enigmaticlegacyplus:projectile_deflect","add_value",1),defs.get("tmm_progression:enigmatic_projectile_deflect"));
     assertEquals(new MagicCatalog.Binding("enigmaticlegacyplus:lifesteal","add_value",1),defs.get("tmm_progression:enigmatic_lifesteal"));
     assertEquals(new MagicCatalog.Binding("neovitae:sigil_cost_reduction","add_value",100),defs.get("tmm_progression:neo_cost_discount"));
     var skill=JsonParser.parseString("""
       {"skills":[{"effects":[{"type":"magic_stat","target":"tmm_progression:goety_range","rank_values":[1,2,4],"when":{"type":"always"}},{"type":"magic_stat","target":"tmm_progression:psitweaks_potency","per_rank":0.1}]}]}
       """).getAsJsonObject();
     var fx=MagicCatalog.expand(skill,defs).getAsJsonArray("skills").get(0).getAsJsonObject().getAsJsonArray("effects");
     assertEquals("goety:spell_range",fx.get(0).getAsJsonObject().get("attribute").getAsString());
     assertEquals(4,fx.get(0).getAsJsonObject().getAsJsonArray("rank_values").get(2).getAsDouble());
     assertTrue(fx.get(0).getAsJsonObject().has("when"));
     assertEquals(0.1,fx.get(1).getAsJsonObject().get("per_rank").getAsDouble(),1e-9);
   }
 }
}
