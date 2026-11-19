package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import moze_intel.projecte.gameObjs.container.TransmutationContainer;
import moze_intel.projecte.gameObjs.container.inventory.TransmutationInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TransmutationContainer.class, remap = false)
public abstract class ScrollQuickMoveMixin {
    @Redirect(method = "quickMoveStack", at = @At(value = "INVOKE",
            target = "Lmoze_intel/projecte/gameObjs/container/inventory/TransmutationInventory;handleKnowledge(Lnet/minecraft/world/item/ItemStack;)V"))
    private void tmm$learnInk(TransmutationInventory inv, ItemStack stack) {
        var learned = ScrollKnowledgePolicy.burnKnowledge(stack);
        if (!learned.isEmpty()) inv.handleKnowledge(learned);
    }
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void tmm$denyStaleOutput(Player player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot >= 11 && slot <= 26 && ScrollKnowledgePolicy.isScroll(
                ((AbstractContainerMenu)(Object)this).getSlot(slot).getItem())) cir.setReturnValue(ItemStack.EMPTY);
    }
}
