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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.slf4j.Logger;
import velrondevs.botania.common.crafting.ManaInfusionRecipe;
import velrondevs.botania.common.crafting.RunicAltarRecipe;
import velrondevs.botania.common.item.material.RuneItem;

/** Source-aligned to Botania Revived 2.0.2. Only ordinary concrete recipe
 * implementations are mapped; special subclasses and catalyst routes are reported.
 * Runic altar consumes additional livingrock and returns rune inputs in survival. */
@EMCMapper(requiredMods = {"botania"})
public final class BotaniaManaMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final int MAX_ALTERNATIVES = 512;
    @Override public String getName() { return "TMM Botania Mana Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.botania_mana"; }
    @Override public String getDescription() { return "Map ordinary mana infusions and runic altar recipes with consumed mana."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager resourceManager) {
        var graph = new ResourceGraph(mapper);
        var rock = BuiltInRegistries.ITEM.getOptional(ResourceLocation.fromNamespaceAndPath("botania","livingrock"));
        int mapped = 0, skipped = 0;
        for (var holder : resources.getRecipeManager().getRecipes()) {
            var recipe = holder.value();
            if (!(recipe instanceof velrondevs.botania.api.recipe.ManaInfusionRecipe)
                    && !(recipe instanceof velrondevs.botania.api.recipe.RunicAltarRecipe)) continue;
            try {
                boolean runic = recipe.getClass() == RunicAltarRecipe.class;
                boolean returnRunes = false;
                int mana;
                if (recipe.getClass() == ManaInfusionRecipe.class) {
                    var infusion = (ManaInfusionRecipe) recipe;
                    if (infusion.getRecipeCatalyst() != null)
                        throw new IllegalArgumentException("catalyst route: classify before mapping");
                    mana = infusion.getManaToConsume();
                } else if (runic) {
                    var altar = (RunicAltarRecipe) recipe;
                    if (rock.isEmpty()) throw new IllegalArgumentException("livingrock missing");
                    mana = altar.getManaUsage();
                    returnRunes = altar.returnsRunes();
                } else throw new IllegalArgumentException("special implementation");
                ItemStack out = recipe.getResultItem(registries);
                if (out.isEmpty() || !out.getComponentsPatch().isEmpty() || mana <= 0)
                    throw new IllegalArgumentException("dynamic output or invalid cost");
                List<Object2IntOpenHashMap<NormalizedSimpleStack>> alternatives = new ArrayList<>();
                var initial = new Object2IntOpenHashMap<NormalizedSimpleStack>();
                if (runic) initial.put(NSSItem.createItem(rock.orElseThrow()), 1);
                alternatives.add(initial);
                for (Ingredient ing : recipe.getIngredients()) {
                    if (ing.isEmpty()) throw new IllegalArgumentException("empty ingredient");
                    ItemStack[] stacks = ing.getItems();
                    if (stacks.length == 0 || (long) alternatives.size() * stacks.length > MAX_ALTERNATIVES)
                        throw new IllegalArgumentException("unresolved or excessive alternatives");
                    var next = new ArrayList<Object2IntOpenHashMap<NormalizedSimpleStack>>();
                    for (var prior : alternatives) for (ItemStack stack : stacks) {
                        if (stack.isEmpty() || !stack.getComponentsPatch().isEmpty() || !ing.test(stack))
                            throw new IllegalArgumentException("component-sensitive input");
                        var copy = new Object2IntOpenHashMap<NormalizedSimpleStack>(prior);
                        // Rune inputs return unchanged; only consumed ingredients determine price.
                        if (!(returnRunes && stack.getItem() instanceof RuneItem))
                            copy.addTo(NSSItem.createItem(stack), 1);
                        next.add(copy);
                    }
                    alternatives = next;
                }
                if (alternatives.isEmpty()) throw new IllegalArgumentException("no alternatives");
                var output = NSSItem.createItem(out);
                var cost = ResourceAccounting.Rate.MANA.of(mana);
                // Validate the whole recipe before contributing any route.
                for (var inputs : alternatives) {
                    if (inputs.isEmpty() || inputs.getInt(output) > 0)
                        throw new IllegalArgumentException("self-referential or no consumed material");
                    cost.emcUnits(); cost.scaleCount(out.getCount());
                    for (int n : inputs.values()) cost.scaleCount(n);
                }
                for (var inputs : alternatives) graph.add(out.getCount(), output, inputs, cost);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException e) {
                skipped++; LOG.info("TMM Botania mapper skipped {}: {}", holder.id(), e.getMessage());
            }
        }
        LOG.info("TMM Botania mapper: {} recipes mapped, {} held for review", mapped, skipped);
    }
}
