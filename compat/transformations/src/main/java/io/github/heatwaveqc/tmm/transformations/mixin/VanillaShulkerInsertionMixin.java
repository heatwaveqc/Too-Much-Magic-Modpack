package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Reject tagged bags in vanilla shulker GUI slots, preserving native rules. */
@Mixin(ShulkerBoxSlot.class)
public abstract class VanillaShulkerInsertionMixin {
    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectBag(ItemStack input, CallbackInfoReturnable<Boolean> cir) {
        if (PortableContainerGuard.rejects(input)) cir.setReturnValue(false);
    }
}
