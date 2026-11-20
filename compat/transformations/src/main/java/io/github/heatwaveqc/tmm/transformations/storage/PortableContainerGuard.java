package io.github.heatwaveqc.tmm.transformations.storage;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Release blocker #16: one predicate shared by all portable-storage destination
 * hooks. Never mutate the incoming stack or delete historical stored stacks.
 * Track only ProjectE's per-player colored-bag handlers by object identity;
 * ordinary chests/handlers must retain their normal insertion policy.
 */
public final class PortableContainerGuard {
    private static final TagKey<Item> PORTABLE_BAGS = TagKey.create(
        Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tmm", "portable_bags"));
    private static final Set<ItemStackHandler> PROJECTE_BAGS =
        Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    private PortableContainerGuard() { }

    public static boolean rejects(ItemStack stack) {
        if (stack.isEmpty()) return false;
        boolean tagged = stack.is(PORTABLE_BAGS);
        boolean shulker = stack.getItem() instanceof BlockItem item
            && item.getBlock() instanceof ShulkerBoxBlock;
        return PortableBagNestingRules.rejects(false, tagged, shulker);
    }

    public static void markProjecteBag(ItemStackHandler handler) {
        if (handler != null) PROJECTE_BAGS.add(handler);
    }

    public static boolean isProjecteBag(ItemStackHandler handler) {
        return handler != null && PROJECTE_BAGS.contains(handler);
    }
}
