package io.github.heatwaveqc.tyr.progression.mxp.core;
import java.util.UUID;

/** Exact catalog and native outcome policy for Roots Classic 1.5.10. */
public final class RootsRitualProof {
    private RootsRitualProof(){}
    private static final String PREFIX="rootsclassic:ritual/";
    public enum Kind { ITEM, SUMMON, RAIN_ON, RAIN_OFF, TIME, GROW, FLARE, DRAIN, BREED }
    public record Rule(Kind kind,int level,String effectId,String implementation,String resultId){
        public boolean matches(int nativeLevel,String nativeEffect,String nativeImplementation){
            return level==nativeLevel&&effectId.equals(nativeEffect)&&implementation.equals(nativeImplementation);
        }
        public boolean resultMatches(String id,int count){
            return (kind==Kind.ITEM||kind==Kind.SUMMON)&&resultId.equals(id)&&count==1;
        }
    }
    public record Operation(String recipeId,UUID actor,UUID transaction){
        public Operation{
            if(rule(recipeId)==null||actor==null||transaction==null)throw new IllegalArgumentException("Invalid Roots ritual operation");
        }
        public RitualRewards.Receipt receipt(){return new RitualRewards.Receipt(recipeId,transaction,actor,true,false);}
    }
    public static boolean started(int before,int after){return before==0&&after==200;}
    public static Rule rule(String id){
        var reward=RitualRewards.lookup(id);
        if(reward==null||!id.startsWith(PREFIX))return null;
        int level=switch(reward.band()){case ROOTS_0 -> 0;case ROOTS_1 -> 1;case ROOTS_2 -> 2;default -> -1;};
        if(level<0)return null;
        String path=id.substring(PREFIX.length());
        if(path.equals("crafting/engraved_crafting"))return create(Kind.ITEM,level,"engraved_crafting","EngravedSword","rootsclassic:engraved_blade");
        if(path.startsWith("crafting/"))return create(Kind.ITEM,level,"crafting","Crafting","rootsclassic:"+path.substring(9));
        if(path.startsWith("summoning/"))return create(Kind.SUMMON,level,"summoning","Summoning","minecraft:"+path.substring(10));
        return switch(path){
            case "cause_rain" -> create(Kind.RAIN_ON,level,"cause_rain","CauseRain","");
            case "banish_rain" -> create(Kind.RAIN_OFF,level,"banish_rain","BanishRain","");
            case "time_shift" -> create(Kind.TIME,level,"time_shift","TimeShift","");
            case "grow" -> create(Kind.GROW,level,"grow","Grow","");
            case "flare" -> create(Kind.FLARE,level,"flare","Flare","");
            case "life_drain" -> create(Kind.DRAIN,level,"life_drain","LifeDrain","");
            case "mass_breeding" -> create(Kind.BREED,level,"mass_breeding","MassBreed","");
            default -> null;
        };
    }
    private static Rule create(Kind kind,int level,String effect,String implementation,String result){
        return new Rule(kind,level,"rootsclassic:"+effect,"elucent.rootsclassic.ritual.rituals.Ritual"+implementation,result);
    }
    public static boolean weatherChanged(Kind kind,boolean before,boolean after){
        return (kind==Kind.RAIN_ON&&!before&&after)||(kind==Kind.RAIN_OFF&&before&&!after);
    }
    public static boolean timeAdvanced(long before,long after){return after>before;}
    public static boolean damageObserved(boolean accepted,float before,float after){
        return accepted&&Float.isFinite(before)&&Float.isFinite(after)&&after<before;
    }
}
