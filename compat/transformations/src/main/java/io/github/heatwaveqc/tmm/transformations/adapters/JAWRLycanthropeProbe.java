package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Read-only JAWR 0.5.39 player-lycanthropy probe, verified against the
 * hash-pinned 1.21.1 jar (Data.isWerewolf() is equivalent to level > 0).
 */
public final class JAWRLycanthropeProbe implements TransformationProbe {
    @Override
    public ProviderState state() {
        return ProviderState.JAWR_LYCANTHROPE;
    }

    @Override
    public Observation observe(ServerPlayer player) {
        if (!ModList.get().isLoaded("witchery")) return Observation.UNKNOWN;
        try {
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) return Observation.UNKNOWN;
            Object current = data.getClass().getMethod("isWerewolf").invoke(data);
            if (!(current instanceof Boolean present)) return Observation.UNKNOWN;
            return present ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | SecurityException e) {
            TransformationsMod.LOGGER.warn("JAWR lycanthropy-state API unavailable; UNKNOWN", e);
            return Observation.UNKNOWN;
        }
    }
}
