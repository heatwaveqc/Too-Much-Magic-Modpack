package io.github.heatwaveqc.tmm.economy;

import com.Polarice3.Goety.api.items.magic.IWand;
import com.Polarice3.Goety.common.crafting.BrazierRecipe;
import com.Polarice3.Goety.common.crafting.CursedInfuserRecipes;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.ritual.CraftItemRitual;
import com.mojang.logging.LogUtils;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;

/** Goety 3.2.2: ordinary item rituals, Necro Brazier and Cursed Infuser.
 * Do not price creature sacrifices, summoning, enchantment or dynamic item states. */
@EMCMapper(requiredMods = {"goety"})
public final class GoetyRecipeMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Goety Recipe Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.goety_recipes"; }
    @Override public String getDescription() { return "Map ordinary item rituals and machines including actual Soul Energy debits."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof RitualRecipe) && !(recipe instanceof BrazierRecipe)
                    && !(recipe instanceof CursedInfuserRecipes)) continue;
            try {
                var plan = new RecipeGraphPlan();
                var cost = ResourceAccounting.Cost.ZERO;
                ItemStack out = recipe.getResultItem(registries);
                if (recipe.getClass() == RitualRecipe.class) {
                    var r = (RitualRecipe) recipe;
                    if (r.getRitual() == null || r.getRitual().getClass() != CraftItemRitual.class
                            || r.requiresSacrifice() || r.isSummoning() || r.isConversion()
                            || r.getXPLevelCost() != 0 || r.getDuration() <= 0)
                        throw new IllegalArgumentException("nonordinary or stateful ritual");
                    if (out.getItem() instanceof IWand) {
                        for (var input : r.getActivationItem().getItems())
                            if (input.getItem() instanceof IWand)
                                throw new IllegalArgumentException("wand focus transfer");
                    }
                    // The ordinary ritual consumes the activation item; pedestal inputs
                    // return their native container remainders.
                    plan.consumed(r.getActivationItem(), 1, false);
                    for (var input : r.getIngredients()) plan.consumed(input, 1, true);
                    // DarkAltarBlockEntity increments currentTime only on its 20-tick debit.
                    cost = ResourceAccounting.Rate.GOETY_SOUL_ENERGY.of(r.getSoulCost()).times(r.getDuration());
                } else if (recipe.getClass() == BrazierRecipe.class) {
                    var r = (BrazierRecipe) recipe;
                    if (r.getSoulCost() <= 0) throw new IllegalArgumentException("nonpositive brazier target");
                    for (var input : r.getIngredients()) plan.consumed(input, 1, false);
                    // Each candle drain is one unit and advances progress by one.
                    // More lit candles can overshoot the target on the last tick.
                    cost = ResourceAccounting.Rate.GOETY_SOUL_ENERGY.of(r.getSoulCost());
                } else if (recipe.getClass() == CursedInfuserRecipes.class) {
                    var r = (CursedInfuserRecipes) recipe;
                    if (r.isGrim()) throw new IllegalArgumentException("grim infuser special route");
                    // Spawner is an infrastructure condition, not consumed.
                    for (var input : r.getIngredients()) plan.consumed(input, 1, false);
                } else throw new IllegalArgumentException("special recipe subtype");
                routes += plan.contribute(graph, out, cost);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Goety mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Goety mapper: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
