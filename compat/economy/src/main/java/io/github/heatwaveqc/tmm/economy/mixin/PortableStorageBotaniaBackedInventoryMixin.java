package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Scoped to Botania's item-backed inventory objects, NOT SimpleContainer.
 *
 * Extra Botany's 24 ordinary Mini Handbag slots and cursor/Hopper helper paths
 * use Container.canPlaceItem. Adding this inherited override lets those
 * existing native paths refuse unsafe embedded sources before moving items.
 * Botania's Flower Pouch subclass retains its own stricter flower-only
 * override; unrelated SimpleContainer/chest storage is untouched.
 *
 * Arbitrary direct setItem callers still need separate coverage/QA: a setter
 * cannot safely refuse an already-removed source item by silently dropping it.
 */
@Pseudo
@Mixin(targets = "velrondevs.botania.common.item.ItemBackedInventory", remap = false)
public abstract class PortableStorageBotaniaBackedInventoryMixin extends SimpleContainer {
    private static final TagKey<Item> TMM_PROTECTED_EMBEDDED_DESTINATION = TagKey.create(
        Registries.ITEM, ResourceLocation.fromNamespaceAndPath("tmm", "non_nestable_embedded_containers"));

    @Shadow @Final private ItemStack stack;

    protected PortableStorageBotaniaBackedInventoryMixin(int slots) {
        super(slots);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack candidate) {
        if (stack.is(TMM_PROTECTED_EMBEDDED_DESTINATION)
                && PortableStorageSafety.rejectsEmbeddedDestination(candidate)) {
            return false;
        }
        return super.canPlaceItem(slot, candidate);
    }
}
