package io.github.heatwaveqc.tmm.economy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import earth.terrarium.pastel.api.energy.color.InkColor;
import earth.terrarium.pastel.blocks.cinderhearth.CinderhearthBlockEntity;
import earth.terrarium.pastel.recipe.InkConvertingRecipe;
import earth.terrarium.pastel.recipe.cinderhearth.CinderhearthRecipe;
import earth.terrarium.pastel.recipe.fusion_shrine.FusionShrineRecipe;
import earth.terrarium.pastel.recipe.pedestal.PedestalRecipe;
import earth.terrarium.pastel.recipe.pedestal.ShapedPedestalRecipe;
import earth.terrarium.pastel.recipe.pedestal.ShapelessPedestalRecipe;
import earth.terrarium.pastel.recipe.spirit_instiller.SpiritInstillerRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSFake;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.slf4j.Logger;

/** Pastel 1.21.1 source candidate. The recipe's own codec avoids linking
 * against the incompatible legacy IngredientStack / newer SizedIngredient APIs.
 * Only recognized plain ingredient encodings are mapped; exact-pin compile/QA required. */
@EMCMapper(requiredMods = {"pastel"})
public final class PastelRecipeMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final int INK_PER_DYE = 5;
    @Override public String getName() { return "TMM Pastel Recipe Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.pastel_recipes"; }
    @Override public String getDescription() { return "Map ordinary pedestal, spirit instilling, fusion and cinderhearth recipes."; }

    private record Material(Ingredient ingredient, int count) {}
    @SuppressWarnings("unchecked")
    private static <R extends Recipe<?>> JsonObject encode(R recipe, RegistryAccess access) {
        RecipeSerializer<R> serializer = (RecipeSerializer<R>) recipe.getSerializer();
        return serializer.codec().codec().encodeStart(RegistryOps.create(JsonOps.INSTANCE, access), recipe)
                .result().orElseThrow(() -> new IllegalArgumentException("native recipe encoding failed"))
                .getAsJsonObject();
    }

    private static Material material(JsonElement raw, RegistryAccess access) {
        if (raw == null || raw.isJsonNull()) throw new IllegalArgumentException("missing ingredient");
        int count = 1;
        JsonElement value = raw;
        if (raw.isJsonObject()) {
            JsonObject obj = raw.getAsJsonObject();
            if (obj.has("components") || obj.has("preview_components"))
                throw new IllegalArgumentException("component-sensitive ingredient");
            if (obj.has("count")) count = obj.get("count").getAsBigDecimal().intValueExact();
            if (obj.has("ingredient")) {
                if (obj.keySet().stream().anyMatch(k -> !k.equals("ingredient") && !k.equals("count")))
                    throw new IllegalArgumentException("unknown sized ingredient fields");
                value = obj.get("ingredient");
            } else {
                JsonObject plain = obj.deepCopy();
                plain.remove("count");
                value = plain;
            }
        }
        if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
            String id = value.getAsString();
            JsonObject plain = new JsonObject();
            plain.addProperty(id.startsWith("#") ? "tag" : "item", id.startsWith("#") ? id.substring(1) : id);
            value = plain;
        }
        if (count <= 0) throw new IllegalArgumentException("nonpositive ingredient count");
        var ing = Ingredient.CODEC_NONEMPTY.parse(RegistryOps.create(JsonOps.INSTANCE, access), value)
                .result().orElseThrow(() -> new IllegalArgumentException("unsupported ingredient encoding"));
        if (ing.getCustomIngredient() != null || ing.isEmpty())
            throw new IllegalArgumentException("custom or empty ingredient");
        return new Material(ing, count);
    }

    private static void consume(RecipeGraphPlan plan, Material material, boolean remainders) {
        plan.consumed(material.ingredient(), material.count(), remainders);
    }

    private static List<Material> list(JsonObject json, String field, RegistryAccess access) {
        List<Material> result = new ArrayList<>();
        for (JsonElement element : json.getAsJsonArray(field)) result.add(material(element, access));
        return result;
    }

    private static void rejectAmbiguousCounts(List<Material> inputs) {
        Map<Item, Integer> counts = new HashMap<>();
        for (var input : inputs) for (var option : input.ingredient().getItems()) {
            Integer previous = counts.putIfAbsent(option.getItem(), input.count());
            if (previous != null && previous != input.count())
                throw new IllegalArgumentException("overlapping ingredients with different counts");
        }
    }

    private static List<Material> shaped(JsonObject json, RegistryAccess access) {
        List<Material> result = new ArrayList<>();
        JsonObject key = json.getAsJsonObject("key");
        for (JsonElement row : json.getAsJsonArray("pattern")) {
            for (char c : row.getAsString().toCharArray()) {
                if (c != ' ') result.add(material(key.get(String.valueOf(c)), access));
            }
        }
        return result;
    }

    private static Map<DyeColor, NormalizedSimpleStack> inkAnchors(
            ResourceGraph graph, ReloadableServerResources resources) {
        Map<DyeColor, NormalizedSimpleStack> result = new HashMap<>();
        for (DyeColor dye : DyeColor.values()) {
            var item = DyeItem.byColor(dye);
            var color = InkColor.ofDyeColor(dye);
            if (item == null || color == null) continue;
            ItemStack stack = new ItemStack(item);
            boolean matched = false, conflict = false;
            for (var holder : resources.getRecipeManager().getRecipes()) {
                if (!(holder.value() instanceof InkConvertingRecipe r)) continue;
                if (r.getIngredients().stream().noneMatch(i -> i.test(stack))) continue;
                if (!color.equals(r.getInkColor()) || r.getInkAmount() != INK_PER_DYE) conflict = true;
                else matched = true;
            }
            if (!matched || conflict) {
                LOG.warn("TMM Pastel: no unambiguous approved 5-Ink dye route for {}", dye);
                continue;
            }
            var key = NSSFake.create("tmm_economy:pastel_ink_" + dye.getName());
            var plan = new RecipeGraphPlan();
            plan.choice(Map.of(NSSItem.createItem(item), 1));
            plan.contribute(graph, key, INK_PER_DYE, ResourceAccounting.Cost.ZERO);
            result.put(dye, key);
        }
        return result;
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        // No pigment-based competing anchor or physical ink item is created.
        var ink = inkAnchors(graph, resources);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof PedestalRecipe) && !(recipe instanceof SpiritInstillerRecipe)
                    && !(recipe instanceof CinderhearthRecipe) && !(recipe instanceof FusionShrineRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                var out = recipe.getResultItem(registries);
                if (out.isEmpty() || !out.getComponentsPatch().isEmpty())
                    throw new IllegalArgumentException("empty or component-sensitive output");
                if (recipe.getClass() == ShapedPedestalRecipe.class
                        || recipe.getClass() == ShapelessPedestalRecipe.class) {
                    var r = (PedestalRecipe) recipe;
                    if (r.isStructureUpgrade(registries)) throw new IllegalArgumentException("pedestal upgrade replaces block");
                    var json = encode(r, registries);
                    var materials = recipe.getClass() == ShapedPedestalRecipe.class
                            ? shaped(json, registries) : list(json, "ingredients", registries);
                    if (recipe.getClass() == ShapelessPedestalRecipe.class) rejectAmbiguousCounts(materials);
                    for (var input : materials) consume(plan, input, !r.skipRecipeRemainders());
                    for (var e : r.getPowderInputs().entrySet()) {
                        int amount = e.getValue();
                        if (amount < 0) throw new IllegalArgumentException("negative powder quantity");
                        if (amount > 0) plan.consumed(Ingredient.of(e.getKey().getPowder()), amount, false);
                    }
                } else if (recipe.getClass() == SpiritInstillerRecipe.class) {
                    var id = BuiltInRegistries.ITEM.getKey(out.getItem());
                    if (id.equals(ResourceLocation.fromNamespaceAndPath("pastel", "memory")))
                        throw new IllegalArgumentException("personalized memory");
                    var json = encode(recipe, registries);
                    var center = material(json.get("center_ingredient"), registries);
                    var left = material(json.get("ingredient1"), registries);
                    var right = material(json.get("ingredient2"), registries);
                    rejectAmbiguousCounts(List.of(left, right));
                    consume(plan, center, false);
                    consume(plan, left, true);
                    consume(plan, right, true);
                } else if (recipe.getClass() == CinderhearthRecipe.class) {
                    var r = (CinderhearthRecipe) recipe;
                    var results = r.getResultsWithChance();
                    if (results.size() != 1 || results.getFirst().getB() != 1.0F)
                        throw new IllegalArgumentException("stochastic or multiple outcomes");
                    out = results.getFirst().getA();
                    var input = material(encode(r, registries).get("ingredient"), registries);
                    if (input.count() > 1 && java.util.Arrays.stream(input.ingredient().getItems())
                            .anyMatch(s -> !s.getCraftingRemainingItem().isEmpty()))
                        throw new IllegalArgumentException("one remainder per multicount batch");
                    consume(plan, input, true);
                    int time = r.getCraftingTime();
                    if (time <= 0) throw new IllegalArgumentException("invalid crafting duration");
                    var orange = ink.get(DyeColor.ORANGE);
                    if (orange == null) throw new IllegalArgumentException("orange ink anchor not verified");
                    // Despite its name, INK_COST_PER_TICK is charged at progress
                    // 0,20,40... before the progress increment, not every tick.
                    long total = Math.multiplyExact(CinderhearthBlockEntity.INK_COST_PER_TICK,
                            ((long) time + 19) / 20);
                    plan.choice(Map.of(orange, Math.toIntExact(total)));
                } else if (recipe.getClass() == FusionShrineRecipe.class) {
                    var r = (FusionShrineRecipe) recipe;
                    var json = encode(r, registries);
                    if (json.has("copy_components") && json.get("copy_components").getAsBoolean())
                        throw new IllegalArgumentException("component transfer");
                    if (!r.getFluid().isEmpty())
                        throw new IllegalArgumentException("full tank per variable batch; fluid allocation held");
                    var materials = list(json, "ingredients", registries);
                    rejectAmbiguousCounts(materials);
                    for (var input : materials) consume(plan, input, true);
                } else throw new IllegalArgumentException("special recipe subtype");
                routes += plan.contribute(graph, out, ResourceAccounting.Cost.ZERO);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException | IllegalStateException | NullPointerException e) {
                skipped++; LOG.info("TMM Pastel mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Pastel mapper: {} dye anchors, {} recipes, {} distinct routes, {} held", ink.size(), mapped, routes, skipped);
    }
}
