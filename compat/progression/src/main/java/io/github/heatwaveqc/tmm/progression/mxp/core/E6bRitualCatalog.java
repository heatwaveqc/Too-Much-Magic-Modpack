package io.github.heatwaveqc.tmm.progression.mxp.core;

import java.util.Map;
import static io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards.Band;

/** Exact native registry identities, never cross-provider aliases. */
public final class E6bRitualCatalog {
    private E6bRitualCatalog(){}
    public enum Kind { ITEM, ENTITY }
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
        Map.entry("witchery:ritual/summon_wither",new JawrOutput("minecraft:wither",Kind.ENTITY,Band.CIRCLE_EXCEPTIONAL))
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
    public static JawrOutput jawr(String id){return id==null?null:JAWR.get(id);}
    public static boolean totemic(String id){return id!=null&&id.startsWith("totemic:")&&RitualRewards.lookup(id)!=null;}
}
