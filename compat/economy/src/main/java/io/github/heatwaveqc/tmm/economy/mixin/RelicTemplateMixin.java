package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.RelicTemplatePolicy;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.emc.components.DataComponentManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Normalize before whitelist and exact-component persistence branches.
 * Fresh output has native default components; Relics rolls missing initial
 * qualities itself. Do not mutate or reroll the physical input stack. */
@Mixin(value = DataComponentManager.class, remap = false)
public abstract class RelicTemplateMixin {
    @Inject(method = "getPersistentInfo", at = @At("HEAD"), cancellable = true)
    private static void tmm$freshRelic(ItemInfo info, CallbackInfoReturnable<ItemInfo> cir) {
        if (RelicTemplatePolicy.applies(info)) cir.setReturnValue(info.itemOnly());
    }
    @Inject(method = "getEmcValue", at = @At("HEAD"), cancellable = true)
    private static void tmm$flatRelic(ItemInfo info, CallbackInfoReturnable<Long> cir) {
        if (info.hasModifiedComponents() && RelicTemplatePolicy.applies(info))
            cir.setReturnValue(DataComponentManager.getEmcValue(info.itemOnly()));
    }
}
