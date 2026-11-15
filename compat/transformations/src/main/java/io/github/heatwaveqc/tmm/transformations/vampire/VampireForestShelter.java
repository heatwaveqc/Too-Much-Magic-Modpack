package io.github.heatwaveqc.tmm.transformations.vampire;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

/** Source-verified Vampirism 1.21 faction biome tag, including Vampire Forest. */
public final class VampireForestShelter {
    private static final TagKey<Biome> VAMPIRE_BIOMES = TagKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("vampirism", "has_faction/vampire"));

    private VampireForestShelter() { }

    public static boolean isSheltered(ServerPlayer player) {
        return player.level().getBiome(player.blockPosition()).is(VAMPIRE_BIOMES);
    }
}
