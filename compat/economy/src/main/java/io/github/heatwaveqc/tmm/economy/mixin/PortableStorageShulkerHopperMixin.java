package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Sided shulker insertion, without a global container/handler hook. */
@Mixin(ShulkerBoxBlockEntity.class)
public abstract class PortableStorageShulkerHopperMixin {
    @Inject(method = "canPlaceItemThroughFace(ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectUnsafeEmbedded(int slot, ItemStack input, Direction side,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (PortableStorageSafety.rejectsEmbeddedDestination(input)) cir.setReturnValue(false);
    }
}
