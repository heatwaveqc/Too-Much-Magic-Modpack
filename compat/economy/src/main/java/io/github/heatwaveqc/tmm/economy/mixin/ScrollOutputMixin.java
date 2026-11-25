package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import moze_intel.projecte.gameObjs.container.slots.transmutation.SlotOutput;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SlotOutput.class, remap = false)
public abstract class ScrollOutputMixin {
    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void tmm$denyStaleTake(int amount, CallbackInfoReturnable<ItemStack> cir) {
        if (ScrollKnowledgePolicy.isScroll(((Slot)(Object)this).getItem())) cir.setReturnValue(ItemStack.EMPTY);
    }
    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void tmm$denyStalePickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (ScrollKnowledgePolicy.isScroll(((Slot)(Object)this).getItem())) cir.setReturnValue(false);
    }
}
