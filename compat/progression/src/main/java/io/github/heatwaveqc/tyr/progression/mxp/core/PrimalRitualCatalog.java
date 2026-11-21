package io.github.heatwaveqc.tyr.progression.mxp.core;

import java.util.LinkedHashMap;
import java.util.Map;
import static io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards.Band;

/** Pinned 6.0.7 ritual IDs only. Source variants share native expertise families. */
public final class PrimalRitualCatalog {
    private PrimalRitualCatalog(){}
    public record Entry(String family,Band band,String output,int count,int instability,int props,int ingredients){
        public RitualRewards.Rule rule(){return new RitualRewards.Rule(family,band);}
        public boolean matches(String item,int amount,int risk,int propCount,int offeringCount){
            return output.equals(item)&&amount==count&&risk==instability&&propCount==props&&offeringCount==ingredients;
        }
    }
    private static final Map<String,Entry> ENTRIES=init();
    private static Map<String,Entry> init(){
        var out=new LinkedHashMap<String,Entry>();
        add(out,"ambrosia_basic_blood","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_blood",1,4,3,5);
        add(out,"ambrosia_basic_earth","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_earth",1,4,3,5);
        add(out,"ambrosia_basic_hallowed","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_hallowed",1,4,3,5);
        add(out,"ambrosia_basic_infernal","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_infernal",1,4,3,5);
        add(out,"ambrosia_basic_moon","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_moon",1,4,3,5);
        add(out,"ambrosia_basic_sea","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_sea",1,4,3,5);
        add(out,"ambrosia_basic_sky","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_sky",1,4,3,5);
        add(out,"ambrosia_basic_sun","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_sun",1,4,3,5);
        add(out,"ambrosia_basic_void","ambrosia_basic",Band.PRIMAL_BASIC,"ambrosia_basic_void",1,4,3,5);
        add(out,"ambrosia_greater_blood","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_blood",1,6,4,6);
        add(out,"ambrosia_greater_earth","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_earth",1,6,4,6);
        add(out,"ambrosia_greater_hallowed","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_hallowed",1,6,4,6);
        add(out,"ambrosia_greater_infernal","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_infernal",1,6,4,6);
        add(out,"ambrosia_greater_moon","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_moon",1,6,4,6);
        add(out,"ambrosia_greater_sea","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_sea",1,6,4,6);
        add(out,"ambrosia_greater_sky","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_sky",1,6,4,6);
        add(out,"ambrosia_greater_sun","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_sun",1,6,4,6);
        add(out,"ambrosia_greater_void","ambrosia_greater",Band.PRIMAL_ADVANCED,"ambrosia_greater_void",1,6,4,6);
        add(out,"ambrosia_supreme_blood","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_blood",1,8,6,6);
        add(out,"ambrosia_supreme_earth","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_earth",1,8,6,6);
        add(out,"ambrosia_supreme_hallowed","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_hallowed",1,8,6,6);
        add(out,"ambrosia_supreme_infernal","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_infernal",1,8,6,6);
        add(out,"ambrosia_supreme_moon","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_moon",1,8,6,6);
        add(out,"ambrosia_supreme_sea","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_sea",1,8,6,6);
        add(out,"ambrosia_supreme_sky","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_sky",1,8,6,6);
        add(out,"ambrosia_supreme_sun","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_sun",1,8,6,6);
        add(out,"ambrosia_supreme_void","ambrosia_supreme",Band.PRIMAL_EXPERT,"ambrosia_supreme_void",1,8,6,6);
        add(out,"blood_rose","blood_rose",Band.PRIMAL_BASIC,"blood_rose",2,4,2,5);
        add(out,"dark_primal_staff_core_item","dark_primal_core",Band.PRIMAL_EXPERT,"dark_primal_staff_core_item",1,6,5,6);
        add(out,"dark_primal_wand_core_item","dark_primal_core",Band.PRIMAL_EXPERT,"dark_primal_wand_core_item",1,5,5,6);
        add(out,"dream_vision_talisman","dream_vision_talisman",Band.PRIMAL_ADVANCED,"dream_vision_talisman",1,4,3,6);
        add(out,"emberflower","emberflower",Band.PRIMAL_BASIC,"emberflower",2,4,2,5);
        add(out,"flying_carpet","flying_carpet",Band.PRIMAL_EXPERT,"flying_carpet",1,6,4,9);
        add(out,"forbidden_bow","forbidden_bow",Band.PRIMAL_ADVANCED,"forbidden_bow",1,3,2,6);
        add(out,"forbidden_sword","forbidden_sword",Band.PRIMAL_ADVANCED,"forbidden_sword",1,3,2,6);
        add(out,"forbidden_trident","forbidden_trident",Band.PRIMAL_ADVANCED,"forbidden_trident",1,3,2,6);
        add(out,"hydromelon_seeds_from_ritual","hydromelon_seeds_from_ritual",Band.PRIMAL_BASIC,"hydromelon_seeds",1,3,2,4);
        add(out,"manafruit","manafruit",Band.PRIMAL_BASIC,"manafruit",1,1,1,3);
        add(out,"pixie_basic_blood","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_blood",1,3,2,4);
        add(out,"pixie_basic_earth","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_earth",1,3,2,4);
        add(out,"pixie_basic_hallowed","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_hallowed",1,3,2,4);
        add(out,"pixie_basic_infernal","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_infernal",1,3,2,4);
        add(out,"pixie_basic_moon","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_moon",1,3,2,4);
        add(out,"pixie_basic_sea","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_sea",1,3,2,4);
        add(out,"pixie_basic_sky","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_sky",1,3,2,4);
        add(out,"pixie_basic_sun","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_sun",1,3,2,4);
        add(out,"pixie_basic_void","pixie_basic",Band.PRIMAL_BASIC,"pixie_basic_void",1,3,2,4);
        add(out,"pixie_grand_blood","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_blood",1,5,3,6);
        add(out,"pixie_grand_earth","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_earth",1,5,3,6);
        add(out,"pixie_grand_hallowed","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_hallowed",1,5,3,6);
        add(out,"pixie_grand_infernal","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_infernal",1,5,3,6);
        add(out,"pixie_grand_moon","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_moon",1,5,3,6);
        add(out,"pixie_grand_sea","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_sea",1,5,3,6);
        add(out,"pixie_grand_sky","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_sky",1,5,3,6);
        add(out,"pixie_grand_sun","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_sun",1,5,3,6);
        add(out,"pixie_grand_void","pixie_grand",Band.PRIMAL_ADVANCED,"pixie_grand_void",1,5,3,6);
        add(out,"pixie_majestic_blood","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_blood",1,7,4,6);
        add(out,"pixie_majestic_earth","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_earth",1,7,4,6);
        add(out,"pixie_majestic_hallowed","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_hallowed",1,7,4,6);
        add(out,"pixie_majestic_infernal","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_infernal",1,7,4,6);
        add(out,"pixie_majestic_moon","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_moon",1,7,4,6);
        add(out,"pixie_majestic_sea","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_sea",1,7,4,6);
        add(out,"pixie_majestic_sky","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_sky",1,7,4,6);
        add(out,"pixie_majestic_sun","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_sun",1,7,4,6);
        add(out,"pixie_majestic_void","pixie_majestic",Band.PRIMAL_EXPERT,"pixie_majestic_void",1,7,4,6);
        add(out,"primal_axe","primal_axe",Band.PRIMAL_ADVANCED,"primal_axe",1,3,2,6);
        add(out,"primal_fishing_rod","primal_fishing_rod",Band.PRIMAL_ADVANCED,"primal_fishing_rod",1,3,2,6);
        add(out,"primal_hoe","primal_hoe",Band.PRIMAL_ADVANCED,"primal_hoe",1,3,2,6);
        add(out,"primal_pickaxe","primal_pickaxe",Band.PRIMAL_ADVANCED,"primal_pickaxe",1,3,2,6);
        add(out,"primal_shovel","primal_shovel",Band.PRIMAL_ADVANCED,"primal_shovel",1,3,2,6);
        add(out,"primal_staff_core_item","primal_core",Band.PRIMAL_ADVANCED,"primal_staff_core_item",1,4,3,7);
        add(out,"primal_wand_core_item","primal_core",Band.PRIMAL_ADVANCED,"primal_wand_core_item",1,3,3,7);
        add(out,"pure_primal_staff_core_item","pure_primal_core",Band.PRIMAL_EXCEPTIONAL,"pure_primal_staff_core_item",1,8,6,6);
        add(out,"pure_primal_wand_core_item","pure_primal_core",Band.PRIMAL_EXCEPTIONAL,"pure_primal_wand_core_item",1,7,6,6);
        add(out,"sacred_shield","sacred_shield",Band.PRIMAL_ADVANCED,"sacred_shield",1,3,2,6);
        add(out,"sanguine_core_inner_demon","sanguine_core_inner_demon",Band.PRIMAL_EXCEPTIONAL,"sanguine_core_inner_demon",1,7,5,9);
        return Map.copyOf(out);
    }
    private static void add(Map<String,Entry> out,String id,String family,Band band,String result,int count,int instability,int props,int ingredients){
        String key="primalmagick:"+id;
        if(out.putIfAbsent(key,new Entry("primalmagick:"+family,band,"primalmagick:"+result,count,instability,props,ingredients))!=null)
            throw new IllegalStateException("Duplicate Primal ritual "+key);
    }
    public static Entry lookup(String id){return id==null?null:ENTRIES.get(id);}
    public static Map<String,Entry> entries(){return ENTRIES;}
    public static void addTo(Map<String,RitualRewards.Rule> out){
        ENTRIES.forEach((id,entry)->{
            if(out.putIfAbsent(id,entry.rule())!=null)throw new IllegalStateException("Duplicate ritual "+id);
        });
    }
}
