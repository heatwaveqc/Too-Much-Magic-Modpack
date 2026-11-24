package io.github.heatwaveqc.tyr.progression.core;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** Shared non-potion mutation arithmetic and bounded server-to-client projection. */
public final class MutationPowerPolicy {
    private MutationPowerPolicy(){}
    // This is wire validation only, never an eligibility allowlist. The server's
    // path policy decides which registered effects can enter a mutation snapshot.
    public static final int MAX_ENTRIES=2112;
    public static final int MAX_ENCODED=262144;
    public static boolean validId(String id){
        return id!=null&&id.length()<=120&&id.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }
    public static int strongest(int permanent,int temporary){return Math.max(permanent,temporary);}
    public static double supplemental(double desired,double nativeAmount,boolean multipliedTotal){
        return multipliedTotal?(1+desired)/(1+nativeAmount)-1:desired-nativeAmount;
    }
    public static int regenerationInterval(int amp){return Math.max(1,50>>Math.max(0,Math.min(9,amp)));}
    public static String encode(Map<String,Integer> values){
        if(values.size()>MAX_ENTRIES)throw new IllegalArgumentException("Too many mutation powers");
        var result=new java.util.ArrayList<String>();
        new TreeMap<>(values).forEach((id,amp)->{
            if(!validId(id)||amp<0||amp>9)throw new IllegalArgumentException("Invalid mutation power");
            result.add(id+"="+amp);
        });
        String encoded=String.join("\n",result);
        if(encoded.length()>MAX_ENCODED)throw new IllegalArgumentException("Oversized mutation power snapshot");
        return encoded;
    }
    public static Map<String,Integer> decode(String encoded){
        if(encoded==null||encoded.isEmpty())return Map.of();
        if(encoded.length()>MAX_ENCODED)throw new IllegalArgumentException("Oversized mutation power snapshot");
        Map<String,Integer> out=new TreeMap<>();
        for(String entry:encoded.split("\n",-1)){
            String[] f=entry.split("=",-1);
            if(f.length!=2||!validId(f[0])||!f[1].matches("[0-9]")||out.putIfAbsent(f[0],Integer.parseInt(f[1]))!=null)
                throw new IllegalArgumentException("Invalid mutation power snapshot");
        }
        if(out.size()>MAX_ENTRIES)throw new IllegalArgumentException("Too many mutation powers");
        return Map.copyOf(out);
    }
}
