package io.github.heatwaveqc.tmm.transformations.vampire;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Event-driven reconciliation of derived JAWR shadow after loading a player or
 * crossing a dimension. No periodic polling and no transformations applied.
 */
public final class VampireLifecycleEvents {
    private VampireLifecycleEvents() { }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(VampireLifecycleEvents::login);
        NeoForge.EVENT_BUS.addListener(VampireLifecycleEvents::dimension);
    }

    private static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            VampireChassisRuntime.onNativeBloodChanged(player);
        }
    }

    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            VampireChassisRuntime.onNativeBloodChanged(player);
        }
    }
}
