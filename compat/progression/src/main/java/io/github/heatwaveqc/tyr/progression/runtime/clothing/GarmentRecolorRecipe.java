package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.foundation.GarmentPalette;
import io.github.heatwaveqc.tyr.progression.runtime.ClothingApi;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/** Cropped 2x2 recipe: garment / primary dye, secondary dye / trim dye.
 * Native crafting consumes exactly one garment and three dyes. Copy the
 * original stack to preserve weight and other lawful components; never
 * accept altered protection or enchantment data through the clothing policy. */
public final class GarmentRecolorRecipe extends CustomRecipe {
    public GarmentRecolorRecipe(CraftingBookCategory category) { super(category); }

    private static Optional<GarmentPalette> palette(CraftingInput input) {
        if (input.width() != 2 || input.height() != 2) return Optional.empty();
        var garment = input.getItem(0);
        if (ClothingItems.martialStyle(garment).isEmpty() || !ClothingApi.isNonProtective(garment))
            return Optional.empty();
        if (!(input.getItem(1).getItem() instanceof DyeItem primary)
                || !(input.getItem(2).getItem() instanceof DyeItem secondary)
                || !(input.getItem(3).getItem() instanceof DyeItem trim))
            return Optional.empty();
        return Optional.of(GarmentPalette.of(primary.getDyeColor().getId(),
                secondary.getDyeColor().getId(), trim.getDyeColor().getId()));
    }
    @Override public boolean matches(CraftingInput input, Level level) { return palette(input).isPresent(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return width >= 2 && height >= 2; }
    @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        return palette(input).map(palette -> {
            var result = input.getItem(0).copyWithCount(1);
            result.set(ClothingComponents.GARMENT_PALETTE.get(), palette);
            return result;
        }).orElse(ItemStack.EMPTY);
    }
    @Override public RecipeSerializer<?> getSerializer() { return ClothingRecipeSerializers.GARMENT_RECOLOR.get(); }
}
