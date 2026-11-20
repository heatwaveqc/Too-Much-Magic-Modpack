package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.storage.PortableContainerGuard;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Client menu's networking constructor creates a fresh handler; mark it too. */
@Pseudo
@Mixin(targets = "moze_intel.projecte.gameObjs.container.AlchBagContainer", remap = false)
public abstract class ProjecteAlchBagMenuMixin {
    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/InteractionHand;Lnet/neoforged/neoforge/items/IItemHandlerModifiable;IZ)V",
            at = @At("TAIL"), require = 1, remap = false)
    private void tmm$markMenuBag(int id, Inventory inventory, InteractionHand hand,
                                 IItemHandlerModifiable bag, int selected, boolean immutable,
                                 CallbackInfo ci) {
        if (bag instanceof ItemStackHandler original) PortableContainerGuard.markProjecteBag(original);
    }
}
