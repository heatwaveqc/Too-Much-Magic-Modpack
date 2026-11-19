package io.github.heatwaveqc.tmm.economy;

import com.klikli_dev.theurgy.content.recipe.CalcinationRecipe;
import com.klikli_dev.theurgy.content.recipe.DistillationRecipe;
import com.klikli_dev.theurgy.registry.RecipeTypeRegistry;
import com.mojang.logging.LogUtils;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.slf4j.Logger;

/** Theurgy 1.79.0 release source: ordinary sized-input thermal processing.
 * Heat is a machine condition, not an independently priced recipe resource.
 * Do not infer productive duplication/reformation routes or fluid costs here. */
@EMCMapper(requiredMods = {"theurgy"})
public final class TheurgyMaterialMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Theurgy Material Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.theurgy_material"; }
    @Override public String getDescription() { return "Map ordinary calcination and distillation using consumed sized ingredients."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof CalcinationRecipe) && !(recipe instanceof DistillationRecipe)) continue;
            try {
                SizedIngredient input;
                if (recipe.getClass() == CalcinationRecipe.class && recipe.getType() == RecipeTypeRegistry.CALCINATION.get()) {
                    input = ((CalcinationRecipe) recipe).sizedIngredient();
                } else if (recipe.getClass() == DistillationRecipe.class && recipe.getType() == RecipeTypeRegistry.DISTILLATION.get()) {
                    input = ((DistillationRecipe) recipe).getIngredient();
                } else throw new IllegalArgumentException("special recipe subtype or machine");
                var plan = new RecipeGraphPlan();
                // CraftingBehaviour extracts the sized input and inserts the assembled output.
                // No native return stack in the inspected ordinary machines.
                plan.consumed(input.ingredient(), input.count(), false);
                routes += plan.contribute(graph, recipe.getResultItem(registries), ResourceAccounting.Cost.ZERO);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Theurgy material mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Theurgy material mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
