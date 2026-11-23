package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * Exactly one verified full native blood bottle and exactly one appropriate
 * cooked sausage or edible #c:foods/pie item, anywhere in a crafting grid.
 * Per-item native FOOD properties are copied to the crafted food (important
 * for variant pie effects) and exactly ONE vanilla glass bottle is returned.
 *
 * Standard shapeless item tags would include special JAWR blood components
 * and cannot safely validate a single common native JAWR bottle registry ID.
 */
public final class BloodCuisineRecipe extends CustomRecipe {
    private static final TagKey<Item> PIES = TagKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/pie"));
    private static final ResourceLocation SAUSAGE =
            ResourceLocation.fromNamespaceAndPath("brewinandchewin", "cooked_sausage");

    private final BloodCuisinePolicy.Product product;

    public BloodCuisineRecipe(CraftingBookCategory category, BloodCuisinePolicy.Product product) {
        super(category);
        this.product = product;
        if (product == BloodCuisinePolicy.Product.WINE)
            throw new IllegalArgumentException("Blood Wine must be prepared in the Aging Cask");
    }

    private boolean matchesInput(CraftingInput input) {
        if (!BloodCuisineNative.pinned()) return false;
        int bottles = 0, foods = 0;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (stack.isEmpty()) continue;
            if (BloodCuisineNative.verifiedBottleBlood(stack) > 0) {
                bottles++;
            } else if (isSourceFood(stack)) {
                foods++;
            } else return false;
        }
        return bottles == 1 && foods == 1;
    }

    private boolean isSourceFood(ItemStack stack) {
        if (stack.get(DataComponents.FOOD) == null) return false;
        return product == BloodCuisinePolicy.Product.SAUSAGE
                ? SAUSAGE.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))
                : stack.is(PIES);
    }

    @Override public boolean matches(CraftingInput input, Level level) {
        return matchesInput(input);
    }

    @Override public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        if (!matchesInput(input)) return ItemStack.EMPTY;
        ItemStack base = ItemStack.EMPTY;
        int amount = 0;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack item = input.getItem(slot);
            int verified = BloodCuisineNative.verifiedBottleBlood(item);
            if (verified > 0) amount = verified;
            else if (!item.isEmpty()) base = item;
        }
        int credit = BloodCuisinePolicy.bloodFor(product, amount);
        if (credit <= 0 || base.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = new ItemStack(product == BloodCuisinePolicy.Product.SAUSAGE
                ? BloodCuisineItems.BLOOD_SAUSAGE.get()
                : BloodCuisineItems.BLOOD_PIE_SLICE.get());
        FoodProperties nativeFood = base.get(DataComponents.FOOD);
        if (nativeFood != null) result.set(DataComponents.FOOD, nativeFood);
        BloodCuisinePayload.set(result, credit);
        return result;
    }

    @Override public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        if (!matchesInput(input)) return result;
        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (BloodCuisineNative.verifiedBottleBlood(stack) > 0) {
                result.set(slot, new ItemStack(Items.GLASS_BOTTLE));
            } else if (!stack.isEmpty() && stack.getItem().hasCraftingRemainingItem()) {
                result.set(slot, new ItemStack(stack.getItem().getCraftingRemainingItem()));
            }
        }
        return result;
    }

    @Override public RecipeSerializer<?> getSerializer() {
        return product == BloodCuisinePolicy.Product.SAUSAGE
                ? BloodCuisineRecipes.BLOOD_SAUSAGE.get()
                : BloodCuisineRecipes.BLOOD_PIE_SLICE.get();
    }
}
