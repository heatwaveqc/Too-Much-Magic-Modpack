package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import cool.furry.mc.neoforge.projectexpansion.util.Util;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(targets = "cool.furry.mc.neoforge.projectexpansion.util.Util", remap = false)
public abstract class ExpansionScrollReturnMixin {
    @Redirect(method = "returnToInventoryOrTransmutation(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/entity/player/Player;Lmoze_intel/projecte/api/capabilities/IKnowledgeProvider;Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/item/ItemStack;",
            at = @At(value = "INVOKE", target = "Lcool/furry/mc/neoforge/projectexpansion/util/Util;addKnowledge(Lnet/minecraft/world/entity/player/Player;Lmoze_intel/projecte/api/capabilities/IKnowledgeProvider;Lnet/minecraft/world/item/ItemStack;)Lcool/furry/mc/neoforge/projectexpansion/util/Util$AddKnowledgeResult;"))
    private static Util.AddKnowledgeResult tmm$learnReturnBurn(Player player, IKnowledgeProvider provider, ItemStack stack) {
        if (!ScrollKnowledgePolicy.isScroll(stack)) return Util.addKnowledge(player, provider, stack);
        var ink = ScrollKnowledgePolicy.burnKnowledge(stack);
        if (ink.isEmpty()) return Util.AddKnowledgeResult.FAIL;
        var result = Util.addKnowledge(player, provider, ink);
        if (result.success() && player instanceof ServerPlayer server)
            provider.syncKnowledgeChange(server, ItemInfo.fromStack(ink), true);
        return result;
    }
}
