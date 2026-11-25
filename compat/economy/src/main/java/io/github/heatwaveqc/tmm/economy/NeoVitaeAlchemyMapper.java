package io.github.heatwaveqc.tmm.economy;

import com.breakinblocks.neovitae.common.recipe.tabulavitae.TabulaVitaeRecipe;
import com.breakinblocks.neovitae.common.tag.NVTags;
import com.mojang.logging.LogUtils;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

/** Neo Vitae 1.1.53 ordinary Tabula Vitae; syphon is the total EV debit,
 * not EV per tick. Cutting-fluid recipes have wear and potential bonus yields,
 * and are deliberately held rather than charged as whole tools. */
@EMCMapper(requiredMods = {"neovitae"})
public final class NeoVitaeAlchemyMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Neo Vitae Alchemy Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.neovitae_alchemy"; }
    @Override public String getDescription() { return "Map ordinary Tabula Vitae recipes including their EV debit."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            if (!(holder.value() instanceof TabulaVitaeRecipe r)) continue;
            try {
                if (r.getClass() != TabulaVitaeRecipe.class) throw new IllegalArgumentException("special alchemy recipe");
                var plan = new RecipeGraphPlan();
                for (var ingredient : r.getInput()) {
                    for (var stack : ingredient.getItems())
                        if (stack.is(NVTags.Items.CUTTING_FLUIDS))
                            throw new IllegalArgumentException("cutting fluid wear or bonus yield");
                    plan.consumed(ingredient, 1, true);
                }
                routes += plan.contribute(graph, r.getOutput(), ResourceAccounting.Rate.BLOOD_MB.of(r.getSyphon()));
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Neo Vitae mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Neo Vitae mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
