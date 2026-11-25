package io.github.heatwaveqc.tmm.economy;

import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.components.DataComponentProcessor;
import moze_intel.projecte.api.components.IDataComponentProcessor;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

/** Protect component-bearing eggs and blocked typed Reliquary items.
 * This is an EMC guard, not a replacement for Reliquary gameplay eligibility. */
@DataComponentProcessor(requiredMods = {"apothic_spawners"}, priority = 1100)
public final class CapturingItemGuard implements IDataComponentProcessor {
    private static final ResourceLocation ENTITY_NAME = ResourceLocation.fromNamespaceAndPath("reliquary", "entity_name");
    @Override public String getName() { return "TMM Capturing Item Guard"; }
    @Override public String getTranslationKey() { return "tmm_economy.processor.capturing_items"; }
    @Override public String getDescription() { return "Prevent EMC on blocked typed charms, fragments and customized eggs."; }
    @Override public boolean hasPersistentComponents() { return true; }

    private static boolean charm(ItemStack s) {
        String id = BuiltInRegistries.ITEM.getKey(s.getItem()).toString();
        return id.equals("reliquary:mob_charm") || id.equals("reliquary:mob_charm_fragment");
    }
    @Override public long recalculateEMC(ItemInfo info, long currentEMC) {
        var s = info.createStack();
        if (s.getItem() instanceof SpawnEggItem egg) {
            var type = egg.getType(s);
            if (type == null || CapturingPolicy.blocked(type) || s.has(DataComponents.ENTITY_DATA)) return 0;
        }
        if (charm(s)) {
            var data = BuiltInRegistries.DATA_COMPONENT_TYPE.getOptional(ENTITY_NAME);
            Object value = data.isPresent() ? s.get(data.get()) : null;
            if (!(value instanceof ResourceLocation id) || CapturingPolicy.blocked(id)) return 0;
        }
        return currentEMC;
    }
    @Override public void collectPersistentComponents(ItemInfo info, DataComponentPatch.Builder b) {
        var s = info.createStack();
        if (s.getItem() instanceof SpawnEggItem) retain(s, DataComponents.ENTITY_DATA, b);
        if (charm(s)) BuiltInRegistries.DATA_COMPONENT_TYPE.getOptional(ENTITY_NAME).ifPresent(t -> retain(s, t, b));
    }
    private static <T> void retain(ItemStack s, DataComponentType<T> type, DataComponentPatch.Builder b) {
        T value = s.get(type);
        if (value != null) b.set(type, value);
    }
}
