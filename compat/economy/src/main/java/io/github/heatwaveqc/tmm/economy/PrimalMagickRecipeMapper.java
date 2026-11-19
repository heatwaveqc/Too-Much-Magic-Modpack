package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import com.verdantartifice.primalmagick.common.blocks.rituals.CelestialHarpBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.IncenseBrazierBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualBellBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.RitualLecternBlock;
import com.verdantartifice.primalmagick.common.blocks.rituals.SoulAnvilBlock;
import com.verdantartifice.primalmagick.common.crafting.*;
import com.verdantartifice.primalmagick.common.items.ItemsPM;
import com.verdantartifice.primalmagick.common.sources.Source;
import com.verdantartifice.primalmagick.common.sources.SourceList;
import com.verdantartifice.primalmagick.common.sources.Sources;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;

/** Primal Magick 6.0.7. Mana values are native centimana, not displayed Mana. */
@EMCMapper(requiredMods = {"primalmagick"})
public final class PrimalMagickRecipeMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Primal Magick Recipe Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.primal_recipes"; }
    @Override public String getDescription() { return "Map ordinary arcane crafting, dissolution, concoction and supported rituals."; }

    private static ResourceAccounting.Cost mana(SourceList list, Source only) {
        var result = ResourceAccounting.Cost.ZERO;
        for (var source : list.getSources()) {
            int amount = list.getAmount(source);
            if (only != null && !only.equals(source) && amount != 0)
                throw new IllegalArgumentException("machine ignores this mana source");
            result = result.plus(ResourceAccounting.Rate.PRIMAL_CENTIMANA.of(amount));
        }
        return result;
    }

    private static void addProps(RecipeGraphPlan plan, RitualRecipe r) {
        for (var prop : r.getProps()) {
            var blocks = prop.getMatchingBlocks();
            if (blocks.length == 0) throw new IllegalArgumentException("unresolved ritual prop");
            Class<?> type = blocks[0].getClass();
            for (var block : blocks)
                if (block.getClass() != type) throw new IllegalArgumentException("mixed ritual prop costs");
            if (type == IncenseBrazierBlock.class) {
                plan.consumed(Ingredient.of(ItemsPM.INCENSE_STICK.get()), 1, false);
            } else if (type == SoulAnvilBlock.class) {
                plan.consumed(Ingredient.of(ItemsPM.SOUL_GEM.get()), 1, false);
            } else if (type != CelestialHarpBlock.class && type != RitualBellBlock.class
                    && type != RitualLecternBlock.class) {
                // Candle tool wear, bloodletting/reset costs, universal entropy
                // sink and custom props are not guessed.
                throw new IllegalArgumentException("unaccounted ritual prop: " + type.getSimpleName());
            }
        }
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof IHasManaCost)) continue;
            try {
                var plan = new RecipeGraphPlan();
                var cost = ResourceAccounting.Cost.ZERO;
                boolean remainders = false;
                if (recipe.getClass() == ShapedArcaneRecipe.class
                        || recipe.getClass() == ShapelessArcaneRecipe.class
                        || recipe.getClass() == ShapelessArcaneTagRecipe.class) {
                    cost = mana(((IArcaneRecipe) recipe).getManaCosts(), null);
                    remainders = true;
                } else if (recipe.getClass() == DissolutionRecipe.class
                        || recipe.getClass() == DissolutionTagRecipe.class) {
                    cost = mana(((IDissolutionRecipe) recipe).getManaCosts(), Sources.EARTH);
                } else if (recipe.getClass() == ConcoctingRecipe.class) {
                    cost = mana(((IConcoctingRecipe) recipe).getManaCosts(), Sources.INFERNAL);
                } else if (recipe.getClass() == RitualRecipe.class) {
                    var r = (RitualRecipe) recipe;
                    cost = mana(r.getManaCosts(), null);
                    addProps(plan, r);
                } else throw new IllegalArgumentException("special mana recipe subtype");

                for (var input : recipe.getIngredients()) {
                    if (input.isEmpty()) {
                        if (recipe.getClass() == ShapedArcaneRecipe.class) continue;
                        throw new IllegalArgumentException("empty material slot");
                    }
                    plan.consumed(input, 1, remainders);
                }
                routes += plan.contribute(graph, recipe.getResultItem(registries), cost);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Primal mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Primal mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
