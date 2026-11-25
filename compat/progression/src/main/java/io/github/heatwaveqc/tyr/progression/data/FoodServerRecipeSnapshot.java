package io.github.heatwaveqc.tyr.progression.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Snapshots ONLY the currently loaded server RecipeManager after reload.
 * Unsupported/dynamic serializers cannot silently become proven nutrition.
 * No server recipe scan is performed in the food-use tick path after caching.
 */
public final class FoodServerRecipeSnapshot {
    private FoodServerRecipeSnapshot() {}
    private static final int MAX_VARIANTS = 64;

    public static List<FoodLiveRecipeValidator.Observed> read(
            MinecraftServer server, FoodProcessingCatalog catalog) {
        var result = new ArrayList<FoodLiveRecipeValidator.Observed>();
        for (var holder : server.getRecipeManager().getRecipes()) {
            String receipt = holder.id().toString();
            Recipe<?> recipe = holder.value();
            ItemStack output;
            try { output = recipe.getResultItem(server.registryAccess()); }
            catch (RuntimeException | LinkageError failed) {
                result.add(FoodLiveRecipeValidator.Observed.unsupported(receipt, null));
                continue;
            }
            String item = output == null || output.isEmpty() ? null
                : BuiltInRegistries.ITEM.getKey(output.getItem()).toString();
            if (item == null) {
                result.add(FoodLiveRecipeValidator.Observed.unsupported(receipt, null));
                continue;
            }
            try {
                var method = catalog.method(recipe);
                int count = output.getCount();
                if (method == null || count < 1 || count > 64) {
                    result.add(FoodLiveRecipeValidator.Observed.unsupported(receipt, item));
                    continue;
                }
                List<List<FoodRecipeEvidence.Ingredient>> variants = expand(recipe, catalog);
                result.add(new FoodLiveRecipeValidator.Observed(
                    receipt, item, count, method, variants, !variants.isEmpty()));
            } catch (RuntimeException | LinkageError unsupported) {
                result.add(FoodLiveRecipeValidator.Observed.unsupported(receipt, item));
            }
        }
        return List.copyOf(result);
    }

    private static List<List<FoodRecipeEvidence.Ingredient>> expand(
            Recipe<?> recipe, FoodProcessingCatalog catalog) {
        List<List<FoodRecipeEvidence.Ingredient>> candidates = new ArrayList<>();
        candidates.add(List.of());
        if (recipe.getIngredients().isEmpty()) return List.of();
        for (Ingredient input : recipe.getIngredients()) {
            ItemStack[] choices = input.getItems();
            if (choices.length == 0 || (long)candidates.size() * choices.length > MAX_VARIANTS)
                return List.of();
            List<List<FoodRecipeEvidence.Ingredient>> next = new ArrayList<>();
            for (List<FoodRecipeEvidence.Ingredient> current : candidates) {
                for (ItemStack choice : choices) {
                    if (choice.isEmpty() || choice.getCount() != 1) return List.of();
                    String item = BuiltInRegistries.ITEM.getKey(choice.getItem()).toString();
                    List<FoodRecipeEvidence.Ingredient> combined = new ArrayList<>(current);
                    combined.add(new FoodRecipeEvidence.Ingredient(
                        item, 1, catalog.neutral(choice)));
                    next.add(combined);
                }
            }
            candidates = next;
        }
        // Normalize repeated items into an exact multiset. Neutral tooling
        // remains non-nutritive; the pure validator discards its role rows.
        List<List<FoodRecipeEvidence.Ingredient>> expanded = new ArrayList<>();
        for (List<FoodRecipeEvidence.Ingredient> one : candidates) {
            Map<String, Integer> food = new HashMap<>();
            for (var input : one) if (!input.neutral())
                food.merge(input.item(), input.count(), Integer::sum);
            if (food.isEmpty()) return List.of();
            expanded.add(food.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> new FoodRecipeEvidence.Ingredient(e.getKey(), e.getValue(), false))
                .toList());
        }
        return expanded;
    }
}
