package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import com.sammy.malum.common.recipe.RuneworkingRecipe;
import com.sammy.malum.common.recipe.SpiritInfusionRecipe;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

/** Malum 1.8.2 ordinary material recipes; spirits are actual priced items.
 * No change to Spirit Jar contents, Impetus wear, repair or other gameplay. */
@EMCMapper(requiredMods = {"malum"})
public final class MalumMaterialMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Malum Material Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.malum_material"; }
    @Override public String getDescription() { return "Map ordinary spirit infusion and runeworking with actual spirit shards and quantities."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof SpiritInfusionRecipe) && !(recipe instanceof RuneworkingRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                ItemStack output;
                if (recipe.getClass() == SpiritInfusionRecipe.class) {
                    var r = (SpiritInfusionRecipe) recipe;
                    if (r.carryOverComponentData) throw new IllegalArgumentException("component transfer");
                    plan.consumed(r.input.ingredient(), r.input.count(), false);
                    for (var extra : r.extraInputs) plan.consumed(extra.ingredient(), extra.count(), false);
                    for (var spirit : r.spirits) {
                        ItemStack shard = spirit.asItemStack();
                        if (shard.isEmpty() || !shard.getComponentsPatch().isEmpty()
                                || shard.getCount() != spirit.count() || !spirit.test(shard))
                            throw new IllegalArgumentException("invalid or component-sensitive spirit shard");
                        plan.consumed(Ingredient.of(shard.getItem()), spirit.count(), false);
                    }
                    output = r.result;
                } else if (recipe.getClass() == RuneworkingRecipe.class) {
                    var r = (RuneworkingRecipe) recipe;
                    plan.consumed(r.input.ingredient(), r.input.count(), false);
                    plan.consumed(r.secondaryInput.ingredient(), r.secondaryInput.count(), false);
                    output = r.output;
                } else throw new IllegalArgumentException("special recipe subclass");
                routes += plan.contribute(graph, output, ResourceAccounting.Cost.ZERO);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Malum material mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Malum material mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
