package io.github.heatwaveqc.tmm.economy;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.components.DataComponentProcessor;
import moze_intel.projecte.api.components.IDataComponentProcessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/** Filled Spirit Jars are non-EMC. Empty jars use their normal recipe.
 * No dependency on Malum classes and no bespoke Impetus durability logic.
 */
@DataComponentProcessor(requiredMods = {"malum"}, priority = 1001)
public final class MalumJarGuard implements IDataComponentProcessor {
    private static final ResourceLocation CONTENTS = ResourceLocation.fromNamespaceAndPath("malum", "spirit_jar_contents");

    @Override public String getName() { return "TMM Filled Spirit Jar Guard"; }
    @Override public String getTranslationKey() { return "tmm_economy.processor.spirit_jar_guard"; }
    @Override public String getDescription() { return "Do not value or strip stored spirits from a filled jar."; }
    @Override public boolean hasPersistentComponents() { return true; }

    @Override public long recalculateEMC(ItemInfo info, long currentEMC) {
        var type = BuiltInRegistries.DATA_COMPONENT_TYPE.getOptional(CONTENTS);
        return type.isPresent() && info.createStack().has(type.get()) ? 0 : currentEMC;
    }

    @Override public void collectPersistentComponents(ItemInfo info, DataComponentPatch.Builder builder) {
        // Retain the marker if a caller canonicalizes before valuation; do not turn a
        // filled jar into a valued empty jar by discarding contents.
        BuiltInRegistries.DATA_COMPONENT_TYPE.getOptional(CONTENTS)
                .ifPresent(type -> preserve(info.createStack(), type, builder));
    }

    private static <T> void preserve(ItemStack stack, DataComponentType<T> type, DataComponentPatch.Builder builder) {
        T contents = stack.get(type);
        if (contents != null) builder.set(type, contents);
    }
}
