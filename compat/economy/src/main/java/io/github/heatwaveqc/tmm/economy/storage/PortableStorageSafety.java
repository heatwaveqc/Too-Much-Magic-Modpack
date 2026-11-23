package io.github.heatwaveqc.tmm.economy.storage;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;

/**
 * Cheap, non-mutating classification on insertion. Do not hook every inventory,
 * periodically scan storage, or recursively walk arbitrary stack payloads.
 *
 * Tags are curated hints, not proof that every untagged item is safe. An
 * actual vanilla CONTAINER payload wins over a reference-only tag.
 * Additional mod-specific component/handler adapters are tracked by #16.
 */
public final class PortableStorageSafety {
    private static final TagKey<Item> EMBEDDED_RISK = TagKey.create(
        Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tmm", "non_nestable_embedded_containers"));
    private static final TagKey<Item> REFERENCE_BACKED = TagKey.create(
        Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tmm", "reference_backed_storage"));

    private PortableStorageSafety() { }

    public static StorageNestingPolicy.Source classify(ItemStack source) {
        if (source.isEmpty()) return StorageNestingPolicy.Source.ORDINARY;

        // Check actual payload first: a declared external handle with an
        // embedded component is no longer known to be reference-only.
        boolean nativeContainerPayload = source.has(DataComponents.CONTAINER);
        boolean shulker = source.getItem() instanceof BlockItem block
            && block.getBlock() instanceof ShulkerBoxBlock;
        if (nativeContainerPayload || shulker || source.is(EMBEDDED_RISK)) {
            return StorageNestingPolicy.Source.EMBEDDED_RISK;
        }
        if (source.is(REFERENCE_BACKED)) {
            return StorageNestingPolicy.Source.EXTERNAL_REFERENCE;
        }
        return StorageNestingPolicy.Source.ORDINARY;
    }

    /** Narrow scope: only tag-classified item-backed inventories use this method. */
    public static boolean isProtectedEmbeddedDestination(ItemStack backing) {
        return !backing.isEmpty() && backing.is(EMBEDDED_RISK);
    }

    /**
     * Call from an actual embedded-storage destination's pre-insertion path.
     * It never consumes, mutates, destroys or relocates a rejected stack.
     */
    public static boolean rejectsEmbeddedDestination(ItemStack source) {
        return StorageNestingPolicy.rejects(
            classify(source), StorageNestingPolicy.Destination.ITEM_EMBEDDED, false);
    }
}
