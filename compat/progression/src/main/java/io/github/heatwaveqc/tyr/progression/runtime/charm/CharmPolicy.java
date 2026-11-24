package io.github.heatwaveqc.tyr.progression.runtime.charm;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.runtime.resources.ResourcePolicy;
import java.util.Set;

/** Pure, bounded duration/eligibility rules for the generic Charm effect. */
public final class CharmPolicy {
    private CharmPolicy(){}
    public static final int DURATION=600, DEFAULT_COOLDOWN=40, MAX_COOLDOWN=72000;
    private static final Set<String> SUPPORTED=Set.of("minecraft:pillager","minecraft:vindicator",
        "minecraft:zombie","minecraft:skeleton");
    public static boolean supported(String entity){return SUPPORTED.contains(entity);}
    public record Spec(int cooldown,boolean sneaking,String resource,int cost){
        public Spec{
            if(cooldown<1||cooldown>MAX_COOLDOWN||resource==null||cost<0||cost>ResourcePolicy.MAX
                    ||(cost==0)!=resource.isEmpty()||(!resource.isEmpty()&&!ResourcePolicy.validId(resource)))
                throw new IllegalArgumentException("Invalid Charm cost/cooldown");
        }
    }
    public static Spec validate(Definition.Effect fx){
        if(!fx.selector().matches("#?[a-z0-9_.-]+:[a-z0-9_./-]+")||fx.amountPerRank()!=0
                ||!fx.attribute().isEmpty()||!fx.weapon().isEmpty()||!fx.damageTag().isEmpty())
            throw new IllegalArgumentException("Charm requires an explicit entity ID/tag and no numeric rank effect");
        var p=fx.params();
        for(String key:p.keySet())if(!Set.of("cooldown_ticks","requires_sneaking","resource","resource_cost",
            "requires_milestone","requires_sprinting","requires_not_sneaking","passive").contains(key))
            throw new IllegalArgumentException("Unknown Charm parameter "+key);
        String sneaking=p.getOrDefault("requires_sneaking","false");
        if(!sneaking.equals("true")&&!sneaking.equals("false"))throw new IllegalArgumentException("Invalid sneak gate");
        return new Spec(Integer.parseInt(p.getOrDefault("cooldown_ticks","40")),
            Boolean.parseBoolean(sneaking),p.getOrDefault("resource",""),Integer.parseInt(p.getOrDefault("resource_cost","0")));
    }
    public static long until(long now,int ticks){
        if(now<0||ticks<1||ticks>MAX_COOLDOWN||now>Long.MAX_VALUE-ticks)
            throw new IllegalArgumentException("Invalid Charm clock");
        return now+ticks;
    }
    public static boolean active(long now,long expires){
        return now>=0&&expires>now&&expires-now<=DURATION;
    }
    public static boolean cooldownReady(long now,long expires){
        return now>=0&&(expires<=now||expires-now>MAX_COOLDOWN);
    }
    public static boolean breaksOnDamage(float actual){
        return Float.isFinite(actual)&&actual>0;
    }
}
