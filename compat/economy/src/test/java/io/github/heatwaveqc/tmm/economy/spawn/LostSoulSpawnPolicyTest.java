package io.github.heatwaveqc.tmm.economy.spawn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LostSoulSpawnPolicyTest {
    @Test void wholeDimensionsIncludeBothSpiritWorldVariants() {
        for (String dimension : new String[] {"twilightforest:twilight_forest",
                "deeperdarker:otherside", "wardentools:abyssdim", "witchery:dream_world",
                "witchery:nightmare_world", "voidscape:void", "theaurorian:the_aurorian"}) {
            assertTrue(LostSoulSpawnPolicy.allowed(dimension, "minecraft:plains"), dimension);
            assertTrue(LostSoulSpawnPolicy.allowed(dimension, null), dimension);
        }
    }

    @Test void vanillaLocationsRequireTheirActualDimensionAndBiome() {
        assertTrue(LostSoulSpawnPolicy.allowed("minecraft:overworld", "minecraft:deep_dark"));
        assertTrue(LostSoulSpawnPolicy.allowed("minecraft:the_nether", "minecraft:soul_sand_valley"));
        assertFalse(LostSoulSpawnPolicy.allowed("minecraft:overworld", "minecraft:soul_sand_valley"));
        assertFalse(LostSoulSpawnPolicy.allowed("minecraft:the_nether", "minecraft:deep_dark"));
        assertFalse(LostSoulSpawnPolicy.allowed("minecraft:overworld", "minecraft:snowy_beach"));
        assertFalse(LostSoulSpawnPolicy.allowed("minecraft:the_nether", "minecraft:nether_wastes"));
        assertFalse(LostSoulSpawnPolicy.allowed("minecraft:overworld", null));
    }

    @Test void noSharedBiomeLeakageOrGuessedSpiritId() {
        for (String dimension : new String[] {"witchery:mirror_world", "witchery:spirit_world",
                "minecraft:the_end", "neovitae:dungeon", "unknown:dimension"}) {
            assertFalse(LostSoulSpawnPolicy.allowed(dimension, "minecraft:deep_dark"), dimension);
            assertFalse(LostSoulSpawnPolicy.allowed(dimension, "minecraft:soul_sand_valley"), dimension);
        }
        assertFalse(LostSoulSpawnPolicy.allowed(null, null));
    }
}
