package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.MutationPowers;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.RegenerationMobEffect;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** A weaker temporary regeneration potion cannot heal in addition to the permanent stronger mutation. */
@Mixin(RegenerationMobEffect.class)
public abstract class MutationRegenerationMixin {
    @Inject(method="applyEffectTick",at=@At("HEAD"),cancellable=true)
    private void tmm$strongestOnly(LivingEntity entity,int amp,CallbackInfoReturnable<Boolean> cir){
        if(MutationPowers.amplifier(entity,MobEffects.REGENERATION)>amp)cir.setReturnValue(true);
    }
}
