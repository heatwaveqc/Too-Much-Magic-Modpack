package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Soulwoven, Ravenous and Gaze's inherited Soulwoven payload all use this
 * mutable insertion implementation. Both entry points must be guarded:
 * tryTransfer calls safeTake BEFORE tryInsert; guarding only tryInsert would
 * remove an item from the source slot and silently lose it.
 *
 * No blanket change to native Malum weight/sorting behavior for safe inputs.
 */
@Pseudo
@Mixin(targets = "com.sammy.malum.common.data.component.pouch.MalumPouchContentsComponent$Mutable",
       remap = false)
public abstract class PortableStorageMalumPouchMixin {
    @Inject(method = "tryInsert(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$rejectUnsafeCursorStack(ItemStack source, CallbackInfoReturnable<Integer> cir) {
        if (PortableStorageSafety.rejectsEmbeddedDestination(source)) cir.setReturnValue(0);
    }

    @Inject(method = "tryTransfer(Lnet/minecraft/world/inventory/Slot;Lnet/minecraft/world/entity/player/Player;)I",
            at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$rejectBeforeSafeTake(Slot slot, Player player, CallbackInfoReturnable<Integer> cir) {
        if (PortableStorageSafety.rejectsEmbeddedDestination(slot.getItem())) cir.setReturnValue(0);
    }
}
