package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import dev.shadowsoffire.apotheosis.Apoth;
import dev.shadowsoffire.apotheosis.affix.salvaging.SalvagingRecipe;
import dev.shadowsoffire.apotheosis.socket.gem.Gem;
import dev.shadowsoffire.apotheosis.socket.gem.GemRegistry;
import dev.shadowsoffire.apotheosis.socket.gem.Purity;
import dev.shadowsoffire.apotheosis.socket.gem.cutting.PurityUpgradeRecipe;
import dev.shadowsoffire.apotheosis.tiers.TieredWeights;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.slf4j.Logger;

/** Concrete component-key mappings; no enumeration or appraisal of gem bonuses. */
@EMCMapper(requiredMods = {"apotheosis"})
public final class ApotheosisGemMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final int MAX_ALTERNATIVES = 256;
    private ModConfigSpec.IntValue referenceWeight, commonDustUnits;

    @Override public String getName() { return "TMM Apotheosis Gem Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.apotheosis_gems"; }
    @Override public String getDescription() { return "Map exact gem identity and purity using rarity weights and live cutting recipes."; }

    @Override public void addConfigOptions(ModConfigSpec.Builder b) {
        referenceWeight = b.comment("Provisional zero-luck common gem weight; smaller weights cost more.")
                .defineInRange("referenceWeight", 100, 1, 65536);
        commonDustUnits = b.comment("Provisional base cost expressed as multiples of mapped Gem Dust; not an independent EMC anchor.")
                .defineInRange("commonDustUnits", 2, 1, 64);
    }

    private record Cut(NSSItem output, Object2IntOpenHashMap<NormalizedSimpleStack> inputs) {}
    private record Input(ItemStack stack, int count) {}
    private record Plan(NSSItem seed, int seedUnits, List<Cut> cuts) {}

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager resourceManager) {
        // The raw item ID is an invalid gem, not a purchasable blank.
        mapper.setValueBefore(NSSItem.createItem(Apoth.Items.GEM.value()), 0L);
        List<PurityUpgradeRecipe> upgrades = new ArrayList<>();
        List<SalvagingRecipe> salvage = new ArrayList<>();
        for (RecipeHolder<?> holder : resources.getRecipeManager().getRecipes()) {
            if (holder.value() instanceof PurityUpgradeRecipe p) upgrades.add(p);
            if (holder.value() instanceof SalvagingRecipe s) salvage.add(s);
        }
        if (upgrades.isEmpty() || salvage.isEmpty()) {
            LOG.warn("TMM gem mapper: no effective cutting/salvaging recipes; skipped");
            return;
        }
        NSSItem dust = NSSItem.createItem(Apoth.Items.GEM_DUST.value());
        int ref = referenceWeight == null ? 100 : referenceWeight.get();
        int common = commonDustUnits == null ? 2 : commonDustUnits.get();
        int gems = 0, conversions = 0;
        for (Gem gem : GemRegistry.INSTANCE.getValues()) {
            Plan plan;
            try {
                plan = plan(gem, upgrades, salvage, ref, common);
            } catch (IllegalArgumentException | ArithmeticException e) {
                LOG.warn("TMM gem mapper: skipped {}: {}", gem.getId(), e.getMessage());
                continue;
            }
            // Fixed relative anchor; dust itself must have an independently mapped price.
            Object2IntOpenHashMap<NormalizedSimpleStack> seed = new Object2IntOpenHashMap<>();
            seed.put(dust, plan.seedUnits());
            mapper.setValueFromConversion(1, plan.seed(), seed);
            // Distinct alternatives: they compete in ProjectE's normal least-cost solver.
            for (Cut cut : plan.cuts()) {
                mapper.addConversion(1, cut.output(), cut.inputs());
                conversions++;
            }
            gems++;
        }
        LOG.info("TMM gem mapper: planned {} gem types and {} cutting conversions", gems, conversions);
    }

    private static Plan plan(Gem gem, List<PurityUpgradeRecipe> upgrades,
                             List<SalvagingRecipe> salvage, int ref, int common) {
        Purity minimum = gem.getMinPurity();
        int maxWeight = gem.weights().weights().values().stream().mapToInt(TieredWeights.Weight::weight).max().orElse(0);
        ItemStack initial = gem.toStack(minimum);
        int seedUnits = GemPricing.seedDustUnits(minimum.ordinal(), maxWeight, ref, common, salvageMax(initial, salvage));
        int lowerBound = seedUnits;
        List<Cut> cuts = new ArrayList<>();
        for (int i = minimum.ordinal(); i < Purity.PERFECT.ordinal(); i++) {
            Purity purity = Purity.values()[i];
            NSSItem previous = NSSItem.createItem(gem.toStack(purity));
            ItemStack nextStack = gem.toStack(purity.next());
            NSSItem next = NSSItem.createItem(nextStack);
            int maxSalvage = salvageMax(nextStack, salvage);
            int minConsumedDust = Integer.MAX_VALUE;
            int alternatives = 0;
            for (PurityUpgradeRecipe recipe : upgrades) {
                if (recipe.purity() != purity) continue;
                List<Input> left = expand(recipe.left());
                List<Input> right = expand(recipe.right());
                for (Input l : left) for (Input r : right) {
                    if (++alternatives > MAX_ALTERNATIVES) throw new IllegalArgumentException("Too many cutting alternatives");
                    int consumedDust = Math.addExact(dustUnits(l), dustUnits(r));
                    minConsumedDust = Math.min(minConsumedDust, consumedDust);
                    Object2IntOpenHashMap<NormalizedSimpleStack> inputs = new Object2IntOpenHashMap<>();
                    inputs.addTo(previous, 2);
                    inputs.addTo(NSSItem.createItem(l.stack()), l.count());
                    inputs.addTo(NSSItem.createItem(r.stack()), r.count());
                    cuts.add(new Cut(next, inputs));
                }
            }
            if (alternatives == 0) throw new IllegalArgumentException("Missing live purity upgrade: " + purity);
            // Each valid cutting path must remain above the maximum possible dust refund,
            // even if all other priced ingredients were worth zero.
            lowerBound = GemPricing.guaranteedDustUnits(lowerBound, minConsumedDust, maxSalvage);
        }
        return new Plan(NSSItem.createItem(initial), seedUnits, List.copyOf(cuts));
    }

    private static int dustUnits(Input input) {
        return input.stack().is(Apoth.Items.GEM_DUST.value()) ? input.count() : 0;
    }

    private static List<Input> expand(List<SizedIngredient> alternatives) {
        List<Input> result = new ArrayList<>();
        for (SizedIngredient sized : alternatives) {
            ItemStack[] choices = sized.ingredient().getItems();
            if (choices.length == 0 || sized.count() <= 0) throw new IllegalArgumentException("Unresolved cutting ingredient");
            for (ItemStack stack : choices) {
                if (stack.isEmpty() || !stack.getComponentsPatch().isEmpty() || stack.is(Apoth.Items.GEM.value()))
                    throw new IllegalArgumentException("Unsupported component-bearing or gem cutting reagent");
                result.add(new Input(stack, sized.count()));
                if (result.size() > MAX_ALTERNATIVES) throw new IllegalArgumentException("Too many ingredient choices");
            }
        }
        if (result.isEmpty()) throw new IllegalArgumentException("Missing cutting reagent");
        return result;
    }

    private static int salvageMax(ItemStack gem, List<SalvagingRecipe> salvage) {
        int max = -1;
        for (SalvagingRecipe s : salvage) {
            if (!s.getInput().test(gem)) continue;
            int total = 0;
            for (SalvagingRecipe.OutputData out : s.getOutputs()) {
                if (!out.stack().is(Apoth.Items.GEM_DUST.value()) || !out.stack().getComponentsPatch().isEmpty())
                    throw new IllegalArgumentException("Unsupported non-dust gem salvage output");
                total = Math.addExact(total, Math.multiplyExact(out.stack().getCount(), out.max()));
            }
            max = Math.max(max, total);
        }
        if (max < 0) throw new IllegalArgumentException("Missing effective gem salvage recipe");
        return max;
    }
}
