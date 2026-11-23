package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.network.DietNetwork;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

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
        String profile = DragonDietHook.dragonProfile(player);
        long generation = FoodPolicyRegistry.generation();
        Last previous = LAST.get(player.getUUID());
        if (!force && previous != null && previous.server() == player.getServer()
                && Objects.equals(previous.profile(), profile)
                && previous.generation() == generation) return;

        var projection = FoodPolicyRegistry.clientPreview(profile);
        // Do not send a stale table if a resource reload landed during evaluation.
        if (generation != FoodPolicyRegistry.generation()) return;
        DietNetwork.sendPreview(player, projection);
        LAST.put(player.getUUID(), new Last(player.getServer(), profile, generation));
    }

    public static void forget(ServerPlayer player) {
        LAST.remove(player.getUUID());
    }
}
