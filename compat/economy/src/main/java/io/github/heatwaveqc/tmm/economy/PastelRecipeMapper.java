package io.github.heatwaveqc.tmm.economy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Set;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import earth.terrarium.pastel.api.energy.color.InkColor;
import earth.terrarium.pastel.blocks.cinderhearth.CinderhearthBlockEntity;
import earth.terrarium.pastel.recipe.InkConvertingRecipe;
import earth.terrarium.pastel.recipe.cinderhearth.CinderhearthRecipe;
import earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumRecipe;
import earth.terrarium.pastel.recipe.crystallarieum.CrystallarieumCatalyst;
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
import moze_intel.projecte.api.nss.NSSFluid;
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
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
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


    /** All input alternatives must admit the same batch limit, and distinct
     * input slots must be disjoint: the shrine's greedy slot matching is not
     * equivalent to arbitrary overlapping ingredient enumeration. */
    private static int fusionBatch(List<Material> inputs, ItemStack output) {
        int batch = output.getMaxStackSize();
        Set<Item> seen = new HashSet<>();
        for (var input : inputs) {
            int cap = -1;
            Set<Item> thisSlot = new HashSet<>();
            for (var option : input.ingredient().getItems()) {
                int possible = option.getMaxStackSize() / input.count();
                if (cap >= 0 && cap != possible)
                    throw new IllegalArgumentException("alternative-dependent batch limit");
                cap = possible;
                thisSlot.add(option.getItem());
            }
            if (cap <= 0 || thisSlot.stream().anyMatch(seen::contains))
                throw new IllegalArgumentException("empty or overlapping fusion input");
            seen.addAll(thisSlot);
            batch = Math.min(batch, cap);
        }
        if (batch <= 0 || inputs.isEmpty()) throw new IllegalArgumentException("invalid fusion batch");
        return batch;
    }

    /** Shrine clears its entire 1,000mB tank once at the end of the batch. */
    private static void fusionFluid(RecipeGraphPlan plan, FluidIngredient fluid) {
        if (!fluid.isSimple()) throw new IllegalArgumentException("component-sensitive fusion liquid");
        Map<NormalizedSimpleStack,Integer> choices = new LinkedHashMap<>();
        for (var fluidStack : fluid.getStacks()) {
            var stack = fluidStack.copy();
            stack.setAmount(1000);
            if (stack.isEmpty() || !stack.getComponentsPatch().isEmpty() || !fluid.test(stack))
                throw new IllegalArgumentException("invalid fusion liquid alternative");
            choices.put(NSSFluid.createFluid(stack), 1000);
        }
        plan.choice(choices);
    }

    private static boolean one(JsonElement e) {
        return e != null && e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber() && e.getAsDouble() == 1;
    }
    private static boolean silkOnly(JsonObject object) {
        if (!object.has("conditions")) return true;
        var conditions = object.getAsJsonArray("conditions");
        if (conditions.size() != 1) return false;
        var c = conditions.get(0).getAsJsonObject();
        if (!"minecraft:match_tool".equals(c.get("condition").getAsString())) return false;
        var predicate = c.getAsJsonObject("predicate");
        if (predicate.size() != 1) return false;
        var predicates = predicate.getAsJsonObject("predicates");
        if (predicates.size() != 1) return false;
        var enchants = predicates.getAsJsonArray("minecraft:enchantments");
        return enchants.size() == 1
                && "minecraft:silk_touch".equals(enchants.get(0).getAsJsonObject().get("enchantments").getAsString())
                && one(enchants.get(0).getAsJsonObject().getAsJsonObject("levels").get("min"));
    }
    /** Only grow actual collectible stage blocks. Never price a display-only
     * extra result or assume a world block is a collectible item. */
    private static boolean collectibleStage(ItemStack stack, ResourceManager manager) {
        var id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        var key = ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "loot_table/blocks/" + id.getPath() + ".json");
        var resource = manager.getResource(key);
        if (resource.isEmpty()) return false;
        try (var reader = resource.get().openAsReader()) {
            var json = JsonParser.parseReader(reader).getAsJsonObject();
            var pools = json.getAsJsonArray("pools");
            if (pools.size() != 1 || json.has("functions")) return false;
            var pool = pools.get(0).getAsJsonObject();
            if (!one(pool.get("rolls")) || pool.has("functions") || !silkOnly(pool)) return false;
            if (pool.has("bonus_rolls") && pool.get("bonus_rolls").getAsDouble() != 0) return false;
            var entries = pool.getAsJsonArray("entries");
            if (entries.size() != 1) return false;
            var entry = entries.get(0).getAsJsonObject();
            if ("minecraft:alternatives".equals(entry.get("type").getAsString())) {
                if (entry.has("conditions") || entry.has("functions")) return false;
                entry = entry.getAsJsonArray("children").get(0).getAsJsonObject();
            }
            return "minecraft:item".equals(entry.get("type").getAsString())
                    && id.toString().equals(entry.get("name").getAsString())
                    && !entry.has("functions") && silkOnly(entry);
        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    private static NormalizedSimpleStack inkKey(Map<DyeColor, NormalizedSimpleStack> ink, InkColor color) {
        for (var entry : ink.entrySet())
            if (color.equals(InkColor.ofDyeColor(entry.getKey()))) return entry.getValue();
        throw new IllegalArgumentException("unverified dye anchor for growth ink");
    }

    private static int growthRoute(ResourceGraph graph, CrystallarieumRecipe r,
                                   ItemStack stage, int index, CrystallarieumCatalyst catalyst,
                                   Map<DyeColor, NormalizedSimpleStack> ink) {
        var plan = new RecipeGraphPlan();
        plan.consumed(r.getIngredientStack(), 1, false);
        if (index == 0) return plan.contribute(graph, stage, ResourceAccounting.Cost.ZERO);
        long rolls = ExpectedQuantity.growthRolls(r.getSecondsPerGrowthStage(), index, catalyst.growthAccelerationMod());
        // Native chanceRound consumes this nominal float amount each processing second.
        var inkCost = ExpectedQuantity.of(r.getInkPerSecond() * catalyst.growthAccelerationMod()
                * catalyst.inkConsumptionMod()).times(rolls);
        var catalystCost = ExpectedQuantity.probability(catalyst.consumeChancePerSecond()).times(rolls);
        int scale = ExpectedQuantity.scale(inkCost, catalystCost);
        plan.scale(scale);
        if (!inkCost.isZero()) plan.choice(Map.of(inkKey(ink, r.getInkColor()), inkCost.countAt(scale)));
        if (!catalystCost.isZero()) plan.consumed(catalyst.ingredient(), catalystCost.countAt(scale), false);
        else if (!catalyst.ingredient().isEmpty())
            plan.slot(catalyst.ingredient(), 1, s -> true, false, false);
        return plan.contribute(graph, NSSItem.createItem(stage), scale, ResourceAccounting.Cost.ZERO);
    }

    private static int growth(ResourceGraph graph, CrystallarieumRecipe r, ResourceManager manager,
                              Map<DyeColor, NormalizedSimpleStack> ink, ResourceLocation id) {
        int routes = 0;
        var states = r.getGrowthStages();
        var medium = r.getFluidMedium();
        if (medium.isEmpty() || !medium.getComponentsPatch().isEmpty())
            throw new IllegalArgumentException("unresolved or component-sensitive growth medium");
        // Medium is a persistent machine condition; native growth never drains it.
        for (int i = 0; i < states.size(); i++) {
            var stage = new ItemStack(states.get(i).getBlock().asItem());
            if (stage.isEmpty() || !stage.getComponentsPatch().isEmpty() || !collectibleStage(stage, manager)) {
                LOG.info("TMM Pastel growth {} stage {} held: no simple collectible self-drop", id, i);
                continue;
            }
            List<CrystallarieumCatalyst> alternatives = new ArrayList<>();
            if (i == 0) alternatives.add(CrystallarieumCatalyst.EMPTY);
            else {
                alternatives.addAll(r.getCatalysts());
                if (r.growsWithoutCatalyst() && CrystallarieumCatalyst.EMPTY.growthAccelerationMod() > 0)
                    alternatives.add(CrystallarieumCatalyst.EMPTY);
            }
            for (var catalyst : alternatives) {
                try { routes += growthRoute(graph, r, stage, i, catalyst, ink); }
                catch (IllegalArgumentException | ArithmeticException e) {
                    LOG.info("TMM Pastel growth {} stage {} catalyst held: {}", id, i, e.getMessage());
                }
            }
        }
        if (routes == 0) throw new IllegalArgumentException("no supported collectible growth stages");
        LOG.info("TMM Pastel growth {}: {} stage routes; raw/pure harvests and display-only extra results remain productive", id, routes);
        return routes;
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
                    && !(recipe instanceof CinderhearthRecipe) && !(recipe instanceof FusionShrineRecipe)
                    && !(recipe instanceof CrystallarieumRecipe)) continue;
            try {
                if (recipe.getClass() == CrystallarieumRecipe.class) {
                    routes += growth(graph, (CrystallarieumRecipe) recipe, manager, ink, holder.id());
                    mapped++;
                    continue;
                }
                var plan = new RecipeGraphPlan();
                var out = recipe.getResultItem(registries);
                if (!(recipe instanceof CinderhearthRecipe) && (out.isEmpty() || !out.getComponentsPatch().isEmpty()))
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
                    // Independently rolled native results, base (unupgraded) yield.
                    Map<Item, ExpectedQuantity> expected = new LinkedHashMap<>();
                    for (var result : results) {
                        var stack = result.getA();
                        if (stack.isEmpty() || !stack.getComponentsPatch().isEmpty())
                            throw new IllegalArgumentException("empty or component-sensitive outcome");
                        var count = ExpectedQuantity.probability(result.getB()).times(stack.getCount());
                        if (!count.isZero()) expected.merge(stack.getItem(), count, ExpectedQuantity::plus);
                    }
                    if (expected.size() != 1)
                        throw new IllegalArgumentException("distinct co-products need independent allocation: " + expected);
                    var output = expected.entrySet().iterator().next();
                    int expectationScale = ExpectedQuantity.scale(output.getValue());
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
                    if (total > 0) plan.choice(Map.of(orange, Math.toIntExact(total)));
                    plan.scale(expectationScale);
                    routes += plan.contribute(graph, NSSItem.createItem(output.getKey()),
                            output.getValue().countAt(expectationScale), ResourceAccounting.Cost.ZERO);
                    mapped++;
                    continue;
                } else if (recipe.getClass() == FusionShrineRecipe.class) {
                    var r = (FusionShrineRecipe) recipe;
                    var json = encode(r, registries);
                    if (json.has("copy_components") && json.get("copy_components").getAsBoolean())
                        throw new IllegalArgumentException("component transfer");
                    var materials = list(json, "ingredients", registries);
                    int batch = fusionBatch(materials, out);
                    for (var input : materials) consume(plan, input, true);
                    if (!r.getFluid().isEmpty()) {
                        plan.scale(batch);
                        fusionFluid(plan, r.getFluid());
                        routes += plan.contribute(graph, NSSItem.createItem(out),
                                Math.multiplyExact(out.getCount(), batch), ResourceAccounting.Cost.ZERO);
                        mapped++;
                        continue;
                    }
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
