package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import hellfirepvp.astralsorcery.common.ingredient.IngredientBridge;
import hellfirepvp.astralsorcery.common.lib.FluidsAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.recipe.altar.AltarRecipe;
import hellfirepvp.astralsorcery.common.recipe.infusion.InfusionRecipe;
import java.util.LinkedHashMap;
import java.util.Map;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSFluid;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.fluids.FluidStack;
import org.slf4j.Logger;

/** Astral Sorcery 2.0.3 release-era API, literal recipes only.
 * Sky starlight is a condition; liquid is a consumed material.
 * Lumen and focus breakage do not yet have approved resource rates. */
@EMCMapper(requiredMods = {"astralsorcery"})
public final class AstralRecipeMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final int STARLIGHT_MB_PER_AQUAMARINE = 2333;
    @Override public String getName() { return "TMM Astral Sorcery Recipe Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.astral_recipes"; }
    @Override public String getDescription() { return "Map literal altar and deterministic infusion recipes, including liquid starlight."; }

    private static void fluid(RecipeGraphPlan plan, FluidStack stack) {
        if (stack.isEmpty() || !stack.getComponentsPatch().isEmpty())
            throw new IllegalArgumentException("empty or component-sensitive fluid");
        plan.choice(Map.of(NSSFluid.createFluid(stack), stack.getAmount()));
    }

    private static void grid(RecipeGraphPlan plan, IngredientBridge bridge) {
        if (bridge.getIngredientType() == IngredientBridge.Type.ITEM) {
            if (!bridge.getIngredient().isEmpty()) plan.consumed(bridge.getIngredient(), 1, true);
            return;
        }
        var input = bridge.getFluidIngredient();
        if (!input.ingredient().isSimple() || input.amount() <= 0)
            throw new IllegalArgumentException("custom or unresolved fluid ingredient");
        Map<NormalizedSimpleStack, Integer> alternatives = new LinkedHashMap<>();
        for (var stack : input.getFluids()) {
            if (stack.isEmpty() || !stack.getComponentsPatch().isEmpty() || stack.getAmount() != input.amount()
                    || !input.test(stack))
                throw new IllegalArgumentException("invalid fluid alternative");
            alternatives.put(NSSFluid.createFluid(stack), stack.getAmount());
        }
        plan.choice(alternatives);
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        // Owner-approved value basis, not a new real crafting recipe.
        var anchor = new RecipeGraphPlan();
        anchor.choice(Map.of(NSSItem.createItem(ItemsAS.AQUAMARINE.get()), 1));
        anchor.contribute(graph, NSSFluid.createFluid(FluidsAS.LIQUID_STARLIGHT.getSource().get()),
                STARLIGHT_MB_PER_AQUAMARINE, ResourceAccounting.Cost.ZERO);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof AltarRecipe) && !(recipe instanceof InfusionRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                if (recipe.getClass() == AltarRecipe.class) {
                    var r = (AltarRecipe) recipe;
                    if (!r.getOutputModifiers().isEmpty() || r.getOutputs().size() != 1
                            || !r.getRequiredLumen().isEmpty() || r.getBaseFocusShatterChance() > 0)
                        throw new IllegalArgumentException("dynamic/multiple output, priced lumen not defined, or focus wear");
                    for (var input : r.getGrid().getInputs()) grid(plan, input);
                    for (var input : r.getGrid().getRelayInputs()) grid(plan, input);
                    for (var input : r.getRequiredAdditionalInputs())
                        plan.consumed(input.ingredient(), input.count(), false);
                    for (var f : r.getRequiredFluid()) fluid(plan, f);
                    routes += plan.contribute(graph, r.getOutputs().getFirst(), ResourceAccounting.Cost.ZERO);
                } else if (recipe.getClass() == InfusionRecipe.class) {
                    var r = (InfusionRecipe) recipe;
                    plan.consumed(r.getItemInput(), 1, false);
                    float chance = r.getFluidConsumptionChance();
                    if (!Float.isFinite(chance) || chance < 0 || chance > 1)
                        throw new IllegalArgumentException("invalid infusion chance");
                    // Do not convert stochastic pool losses into a guessed fixed
                    // quantity. A valid deterministic chalice path is accounted
                    // from the actual loaded recipe getter.
                    if (chance > 0) {
                        if (!r.acceptChaliceInput())
                            throw new IllegalArgumentException("pool-only fluid loss not modeled");
                        fluid(plan, r.getChaliceInputFluidStack());
                    }
                    routes += plan.contribute(graph, r.getOutput(), ResourceAccounting.Cost.ZERO);
                } else throw new IllegalArgumentException("special Astral recipe");
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Astral mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Astral mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
