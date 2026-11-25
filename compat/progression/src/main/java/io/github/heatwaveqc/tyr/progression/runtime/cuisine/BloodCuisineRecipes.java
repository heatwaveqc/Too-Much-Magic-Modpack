package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Dynamic crafting required for JAWR's native component-sensitive blood vessels. */
public final class BloodCuisineRecipes {
    private BloodCuisineRecipes() {}
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ProgressionMod.MOD_ID);
    public static final Supplier<RecipeSerializer<BloodCuisineRecipe>> BLOOD_SAUSAGE =
            SERIALIZERS.register("blood_sausage",
                    () -> new SimpleCraftingRecipeSerializer<>(
                            category -> new BloodCuisineRecipe(category, BloodCuisinePolicy.Product.SAUSAGE)));
    public static final Supplier<RecipeSerializer<BloodCuisineRecipe>> BLOOD_PIE_SLICE =
            SERIALIZERS.register("blood_pie_slice",
                    () -> new SimpleCraftingRecipeSerializer<>(
                            category -> new BloodCuisineRecipe(category, BloodCuisinePolicy.Product.PIE)));
}
