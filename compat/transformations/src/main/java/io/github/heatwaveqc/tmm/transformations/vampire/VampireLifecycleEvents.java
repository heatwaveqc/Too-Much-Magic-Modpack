package io.github.heatwaveqc.tmm.transformations.vampire;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Event-driven reconciliation of derived Vampirism blood mirror after loading a player or
 * crossing a dimension. No periodic polling and no transformations applied.
 */
public final class VampireLifecycleEvents {
    private VampireLifecycleEvents() { }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(VampireLifecycleEvents::login);
        NeoForge.EVENT_BUS.addListener(VampireLifecycleEvents::dimension);
        NeoForge.EVENT_BUS.addListener(VampireLifecycleEvents::tick);
    }

    private static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            VampireChassisRuntime.onJAWRBloodChanged(player);
            VampireChassisRuntime.reconcileAscension(player);
        }
    }

    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            VampireChassisRuntime.onJAWRBloodChanged(player);
        }
    }
    private static void tick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            VampireChassisRuntime.reconcileAscension(player);
        }
    }
}
