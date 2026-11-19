package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.converter.OpExchange;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 0.1.4 ledger forward path: return the actual spent EMC, as upstream
 * tick subtracts this quantity from conversionCredit. Reverse is native. */
@Pseudo
@Mixin(targets = "dev.glorps.energy.Ledger", remap = false)
public abstract class OpConverterLedgerMixin {
    @Shadow public long emc;
    @Shadow public long energy;

    @Inject(method = "convert(ZJJJJ)J", at = @At("HEAD"), cancellable = true)
    private void tmm$convertForward(boolean toEnergy, long ratio, long budget,
                                    long emcCapacity, long energyCapacity,
                                    CallbackInfoReturnable<Long> cir) {
        if (!toEnergy) return;
        if (ratio != 1 || budget < 0 || emcCapacity < 1 || energyCapacity < 1
                || emc < 0 || energy < 0)
            throw new IllegalArgumentException("Invalid TMM OP conversion limits");
        long freeOp = energy >= energyCapacity ? 0 : energyCapacity - energy;
        long spent = OpExchange.forwardEmc(emc, freeOp, budget);
        long produced = Math.multiplyExact(spent, (long) OpExchange.OP_PER_EMC_OUT);
        // Both results fit their real buffers before either field is mutated.
        long nextEmc = emc - spent;
        long nextEnergy = Math.addExact(energy, produced);
        emc = nextEmc;
        energy = nextEnergy;
        cir.setReturnValue(spent);
    }
}
