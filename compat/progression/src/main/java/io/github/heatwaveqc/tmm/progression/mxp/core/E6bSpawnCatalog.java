package io.github.heatwaveqc.tmm.progression.mxp.core;

import java.util.Map;
import static io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards.Band;

/** Exact IDs and expected concrete outcomes of pinned Enchanted and Eidolon ceremonies. */
public final class E6bSpawnCatalog {
    private E6bSpawnCatalog(){}
    public record Proof(String output,String nativeClass,boolean item,int count,Band band){}
    private static final Map<String,Proof> ENCHANTED=Map.ofEntries(
        Map.entry("enchanted:charging_stone",new Proof("enchanted:attuned_stone_charged","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.CreateItemRite",true,1,Band.CIRCLE_BASIC)),
        Map.entry("enchanted:infuse_broom",new Proof("enchanted:enchanted_broomstick","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.CreateItemRite",true,1,Band.CIRCLE_MAJOR)),
        Map.entry("enchanted:waystone",new Proof("enchanted:bound_waystone","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.LocationBoundCreateItemRite",true,1,Band.CIRCLE_BASIC)),
        Map.entry("enchanted:waystone_charged",new Proof("enchanted:bound_waystone","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.LocationBoundCreateItemRite",true,1,Band.CIRCLE_BASIC)),
        Map.entry("enchanted:waystone_blooded",new Proof("enchanted:blooded_waystone","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.EntityBoundCreateItemRite",true,1,Band.CIRCLE_BASIC)),
        Map.entry("enchanted:waystone_blooded_charged",new Proof("enchanted:blooded_waystone","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.EntityBoundCreateItemRite",true,1,Band.CIRCLE_BASIC))
    );
    private static final Map<String,Proof> EIDOLON=Map.ofEntries(
        Map.entry("eidolon_repraised:rituals/brazier_craft_sanguine_amulet",new Proof("eidolon_repraised:sanguine_amulet","alexthw.eidolon_repraised.common.ritual.CraftingRitual",true,1,Band.EIDOLON_MAJOR)),
        Map.entry("eidolon_repraised:rituals/brazier_craft_sapping_sword",new Proof("eidolon_repraised:sapping_sword","alexthw.eidolon_repraised.common.ritual.CraftingRitual",true,1,Band.EIDOLON_ADVANCED)),
        Map.entry("eidolon_repraised:rituals/summon_creeper",new Proof("minecraft:creeper","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_drowned",new Proof("minecraft:drowned","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,3,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_husk",new Proof("minecraft:husk","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,3,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_phantom",new Proof("minecraft:phantom","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_ADVANCED)),
        Map.entry("eidolon_repraised:rituals/summon_raven",new Proof("eidolon_repraised:raven","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,3,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_skeleton",new Proof("minecraft:skeleton","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_slimy_slug",new Proof("eidolon_repraised:slimy_slug","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,3,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_stray",new Proof("minecraft:stray","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/summon_wither_skeleton",new Proof("minecraft:wither_skeleton","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_ADVANCED)),
        Map.entry("eidolon_repraised:rituals/summon_wraith",new Proof("eidolon_repraised:wraith","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_ADVANCED)),
        Map.entry("eidolon_repraised:rituals/summon_zombie",new Proof("minecraft:zombie","alexthw.eidolon_repraised.common.ritual.SummonRitual",false,1,Band.EIDOLON_BASIC)),
        Map.entry("eidolon_repraised:rituals/daylight",new Proof("eidolon_repraised:daylight","alexthw.eidolon_repraised.common.ritual.DaylightRitual",false,1,Band.EIDOLON_ADVANCED)),
        Map.entry("eidolon_repraised:rituals/moonlight",new Proof("eidolon_repraised:moonlight","alexthw.eidolon_repraised.common.ritual.MoonlightRitual",false,1,Band.EIDOLON_ADVANCED))
    );
    static void addTo(Map<String,RitualRewards.Rule> out){
        ENCHANTED.forEach((id,p)->add(out,id,p));
        EIDOLON.forEach((id,p)->add(out,id,p));
    }
    private static void add(Map<String,RitualRewards.Rule> out,String id,Proof p){
        if(out.putIfAbsent(id,new RitualRewards.Rule(id,p.band()))!=null)throw new IllegalStateException("Duplicate ritual "+id);
    }
    public static Proof enchanted(String id){return id==null?null:ENCHANTED.get(id);}
    public static Proof eidolon(String id){return id==null?null:EIDOLON.get(id);}
    public static java.util.Set<String> eidolonIds(){return EIDOLON.keySet();}
}
