package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** JAWR 0.5.39 native WerewolfPlayerAttachment, with an unknown/fail-closed state. */
public final class JAWRLycanthropeProbe implements TransformationProbe {
    @Override public ProviderState state() { return ProviderState.JAWR_LYCANTHROPE; }

    @Override public Observation observe(ServerPlayer player) {
        if (!ModList.get().isLoaded("witchery")) return Observation.UNKNOWN;
        try {
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery.feature.affliction.werewolf.WerewolfPlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) return Observation.UNKNOWN;
            Object present = data.getClass().getMethod("isWerewolf").invoke(data);
            if (!(present instanceof Boolean flag)) return Observation.UNKNOWN;
            return flag ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            TransformationsMod.LOGGER.warn("JAWR lycanthrope probe unavailable", failure);
            return Observation.UNKNOWN;
        }
    }
}
