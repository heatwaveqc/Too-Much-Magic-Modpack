package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Shulker sided automation must obey the same policy as GUI insertion. */
@Mixin(ShulkerBoxBlockEntity.class)
public abstract class VanillaShulkerHopperMixin {
    @Inject(method = "canPlaceItemThroughFace(ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Z",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectBag(int slot, ItemStack input, Direction direction,
                                CallbackInfoReturnable<Boolean> cir) {
        if (PortableContainerGuard.rejects(input)) cir.setReturnValue(false);
    }
}
