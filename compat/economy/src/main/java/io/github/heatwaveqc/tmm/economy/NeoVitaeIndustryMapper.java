package io.github.heatwaveqc.tmm.economy;

import com.breakinblocks.neovitae.common.alchemyarray.AlchemyArrayEffectType;
import com.breakinblocks.neovitae.common.datacomponent.SpiritusType;
import com.breakinblocks.neovitae.common.recipe.alchemyarray.AlchemyArrayRecipe;
import com.breakinblocks.neovitae.common.recipe.aravitae.AraVitaeRecipe;
import com.breakinblocks.neovitae.common.recipe.athanor.AthanorRecipe;
import com.breakinblocks.neovitae.common.recipe.forge.ForgeRecipe;
import com.breakinblocks.neovitae.common.tag.NVTags;
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

/** Plain Neo Vitae 1.1.53 industry. Do not price modified Spiritus gems
 * as if their contents were free, or randomized/byproduct/tool routes as deterministic. */
@EMCMapper(requiredMods = {"neovitae"})
public final class NeoVitaeIndustryMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Neo Vitae Industry Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.neovitae_industry"; }
    @Override public String getDescription() { return "Map blood altar, ordinary Hellfire Forge, simple Athanor and crafting arrays."; }

    private static ResourceAccounting.Rate spiritRate(SpiritusType type) {
        return type == SpiritusType.RAW ? ResourceAccounting.Rate.RAW_SPIRITUS : ResourceAccounting.Rate.ASPECTED_SPIRITUS;
    }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0, routes = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof com.breakinblocks.neovitae.api.recipe.AraVitaeRecipe)
                    && !(recipe instanceof ForgeRecipe) && !(recipe instanceof AthanorRecipe)
                    && !(recipe instanceof AlchemyArrayRecipe)) continue;
            try {
                var plan = new RecipeGraphPlan();
                ResourceAccounting.Cost cost = ResourceAccounting.Cost.ZERO;
                ItemStack out;
                if (recipe.getClass() == AraVitaeRecipe.class) {
                    var r = (AraVitaeRecipe) recipe;
                    if (r.shouldCopyInputComponents()) throw new IllegalArgumentException("input component transfer");
                    plan.consumed(r.getInput(), 1, false);
                    // AraVitaeTile replaces the entire input stack, overriding result count
                    // with input count; getTotalBlood is per input item.
                    out = r.getResult().copyWithCount(1);
                    cost = ResourceAccounting.Rate.BLOOD_MB.of(r.getTotalBlood());
                } else if (recipe.getClass() == ForgeRecipe.class) {
                    var r = (ForgeRecipe) recipe;
                    out = r.getOutput();
                    if (out.is(NVTags.Items.SPIRITUS_GEM))
                        throw new IllegalArgumentException("Spiritus gem output may transfer stored charge");
                    for (var ingredient : r.getCraftingIngredients()) {
                        for (var stack : ingredient.getItems())
                            if (stack.is(NVTags.Items.SPIRITUS_GEM))
                                throw new IllegalArgumentException("Spiritus gem ingredient may act as fuel");
                        plan.consumed(ingredient, 1, true);
                    }
                    // minDrain is a starting requirement, not consumption.
                    // Untyped recipes can use raw Spiritus at the lowest approved rate.
                    cost = spiritRate(r.getSpiritusType().orElse(SpiritusType.RAW)).ofDecimal(r.getDrain());
                } else if (recipe.getClass() == AthanorRecipe.class) {
                    var r = (AthanorRecipe) recipe;
                    if (r.requiresTool() || r.getInputFluid().isPresent() || r.getOutputFluid().isPresent()
                            || r.isSpiritusBoosted() || !r.getChanceOutput().isEmpty()
                            || r.getGuaranteedOutput().size() != 1)
                        throw new IllegalArgumentException("tool wear, fluid, random/boosted or multiple outputs");
                    out = r.getGuaranteedOutput().get(0);
                    for (var ing : r.getInputs()) plan.consumed(ing, 1, false);
                    for (var e : r.getSpiritusCosts().entrySet())
                        cost = cost.plus(spiritRate(e.getKey()).ofDecimal(e.getValue()));
                } else if (recipe.getClass() == AlchemyArrayRecipe.class) {
                    var r = (AlchemyArrayRecipe) recipe;
                    if (r.getEffectType() != AlchemyArrayEffectType.CRAFTING || r.getEvCost() != 0)
                        throw new IllegalArgumentException("effect/binding or unverified EV semantics");
                    // The block extracts one base and one added item on success.
                    plan.consumed(r.getBaseInput(), 1, false);
                    plan.consumed(r.getAddedInput(), 1, false);
                    out = r.getOutput();
                } else throw new IllegalArgumentException("custom recipe class");
                routes += plan.contribute(graph, out, cost);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Neo Vitae industry skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Neo Vitae industry: {} recipes, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
