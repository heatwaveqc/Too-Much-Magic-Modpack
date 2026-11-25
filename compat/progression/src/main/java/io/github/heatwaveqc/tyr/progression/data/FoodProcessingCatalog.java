package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Shared, declarative preparation-method vocabulary. The same JSON schema
 * drives the offline survey compiler and server active-recipe reconciliation.
 * Unsupported native serializers fail closed; shape-specific methods are not
 * guessed from an item's display name.
 */
public record FoodProcessingCatalog(
        Map<String, FoodRecipeEvidence.Method> methods,
        Set<String> hotToolTokens, Set<String> neutralItems,
        Set<String> neutralTagPrefixes) {

    public FoodProcessingCatalog {
        methods = Map.copyOf(methods);
        hotToolTokens = Set.copyOf(hotToolTokens);
        neutralItems = Set.copyOf(neutralItems);
        neutralTagPrefixes = Set.copyOf(neutralTagPrefixes);
    }

    private static List<String> strings(JsonObject raw, String key) {
        JsonArray values = raw.getAsJsonArray(key);
        if (values == null) throw new IllegalArgumentException("Missing food processing " + key);
        List<String> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (JsonElement element : values) {
            String token = element.getAsString();
            if (token.isBlank() || !seen.add(token))
                throw new IllegalArgumentException("Empty/duplicate food processing " + key);
            result.add(token);
        }
        return result;
    }

    public static FoodProcessingCatalog parse(JsonObject json) {
        Set<String> fields = Set.of("schema", "method_by_recipe_type", "hot_tool_tag_tokens",
            "neutral_item_ids", "neutral_tag_prefixes", "material_tags", "material_items",
            "identity_unfavored_patterns", "thermal_ambiguity_patterns",
            "native_food_target_tags", "nonfood_output_tags");
        if (json == null || !json.keySet().equals(fields)
                || json.get("schema").getAsInt() != 1)
            throw new IllegalArgumentException("Invalid food processing configuration schema");
        JsonObject kinds = json.getAsJsonObject("method_by_recipe_type");
        if (kinds == null || kinds.isEmpty()) throw new IllegalArgumentException("No food recipe types");
        Map<String, FoodRecipeEvidence.Method> methods = new HashMap<>();
        for (var entry : kinds.entrySet()) {
            String key = entry.getKey();
            if (!id(key)) throw new IllegalArgumentException("Invalid recipe type " + key);
            try {
                methods.put(key, FoodRecipeEvidence.Method.valueOf(
                    entry.getValue().getAsString().toUpperCase(java.util.Locale.ROOT)));
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Unsupported processing method for " + key, e);
            }
        }
        Set<String> neutralItems = new HashSet<>(strings(json, "neutral_item_ids"));
        for (String id : neutralItems) if (!id(id))
            throw new IllegalArgumentException("Invalid neutral item " + id);
        // Check auxiliary source-compiler schema even though runtime does not
        // assign global source families or target-food tags.
        for (String field : List.of("material_tags", "material_items")) {
            JsonObject data = json.getAsJsonObject(field);
            if (data == null) throw new IllegalArgumentException("Missing " + field);
            for (var entry : data.entrySet())
                if (!id(entry.getKey()) || !entry.getValue().getAsString().startsWith("#")
                        || !id(entry.getValue().getAsString().substring(1)))
                    throw new IllegalArgumentException("Invalid processing material mapping");
        }
        for (String field : List.of("identity_unfavored_patterns", "thermal_ambiguity_patterns",
                "native_food_target_tags", "nonfood_output_tags")) {
            for (String value : strings(json, field)) {
                if (field.endsWith("_tags") && !id(value))
                    throw new IllegalArgumentException("Invalid processing tag " + value);
                if (field.endsWith("_patterns")) java.util.regex.Pattern.compile(value);
            }
        }
        return new FoodProcessingCatalog(methods,
            Set.copyOf(strings(json, "hot_tool_tag_tokens")), neutralItems,
            Set.copyOf(strings(json, "neutral_tag_prefixes")));
    }

    private static boolean id(String value) {
        return value.matches("[a-z0-9_.-]+:[a-z0-9_./-]+");
    }

    public boolean neutral(ItemStack option) {
        if (neutralItems.contains(BuiltInRegistries.ITEM.getKey(option.getItem()).toString()))
            return true;
        return option.getTags().anyMatch(tag -> {
            String name = tag.location().toString();
            for (String prefix : neutralTagPrefixes)
                if (name.startsWith(prefix)) return true;
            return false;
        });
    }

    public FoodRecipeEvidence.Method method(Recipe<?> recipe) {
        String nativeType = BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType()).toString();
        // Crafting recipes share one native type; do not infer a shaped or
        // dynamic recipe's ingredient semantics from a shapeless schema.
        String resolved = nativeType;
        if ("minecraft:crafting".equals(nativeType)) {
            if (!(recipe instanceof ShapelessRecipe)) return null;
            resolved = "minecraft:crafting_shapeless";
        }
        FoodRecipeEvidence.Method method = methods.get(resolved);
        if (method == null) return null;
        if (method == FoodRecipeEvidence.Method.NONTHERMAL) {
            for (var ingredient : recipe.getIngredients()) {
                for (ItemStack option : ingredient.getItems()) {
                    if (option.getTags().anyMatch(tag -> {
                        String tagName = tag.location().toString();
                        if (!neutralTagPrefixes.stream().anyMatch(tagName::startsWith))
                            return false;
                        for (String token : hotToolTokens)
                            if (tagName.contains(token)) return true;
                        return false;
                    })) return FoodRecipeEvidence.Method.HEAT;
                }
            }
        }
        return method;
    }
}
