package io.github.heatwaveqc.tyr.progression.mxp.core;

import java.util.Map;
import static io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards.Band;

/** Exact native registry identities, never cross-provider aliases. */
public final class E6bRitualCatalog {
    private E6bRitualCatalog(){}
    public enum Kind { ITEM, ENTITY, WEATHER, TIME, COMMAND, CURSE, INFUSION, MANIFESTATION, REMOVE_CURSE, OWNER_ITEM, MINE, EVENT_SPAWN, EVENT_MUTATION, TELEPORT, BIOME, WOLF_CURSE, MIRROR_PAIR, SPECTRAL }
    public record JawrOutput(String output, Kind kind, Band band) {}
    private static final Map<String,JawrOutput> JAWR=Map.ofEntries(
        Map.entry("witchery:ritual/apply_ointment",new JawrOutput("witchery:broom",Kind.ITEM,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/apply_ungent",new JawrOutput("witchery:ent_twig",Kind.ITEM,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/infuse_seer",new JawrOutput("witchery:seer_stone",Kind.ITEM,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/necro_stone",new JawrOutput("witchery:necromantic_stone",Kind.ITEM,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/corrupt_poppet",new JawrOutput("witchery:corrupt_poppet",Kind.ITEM,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/rite_of_bloodbinding",new JawrOutput("witchery:bloodstone_pendant",Kind.ITEM,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/rite_of_moonbinding",new JawrOutput("witchery:moon_charm",Kind.ITEM,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/rite_of_sunbinding",new JawrOutput("witchery:sunstone_pendant",Kind.ITEM,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/summon_spectral_pig",new JawrOutput("witchery:spectral_pig",Kind.ENTITY,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/summon_witch",new JawrOutput("minecraft:witch",Kind.ENTITY,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/summon_demon",new JawrOutput("witchery:demon",Kind.ENTITY,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/summon_imp",new JawrOutput("witchery:imp",Kind.ENTITY,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/summon_wither",new JawrOutput("minecraft:wither",Kind.ENTITY,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/clear_weather",new JawrOutput("dev.sterner.witchery.content.ritual_event.ClearWeatherRitualEvent",Kind.WEATHER,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/set_day",new JawrOutput("dev.sterner.witchery.content.ritual_event.SetDayRitualEvent",Kind.TIME,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/set_midnight",new JawrOutput("dev.sterner.witchery.content.ritual_event.SetMidnightRitualEvent",Kind.TIME,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/summon_lightning",new JawrOutput("summon lightning_bolt {chalkPos}",Kind.COMMAND,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/summon_lightning_on_waystone",new JawrOutput("summon lightning_bolt {waystonePos}",Kind.COMMAND,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/befuddlement",new JawrOutput("witchery:befuddlement",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/curse_of_insanity",new JawrOutput("witchery:insanity",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/curse_of_misfortune",new JawrOutput("witchery:misfortune",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/curse_of_overheating",new JawrOutput("witchery:overheating",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/curse_of_sinking",new JawrOutput("witchery:sinking",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/curse_of_the_undead",new JawrOutput("witchery:undead",Kind.CURSE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/fragility",new JawrOutput("witchery:fragility",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/hunger",new JawrOutput("witchery:hunger",Kind.CURSE,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/infuse_infernal",new JawrOutput("witchery:infernal",Kind.INFUSION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/infuse_light",new JawrOutput("witchery:light",Kind.INFUSION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/infuse_otherwhere",new JawrOutput("witchery:otherwhere",Kind.INFUSION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/infuse_overworld",new JawrOutput("witchery:overworld",Kind.INFUSION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/manifestation",new JawrOutput("witchery:manifestation",Kind.MANIFESTATION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/remove_curse",new JawrOutput("dev.sterner.witchery.content.ritual_event.RemoveCurseRitualEvent",Kind.REMOVE_CURSE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/bind_hobgoblin_statue",new JawrOutput("witchery:statue_of_hobgoblin_patron",Kind.OWNER_ITEM,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/raining_toad",new JawrOutput("minecraft:frog",Kind.ENTITY,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/blocks_below_amber",new JawrOutput("amber",Kind.MINE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/blocks_below_cinnabar",new JawrOutput("cinnabar",Kind.MINE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/blocks_below_copper",new JawrOutput("copper",Kind.MINE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/blocks_below_gold",new JawrOutput("gold",Kind.MINE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/blocks_below_iron",new JawrOutput("iron",Kind.MINE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/blocks_below_zinc",new JawrOutput("zinc",Kind.MINE,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/bestial_call",new JawrOutput("dev.sterner.witchery.content.ritual_event.BestialCallRitualEvent",Kind.EVENT_SPAWN,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/rot",new JawrOutput("dev.sterner.witchery.content.ritual_event.RotRitualEvent",Kind.EVENT_MUTATION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/bind_familiar",new JawrOutput("dev.sterner.witchery.content.ritual_event.BindFamiliarRitualEvent",Kind.EVENT_MUTATION,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/resurrect_familiar",new JawrOutput("dev.sterner.witchery.content.ritual_event.ResurrectFamiliarRitualEvent",Kind.EVENT_MUTATION,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/teleport_owner_to_waystone",new JawrOutput("tp {owner} {waystonePos}",Kind.TELEPORT,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/teleport_taglock_to_waystone",new JawrOutput("tp {taglockPlayerOrEntity} {waystonePos}",Kind.TELEPORT,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/push_mobs",new JawrOutput("dev.sterner.witchery.content.ritual_event.PushMobsRitualEvent",Kind.EVENT_MUTATION,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/pull_mobs",new JawrOutput("dev.sterner.witchery.content.ritual_event.PullMobsRitualEvent",Kind.EVENT_MUTATION,Band.CIRCLE_BASIC)),
        Map.entry("witchery:ritual/change_biome",new JawrOutput("witchery changeBiome {chalkPos} {targetBiome} 64",Kind.BIOME,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/change_biome_cheap",new JawrOutput("witchery changeBiome {chalkPos} {targetBiome} 16",Kind.BIOME,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/change_biome_waystone",new JawrOutput("witchery changeBiome {waystonePos} {targetBiome} 64",Kind.BIOME,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/change_biome_waystone_cheap",new JawrOutput("witchery changeBiome {waystonePos} {targetBiome} 16",Kind.BIOME,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:ritual/curse_of_the_wolf",new JawrOutput("witchery werewolf level try_curse {taglockPlayerOrEntity}",Kind.WOLF_CURSE,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/mirror_pair",new JawrOutput("dev.sterner.witchery.content.ritual_event.MirrorPairRitualEvent",Kind.MIRROR_PAIR,Band.CIRCLE_EXCEPTIONAL)),
        Map.entry("witchery:ritual/bind_spectral_creatures",new JawrOutput("dev.sterner.witchery.content.ritual_event.BindSpectralCreaturesRitualEvent",Kind.SPECTRAL,Band.CIRCLE_MAJOR))
    );
    static void addTo(Map<String,RitualRewards.Rule> out){
        for(String path:java.util.List.of("war_dance","depths","fertility","zaphkiel_waltz","animal_growth","buffalo_dance")) add(out,"totemic:"+path,Band.TOTEMIC_INTRO);
        for(String path:java.util.List.of("rain","drought","flute_infusion","eagle_dance")) add(out,"totemic:"+path,Band.TOTEMIC_ADVANCED);
        for(String path:java.util.List.of("cleansing","sun_dance","danse_macabre","baykok_summon")) add(out,"totemic:"+path,Band.TOTEMIC_EXCEPTIONAL);
        JAWR.forEach((id,proof)->add(out,id,proof.band()));
    }
    private static void add(Map<String,RitualRewards.Rule> out,String id,Band band){
        if(out.putIfAbsent(id,new RitualRewards.Rule(id,band))!=null)throw new IllegalStateException("Duplicate ritual "+id);
    }
    public static boolean neutralRite(String id){
        return "witchery:ritual/push_mobs".equals(id)||"witchery:ritual/pull_mobs".equals(id)||"witchery:ritual/rot".equals(id)||"witchery:ritual/raining_toad".equals(id);
    }
    public static boolean neutralOnly(String id){
        return "witchery:ritual/push_mobs".equals(id)||"witchery:ritual/pull_mobs".equals(id);
    }
    public static boolean routineJawr(String id){
        return "witchery:ritual/charge_attuned".equals(id)||"witchery:ritual/rite_of_charging_infusion".equals(id);
    }
    public static JawrOutput jawr(String id){return id==null?null:JAWR.get(id);}
    public static boolean totemic(String id){return id!=null&&id.startsWith("totemic:")&&RitualRewards.lookup(id)!=null;}
}
