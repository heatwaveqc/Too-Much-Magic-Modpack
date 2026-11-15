package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichDragonFirstEvents;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Prevent duplicate Dragon Survival passive activation after our early dispatch. */
@Pseudo
@Mixin(targets = "by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.activation.trigger.OnDeath",
        remap = false)
public abstract class DragonSurvivalDeathRepeatMixin {
    @Inject(method = "trigger(Lnet/neoforged/neoforge/event/entity/living/LivingDeathEvent;)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void tmm$singleDragonAttempt(LivingDeathEvent event, CallbackInfo ci) {
        if (DracolichDragonFirstEvents.consumeNativeRepeat(event)) ci.cancel();
    }
}
