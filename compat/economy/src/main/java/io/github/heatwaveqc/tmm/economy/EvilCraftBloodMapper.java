package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.cyclops.evilcraft.core.recipe.type.RecipeBloodInfuser;
import org.slf4j.Logger;

/** Ordinary Blood Infuser recipes, at native unupgraded blood consumption.
 * The intentional tier-3 bone-to-blaze-rod production route is not a valuation route.
 * Dynamic/ingredient-derived outputs and noncanonical fluids are held for review. */
@EMCMapper(requiredMods = {"evilcraft"})
public final class EvilCraftBloodMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final ResourceLocation BLOOD = ResourceLocation.fromNamespaceAndPath("evilcraft", "blood");
    @Override public String getName() { return "TMM EvilCraft Blood Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.evilcraft_blood"; }
    @Override public String getDescription() { return "Map literal Blood Infuser outputs including consumed canonical blood."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager resourceManager) {
        var graph = new ResourceGraph(mapper);
        int mapped = 0, skipped = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            if (!(holder.value() instanceof RecipeBloodInfuser recipe)) continue;
            try {
                if (recipe.getClass() != RecipeBloodInfuser.class)
                    throw new IllegalArgumentException("special implementation");
                var fluid = recipe.getInputFluid().orElseThrow(() -> new IllegalArgumentException("no explicit blood cost"));
                if (fluid.isEmpty() || !BLOOD.equals(BuiltInRegistries.FLUID.getKey(fluid.getFluid())))
                    throw new IllegalArgumentException("noncanonical or empty fluid");
                ItemStack out = recipe.getOutputItem().left()
                        .orElseThrow(() -> new IllegalArgumentException("ingredient-derived output"));
                if (out.isEmpty() || !out.getComponentsPatch().isEmpty())
                    throw new IllegalArgumentException("component-sensitive output");
                if (out.is(Items.BLAZE_ROD))
                    throw new IllegalArgumentException("approved productive blaze rod route");
                var output = NSSItem.createItem(out);
                var cost = ResourceAccounting.Rate.BLOOD_MB.of(Math.max(1, fluid.getAmount()));
                List<Object2IntOpenHashMap<NormalizedSimpleStack>> plans = new ArrayList<>();
                if (recipe.getInputIngredient().isEmpty()) {
                    plans.add(new Object2IntOpenHashMap<>());
                } else {
                    var ingredient = recipe.getInputIngredient().orElseThrow();
                    ItemStack[] inputs = ingredient.getItems();
                    if (inputs.length == 0 || inputs.length > 512)
                        throw new IllegalArgumentException("unresolved or excessive alternatives");
                    for (ItemStack input : inputs) {
                        if (input.isEmpty() || !input.getComponentsPatch().isEmpty() || !ingredient.test(input))
                            throw new IllegalArgumentException("component-sensitive input");
                        var key = NSSItem.createItem(input);
                        if (key.equals(output)) throw new IllegalArgumentException("self-referential recipe");
                        var consumed = new Object2IntOpenHashMap<NormalizedSimpleStack>();
                        consumed.put(key, 1); // Native tick action removes exactly one input.
                        plans.add(consumed);
                    }
                }
                cost.scaleCount(out.getCount()); cost.emcUnits();
                for (var consumed : plans) graph.add(out.getCount(), output, consumed, cost);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM EvilCraft mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM EvilCraft mapper: {} mapped, {} held for review", mapped, skipped);
    }
}
