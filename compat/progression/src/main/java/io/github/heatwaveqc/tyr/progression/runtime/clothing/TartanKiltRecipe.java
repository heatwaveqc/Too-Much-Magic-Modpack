package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/** Native crafting transaction. All palette data derives from consumed wool,
 * never from client-supplied color values. */
public final class TartanKiltRecipe extends CustomRecipe {
    public TartanKiltRecipe(CraftingBookCategory category) { super(category); }

    @Override public boolean matches(CraftingInput input, Level level) {
        return TartanCrafting.palette(input).isPresent();
    }
    @Override public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 2;
    }
    @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        return TartanCrafting.palette(input).map(palette -> {
            var result = new ItemStack(ClothingItems.TARTAN_KILT.get());
            result.set(ClothingComponents.TARTAN_PALETTE.get(), palette);
            return result;
        }).orElse(ItemStack.EMPTY);
    }
    @Override public RecipeSerializer<?> getSerializer() {
        return ClothingRecipeSerializers.TARTAN_KILT.get();
    }
}
