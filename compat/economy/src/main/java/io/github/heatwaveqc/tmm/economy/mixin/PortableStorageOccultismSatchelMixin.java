package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Augment the native Occultism satchel slot check, never replace it. */
@Pseudo
@Mixin(targets = "com.klikli_dev.occultism.common.container.satchel.StorageSatchelSlot", remap = false)
public abstract class PortableStorageOccultismSatchelMixin {
    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$rejectUnsafeEmbedded(ItemStack input, CallbackInfoReturnable<Boolean> cir) {
        if (PortableStorageSafety.rejectsEmbeddedDestination(input)) cir.setReturnValue(false);
    }
}
