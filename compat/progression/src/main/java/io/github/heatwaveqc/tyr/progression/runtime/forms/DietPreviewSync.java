package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.network.DietNetwork;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/**
 * Server-side, per-player diet preview projection. Refresh on the authoritative
 * policy reload generation or native dragon-species change; never from a client
 * request and never every tick. No food census, item history or recipe scan.
 */
public final class DietPreviewSync {
    private DietPreviewSync() {}

    private record Last(Object server, String profile, long generation) {}
    private static final Map<UUID, Last> LAST = new HashMap<>();

    public static void sync(ServerPlayer player, boolean force) {
        if (player.getServer() == null || !player.getServer().isSameThread()) return;
        // Do not project a policy the pinned native food hook cannot apply.
        boolean supported = ModList.get().getModContainerById("dragonsurvival")
            .filter(c -> c.getModInfo().getVersion().toString().equals("2.0.74"))
            .isPresent();
        String profile = supported ? DragonDietHook.dragonProfile(player) : null;
        long generation = FoodPolicyRegistry.generation();
        Last previous = LAST.get(player.getUUID());
        if (!force && previous != null && previous.server() == player.getServer()
                && Objects.equals(previous.profile(), profile)
                && previous.generation() == generation) return;

        var projection = FoodPolicyRegistry.clientPreview(profile);
        // Do not send a stale table if a resource reload landed during evaluation.
        if (generation != FoodPolicyRegistry.generation()) return;
        try {
            DietNetwork.sendPreview(player, projection);
        } catch (IllegalArgumentException oversized) {
            // Never crash an ordinary player tick because a modpack's food
            // registry grew beyond the bounded preview protocol.
            ProgressionMod.LOGGER.warn("Skipping oversized client food preview: {}",
                oversized.getMessage());
            DietNetwork.sendPreview(player, Map.of());
        }
        LAST.put(player.getUUID(), new Last(player.getServer(), profile, generation));
    }

    public static void forget(ServerPlayer player) {
        LAST.remove(player.getUUID());
    }
}
