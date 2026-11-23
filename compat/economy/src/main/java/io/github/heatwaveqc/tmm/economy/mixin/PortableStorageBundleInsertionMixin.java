package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Native bundle weight/native acceptance remain authoritative for safe inputs. */
@Mixin(BundleContents.Mutable.class)
public abstract class PortableStorageBundleInsertionMixin {
    @Inject(method = "tryInsert(Lnet/minecraft/world/item/ItemStack;)I",
            at = @At("HEAD"), cancellable = true, require = 1)
    private void tmm$rejectUnsafeEmbedded(ItemStack input, CallbackInfoReturnable<Integer> cir) {
        if (PortableStorageSafety.rejectsEmbeddedDestination(input)) cir.setReturnValue(0);
    }
}
