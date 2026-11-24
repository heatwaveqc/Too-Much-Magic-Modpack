package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Preserve vanilla's mayPlace policy; add a narrow pre-transfer cross-mod veto. */
@Mixin(ShulkerBoxSlot.class)
public abstract class PortableStorageShulkerSlotMixin {
    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectUnsafeEmbedded(ItemStack input, CallbackInfoReturnable<Boolean> cir) {
        if (PortableStorageSafety.rejectsEmbeddedDestination(input)) cir.setReturnValue(false);
    }
}
