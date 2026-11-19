package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.CaptureInstancePolicy;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Instance exception only. Do not cancel the actual death event or other rites. */
@Pseudo
@Mixin(targets = "com.breakinblocks.neovitae.common.event.ImprisonmentArrayHandler", remap = false)
public abstract class NeoImprisonmentMixin {
    @Inject(method = "onLivingDeath(Lnet/neoforged/neoforge/event/entity/living/LivingDeathEvent;)V",
            at = @At("HEAD"), cancellable = true)
    private static void tmm$denyForeman(LivingDeathEvent event, CallbackInfo ci) {
        if (CaptureInstancePolicy.denyForeman(event.getEntity())) ci.cancel();
    }
}
