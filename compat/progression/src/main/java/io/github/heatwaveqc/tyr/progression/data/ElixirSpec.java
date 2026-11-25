package io.github.heatwaveqc.tyr.progression.data;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import java.util.*;

/** Configurable per-path binding, exclusive nourishment and ordinary effect policy. */
public record ElixirSpec(String candidates,String restricted,String protectedEffects,int fixedMxp,
                         float saturation,boolean reset,boolean stackMeditative,List<Buff> buffs) {
    public record Buff(String id,int amplifier,int ticks){}
    private static final String ID="[a-z0-9_.-]+:[a-z0-9_./-]+";
    private static String tag(String input){
        if(!input.matches("#"+ID))throw new IllegalArgumentException("Elixir requires an explicit tag");
        return input.substring(1);
    }
    public static ElixirSpec read(Definition.Effect effect){
        var p=effect.params();
        String candidates=tag(effect.selector());
        String restricted=tag(p.getOrDefault("restricted_effects","#tyr_progression:elixir_restricted_effects"));
        String protectedEffects=tag(p.getOrDefault("protected_effects","#tyr_progression:elixir_protected_effects"));
        List<Buff> buffs=new ArrayList<>();Set<String> ids=new HashSet<>();
        String encoded=p.getOrDefault("effects","minecraft:regeneration,1,100;minecraft:absorption,0,2400");
        if(!encoded.isBlank())for(String entry:encoded.split(";",-1)){
            String[] fields=entry.split(",",-1);
            if(fields.length!=3||!fields[0].matches(ID)||!ids.add(fields[0])||buffs.size()>=8)
                throw new IllegalArgumentException("Invalid/duplicate Elixir effect");
            int amp=Integer.parseInt(fields[1]),ticks=Integer.parseInt(fields[2]);
            if(amp<0||amp>4||ticks<1||ticks>12000)throw new IllegalArgumentException("Unsafe Elixir effect");
            buffs.add(new Buff(fields[0],amp,ticks));
        }
        return new ElixirSpec(candidates,restricted,protectedEffects,
            MechanicalSpec.integer(p,"fixed_mxp",10,0,100000),
            (float)MechanicalSpec.number(p,"saturation",6,0,20),
            MechanicalSpec.bool(p,"reset_on_path_reset",true),
            MechanicalSpec.bool(p,"stack_meditative",false),List.copyOf(buffs));
    }
    /** A first bind may only use a whitelisted serving; later candidates never replace it. */
    public static String nextBinding(String bound,String item,boolean candidate){
        return bound.isEmpty()&&candidate?item:bound;
    }
    public static boolean excluded(String itemId){
        String id=itemId.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]","");
        return id.contains("kombucha")||id.contains("blood")&&id.contains("wine");
    }
}
