package io.github.heatwaveqc.tmm.economy;

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

/** Fixed zero on intrinsically excluded base items, even after new recipe mappers. */
@EMCMapper
public final class ApprovedExclusionsMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    static final List<String> IDS = List.of("minecraft:enchanted_book", "minecraft:written_book",
        "astralsorcery:rock_crystal", "astralsorcery:attuned_rock_crystal",
        "astralsorcery:celestial_crystal", "astralsorcery:attuned_celestial_crystal",
        "astralsorcery:starlight_focus_rock_crystal", "astralsorcery:starlight_focus_celestial_crystal");
    @Override public String getName() { return "TMM Approved Exclusions"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.approved_exclusions"; }
    @Override public String getDescription() { return "Fixed zero for owner-approved intrinsically excluded item IDs."; }
    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager resourceManager) {
        for (String id : IDS) BuiltInRegistries.ITEM.getOptional(ResourceLocation.parse(id)).ifPresent(item -> {
            var key = NSSItem.createItem(item);
            mapper.setValueBefore(key, 0L);
            mapper.setValueAfter(key, 0L);
        });
    }
}
