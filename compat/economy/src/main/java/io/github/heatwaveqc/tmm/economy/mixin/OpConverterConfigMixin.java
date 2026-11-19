package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.converter.OpExchange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Native 0.1.4 config only represents whole input per one output.
 * Forward ratio 1 is an EMC batch size for accrue(), NOT an OP price.
 * Ledger/port/display mixins below are mandatory companions. */
@Pseudo
@Mixin(targets = "dev.glorps.energy.Config", remap = false)
public abstract class OpConverterConfigMixin {
    @Inject(method = "ratio()J", at = @At("HEAD"), cancellable = true)
    private static void tmm$forwardBatch(CallbackInfoReturnable<Long> cir) {
        cir.setReturnValue(1L);
    }

    @Inject(method = "reverseRatio()J", at = @At("HEAD"), cancellable = true)
    private static void tmm$reverseRate(CallbackInfoReturnable<Long> cir) {
        cir.setReturnValue((long) OpExchange.OP_PER_EMC_IN);
    }
}
