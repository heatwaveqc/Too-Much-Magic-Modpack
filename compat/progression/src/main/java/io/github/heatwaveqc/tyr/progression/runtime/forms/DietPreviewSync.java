package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.data.FoodPolicyRegistry;
import io.github.heatwaveqc.tyr.progression.network.DietNetwork;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

/**
 * Server-originated client food view for all verified active forms, Paths and
 * owned food restrictions. Never trusts a client to select dietary profiles.
 */
public final class DietPreviewSync {
    private DietPreviewSync() {}

    private record Last(Object server, Set<String> profiles, String ownedEffects,
                        long generation) {}
    private static final Map<UUID, Last> LAST = new HashMap<>();

    public static void sync(ServerPlayer player, boolean force) {
        if (player.getServer() == null || !player.getServer().isSameThread()) return;
        Set<String> profiles = NativeDietAdapters.activeProfiles(player);
        String ownedEffects = NativeDietAdapters.clientFingerprint(player);
        long generation = FoodPolicyRegistry.generation();
        Last previous = LAST.get(player.getUUID());
        if (!force && previous != null && previous.server() == player.getServer()
                && Objects.equals(previous.profiles(), profiles)
                && Objects.equals(previous.ownedEffects(), ownedEffects)
                && previous.generation() == generation) return;

        var projection = NativeDietAdapters.clientPreview(player, profiles);
        if (generation != FoodPolicyRegistry.generation()) return;
        try {
            DietNetwork.sendPreview(player, projection);
        } catch (IllegalArgumentException oversized) {
            ProgressionMod.LOGGER.warn("Skipping oversized client food preview: {}",
                oversized.getMessage());
            DietNetwork.sendPreview(player, Map.of());
        }
        LAST.put(player.getUUID(), new Last(player.getServer(),
            profiles, ownedEffects, generation));
    }

    public static void forget(ServerPlayer player) {
        LAST.remove(player.getUUID());
    }
}
