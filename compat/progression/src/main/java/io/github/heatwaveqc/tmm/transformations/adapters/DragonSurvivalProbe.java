package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.ModList;

/** Native Dragon Survival 2.0.74 isDragon(Entity) observation, read-only. */
public final class DragonSurvivalProbe implements TransformationProbe {
    @Override public ProviderState state() {
        return ProviderState.DRAGON_SURVIVAL_DRAGON;
    }

    @Override public Observation observe(ServerPlayer player) {
        if (ModList.get().getModContainerById("dragonsurvival")
                .filter(mod -> mod.getModInfo().getVersion().toString().startsWith("2.0.74"))
                .isEmpty()) return Observation.UNKNOWN;
        try {
            Class<?> provider = Class.forName(
                    "by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider");
            Object value = provider.getMethod("isDragon", Entity.class).invoke(null, player);
            if (!(value instanceof Boolean flag)) return Observation.UNKNOWN;
            return flag ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            TransformationsMod.LOGGER.warn("Dragon Survival state probe unavailable", e);
            return Observation.UNKNOWN;
        }
    }
}
