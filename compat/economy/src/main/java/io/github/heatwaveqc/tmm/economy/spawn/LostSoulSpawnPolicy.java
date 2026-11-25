package io.github.heatwaveqc.tmm.economy.spawn;

import java.util.Set;

/** Natural ordinary Lost Soul locations, not a global entity spawn veto. */
final class LostSoulSpawnPolicy {
    private static final Set<String> FULL_DIMENSIONS = Set.of(
        "twilightforest:twilight_forest", "deeperdarker:otherside",
        "wardentools:abyssdim", "witchery:dream_world", "witchery:nightmare_world",
        "voidscape:void", "theaurorian:the_aurorian");
    private LostSoulSpawnPolicy() {}

    static boolean wholeDimension(String dimension) {
        return dimension != null && FULL_DIMENSIONS.contains(dimension);
    }

    static boolean allowed(String dimension, String biome) {
        return wholeDimension(dimension)
            || ("minecraft:overworld".equals(dimension) && "minecraft:deep_dark".equals(biome))
            || ("minecraft:the_nether".equals(dimension) && "minecraft:soul_sand_valley".equals(biome));
    }
}
