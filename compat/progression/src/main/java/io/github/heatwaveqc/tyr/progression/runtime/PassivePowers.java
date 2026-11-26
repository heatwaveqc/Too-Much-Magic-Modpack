package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.PassivePowerGrants;
import io.github.heatwaveqc.tyr.progression.core.MutationPowerPolicy;
import io.github.heatwaveqc.tyr.progression.network.PassivePowerNetwork;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;

/**
 * Single server-authoritative registry for HUD-invisible Progression abilities.
 * Providers DERIVE ownership from current persisted progression (never a
 * saved second entitlement). Client receives only the active-ID projection.
 *
 * E.g. a future Hunter tracking ability registers a source provider without
 * inventing a separate status-effect or client-side command.
 */
public final class PassivePowers {
    private PassivePowers() {}
    private static final Map<String, Function<ServerPlayer, Set<String>>> PROVIDERS =
            new TreeMap<>();
    private static final Map<UUID, Sent> LAST_SENT = new HashMap<>();
    private record Sent(String dimension, String encoded,String mutations,String mining) {}

    public static void register(String power, Function<ServerPlayer, Set<String>> sourceResolver) {
        PassivePowerGrants.validate(power);
        if (sourceResolver == null || PROVIDERS.putIfAbsent(power, sourceResolver) != null)
            throw new IllegalArgumentException("Duplicate/empty passive power provider " + power);
        if (PROVIDERS.size() > PassivePowerGrants.MAX_POWERS) {
            PROVIDERS.remove(power);
            throw new IllegalStateException("Too many passive power providers");
        }
    }

    /** Server-side gameplay hooks NEVER trust the cached/client projection. */
    public static boolean active(ServerPlayer player, String power) {
        if (player == null || player instanceof net.neoforged.neoforge.common.util.FakePlayer || !player.isAlive() || player.isSpectator() || player.isCreative())
            return false;
        var resolver = PROVIDERS.get(power);
        return resolver != null && !resolver.apply(player).isEmpty();
    }

    private static PassivePowerGrants calculate(ServerPlayer player) {
        PassivePowerGrants powers = PassivePowerGrants.empty();
        if (!player.isAlive() || player.isCreative() || player.isSpectator()) return powers;
        for (var entry : PROVIDERS.entrySet())
            powers = powers.withSources(entry.getKey(), entry.getValue().apply(player));
        return powers;
    }

    /** Send on change, dimension change, first sight or 20-tick live lease. */
    public static void reconcile(ServerPlayer player, boolean force) {
        if (player == null || player.getServer() == null) return;
        String dimension = player.level().dimension().location().toString();
        String encoded = calculate(player).encodeActive();
        String mutations=MutationPowerPolicy.encode(HunterMutations.active(player));
        String mining=MiningPrediction.encode(player);
        var value = new Sent(dimension, encoded,mutations,mining);
        var old = LAST_SENT.put(player.getUUID(), value);
        if (force || !value.equals(old) || player.tickCount % 20 == 0)
            PassivePowerNetwork.send(player, dimension, encoded,mutations,mining);
    }

    /** Death/logout/respawn clears only the transient network cache. */
    public static void forget(ServerPlayer player) {
        LAST_SENT.remove(player.getUUID());
    }
}
