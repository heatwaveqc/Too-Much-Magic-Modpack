package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.TomeTemplatePolicy;
import io.github.heatwaveqc.tmm.economy.RelicTemplatePolicy;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.gameObjs.block_entities.CondenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Read stale locks as their empty template so native output equality still works.
 * Both native condenser tiers use this final getter. */
@Mixin(value = CondenserBlockEntity.class, remap = false)
public abstract class TomeCondenserLockMixin {
    @Inject(method = "getLockInfo", at = @At("RETURN"), cancellable = true)
    private void tmm$normalizeTomeLock(CallbackInfoReturnable<ItemInfo> cir) {
        ItemInfo original = cir.getReturnValue();
        ItemInfo normalized = RelicTemplatePolicy.applies(original) ? original.itemOnly() : TomeTemplatePolicy.normalize(original);
        if (normalized != original) cir.setReturnValue(normalized);
    }
}
