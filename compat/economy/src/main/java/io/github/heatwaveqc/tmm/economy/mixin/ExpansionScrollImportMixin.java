package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Both import paths call these only in an executing, credited transaction. */
@Pseudo
@Mixin(targets = {
    "cool.furry.mc.neoforge.projectexpansion.block.entity.BlockEntityEMCLink$ItemHandler",
    "cool.furry.mc.neoforge.projectexpansion.block.entity.BlockEntityTransmutationInterface$ItemHandler"
}, remap = false)
public abstract class ExpansionScrollImportMixin {
    @Redirect(method = "insertItem", at = @At(value = "INVOKE",
            target = "Lmoze_intel/projecte/api/capabilities/IKnowledgeProvider;addKnowledge(Lnet/minecraft/world/item/ItemStack;)Z"))
    private boolean tmm$learnInk(IKnowledgeProvider provider, ItemStack stack) {
        return ScrollKnowledgePolicy.learnAfterBurn(provider, stack);
    }
    @Redirect(method = "insertItem", at = @At(value = "INVOKE",
            target = "Lmoze_intel/projecte/api/capabilities/IKnowledgeProvider;syncKnowledgeChange(Lnet/minecraft/server/level/ServerPlayer;Lmoze_intel/projecte/api/ItemInfo;Z)V"))
    private void tmm$syncInk(IKnowledgeProvider provider, ServerPlayer player, ItemInfo info, boolean learned) {
        ScrollKnowledgePolicy.syncAfterBurn(provider, player, info, learned);
    }
}
