package io.github.heatwaveqc.tmm.economy;

import com.brandon3055.draconicevolution.api.crafting.FusionRecipe;
import com.brandon3055.draconicevolution.api.crafting.IFusionRecipe;
import com.brandon3055.draconicevolution.api.crafting.IFusionDataTransfer;
import com.brandon3055.draconicevolution.api.crafting.StackIngredient;
import com.mojang.logging.LogUtils;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

/** Ordinary Draconic Evolution fusion. The native configured total energy
 * is an accounting baseline; injector charging rounding may consume extra FE. */
@EMCMapper(requiredMods = {"draconicevolution"})
public final class DraconicFusionMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Draconic Fusion Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.draconic_fusion"; }
    @Override public String getDescription() { return "Map standard fusion recipes at their nominal energy and consumed item cost."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            if (!(holder.value() instanceof IFusionRecipe r)) continue;
            try {
                if (r.getClass() != FusionRecipe.class) throw new IllegalArgumentException("special fusion state machine");
                var out = r.getResultItem(registries);
                if (out.isEmpty() || out.getItem() instanceof IFusionDataTransfer)
                    throw new IllegalArgumentException("dynamic data transfer or output");
                // Native completeCraft shrinks the central catalyst by the explicit StackIngredient count.
                var plan = new RecipeGraphPlan();
                var catalyst = r.getCatalyst();
                if (catalyst.getCustomIngredient() instanceof StackIngredient sized)
                    plan.slot(catalyst, sized.getCount(), s -> false, false, true);
                else plan.consumed(catalyst, 1, false);
                for (var injector : r.fusionIngredients()) {
                    // Native injector consumes one item or replaces it with its remainder.
                    // Count-bearing injector ingredients are not the same as the central catalyst.
                    plan.slot(injector.get(), 1, s -> !injector.consume(), injector.consume(), false);
                }
                routes += plan.contribute(graph, out, ResourceAccounting.Rate.FE.of(r.getEnergyCost()));
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM fusion mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM fusion mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
