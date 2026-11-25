package io.github.heatwaveqc.tmm.transformations.dracolich;

import io.github.heatwaveqc.tmm.transformations.adapters.ForbiddenMagicLichProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.GoetyLichProbe;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.network.DracolichVisualPayload;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Read-only native classification + cosmetic replication. No saved state writer. */
public final class DracolichAppearanceEvents {
    private static final Map<UUID, Boolean> SENT = new HashMap<>();
    private static final GoetyLichProbe GOETY = new GoetyLichProbe();
    private static final ForbiddenMagicLichProbe FORBIDDEN = new ForbiddenMagicLichProbe();
    private DracolichAppearanceEvents() { }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::tick);
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::login);
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::dimension);
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::respawn);
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::tracking);
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::logout);
        NeoForge.EVENT_BUS.addListener(DracolichAppearanceEvents::stopped);
    }

    public static boolean active(ServerPlayer player) {
        if (ModList.get().getModContainerById("dragonsurvival")
                .filter(mod -> mod.getModInfo().getVersion().toString().startsWith("2.0.74")).isEmpty()) return false;
        try {
            Class<?> provider = Class.forName("by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider");
            if (!(Boolean) provider.getMethod("isDragon", Entity.class).invoke(null, player)) return false;
            Object handler = provider.getMethod("getData", Player.class).invoke(null, player);
            String species = handler.getClass().getMethod("speciesId").invoke(handler).toString();
            String body = handler.getClass().getMethod("bodyId").invoke(handler).toString();
            String model = handler.getClass().getMethod("getModel").invoke(handler).toString();
            if (!"dragonsurvival:cave_dragon".equals(species) || !"dragonsurvival:center".equals(body)) return false;
            Observation goety = ModList.get().isLoaded("goety") ? GOETY.observe(player) : Observation.ABSENT;
            Observation forbidden = ModList.get().isLoaded("witchery_forbidden_magic") ? FORBIDDEN.observe(player) : Observation.ABSENT;
            return DracolichAppearancePolicy.eligible(true, goety == Observation.PRESENT,
                    forbidden == Observation.PRESENT, goety != Observation.UNKNOWN && forbidden != Observation.UNKNOWN,
                    species, body, model);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            return false; // Native form remains visible on unsupported APIs.
        }
    }

    private static void sync(ServerPlayer player, boolean force) {
        boolean active = active(player);
        Boolean previous = SENT.put(player.getUUID(), active);
        if (force || previous == null || previous != active) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(player,
                    new DracolichVisualPayload(player.getUUID(), active));
        }
    }

    private static void tick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 5 == 0) sync(player, false);
    }
    private static void login(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) sync(player, true);
    }
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) sync(player, true);
    }
    private static void respawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) sync(player, true);
    }
    private static void tracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer viewer && event.getTarget() instanceof ServerPlayer subject) {
            PacketDistributor.sendToPlayer(viewer, new DracolichVisualPayload(subject.getUUID(), active(subject)));
        }
    }
    private static void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            SENT.remove(player.getUUID());
            PacketDistributor.sendToPlayersTrackingEntity(player, new DracolichVisualPayload(player.getUUID(), false));
        }
    }
    private static void stopped(ServerStoppedEvent event) { SENT.clear(); }
}
