package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.MutationPowers;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Native condition checks see permanent potency; HUD, cures and serialization still see only actual potion instances. */
@Mixin(LivingEntity.class)
public abstract class MutationEffectQueryMixin {
    @Inject(method="hasEffect",at=@At("RETURN"),cancellable=true)
    private void tmm$hasMutation(Holder<MobEffect> effect,CallbackInfoReturnable<Boolean> cir){
        if(!cir.getReturnValue()&&(MutationPowers.amplifier((LivingEntity)(Object)this,effect)>=0
            ||io.github.heatwaveqc.tyr.progression.runtime.AttackIdleDefense.scopedAmplifier((LivingEntity)(Object)this,effect)>=0))cir.setReturnValue(true);
    }
    @Inject(method="getEffect",at=@At("RETURN"),cancellable=true)
    private void tmm$mutation(Holder<MobEffect> effect,CallbackInfoReturnable<MobEffectInstance> cir){
        var result=MutationPowers.query((LivingEntity)(Object)this,effect,cir.getReturnValue());
        result=io.github.heatwaveqc.tyr.progression.runtime.AttackIdleDefense.query((LivingEntity)(Object)this,effect,result);
        if(result!=cir.getReturnValue())cir.setReturnValue(result);
    }
}
