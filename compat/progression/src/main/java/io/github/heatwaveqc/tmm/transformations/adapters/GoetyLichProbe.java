package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/** Goety 3.2.2 native LichdomHelper state, read-only and version-pinned. */
public final class GoetyLichProbe implements TransformationProbe {
    @Override public ProviderState state() {
        return ProviderState.GOETY_LICH;
    }

    @Override public Observation observe(ServerPlayer player) {
        if (ModList.get().getModContainerById("goety")
                .filter(mod -> mod.getModInfo().getVersion().toString().startsWith("3.2.2"))
                .isEmpty()) return Observation.UNKNOWN;
        try {
            Class<?> lich = Class.forName("com.Polarice3.Goety.utils.LichdomHelper");
            Object value = lich.getMethod("isLich", Player.class).invoke(null, player);
            if (!(value instanceof Boolean flag)) return Observation.UNKNOWN;
            return flag ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            TransformationsMod.LOGGER.warn("Goety lich state probe unavailable", e);
            return Observation.UNKNOWN;
        }
    }
}
