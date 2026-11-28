package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ClothingRecipeSerializers {
    private ClothingRecipeSerializers() {}
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ProgressionMod.MOD_ID);
    public static final Supplier<RecipeSerializer<TartanKiltRecipe>> TARTAN_KILT =
            SERIALIZERS.register("tartan_kilt", () -> new SimpleCraftingRecipeSerializer<>(TartanKiltRecipe::new));
}
