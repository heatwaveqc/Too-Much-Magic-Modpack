package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.foundation.MagicSkillMath;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

/** Reusable server-owned modifier queries, not a second mana/XP system.
 * Callers must identify the exact native action, only apply once, and retain native bounds/sync. */
public final class MagicSkillRuntime {
    private MagicSkillRuntime(){}
    public static double contribution(ServerPlayer player,String type,String source,double limit){
        if(!MxpPlayerPolicy.eligible(player)||NewbieMode.isNewbie(player)||source==null||source.isEmpty())return 0;
        double sum=0;
        for(var active:EffectRuntime.active(player)){
            var effect=active.effect();
            if(effect.type().equals(type)&&effect.selector().equals(source))
                sum+=effect.amountPerRank()*active.rank();
        }
        return MagicSkillMath.cap(sum,limit);
    }
    public static double capacity(ServerPlayer p,String source,double nativeCapacity,double limit){
        return MagicSkillMath.increase(nativeCapacity,contribution(p,"magic_resource_capacity",source,limit),limit);
    }
    public static double recovery(ServerPlayer p,String source,double nativeRecovery,double limit){
        return MagicSkillMath.increase(nativeRecovery,contribution(p,"magic_resource_recovery",source,limit),limit);
    }
    public static double potency(ServerPlayer p,String source,double nativeMagnitude,double limit){
        return MagicSkillMath.increase(nativeMagnitude,contribution(p,"magic_potency",source,limit),limit);
    }
    public static long castingTime(ServerPlayer p,String source,long nativeTime,double limit){
        return MagicSkillMath.reduce(nativeTime,contribution(p,"magic_cast_time_reduction",source,limit),limit);
    }
    public static long cooldown(ServerPlayer p,String source,long nativeTime,double limit){
        return MagicSkillMath.reduce(nativeTime,contribution(p,"magic_cooldown_reduction",source,limit),limit);
    }
    /** Exact configured damage type only; no item check, no wildcard/tag-wide Tyr bonus.
     * Native equipment modifiers still run in the owning mod's own listener. */
    public static void damage(LivingDamageEvent.Pre event){
        if(!(event.getSource().getEntity() instanceof ServerPlayer p)||p==event.getEntity())return;
        if(!event.getSource().is(net.neoforged.neoforge.common.Tags.DamageTypes.IS_MAGIC))return;
        var key=event.getSource().typeHolder().unwrapKey();
        if(key.isEmpty())return;
        double original=event.getNewDamage();
        double adjusted=potency(p,key.get().location().toString(),original,1);
        if(adjusted!=original&&Double.isFinite(adjusted))
            event.setNewDamage((float)Math.min(Float.MAX_VALUE,adjusted));
    }
}
