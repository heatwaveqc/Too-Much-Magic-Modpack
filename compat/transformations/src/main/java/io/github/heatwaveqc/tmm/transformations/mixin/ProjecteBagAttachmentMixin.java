package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * ProjectE's AlchemicalBagAttachment holds 16 concrete ItemStackHandlers.
 * Mark the original handler object returned by getBag, not a proxy: codecs,
 * per-color identity, saved contents and synchronization remain untouched.
 */
@Pseudo
@Mixin(targets = "moze_intel.projecte.impl.capability.AlchBagImpl$AlchemicalBagAttachment", remap = false)
public abstract class ProjecteBagAttachmentMixin {
    @Inject(method = "getBag(Lnet/minecraft/world/item/DyeColor;)Lnet/neoforged/neoforge/items/ItemStackHandler;",
            at = @At("RETURN"), require = 1, remap = false)
    private void tmm$markPlayerBag(DyeColor color, CallbackInfoReturnable<ItemStackHandler> cir) {
        PortableContainerGuard.markProjecteBag(cir.getReturnValue());
    }
}
