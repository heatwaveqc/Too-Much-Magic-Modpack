package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import cool.furry.mc.neoforge.projectexpansion.util.Util;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.gameObjs.container.inventory.TransmutationInventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "cool.furry.mc.neoforge.projectexpansion.gui.container.ContainerArcaneTransmutationTablet", remap = false)
public abstract class ExpansionScrollTabletMixin {
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
    @Redirect(method = "clearCrafting", at = @At(value = "INVOKE",
            target = "Lcool/furry/mc/neoforge/projectexpansion/util/Util;addKnowledge(Lnet/minecraft/world/entity/player/Player;Lmoze_intel/projecte/api/capabilities/IKnowledgeProvider;Lnet/minecraft/world/item/ItemStack;)Lcool/furry/mc/neoforge/projectexpansion/util/Util$AddKnowledgeResult;"))
    private Util.AddKnowledgeResult tmm$learnCraftBurn(Player player, IKnowledgeProvider provider, ItemStack stack) {
        if (!ScrollKnowledgePolicy.isScroll(stack)) return Util.addKnowledge(player, provider, stack);
        var ink = ScrollKnowledgePolicy.burnKnowledge(stack);
        if (ink.isEmpty()) return Util.AddKnowledgeResult.FAIL;
        var result = Util.addKnowledge(player, provider, ink);
        if (result.success() && player instanceof ServerPlayer server)
            provider.syncKnowledgeChange(server, ItemInfo.fromStack(ink), true);
        return result;
    }
}
