package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.blockentity.AlchemyTabletBlockEntity;
import com.rekindled.embers.recipe.IAlchemyRecipe;
import com.rekindled.embers.recipe.AlchemyRecipe;
import com.rekindled.embers.recipe.AlchemyRecipeForBabies;
import com.rekindled.embers.blockentity.MixerCentrifugeBottomBlockEntity;
import com.rekindled.embers.blockentity.StamperBlockEntity;
import com.rekindled.embers.recipe.FluidIngredient;
import com.rekindled.embers.recipe.IMeltingRecipe;
import com.rekindled.embers.recipe.IMixingRecipe;
import com.rekindled.embers.recipe.IStampingRecipe;
import com.rekindled.embers.recipe.MeltingRecipe;
import com.rekindled.embers.recipe.MixingRecipe;
import com.rekindled.embers.recipe.StampingRecipe;
import java.util.LinkedHashMap;
import java.util.Map;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSFluid;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.fluids.FluidStack;
import org.slf4j.Logger;

/** Embers Re-Ignited 1.5.9. Base-machine recipes use native mB graph keys
 * and nominal consumed Ember, not fixed per-metal prices. */
@EMCMapper(requiredMods = {"embers"})
public final class EmbersMachineMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Embers Machine Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.embers_machines"; }
    @Override public String getDescription() { return "Map ordinary melting, mixing, stamping and alchemy including Ember and fluids."; }

    private static NSSFluid fluidKey(FluidStack stack) {
        if (stack.isEmpty() || stack.getAmount() <= 0 || !stack.getComponentsPatch().isEmpty())
            throw new IllegalArgumentException("empty or component-sensitive fluid");
        return NSSFluid.createFluid(stack);
    }

    private static void fluid(RecipeGraphPlan plan, FluidIngredient ingredient) {
        Map<NormalizedSimpleStack, Integer> alternatives = new LinkedHashMap<>();
        for (var stack : ingredient.getFluids()) {
            var key = fluidKey(stack);
            int amount = ingredient.getAmount(stack.getFluid());
            if (amount <= 0 || stack.getAmount() != amount || !ingredient.test(stack))
                throw new IllegalArgumentException("ambiguous fluid quantity");
            Integer old = alternatives.putIfAbsent(key, amount);
            if (old != null && old != amount) throw new IllegalArgumentException("ambiguous fluid alternative");
        }
        plan.choice(alternatives);
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof IMeltingRecipe) && !(recipe instanceof IMixingRecipe)
                    && !(recipe instanceof IStampingRecipe) && !(recipe instanceof IAlchemyRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                if (recipe.getClass() == MeltingRecipe.class) {
                    var r = (MeltingRecipe) recipe;
                    // Base melter consumes one input and outputs the ordinary fluid;
                    // upgrade-dependent bonus routes are not independent valuations.
                    plan.consumed(r.ingredient, 1, false);
                    var out = r.getDisplayOutput();
                    double rate = ConfigManager.MELTER_EMBER_COST.get();
                    int time = ConfigManager.MELTER_PROCESS_TIME.get();
                    if (time <= 0) throw new IllegalArgumentException("invalid melter process time");
                    var cost = ResourceAccounting.Rate.EMBER.ofDecimal(rate).times(time);
                    routes += plan.contribute(graph, fluidKey(out), out.getAmount(), cost);
                } else if (recipe.getClass() == MixingRecipe.class) {
                    var r = (MixingRecipe) recipe;
                    for (var input : r.inputs) fluid(plan, input);
                    var out = r.getDisplayOutput();
                    var cost = ResourceAccounting.Rate.EMBER.ofDecimal(MixerCentrifugeBottomBlockEntity.EMBER_COST);
                    routes += plan.contribute(graph, fluidKey(out), out.getAmount(), cost);
                } else if (recipe.getClass() == StampingRecipe.class) {
                    var r = (StampingRecipe) recipe;
                    // Stamp persists; only the optional solid and fluid are consumed.
                    plan.slot(r.stamp, 1, s -> true, false, false);
                    if (!r.input.isEmpty()) plan.consumed(r.input, 1, false);
                    if (r.fluid != FluidIngredient.EMPTY) fluid(plan, r.fluid);
                    // Native resolves tag-selected outputs; do not select a different item.
                    var out = r.getResultItem();
                    var cost = ResourceAccounting.Rate.EMBER.ofDecimal(StamperBlockEntity.EMBER_COST);
                    routes += plan.contribute(graph, out, cost);
                } else if (recipe.getClass() == AlchemyRecipe.class || recipe.getClass() == AlchemyRecipeForBabies.class) {
                    var r = (IAlchemyRecipe) recipe;
                    if (r.getInputs().isEmpty() || r.getAspects().isEmpty())
                        throw new IllegalArgumentException("unresolved ordinary alchemy code");
                    if (recipe.getClass() == AlchemyRecipeForBabies.class
                            && ConfigManager.randomizeAlchemyAspectus()
                            && r.getAspects().size() > r.getInputs().size())
                        throw new IllegalArgumentException("impossible randomized aspect code");
                    plan.consumed(r.getCenterInput(), 1, false);
                    for (var input : r.getInputs()) plan.consumed(input, 1, false);
                    // The top pedestal items are consumed; the bottom Aspectus
                    // are not. Their world-seeded ordering never changes the debit.
                    for (var aspect : r.getAspects()) plan.slot(aspect, 1, s -> true, false, false);
                    // A direct successful ignition clears the Beam Cannon charge.
                    // Use the minimum native accepted threshold; excess charge and
                    // failed attempts are not required nominal recipe costs.
                    var cost = ResourceAccounting.Rate.EMBER.of(AlchemyTabletBlockEntity.SPARK_THRESHOLD);
                    routes += plan.contribute(graph, r.getResultItem(), cost);
                } else throw new IllegalArgumentException("special machine or alchemy recipe");
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Embers mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Embers mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
