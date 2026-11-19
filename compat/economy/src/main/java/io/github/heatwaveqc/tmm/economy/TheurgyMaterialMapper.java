package io.github.heatwaveqc.tmm.economy;

import com.klikli_dev.theurgy.content.recipe.*;
import com.mojang.logging.LogUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSFake;
import moze_intel.projecte.api.nss.NSSFluid;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.slf4j.Logger;

/** Theurgy 1.79.0: effective deterministic thermal and solvent processing.
 * Never infer transmutation prices from productive reformation or free solvent growth. */
@EMCMapper(requiredMods = {"theurgy"})
public final class TheurgyMaterialMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final NSSFake MERCURY_FLUX = NSSFake.create("tmm_economy:theurgy_mercury_flux");
    @Override public String getName() { return "TMM Theurgy Material Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.theurgy_material"; }
    @Override public String getDescription() { return "Map ordinary thermal, solvent and incubation recipes; preserve productive routes."; }

    private static NSSFluid fluidKey(FluidStack stack) {
        if (stack.isEmpty() || stack.getAmount() <= 0 || !stack.getComponentsPatch().isEmpty())
            throw new IllegalArgumentException("empty or component-sensitive fluid");
        return NSSFluid.createFluid(stack);
    }

    private static void fluid(RecipeGraphPlan plan, SizedFluidIngredient input) {
        if (!input.ingredient().isSimple() || input.amount() <= 0)
            throw new IllegalArgumentException("custom or unresolved fluid ingredient");
        Map<NormalizedSimpleStack, Integer> choices = new LinkedHashMap<>();
        for (var stack : input.getFluids()) {
            if (stack.getAmount() != input.amount() || !input.test(stack))
                throw new IllegalArgumentException("invalid fluid quantity");
            choices.put(fluidKey(stack), stack.getAmount());
        }
        plan.choice(choices);
    }

    /** Native greedy extraction can consume fewer items for differently sized
     * overlapping slots; hold these rather than model a false cheaper route. */
    private static void distinctSized(java.util.List<net.neoforged.neoforge.common.crafting.SizedIngredient> inputs) {
        Map<net.minecraft.world.item.Item,Integer> counts = new LinkedHashMap<>();
        for (var in : inputs) for (var option : in.ingredient().getItems()) {
            var old = counts.putIfAbsent(option.getItem(), in.count());
            if (old != null && old.intValue() != in.count())
                throw new IllegalArgumentException("overlapping sized inputs");
        }
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof CalcinationRecipe || recipe instanceof DistillationRecipe
                    || recipe instanceof DigestionRecipe || recipe instanceof FermentationRecipe
                    || recipe instanceof IncubationRecipe || recipe instanceof LiquefactionRecipe
                    || recipe instanceof AccumulationRecipe || recipe instanceof CatalysationRecipe
                    || recipe instanceof ReformationRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                if (recipe.getClass() == CalcinationRecipe.class) {
                    var input = ((CalcinationRecipe) recipe).sizedIngredient();
                    plan.consumed(input.ingredient(), input.count(), false);
                } else if (recipe.getClass() == DistillationRecipe.class) {
                    var input = ((DistillationRecipe) recipe).getIngredient();
                    plan.consumed(input.ingredient(), input.count(), false);
                } else if (recipe.getClass() == DigestionRecipe.class) {
                    var r = (DigestionRecipe) recipe;
                    distinctSized(r.getSizedIngredients());
                    for (var input : r.getSizedIngredients())
                        plan.consumed(input.ingredient(), input.count(), false);
                    fluid(plan, r.getFluid());
                } else if (recipe.getClass() == FermentationRecipe.class) {
                    var r = (FermentationRecipe) recipe;
                    for (var input : r.getIngredients()) plan.consumed(input, 1, false);
                    fluid(plan, r.getFluid());
                } else if (recipe.getClass() == LiquefactionRecipe.class) {
                    var r = (LiquefactionRecipe) recipe;
                    plan.consumed(r.getIngredient(), 1, false);
                    fluid(plan, r.getSolvent());
                } else if (recipe.getClass() == IncubationRecipe.class) {
                    var r = (IncubationRecipe) recipe;
                    plan.consumed(r.getMercury(), 1, false);
                    plan.consumed(r.getSalt(), 1, false);
                    plan.consumed(r.getSulfur(), 1, false);
                } else if (recipe.getClass() == CatalysationRecipe.class) {
                    var r = (CatalysationRecipe) recipe;
                    plan.consumed(r.getIngredient(), 1, false);
                    routes += plan.contribute(graph, MERCURY_FLUX, r.getTotalMercuryFlux(), ResourceAccounting.Cost.ZERO);
                    mapped++;
                    continue;
                } else if (recipe.getClass() == AccumulationRecipe.class) {
                    var r = (AccumulationRecipe) recipe;
                    if (!r.hasSolute())
                        throw new IllegalArgumentException("renewable solvent production; not an EMC price route");
                    plan.consumed(r.getSolute(), 1, false);
                    if (r.hasEvaporant()) fluid(plan, r.getEvaporant());
                    var output = r.getResult();
                    routes += plan.contribute(graph, fluidKey(output), output.getAmount(), ResourceAccounting.Cost.ZERO);
                    mapped++;
                    continue;
                } else if (recipe.getClass() == ReformationRecipe.class) {
                    var r = (ReformationRecipe) recipe;
                    var output = r.getResult();
                    // Native target pedestal is reusable. Copying that target is intentional
                    // physical production, not a way to lower its anchor in the EMC graph.
                    if (r.getTarget().test(output.copyWithCount(1)))
                        throw new IllegalArgumentException("productive target replication excluded; mercury flux "
                                + r.getMercuryFlux() + ", output " + output.getCount());
                    distinctSized(r.getSources());
                    for (var input : r.getSources()) plan.consumed(input.ingredient(), input.count(), false);
                    plan.slot(r.getTarget(), 1, s -> true, false, false);
                    if (r.getMercuryFlux() < 0) throw new IllegalArgumentException("negative mercury flux");
                    if (r.getMercuryFlux() > 0) plan.choice(Map.of(MERCURY_FLUX, r.getMercuryFlux()));
                } else throw new IllegalArgumentException("special recipe subtype");
                routes += plan.contribute(graph, recipe.getResultItem(registries), ResourceAccounting.Cost.ZERO);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Theurgy material mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Theurgy material mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
