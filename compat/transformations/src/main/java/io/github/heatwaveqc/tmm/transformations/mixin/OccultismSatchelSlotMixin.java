package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Reject cross-mod bags before any normal Occultism satchel GUI slot transfer. */
@Pseudo
@Mixin(targets = "com.klikli_dev.occultism.common.container.satchel.StorageSatchelSlot", remap = false)
public abstract class OccultismSatchelSlotMixin {
    @Inject(method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 1, remap = false)
    private void tmm$rejectNestedBag(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (PortableContainerGuard.rejects(stack)) cir.setReturnValue(false);
    }
}
