package io.github.heatwaveqc.tmm.economy;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import moze_intel.projecte.api.mapper.EMCMapper;
import moze_intel.projecte.api.mapper.IEMCMapper;
import moze_intel.projecte.api.mapper.collector.IMappingCollector;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.slf4j.Logger;

/** Maps the actual canonical eggs returned by SpawnEggItem.byId, exactly as
 * Apothic Spawners does, rather than guessing from item registry names.
 * Emits a machine-readable resolved tag snapshot for the pinned Reliquary config. */
@EMCMapper(requiredMods = {"apothic_spawners"})
public final class CapturingEggMapper implements IEMCMapper<NormalizedSimpleStack, Long> {
    private static final Logger LOG = LogUtils.getLogger();
    private static final long EGG_EMC = 532L;
    @Override public String getName() { return "TMM Capturing Egg Mapper"; }
    @Override public String getTranslationKey() { return "tmm_economy.mapper.capturing_eggs"; }
    @Override public String getDescription() { return "Price native Capturing eggs and deny blacklisted entities."; }

    @Override public void addMappings(IMappingCollector<NormalizedSimpleStack, Long> mapper,
            ReloadableServerResources resources, RegistryAccess registries, ResourceManager resourceManager) {
        var tag = BuiltInRegistries.ENTITY_TYPE.getTag(CapturingPolicy.BLACKLIST);
        if (tag.isEmpty()) {
            LOG.error("TMM Capturing mapper: authoritative entity tag missing; no eligibility snapshot or positive egg anchors");
            // Do not let an absent tag accidentally expose boss eggs.
            for (var item : BuiltInRegistries.ITEM) if (item instanceof SpawnEggItem) {
                NSSItem key = NSSItem.createItem(item);
                mapper.setValueBefore(key, 0L); mapper.setValueAfter(key, 0L);
            }
            return;
        }
        List<String> denied = tag.get().stream().map(h -> h.unwrapKey().orElseThrow().location().toString()).sorted().toList();
        Map<String,String> eligible = new TreeMap<>();
        List<String> held = new ArrayList<>();
        for (var item : BuiltInRegistries.ITEM) {
            if (!(item instanceof SpawnEggItem egg)) continue;
            String id = BuiltInRegistries.ITEM.getKey(item).toString();
            var stack = new ItemStack(egg);
            var type = egg.getType(stack);
            var key = NSSItem.createItem(item);
            if (type == null || CapturingPolicy.blocked(type)) {
                mapper.setValueBefore(key, 0L); mapper.setValueAfter(key, 0L);
            } else if (SpawnEggItem.byId(type) == egg && stack.getComponentsPatch().isEmpty()) {
                // Native identity is eligibility evidence, not proof of natural spawn reachability.
                mapper.setValueBefore(key, EGG_EMC);
                eligible.put(BuiltInRegistries.ENTITY_TYPE.getKey(type).toString(), id);
            } else {
                held.add(id); // No blanket anchors for noncanonical/custom egg variants.
            }
        }
        held.sort(String::compareTo);
        LOG.info("TMM_CAPTURING_SNAPSHOT {}", new Gson().toJson(Map.of(
                "schema", 1, "tag", CapturingPolicy.BLACKLIST.location().toString(),
                "blocked_entity_ids", denied, "native_eligible_pairs", eligible,
                "held_noncanonical_eggs", held)));
        LOG.info("TMM Capturing mapper: {} native eligible eggs, {} blocked entity types, {} noncanonical eggs held",
                eligible.size(), denied.size(), held.size());
    }
}
