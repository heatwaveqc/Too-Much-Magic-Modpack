package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.CaptureInstancePolicy;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Skip only the native Capturing handler; ordinary death drops and Echoing remain untouched. */
@Pseudo
@Mixin(targets = "dev.shadowsoffire.apothic_spawners.ASEvents", remap = false)
public abstract class CapturingForemanMixin {
    @Inject(method = "handleCapturing(Lnet/neoforged/neoforge/event/entity/living/LivingDropsEvent;)V",
            at = @At("HEAD"), cancellable = true)
    private void tmm$denyForeman(LivingDropsEvent event, CallbackInfo ci) {
        if (CaptureInstancePolicy.denyForeman(event.getEntity())) ci.cancel();
    }
}
