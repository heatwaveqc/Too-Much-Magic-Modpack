package io.github.heatwaveqc.tyr.progression.mxp.core;
import java.util.Map;
import static io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards.Band;
public final class JawrBrazierCatalog{
    private JawrBrazierCatalog(){}
    public record Proof(String outcome,boolean passive,int altarPower,Band band){}
    private static final Map<String,Proof> RULES=Map.ofEntries(
        Map.entry("witchery:brazier/anguish_of_the_dead",new Proof("dev.sterner.witchery.content.brazier.AnguishOfTheDeadBrazierPassive",true,200,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:brazier/deathly_veil",new Proof("dev.sterner.witchery.content.brazier.DeathlyVeilBrazierPassive",true,200,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:brazier/drain_growth",new Proof("dev.sterner.witchery.content.brazier.DrainGrowthBrazierPassive",true,200,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:brazier/fortification_of_the_corpse",new Proof("dev.sterner.witchery.content.brazier.FortificationOfTheCorpseBrazierPassive",true,200,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:brazier/graveyard_mist",new Proof("dev.sterner.witchery.content.brazier.GraveyardMistBrazierPassive",true,200,Band.CIRCLE_MAJOR)),
        Map.entry("witchery:brazier_summoning/summon_banshee",new Proof("witchery:banshee",false,500,Band.CIRCLE_BASIC)),
        Map.entry("witchery:brazier_summoning/summon_poltergeist",new Proof("witchery:poltergeist",false,500,Band.CIRCLE_BASIC)),
        Map.entry("witchery:brazier_summoning/summon_spectre",new Proof("witchery:spectre",false,500,Band.CIRCLE_BASIC))
    );
    static void addTo(Map<String,RitualRewards.Rule> out){
        RULES.forEach((id,proof)->{if(out.putIfAbsent(id,new RitualRewards.Rule(id,proof.band()))!=null)throw new IllegalStateException("Duplicate ritual "+id);});
    }
    public static Proof lookup(String id){return id==null?null:RULES.get(id);}
    public static Map<String,Proof> rules(){return RULES;}
}
