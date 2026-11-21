package io.github.heatwaveqc.tyr.progression.mxp.core;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;

import static io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards.Band;

/** Exact Malum 1.8.2 rite IDs and Malum/Gaze recipe-backed Geas effects. */
public final class MalumRitualCatalog {
    private MalumRitualCatalog(){}
    public record Geas(String recipe,String effect,int input,List<Integer> extras,List<Integer> spirits){
        public Geas {extras=List.copyOf(extras);spirits=List.copyOf(spirits);}
        public String family(){return geasFamily(effect);}
        public boolean matches(String recipeId,int inputCount,List<Integer> extraCounts,List<Integer> spiritCounts){
            return recipe.equals(recipeId)&&input==inputCount&&extras.equals(extraCounts)&&spirits.equals(spiritCounts);
        }
    }
    private static final Set<String> RITES=Set.of(
        "malum:undirected_rite",
        "malum:unchained_rite",
        "malum:rite_of_healing",
        "malum:rite_of_nourishment",
        "malum:rite_of_nurturing",
        "malum:rite_of_lust",
        "malum:rite_of_harming",
        "malum:rite_of_empowerment",
        "malum:rite_of_culling",
        "malum:rite_of_rending",
        "malum:rite_of_the_howling_gale",
        "malum:rite_of_the_sky_tether",
        "malum:rite_of_gravity",
        "malum:rite_of_ascension",
        "malum:rite_of_the_flowing_grasp",
        "malum:rite_of_the_good_tides",
        "malum:rite_of_soaking",
        "malum:rite_of_sapping",
        "malum:rite_of_the_stone_ward",
        "malum:rite_of_the_oaken_might",
        "malum:rite_of_creation",
        "malum:rite_of_destruction",
        "malum:rite_of_the_burning_fervor",
        "malum:rite_of_the_fiery_embrace",
        "malum:rite_of_smelting",
        "malum:rite_of_quickening"
    );
    private static final Map<String,Geas> GEAS=init();
    private static Map<String,Geas> init(){
        var out=new LinkedHashMap<String,Geas>();
        add(out,"malum:soul_binding/authority_of_the_gleeful_target","malum:authority_of_the_gleeful_target",1,List.of(16,16,16),List.of(32,32,32,32,32,32,32,32));
        add(out,"malum:soul_binding/authority_of_the_inverted_heart","malum:authority_of_the_inverted_heart",1,List.of(16,16,16),List.of(32,32,32,32,32,32,32,32));
        add(out,"malum:soul_binding/oath_of_the_overburdened_mind","malum:oath_of_the_overburdened_mind",8,List.of(8,4,4),List.of(32,32,32));
        add(out,"malum:soul_binding/oath_of_the_overeager_fist","malum:oath_of_the_overeager_fist",8,List.of(8,4,4),List.of(32,32,32));
        add(out,"malum:soul_binding/oath_of_the_overkeen_eye","malum:oath_of_the_overkeen_eye",8,List.of(8,4,4),List.of(32,32,32));
        add(out,"malum:soul_binding/oath_of_the_undiscerned_maw","malum:oath_of_the_undiscerned_maw",8,List.of(8,4),List.of(32,32,32));
        add(out,"malum:soul_binding/oath_of_unmakers_disdain","malum:oath_of_unmakers_disdain",8,List.of(8,4),List.of(32,32,32));
        add(out,"malum:soul_binding/oath_of_unsighted_resistance","malum:oath_of_unsighted_resistance",8,List.of(8,4),List.of(32,32,32));
        add(out,"malum:soul_binding/pact_of_combustion","malum:pact_of_combustion",12,List.of(8,4),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_contentedness","malum:pact_of_contentedness",12,List.of(8,4),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_defiance","malum:pact_of_defiance",12,List.of(4,1),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_patience_repaid","malum:pact_of_patience_repaid",1,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_reciprocation","malum:pact_of_reciprocation",8,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_rune_exploitation","malum:pact_of_rune_exploitation",8,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_self_care","malum:pact_of_self_care",12,List.of(8,2),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_the_arcanaphage","malum:pact_of_the_arcanaphage",12,List.of(8,4),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_berserker","malum:pact_of_the_berserker",8,List.of(16,16,16),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_blastweaver","malum:pact_of_the_blastweaver",16,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_cloudskipper","malum:pact_of_the_cloudskipper",16,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_continuing_shot","malum:pact_of_the_continuing_shot",12,List.of(8,4),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_fortress","malum:pact_of_the_fortress",12,List.of(8),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_the_high_priest","malum:pact_of_the_high_priest",12,List.of(8,2),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_lifeweaver","malum:pact_of_the_lifeweaver",8,List.of(4,4,4),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_lone_druid","malum:pact_of_the_lone_druid",12,List.of(8,4),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_parasite","malum:pact_of_the_parasite",12,List.of(4,1),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_profane_ascetic","malum:pact_of_the_profane_ascetic",16,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_profane_glutton","malum:pact_of_the_profane_glutton",16,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_prospector","malum:pact_of_the_prospector",12,List.of(8,4),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_reaper","malum:pact_of_the_reaper",12,List.of(4,4),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_shattering_addict","malum:pact_of_the_shattering_addict",12,List.of(8,4),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_the_shield","malum:pact_of_the_shield",12,List.of(8),List.of(16,16,16));
        add(out,"malum:soul_binding/pact_of_the_skybreaker","malum:pact_of_the_skybreaker",1,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_the_warlock","malum:pact_of_the_warlock",12,List.of(4,4),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_the_windswept","malum:pact_of_the_windswept",12,List.of(8,4),List.of(16,16));
        add(out,"malum:soul_binding/pact_of_tidal_affinity","malum:pact_of_tidal_affinity",1,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/pact_of_wyrd_reconstruction","malum:pact_of_wyrd_reconstruction",16,List.of(8,8,8),List.of(32,32,16,16));
        add(out,"malum:soul_binding/domain_of_swords","gaze:domain_of_swords",1,List.of(16,4,8),List.of(1,64,64,64));
        add(out,"malum:soul_binding/pact_of_encroaching_malice","gaze:pact_of_encroaching",12,List.of(6,6,6),List.of(1,60,60,60));
        return Map.copyOf(out);
    }
    private static void add(Map<String,Geas> out,String recipe,String effect,int input,List<Integer> extras,List<Integer> spirits){
        if(out.putIfAbsent(effect,new Geas(recipe,effect,input,extras,spirits))!=null)throw new IllegalStateException("Duplicate Geas "+effect);
    }
    public static Set<String> rites(){return RITES;}
    public static Map<String,Geas> geas(){return GEAS;}
    public static Geas lookupGeas(String effect){return effect==null?null:GEAS.get(effect);}
    public static String riteFamily(String nativeId){return nativeId!=null&&RITES.contains(nativeId)?"malum:totem/"+nativeId.substring(6):null;}
    public static String geasFamily(String effect){
        return effect==null||!GEAS.containsKey(effect)?null:effect.split(":",2)[0]+":geas/"+effect.split(":",2)[1];
    }
    public static void addTo(Map<String,RitualRewards.Rule> out){
        for(String nativeId:RITES){String id=riteFamily(nativeId);if(out.putIfAbsent(id,new RitualRewards.Rule(id,Band.MALUM_TOTEM))!=null)throw new IllegalStateException("Duplicate rite "+id);}
        for(Geas geas:GEAS.values()){String id=geas.family();if(out.putIfAbsent(id,new RitualRewards.Rule(id,Band.MALUM_GEAS))!=null)throw new IllegalStateException("Duplicate Geas "+id);}
    }
}
