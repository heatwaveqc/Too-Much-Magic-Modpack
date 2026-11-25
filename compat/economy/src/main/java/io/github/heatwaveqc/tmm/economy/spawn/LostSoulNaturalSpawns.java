package io.github.heatwaveqc.tmm.economy.spawn;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;

/**
 * Edit only the natural spawn candidate list, before vanilla weighted selection.
 * The native broad biome modifier is suppressed by the same PR's datapack.
 * Native spawn placement, caps, groups, kill hooks, eggs and commands remain native.
 */
@EventBusSubscriber(modid = "tmm_economy")
public final class LostSoulNaturalSpawns {
    private LostSoulNaturalSpawns() {}

    // Lazy after registry bootstrap. A stable SpawnerData identity is required
    // because NaturalSpawner queries the list again to validate the chosen entry.
    private static final class Entry {
        static final EntityType<?> TYPE = BuiltInRegistries.ENTITY_TYPE.getOptional(
            ResourceLocation.fromNamespaceAndPath("forbidden_arcanus", "lost_soul")).orElse(null);
        static final MobSpawnSettings.SpawnerData DATA = TYPE == null ? null
            : new MobSpawnSettings.SpawnerData(TYPE, 35, 1, 3);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void naturalSpawns(LevelEvent.PotentialSpawns event) {
        if (!(event.getLevel() instanceof ServerLevel level) || Entry.DATA == null
                || event.getMobCategory() != Entry.TYPE.getCategory()) return;
        String dimension = level.dimension().location().toString();
        boolean allowed = LostSoulSpawnPolicy.wholeDimension(dimension);
        if (!allowed && ("minecraft:overworld".equals(dimension)
                || "minecraft:the_nether".equals(dimension))) {
            String biome = level.getBiome(event.getPos()).unwrapKey()
                .map(key -> key.location().toString()).orElse(null);
            allowed = LostSoulSpawnPolicy.allowed(dimension, biome);
        }
        // Use event mutation methods, not its read-only list view. Remove only
        // ordinary Lost Soul entries, not corrupt/enchanted variants or other mobs.
        for (var existing : event.getSpawnerDataList().stream()
                .filter(data -> data.type == Entry.TYPE).toList()) {
            event.removeSpawnerData(existing);
        }
        if (allowed) event.addSpawnerData(Entry.DATA);
    }
}
