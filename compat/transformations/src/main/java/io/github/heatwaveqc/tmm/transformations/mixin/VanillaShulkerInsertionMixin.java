package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Preserve vanilla shulker checks; additionally reject all portable bags. */
@Mixin(ShulkerBoxBlockEntity.class)
public abstract class VanillaShulkerInsertionMixin {
    @Inject(method = "canPlaceItem(ILnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectBag(int slot, ItemStack input, CallbackInfoReturnable<Boolean> cir) {
        if (PortableContainerGuard.rejects(input)) cir.setReturnValue(false);
    }
}
