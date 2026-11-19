package io.github.heatwaveqc.tmm.economy;

import com.hollingsworth.arsnouveau.common.crafting.recipes.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.mojang.logging.LogUtils;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;

/** Ars Nouveau 5.13.5: actual Source debit and pedestal consumption.
 * Imbuement pedestal items are retained; apparatus preserves tagged items
 * and returns a native crafting remainder for other pedestal items. */
@EMCMapper(requiredMods = {"ars_nouveau"})
public final class ArsSourceMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final TagKey<Item> PRESERVED = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("ars_nouveau", "apparatus_not_consumed"));
    @Override public String getName() { return "TMM Ars Source Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.ars_source"; }
    @Override public String getDescription() { return "Map imbuement and enchanting apparatus recipes at their consumed Source cost."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof ImbuementRecipe) && !(recipe instanceof EnchantingApparatusRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                int source;
                if (recipe.getClass() == ImbuementRecipe.class) {
                    var r = (ImbuementRecipe) recipe;
                    plan.consumed(r.getInput(), 1, false); // input replaced entirely
                    // Pedestals are reusable infrastructure; not consumed.
                    for (var pedestal : r.getPedestalItems())
                        plan.slot(pedestal, 1, s -> true, false, false);
                    source = r.getSource();
                } else if (recipe.getClass() == EnchantingApparatusRecipe.class) {
                    var r = (EnchantingApparatusRecipe) recipe;
                    if (r.keepNbtOfReagent()) throw new IllegalArgumentException("reagent component transfer");
                    plan.consumed(r.reagent(), 1, false);
                    for (var pedestal : r.pedestalItems())
                        plan.slot(pedestal, 1, s -> s.is(PRESERVED), true, false);
                    source = r.sourceCost();
                } else throw new IllegalArgumentException("special recipe subclass");
                routes += plan.contribute(graph, recipe.getResultItem(registries), ResourceAccounting.Rate.SOURCE.of(source));
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Ars mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Ars mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
