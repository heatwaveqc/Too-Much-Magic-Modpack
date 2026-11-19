package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.TomeTemplatePolicy;
import io.github.heatwaveqc.tmm.economy.RelicTemplatePolicy;
import moze_intel.projecte.api.ItemInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Same lock semantics for Project Expansion's optional MK3 sided handlers. */
@Pseudo
@Mixin(targets = "cool.furry.mc.neoforge.projectexpansion.block.entity.BlockEntityCondenserMK3$SidedHandler", remap = false)
public abstract class ExpansionTomeCondenserLockMixin {
    @Inject(method = "getLockInfo", at = @At("RETURN"), cancellable = true)
    private void tmm$normalizeTomeLock(CallbackInfoReturnable<ItemInfo> cir) {
        ItemInfo original = cir.getReturnValue();
        ItemInfo normalized = RelicTemplatePolicy.applies(original) ? original.itemOnly() : TomeTemplatePolicy.normalize(original);
        if (normalized != original) cir.setReturnValue(normalized);
    }
}
