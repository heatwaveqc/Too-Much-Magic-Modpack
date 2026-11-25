package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.StoredResourcePolicy;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.emc.components.DataComponentManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Mandatory, before exact mappings, whitelist and configurable processors. */
@Mixin(value = DataComponentManager.class, remap = false)
public abstract class StoredResourceGuardMixin {
    @Inject(method = "getPersistentInfo", at = @At("HEAD"), cancellable = true)
    private static void tmm$keepUnknownFilledState(ItemInfo info, CallbackInfoReturnable<ItemInfo> cir) {
        if (StoredResourcePolicy.held(info)) cir.setReturnValue(info);
    }
    @Inject(method = "getEmcValue", at = @At("HEAD"), cancellable = true)
    private static void tmm$denyUnknownFilledState(ItemInfo info, CallbackInfoReturnable<Long> cir) {
        if (StoredResourcePolicy.held(info)) cir.setReturnValue(0L);
    }
}
