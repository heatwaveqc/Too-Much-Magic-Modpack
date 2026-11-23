package io.github.heatwaveqc.tyr.progression.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Explicit, admin-only census of the ACTUAL loaded item registry after the
 * pack's current datapacks and KubeJS have been applied.
 *
 * Read-only: no mutations to Item, ItemStack, FOOD components, tags or recipes.
 * Default stack FOOD is an authoritative positive signal. An item whose food
 * component is attached to a non-default crafted stack requires a separate
 * recipe-output census; the report labels that limitation explicitly.
 */
public final class FoodRegistryCensus {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private FoodRegistryCensus() {}

    public static JsonObject snapshot() {
        JsonObject report = new JsonObject();
        report.addProperty("schema", 1);
        report.addProperty("kind", "tmm:default_item_stack_food_registry");
        report.addProperty("scope", "loaded_server_registry");
        report.addProperty("authoritative_for", "default_stack_food_component");
        report.addProperty("recipe_output_stacks_examined", false);
        JsonArray items = new JsonArray();
        List<ResourceLocation> keys = new ArrayList<>(BuiltInRegistries.ITEM.keySet());
        keys.sort(Comparator.comparing(ResourceLocation::toString));
        int foods = 0;
        for (ResourceLocation id : keys) {
            Item item = BuiltInRegistries.ITEM.get(id);
            ItemStack stack = item.getDefaultInstance();
            boolean edible = stack.get(DataComponents.FOOD) != null;
            JsonObject entry = new JsonObject();
            entry.addProperty("id", id.toString());
            entry.addProperty("food", edible);
            JsonArray tags = new JsonArray();
            stack.getTags().map(tag -> tag.location().toString()).sorted()
                .forEach(tags::add);
            entry.add("tags", tags);
            items.add(entry);
            if (edible) foods++;
        }
        report.addProperty("item_count", items.size());
        report.addProperty("food_count", foods);
        report.add("items", items);
        return report;
    }

    /**
     * Inspect the effective recipe manager after datapack conditions and pack
     * runtime recipe mutations. getResultItem is only an output *preview*:
     * dynamic recipes may return EMPTY or a different result at assemble-time.
     */
    public static void appendRecipeOutputPreviews(JsonObject report,
            net.minecraft.server.MinecraftServer server) {
        List<RecipeHolder<?>> holders = new ArrayList<>(
            server.getRecipeManager().getRecipes());
        holders.sort(Comparator.comparing(holder -> holder.id().toString()));
        JsonArray preview = new JsonArray();
        JsonArray unresolved = new JsonArray();
        int foodOutputs = 0;
        for (RecipeHolder<?> holder : holders) {
            try {
                ItemStack stack = holder.value().getResultItem(server.registryAccess());
                if (stack.isEmpty()) {
                    unresolved.add(holder.id().toString());
                    continue;
                }
                JsonObject entry = new JsonObject();
                entry.addProperty("recipe", holder.id().toString());
                entry.addProperty("item", BuiltInRegistries.ITEM.getKey(
                    stack.getItem()).toString());
                entry.addProperty("food", stack.get(DataComponents.FOOD) != null);
                entry.addProperty("count", stack.getCount());
                preview.add(entry);
                if (stack.get(DataComponents.FOOD) != null) foodOutputs++;
            } catch (RuntimeException notPreviewable) {
                // Never treat a dynamic or failing output preview as nonfood.
                unresolved.add(holder.id().toString());
            }
        }
        report.addProperty("recipe_result_previews_examined", true);
        report.addProperty("loaded_recipe_count", holders.size());
        report.addProperty("recipe_preview_count", preview.size());
        report.addProperty("recipe_preview_food_count", foodOutputs);
        report.addProperty("recipe_preview_unresolved_count", unresolved.size());
        report.add("recipe_result_previews", preview);
        report.add("unresolved_recipe_result_previews", unresolved);
    }

    /**
     * Only invoked by /tyrprogress admin foodcensus; no unattended boot,
     * scheduled world write or automatic full integration workflow.
     * The report is saved inside the active world's reports directory.
     */
    public static int export(CommandSourceStack source) {
        Path destination = source.getServer().getWorldPath(LevelResource.ROOT)
            .resolve("reports").resolve("tmm-food-registry-v1.json");
        try {
            JsonObject snapshot = snapshot();
            appendRecipeOutputPreviews(snapshot, source.getServer());
            Files.createDirectories(destination.getParent());
            Path temporary = Files.createTempFile(destination.getParent(),
                ".tmm-food-registry-", ".json.tmp");
            try {
                Files.writeString(temporary, GSON.toJson(snapshot) + "\n",
                    StandardCharsets.UTF_8);
                try {
                    Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException unsupported) {
                    Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                Files.deleteIfExists(temporary);
            }
            int foods = snapshot.get("food_count").getAsInt();
            int total = snapshot.get("item_count").getAsInt();
            source.sendSuccess(() -> Component.literal(
                "TMM food census: " + foods + "/" + total
                + " default-stack items have FOOD; "
                + snapshot.get("recipe_preview_food_count").getAsInt()
                + " active recipe result previews have FOOD; JSON at "
                + destination), false);
            return foods;
        } catch (IOException ex) {
            source.sendFailure(Component.literal(
                "TMM food census could not write report: " + ex.getMessage()));
            return 0;
        }
    }
}
