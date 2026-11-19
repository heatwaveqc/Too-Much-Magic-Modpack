package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ScrollKnowledgePolicy;
import moze_intel.projecte.gameObjs.container.inventory.TransmutationInventory;
import moze_intel.projecte.gameObjs.container.slots.transmutation.SlotConsume;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SlotConsume.class, remap = false)
public abstract class ScrollConsumeMixin {
    @Redirect(method = "set", at = @At(value = "INVOKE",
            target = "Lmoze_intel/projecte/gameObjs/container/inventory/TransmutationInventory;handleKnowledge(Lnet/minecraft/world/item/ItemStack;)V"))
    private void tmm$learnInk(TransmutationInventory inv, ItemStack stack) {
        var learned = ScrollKnowledgePolicy.burnKnowledge(stack);
        if (!learned.isEmpty()) inv.handleKnowledge(learned);
    }
}
