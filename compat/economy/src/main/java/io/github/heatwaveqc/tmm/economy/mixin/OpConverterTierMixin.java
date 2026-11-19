package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.converter.OpExchange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Forward OP port rate and capacity must scale with produced OP, not with
 * the internal one-EMC batch marker. Preserve tier EMC rate and reverse ports. */
@Pseudo
@Mixin(targets = "dev.glorps.energy.Tier", remap = false)
public abstract class OpConverterTierMixin {
    @Shadow public abstract long emcRate();
    @Shadow public abstract long emcCapacity();

    @Inject(method = "energyRate(Z)J", at = @At("HEAD"), cancellable = true)
    private void tmm$opRate(boolean toEnergy, CallbackInfoReturnable<Long> cir) {
        if (toEnergy) cir.setReturnValue(Math.multiplyExact(emcRate(), (long) OpExchange.OP_PER_EMC_OUT));
    }

    @Inject(method = "energyCapacity(Z)J", at = @At("HEAD"), cancellable = true)
    private void tmm$opCapacity(boolean toEnergy, CallbackInfoReturnable<Long> cir) {
        if (toEnergy) cir.setReturnValue(Math.multiplyExact(emcCapacity(), (long) OpExchange.OP_PER_EMC_OUT));
    }
}
