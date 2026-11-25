package io.github.heatwaveqc.tmm.economy.mixin;

import dev.glorps.energy.ConverterEntity;
import io.github.heatwaveqc.tmm.economy.converter.OpExchange;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Replace the pinned converter's hard-coded status text with the true rates. */
@Pseudo
@Mixin(targets = "dev.glorps.energy.ConverterEntity", remap = false)
public abstract class OpConverterStatusMixin {
    @Inject(method = "status()Lnet/minecraft/network/chat/Component;", at = @At("HEAD"), cancellable = true)
    private void tmm$showOp(CallbackInfoReturnable<Component> cir) {
        ConverterEntity machine = (ConverterEntity)(Object)this;
        String ratio = machine.toEnergy()
                ? "1 EMC → " + OpExchange.OP_PER_EMC_OUT + " OP"
                : OpExchange.OP_PER_EMC_IN + " OP → 1 EMC";
        boolean paused = machine.getLevel() != null && machine.getLevel().hasNeighborSignal(machine.getBlockPos());
        cir.setReturnValue(Component.literal(String.format(Locale.ROOT,
                "%,d EMC · %,d OP · %,d EMC/t · %s%s", machine.ledger.emc,
                machine.ledger.energy, machine.tier().emcRate(), ratio, paused ? " · PAUSED" : "")));
    }
}
