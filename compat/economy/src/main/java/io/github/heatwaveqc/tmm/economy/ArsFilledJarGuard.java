package io.github.heatwaveqc.tmm.economy;

import com.hollingsworth.arsnouveau.common.items.data.BlockFillContents;
import com.hollingsworth.arsnouveau.setup.registry.DataComponentRegistry;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.components.DataComponentProcessor;
import moze_intel.projecte.api.components.IDataComponentProcessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;

/** Native Source jars remain portable; charged variants are not EMC items.
 * Preserve the fill marker through persistent-info canonicalization so the
 * condenser cannot copy contents for the empty jar price. */
@DataComponentProcessor(requiredMods = {"ars_nouveau"}, priority = 1101)
public final class ArsFilledJarGuard implements IDataComponentProcessor {
    @Override public String getName() { return "TMM Filled Source Jar Guard"; }
    @Override public String getTranslationKey() { return "tmm_economy.processor.source_jar_guard"; }
    @Override public String getDescription() { return "Reject filled Source jars while retaining their fill components."; }
    @Override public boolean hasPersistentComponents() { return true; }
    private static boolean isJar(ItemInfo info) {
        String id = BuiltInRegistries.ITEM.getKey(info.getItem().value()).toString();
        return id.equals("ars_nouveau:source_jar") || id.equals("tmm_economy:emc_source_jar");
    }
    @Override public long recalculateEMC(ItemInfo info, long currentEMC) {
        return isJar(info) && BlockFillContents.get(info.createStack()) != 0 ? 0 : currentEMC;
    }
    @Override public void collectPersistentComponents(ItemInfo info, DataComponentPatch.Builder builder) {
        if (!isJar(info)) return;
        var contents = info.createStack().get(DataComponentRegistry.BLOCK_FILL_CONTENTS);
        if (contents != null && contents.amount() != 0)
            builder.set(DataComponentRegistry.BLOCK_FILL_CONTENTS.get(), contents);
    }
}
