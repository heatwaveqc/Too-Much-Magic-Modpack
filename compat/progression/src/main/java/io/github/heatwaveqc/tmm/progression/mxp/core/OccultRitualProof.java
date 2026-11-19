package io.github.heatwaveqc.tmm.progression.mxp.core;

/** Only explicitly catalogued native book rites and supported outcome families. */
public final class OccultRitualProof {
    private OccultRitualProof(){}
    public enum Kind { ITEM, SUMMON, HELD }
    public static String book(String id){
        var rule=RitualRewards.lookup(id);
        if(rule==null)return "";
        return switch(rule.band()){
            case OCCULT_FOLIOT -> "occultism:book_of_binding_bound_foliot";
            case OCCULT_DJINNI -> "occultism:book_of_binding_bound_djinni";
            case OCCULT_AFRIT -> "occultism:book_of_binding_bound_afrit";
            case OCCULT_MARID -> "occultism:book_of_binding_bound_marid";
            default -> "";
        };
    }
    public static boolean matchesBook(String id,String actual){
        String expected=book(id);
        return !expected.isEmpty()&&expected.equals(actual);
    }
    public static Kind kind(String id){
        if(book(id).isEmpty()||!id.startsWith("occultism:ritual/"))return Kind.HELD;
        String path=id.substring("occultism:ritual/".length());
        // Upgrade output and the randomized parrot/chicken result need separate outcome proof.
        if(path.equals("craft_upgrade_ritual_satchel")||path.equals("familiar_parrot"))return Kind.HELD;
        if(path.startsWith("craft_"))return Kind.ITEM;
        if(path.startsWith("summon_")||path.startsWith("possess_")||path.startsWith("familiar_"))return Kind.SUMMON;
        return Kind.HELD;
    }
}
