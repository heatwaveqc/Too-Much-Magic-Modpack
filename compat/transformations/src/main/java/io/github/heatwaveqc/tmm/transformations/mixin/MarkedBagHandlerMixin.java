package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Tag check happens in normal NeoForge ItemStackHandler.insertItem, before a
 * stack is moved. Only handler instances identified as ProjectE bag storage
 * are restricted; no global handler ban (chests/machines remain usable).
 */
@Mixin(ItemStackHandler.class)
public abstract class MarkedBagHandlerMixin {
    @Inject(method = "isItemValid(ILnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$validatePortableBagInput(int slot, ItemStack stack,
                                                CallbackInfoReturnable<Boolean> cir) {
        if (PortableContainerGuard.isProjecteBag((ItemStackHandler)(Object)this)
                && PortableContainerGuard.rejects(stack)) cir.setReturnValue(false);
    }
}
