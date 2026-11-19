package io.github.heatwaveqc.tmm.economy;

import com.mojang.logging.LogUtils;
import com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.result.CreateItemResult;
import com.stal111.forbidden_arcanus.core.registry.FARegistries;
import java.util.Map;
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
import org.slf4j.Logger;

/** F&A 2.6.1 live ritual registry. Nominal successful item rituals, no
 * guessed upgrades, transmutation state transfers, failure or world effects. */
@EMCMapper(requiredMods = {"forbidden_arcanus"})
public final class ForbiddenForgeMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final ResourceLocation AUREAL_BOTTLE =
            ResourceLocation.fromNamespaceAndPath("forbidden_arcanus", "aureal_bottle");
    private static final int AUREAL_PER_BOTTLE = 35;
    @Override public String getName() { return "TMM Hephaestus Forge Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.forbidden_forge"; }
    @Override public String getDescription() { return "Map ordinary item rituals with all four essence costs."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        var graph = new ResourceGraph(mapper);
        var ritualRegistry = registries.registry(FARegistries.RITUAL);
        if (ritualRegistry.isEmpty()) {
            LOG.warn("TMM Hephaestus Forge mapper: missing loaded ritual registry");
            return;
        }
        int mapped = 0, skipped = 0, routes = 0;
        for (var entry : ritualRegistry.orElseThrow().entrySet()) {
            var ritual = entry.getValue();
            try {
                if (!(ritual.result() instanceof CreateItemResult result))
                    throw new IllegalArgumentException("nonliteral or world-effect ritual");
                var plan = new RecipeGraphPlan();
                plan.consumed(ritual.mainIngredient(), 1, false);
                for (var input : ritual.inputs()) {
                    if (input.amount() <= 0 || input.amount() > 8)
                        throw new IllegalArgumentException("invalid pedestal amount");
                    for (int i = 0; i < input.amount(); i++) plan.consumed(input.ingredient(), 1, false);
                }
                var e = ritual.requirements().essences();
                var cost = ResourceAccounting.Rate.SOUL.of(e.souls())
                        .plus(ResourceAccounting.Rate.BLOOD_MB.of(e.blood()))
                        .plus(ResourceAccounting.Rate.XP_POINT.of(e.experience()));
                var out = result.result();
                if (out.isEmpty() || !out.getComponentsPatch().isEmpty())
                    throw new IllegalArgumentException("component-sensitive result");
                int batches = 1;
                if (e.aureal() > 0) {
                    // The bottle is consumed in forge essence input; its computed
                    // value, not a frozen EMC number, represents 35 Aureal.
                    var bottle = BuiltInRegistries.ITEM.getOptional(AUREAL_BOTTLE)
                            .orElseThrow(() -> new IllegalArgumentException("missing Aureal Bottle"));
                    int gcd = java.math.BigInteger.valueOf(e.aureal())
                            .gcd(java.math.BigInteger.valueOf(AUREAL_PER_BOTTLE)).intValueExact();
                    batches = AUREAL_PER_BOTTLE / gcd;
                    plan.scale(batches);
                    plan.choice(Map.of(NSSItem.createItem(bottle), e.aureal() / gcd));
                    cost = cost.times(batches);
                }
                routes += plan.contribute(graph, NSSItem.createItem(out),
                        Math.multiplyExact(out.getCount(), batches), cost);
                mapped++;
            } catch (IllegalArgumentException | ArithmeticException ex) {
                skipped++; LOG.info("TMM Forge mapper skipped {}: {}", entry.getKey().location(), ex.getMessage());
            }
        }
        LOG.info("TMM Hephaestus Forge mapper: {} rituals, {} distinct routes, {} held", mapped, routes, skipped);
    }
}
