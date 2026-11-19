package io.github.heatwaveqc.tmm.economy;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.components.DataComponentProcessor;
import moze_intel.projecte.api.components.IDataComponentProcessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

/** Do not duplicate or burn filled vanilla storage and written/filled books.
 * Empty containers, blank writable books, regular enchantments and repairs retain native handling. */
@DataComponentProcessor(priority = 1100)
public final class VanillaStateGuard implements IDataComponentProcessor {
    @Override public String getName() { return "TMM Vanilla State Guard"; }
    @Override public String getTranslationKey() { return "tmm_economy.processor.vanilla_state"; }
    @Override public String getDescription() { return "Reject filled storage, filled books and intrinsically excluded items."; }
    @Override public boolean hasPersistentComponents() { return true; }
    private static boolean denied(ItemStack s) {
        if (ApprovedExclusionsMapper.IDS.contains(BuiltInRegistries.ITEM.getKey(s.getItem()).toString())) return true;
        var container = s.get(DataComponents.CONTAINER);
        var bundle = s.get(DataComponents.BUNDLE_CONTENTS);
        var writable = s.get(DataComponents.WRITABLE_BOOK_CONTENT);
        return (container != null && container.stream().anyMatch(x -> !x.isEmpty()))
                || (bundle != null && !bundle.isEmpty())
                || (writable != null && !writable.pages().isEmpty())
                || s.has(DataComponents.WRITTEN_BOOK_CONTENT);
    }
    @Override public long recalculateEMC(ItemInfo info, long currentEMC) {
        return denied(info.createStack()) ? 0 : currentEMC;
    }
    @Override public void collectPersistentComponents(ItemInfo info, DataComponentPatch.Builder b) {
        ItemStack s = info.createStack();
        if (!denied(s)) return;
        retain(s, DataComponents.CONTAINER, b);
        retain(s, DataComponents.BUNDLE_CONTENTS, b);
        retain(s, DataComponents.WRITABLE_BOOK_CONTENT, b);
        retain(s, DataComponents.WRITTEN_BOOK_CONTENT, b);
    }
    private static <T> void retain(ItemStack s, DataComponentType<T> type, DataComponentPatch.Builder b) {
        T value = s.get(type);
        if (value != null) b.set(type, value);
    }
}
