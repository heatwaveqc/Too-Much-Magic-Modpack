package io.github.heatwaveqc.tmm.economy;

import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.socket.gem.GemItem;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.components.DataComponentProcessor;
import moze_intel.projecte.api.components.IDataComponentProcessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

/** Keep only the two canonical identity components in supported gems. */
@DataComponentProcessor(requiredMods = {"apotheosis"}, priority = 1000)
public final class ApotheosisGemComponentGuard implements IDataComponentProcessor {
    @Override public String getName() { return "TMM Gem Component Guard"; }
    @Override public String getTranslationKey() { return "tmm_economy.processor.gem_guard"; }
    @Override public String getDescription() { return "Reject unbound, invalid-purity, or noncanonical gem stacks."; }
    @Override public boolean hasPersistentComponents() { return true; }

    @Override public long recalculateEMC(ItemInfo info, long currentEMC) {
        ItemStack stack = info.createStack();
        if (!stack.is(Apoth.Items.GEM.value())) return currentEMC;
        var holder = GemItem.getGem(stack);
        if (!holder.isBound()) return 0;
        var purity = GemItem.getPurity(stack);
        var gem = holder.get();
        if (!purity.isAtLeast(gem.getMinPurity())) return 0;
        return stack.getComponentsPatch().equals(gem.toStack(purity).getComponentsPatch()) ? currentEMC : 0;
    }

    @Override public void collectPersistentComponents(ItemInfo info, DataComponentPatch.Builder builder) {
        ItemStack stack = info.createStack();
        if (!stack.is(Apoth.Items.GEM.value())) return;
        var holder = GemItem.getGem(stack);
        var purity = GemItem.getPurity(stack);
        if (holder.isBound() && purity.isAtLeast(holder.get().getMinPurity())
                && stack.getComponentsPatch().equals(holder.get().toStack(purity).getComponentsPatch())) {
            builder.set(Apoth.Components.GEM, holder);
            builder.set(Apoth.Components.PURITY, purity);
        }
    }
}
