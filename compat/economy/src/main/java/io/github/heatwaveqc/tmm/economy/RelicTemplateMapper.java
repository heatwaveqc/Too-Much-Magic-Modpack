package io.github.heatwaveqc.tmm.economy;

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

/** Fixed acquisition value, independent of rolled stats and mastery. */
@EMCMapper
public final class RelicTemplateMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    @Override public String getName() { return "TMM Relic and Artifact Templates"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.relic_templates"; }
    @Override public String getDescription() { return "Fixed value for approved fresh relic and artifact equipment."; }
    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager manager) {
        for (String id : RelicTemplatePolicy.IDS)
            BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(id)).ifPresent(item -> {
                var key = NSSItem.createItem(item);
                mapper.setValueBefore(key, RelicTemplatePolicy.VALUE);
                mapper.setValueAfter(key, RelicTemplatePolicy.VALUE);
            });
    }
}
