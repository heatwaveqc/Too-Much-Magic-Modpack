package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Prevent portable-bag recursion through vanilla bundle insertion. */
@Mixin(BundleContents.Mutable.class)
public abstract class VanillaBundleInsertionMixin {
    @Inject(method = "tryInsert(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectBag(ItemStack input, CallbackInfoReturnable<Integer> cir) {
        if (PortableContainerGuard.rejects(input)) cir.setReturnValue(0);
    }
}
