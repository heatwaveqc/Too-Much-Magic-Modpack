package io.github.heatwaveqc.tmm.progression.mxp.core;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;

/** Owner-approved bands and explicit native recipe-to-family assignments. No namespace defaults. */
public final class RitualRewards {
    private RitualRewards() {}
    public enum Band {
        OCCULT_FOLIOT(1000,200), OCCULT_DJINNI(1750,350), OCCULT_AFRIT(3000,600), OCCULT_MARID(5000,1000),
        FORGE_1(1000,200), FORGE_2(1600,320), FORGE_3(2400,480), FORGE_4(3500,700), FORGE_5(5000,1000),
        CIRCLE_BASIC(1000,200), CIRCLE_MAJOR(2000,400), CIRCLE_EXCEPTIONAL(4000,800),
        EIDOLON_BASIC(1000,200), EIDOLON_ADVANCED(2000,400), EIDOLON_MAJOR(3500,700),
        TOTEMIC_INTRO(1200,240), TOTEMIC_ADVANCED(2200,440), TOTEMIC_EXCEPTIONAL(3500,700),
        ROOTS_0(100,20), ROOTS_1(250,50), ROOTS_2(500,100),
        GOETY_BASIC(1000,200), GOETY_ADVANCED(2000,400), GOETY_EXPERT(3000,600), GOETY_EXCEPTIONAL(5000,1000),
        PRIMAL_BASIC(1000,200), PRIMAL_ADVANCED(2000,400), PRIMAL_EXPERT(3000,600), PRIMAL_EXCEPTIONAL(5000,1000),
        MALUM_TOTEM(500,0), MALUM_GEAS(1500,0), ASTRAL_ATTUNEMENT(3000,0);
        private final int first, repeat;
        Band(int first,int repeat){this.first=first;this.repeat=repeat;}
        public int first(){return first;}
        public int repeat(){return repeat;}
    }
    public record Rule(String family,Band band) {
        public Rule {
            if(!validId(family)||band==null)throw new IllegalArgumentException("Invalid ritual rule");
        }
    }
    /** Receipt is supplied only by a native adapter after an actual, attributable terminal operation.
     * The originating adapter owns a stable UUID for that operation, not a new ID per notification. */
    public record Receipt(String recipeId,UUID operation,UUID actor,boolean successful,boolean separatelyPaid) {
        public Rule rule(){return successful&&!separatelyPaid&&operation!=null&&actor!=null?lookup(recipeId):null;}
        public String transaction(){return "ritual:"+operation;}
    }
    private static boolean validId(String id){
        return id!=null&&id.length()<=180&&id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }
    private static final Map<String,Rule> RULES=initialRules();
    private static Map<String,Rule> initialRules(){
        var rules=new LinkedHashMap<String,Rule>();
        forge(rules,Band.FORGE_1,"ferrognetic_mixture","quantum_catcher","smelter_prism","upgrade_tier_2");
        forge(rules,Band.FORGE_2,"draco_arcanus_boots","draco_arcanus_chestplate","draco_arcanus_helmet","draco_arcanus_leggings","terrastomp_prism","upgrade_tier_3");
        forge(rules,Band.FORGE_3,"eternal_stella","quantum_injector","sea_prism","whirlwind_prism","upgrade_tier_4");
        forge(rules,Band.FORGE_4,"boss_catcher","soul_binding_crystal","tyr_boots","tyr_chestplate","tyr_helmet","tyr_leggings","upgrade_tier_5");
        PinnedRitualCatalog.addTo(rules);
        E6bRitualCatalog.addTo(rules);
        E6bSpawnCatalog.addTo(rules);
        JawrBrazierCatalog.addTo(rules);
        PrimalRitualCatalog.addTo(rules);
        return Map.copyOf(rules);
    }
    private static void forge(Map<String,Rule> out,Band band,String... paths){
        for(String path:paths){
            String id="forbidden_arcanus:"+path;
            if(out.putIfAbsent(id,new Rule(id,band))!=null)throw new IllegalStateException("Duplicate ritual "+id);
        }
    }
    public static Rule lookup(String recipeId){return recipeId==null?null:RULES.get(recipeId);}
    public static Map<String,Rule> rules(){return RULES;}
}
