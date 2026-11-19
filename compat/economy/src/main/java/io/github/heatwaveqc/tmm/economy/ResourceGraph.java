package io.github.heatwaveqc.tmm.economy;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSFake;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;

/** Synthetic one-EMC accounting node. No item, currency exchange or new recipes. */
final class ResourceGraph {
    private final IMappingCollector<NormalizedSimpleStack, Long> mapper;
    private final NSSFake unit = NSSFake.create("tmm_economy:resource_emc_unit_v1");
    ResourceGraph(IMappingCollector<NormalizedSimpleStack, Long> mapper) {
        this.mapper = mapper;
        mapper.setValueBefore(unit, 1L);
    }
    void add(int outputCount, NormalizedSimpleStack output,
            Object2IntMap<NormalizedSimpleStack> consumed, ResourceAccounting.Cost cost) {
        if (outputCount <= 0 || consumed.isEmpty()) throw new IllegalArgumentException("Invalid material recipe");
        var scaled = new Object2IntOpenHashMap<NormalizedSimpleStack>();
        for (var e : consumed.object2IntEntrySet()) {
            int amount = cost.scaleCount(e.getIntValue());
            if (amount != 0) scaled.put(e.getKey(), amount);
        }
        int units = cost.emcUnits();
        if (units > 0) scaled.put(unit, units);
        int outputs = cost.scaleCount(outputCount);
        if (scaled.isEmpty()) throw new IllegalArgumentException("No consumed input");
        mapper.addConversion(outputs, output, scaled);
    }
}
