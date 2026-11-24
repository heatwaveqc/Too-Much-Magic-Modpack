package io.github.heatwaveqc.tyr.progression.core;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Audited vanilla passive semantics; unknown native stateful effects need explicit adapters. */
public final class MutationPowerPolicy {
    private MutationPowerPolicy(){}
    private static final Set<String> SUPPORTED=Set.of(
        "minecraft:speed","minecraft:strength","minecraft:haste","minecraft:jump_boost",
        "minecraft:regeneration","minecraft:resistance","minecraft:fire_resistance",
        "minecraft:water_breathing","minecraft:invisibility","minecraft:night_vision",
        "minecraft:health_boost","minecraft:luck","minecraft:slow_falling",
        "minecraft:dolphins_grace","minecraft:conduit_power");
    public static boolean supported(String id){return SUPPORTED.contains(id);}
    public static int strongest(int permanent,int temporary){return Math.max(permanent,temporary);}
    public static double supplemental(double desired,double nativeAmount,boolean multipliedTotal){
        return multipliedTotal?(1+desired)/(1+nativeAmount)-1:desired-nativeAmount;
    }
    public static int regenerationInterval(int amp){return Math.max(1,50>>Math.max(0,Math.min(9,amp)));}
    public static String encode(Map<String,Integer> values){
        if(values.size()>SUPPORTED.size())throw new IllegalArgumentException("Too many mutation powers");
        var result=new java.util.ArrayList<String>();
        new TreeMap<>(values).forEach((id,amp)->{
            if(!supported(id)||amp<0||amp>9)throw new IllegalArgumentException("Invalid mutation power");
            result.add(id+"="+amp);
        });
        return String.join("\n",result);
    }
    public static Map<String,Integer> decode(String encoded){
        if(encoded==null||encoded.isEmpty())return Map.of();
        if(encoded.length()>2048)throw new IllegalArgumentException("Oversized mutation power snapshot");
        Map<String,Integer> out=new TreeMap<>();
        for(String entry:encoded.split("\n",-1)){
            String[] f=entry.split("=",-1);
            if(f.length!=2||!supported(f[0])||!f[1].matches("[0-9]")||out.putIfAbsent(f[0],Integer.parseInt(f[1]))!=null)
                throw new IllegalArgumentException("Invalid mutation power snapshot");
        }
        return Map.copyOf(out);
    }
}
