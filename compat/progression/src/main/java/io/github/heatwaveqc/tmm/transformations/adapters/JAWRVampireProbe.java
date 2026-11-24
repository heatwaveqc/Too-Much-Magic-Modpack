package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Read-only observer against hash-verified JAWR 0.5.39 for MC 1.21.1.
 * Binaries expose VampirePlayerAttachment.getData(Player) and Data.isVampire().
 *
 * Intentionally does not read assumed NBT keys or mutate the JAWR attachment.
 */
public final class JAWRVampireProbe implements TransformationProbe {
    @Override public ProviderState state() {
        return ProviderState.JAWR_VAMPIRE;
    }

    @Override public Observation observe(ServerPlayer player) {
        if (!ModList.get().isLoaded("witchery")) return Observation.UNKNOWN;
        try {
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) return Observation.UNKNOWN;
            Object present = data.getClass().getMethod("isVampire").invoke(data);
            if (!(present instanceof Boolean valid)) return Observation.UNKNOWN;
            return valid ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | SecurityException e) {
            TransformationsMod.LOGGER.warn("JAWR vampire-state API unavailable; returning UNKNOWN", e);
            return Observation.UNKNOWN;
        }
    }
}
