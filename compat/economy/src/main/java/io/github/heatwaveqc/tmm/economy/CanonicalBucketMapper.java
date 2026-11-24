package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import java.util.Map;
import java.util.HashSet;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSFluid;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.slf4j.Logger;

/** Native standard 1,000mB containers only. Do not infer the contents
 * or remainder of a mod-specific bottle, tank, fish bucket or custom handler. */
@EMCMapper
public final class CanonicalBucketMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    @Override public String getName() { return "TMM Canonical Bucket Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.canonical_buckets"; }
    @Override public String getDescription() { return "Map verified full bucket recipes and returned empty buckets."; }
    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        var seen = new HashSet<Item>();
        int mapped = 0;
        for (var fluid : BuiltInRegistries.FLUID) {
            try {
                Item bucket = fluid.getBucket();
                if (bucket == Items.AIR || bucket == Items.BUCKET
                        || BuiltInRegistries.ITEM.getKey(bucket).getNamespace().equals("minecraft")
                        || bucket.getClass() != BucketItem.class) continue;
                if (!fluid.defaultFluidState().isSource() || seen.contains(bucket)) continue;
                var full = new ItemStack(bucket);
                if (!full.getComponentsPatch().isEmpty()) throw new IllegalArgumentException("component-sensitive bucket");
                var cap = full.getCapability(Capabilities.FluidHandler.ITEM);
                var emptyCap = new ItemStack(Items.BUCKET).getCapability(Capabilities.FluidHandler.ITEM);
                if (cap == null || cap.getClass() != FluidBucketWrapper.class
                        || emptyCap == null || emptyCap.getClass() != FluidBucketWrapper.class)
                    throw new IllegalArgumentException("not the native bucket wrapper");
                var available = cap.getFluidInTank(0);
                if (available.isEmpty() || available.getFluid() != fluid || available.getAmount() != 1000
                        || !available.getComponentsPatch().isEmpty()
                        || !full.getCraftingRemainingItem().is(Items.BUCKET))
                    throw new IllegalArgumentException("bucket fluid or remainder is not canonical");
                // These are exact native wrappers of disposable one-item stacks;
                // execute only against those copies, never a world or real inventory.
                var drained = cap.drain(1000, FluidAction.EXECUTE);
                if (!FluidStack.matches(available, drained) || !cap.getContainer().is(Items.BUCKET)
                        || emptyCap.fill(available.copy(), FluidAction.EXECUTE) != 1000
                        || !emptyCap.getContainer().is(bucket)
                        || !emptyCap.getContainer().getComponentsPatch().isEmpty())
                    throw new IllegalArgumentException("failed native round-trip check");
                var key = NSSFluid.createFluid(available);
                var fill = new RecipeGraphPlan();
                fill.choice(Map.of(NSSItem.createItem(Items.BUCKET),1));
                fill.choice(Map.of(key,1000));
                fill.contribute(graph, new ItemStack(bucket), ResourceAccounting.Cost.ZERO);
                var empty = new RecipeGraphPlan();
                empty.consumed(Ingredient.of(bucket),1,true);
                empty.contribute(graph,key,1000,ResourceAccounting.Cost.ZERO);
                seen.add(bucket);
                mapped++;
            } catch (RuntimeException | LinkageError e) {
                LOG.info("TMM canonical bucket held fluid {}: {}", BuiltInRegistries.FLUID.getKey(fluid), e.getMessage());
            }
        }
        LOG.info("TMM canonical bucket mapper: {} verified nonvanilla containers", mapped);
    }
}
