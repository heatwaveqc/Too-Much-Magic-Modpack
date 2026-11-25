package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** JAWR Forbidden Magic 0.5.37 native lich attachment, read-only. */
public final class ForbiddenMagicLichProbe implements TransformationProbe {
    @Override public ProviderState state() {
        return ProviderState.FORBIDDEN_MAGIC_LICH;
    }

    @Override public Observation observe(ServerPlayer player) {
        if (ModList.get().getModContainerById("witchery_forbidden_magic")
                .filter(mod -> mod.getModInfo().getVersion().toString().startsWith("0.5.37"))
                .isEmpty()) return Observation.UNKNOWN;
        try {
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichPlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) return Observation.UNKNOWN;
            Object value = data.getClass().getMethod("isLich").invoke(data);
            if (!(value instanceof Boolean flag)) return Observation.UNKNOWN;
            return flag ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            TransformationsMod.LOGGER.warn("Forbidden Magic lich state probe unavailable", e);
            return Observation.UNKNOWN;
        }
    }
}
