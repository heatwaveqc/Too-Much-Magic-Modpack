package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.GoetyRemainderPolicy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * PE1.1.0 normalizes a reported crafting remainder before testing emptiness.
 * Goety's Roots totem may legitimately return EMPTY. Correct only PE's
 * mapping probe; actual crafting and every other item's mapping are untouched.
 */
@Mixin(targets = "moze_intel.projecte.emc.mappers.recipe.BaseRecipeTypeMapper", remap = false)
public abstract class GoetyProjectERemainderMixin {
    @Unique
    private static final ResourceLocation tmm$ROOTS = ResourceLocation.fromNamespaceAndPath("goety", "totem_of_roots");

    @Redirect(method = "addIngredient", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/item/Item;hasCraftingRemainingItem(Lnet/minecraft/world/item/ItemStack;)Z",
        remap = false), require = 1)
    private boolean tmm$checkActualRootsRemainder(Item item, ItemStack stack) {
        boolean advertised = item.hasCraftingRemainingItem(stack);
        return GoetyRemainderPolicy.hasActualRemainder(advertised,
            tmm$ROOTS.equals(BuiltInRegistries.ITEM.getKey(item)),
            () -> item.getCraftingRemainingItem(stack.copy()).isEmpty());
    }
}
