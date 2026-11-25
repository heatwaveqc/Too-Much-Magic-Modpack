package io.github.heatwaveqc.tmm.economy;

import dev.shadowsoffire.apotheosis.Apoth;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.components.DataComponentProcessor;
import moze_intel.projecte.api.components.IDataComponentProcessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;

/** No EMC for actual affixed or gem-filled socketed gear; leave ordinary tools alone. */
@DataComponentProcessor(requiredMods = {"apotheosis"}, priority = 1100)
public final class ApotheosisEquipmentGuard implements IDataComponentProcessor {
    @Override public String getName() { return "TMM Apotheosis Equipment Guard"; }
    @Override public String getTranslationKey() { return "tmm_economy.processor.affixed_gear"; }
    @Override public String getDescription() { return "Reject equipment with actual affixes or inserted gems."; }
    @Override public boolean hasPersistentComponents() { return true; }
    private static boolean denied(ItemStack s) {
        var affixes = s.get(Apoth.Components.AFFIXES);
        var gems = s.get(Apoth.Components.SOCKETED_GEMS);
        return (affixes != null && !affixes.isEmpty()) || (gems != null && gems.stream().anyMatch(x -> !x.isEmpty()));
    }
    @Override public long recalculateEMC(ItemInfo info, long currentEMC) {
        return denied(info.createStack()) ? 0 : currentEMC;
    }
    @Override public void collectPersistentComponents(ItemInfo info, DataComponentPatch.Builder b) {
        var s = info.createStack();
        if (!denied(s)) return;
        var affixes = s.get(Apoth.Components.AFFIXES);
        var gems = s.get(Apoth.Components.SOCKETED_GEMS);
        if (affixes != null) b.set(Apoth.Components.AFFIXES, affixes);
        if (gems != null) b.set(Apoth.Components.SOCKETED_GEMS, gems);
    }
}
