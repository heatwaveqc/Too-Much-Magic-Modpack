package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.TomeTemplatePolicy;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.emc.components.DataComponentManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Normalize before the exact-component lookup, whitelist and processors.
 * The charged original remains intact until the native transaction consumes it. */
@Mixin(value = DataComponentManager.class, remap = false)
public abstract class TomeTemplateMixin {
    @Inject(method = "getPersistentInfo", at = @At("HEAD"), cancellable = true)
    private static void tmm$emptyTome(ItemInfo info, CallbackInfoReturnable<ItemInfo> cir) {
        if (TomeTemplatePolicy.applies(info)) cir.setReturnValue(TomeTemplatePolicy.normalize(info));
    }

    @Inject(method = "getEmcValue", at = @At("HEAD"), cancellable = true)
    private static void tmm$baseTomeValue(ItemInfo info, CallbackInfoReturnable<Long> cir) {
        if (info.hasModifiedComponents() && TomeTemplatePolicy.applies(info))
            cir.setReturnValue(DataComponentManager.getEmcValue(info.itemOnly()));
    }
}
