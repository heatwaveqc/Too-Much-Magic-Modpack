package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyObservations;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Form;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Presence;
import io.github.heatwaveqc.tyr.progression.compat.physiology.NativePhysiologyPolicy.Snapshot;
import io.github.heatwaveqc.tyr.progression.data.Eligibility;
import io.github.heatwaveqc.tyr.progression.foundation.Condition.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;

/**
 * Source-backed optional native form flags, owned by Progression itself.
 *
 * No reflection through the retired tmm_transformations JAR. The actual
 * JAWR, Goety and Dragon Survival providers remain authoritative. Observations
 * are cached per server tick so multiple declarative gates use one snapshot.
 */
public final class ProviderFlags {
    private ProviderFlags() {}

    private record Cached(Object server, long tick, Snapshot snapshot) {}
    private static final Map<UUID, Cached> CACHE = new HashMap<>();

    public static void register() {
        register("jawr_vampire", "JAWR_VAMPIRE");
        register("jawr_werewolf", "JAWR_WEREWOLF");
        register("jawr_lich", "FORBIDDEN_MAGIC_LICH");
        register("goety_lich", "GOETY_LICH");
        register("dragon", "DRAGON_SURVIVAL_DRAGON");
        Eligibility.registerFlag("tyr_progression:lich", player -> {
            var observed = observations(player);
            return io.github.heatwaveqc.tyr.progression.foundation.FormCombination.lich(
                observed.getOrDefault("GOETY_LICH", Result.UNAVAILABLE),
                observed.getOrDefault("FORBIDDEN_MAGIC_LICH", Result.UNAVAILABLE));
        });
        Eligibility.registerFlag("tyr_progression:archlich", player -> {
            var observed = observations(player);
            return io.github.heatwaveqc.tyr.progression.foundation.FormCombination.archlich(
                nativeDragon(player),
                observed.getOrDefault("GOETY_LICH", Result.UNAVAILABLE),
                observed.getOrDefault("FORBIDDEN_MAGIC_LICH", Result.UNAVAILABLE));
        });
        Eligibility.registerFlag("tyr_progression:dracolich", player -> {
            var observed = observations(player);
            return io.github.heatwaveqc.tyr.progression.foundation.FormCombination.dracolich(
                nativeDragon(player),
                observed.getOrDefault("GOETY_LICH", Result.UNAVAILABLE),
                observed.getOrDefault("FORBIDDEN_MAGIC_LICH", Result.UNAVAILABLE));
        });
        for (String species : java.util.List.of("cave", "forest", "sea")) {
            Eligibility.registerFlag("tyr_progression:" + species + "_dragon",
                player -> dragonSpecies(player, "dragonsurvival:" + species + "_dragon"));
        }
    }

    private static void register(String name, String nativeId) {
        Eligibility.registerFlag("tyr_progression:" + name,
            player -> observations(player).getOrDefault(nativeId, Result.UNAVAILABLE));
    }

    private static Result toResult(Presence p) {
        return switch (p) {
            case PRESENT -> Result.PASS;
            case ABSENT -> Result.FAIL;
            case UNKNOWN -> Result.UNAVAILABLE;
        };
    }

    private static Snapshot snapshot(ServerPlayer player) {
        if (player == null || player.getServer() == null
                || !player.getServer().isSameThread()) {
            return NativePhysiologyObservations.observe(player);
        }
        long tick = player.getServer().overworld().getGameTime();
        Cached prior = CACHE.get(player.getUUID());
        if (prior != null && prior.server() == player.getServer()
                && prior.tick() == tick) return prior.snapshot();
        Snapshot fresh = NativePhysiologyObservations.observe(player);
        CACHE.put(player.getUUID(), new Cached(player.getServer(), tick, fresh));
        return fresh;
    }

    public static Result nativeDragon(ServerPlayer player) {
        Snapshot s = snapshot(player);
        Result cave = toResult(s.get(Form.CAVE_DRAGON));
        Result forest = toResult(s.get(Form.FOREST_DRAGON));
        Result sea = toResult(s.get(Form.SEA_DRAGON));
        if (cave == Result.PASS || forest == Result.PASS || sea == Result.PASS) return Result.PASS;
        return cave == Result.FAIL && forest == Result.FAIL && sea == Result.FAIL
            ? Result.FAIL : Result.UNAVAILABLE;
    }

    public static Result dragonSpecies(ServerPlayer player, String expected) {
        Form species = switch (expected) {
            case "dragonsurvival:cave_dragon" -> Form.CAVE_DRAGON;
            case "dragonsurvival:forest_dragon" -> Form.FOREST_DRAGON;
            case "dragonsurvival:sea_dragon" -> Form.SEA_DRAGON;
            default -> null;
        };
        return species == null ? Result.UNAVAILABLE : toResult(snapshot(player).get(species));
    }

    private static Map<String, Result> observations(ServerPlayer player) {
        Snapshot s = snapshot(player);
        Map<String, Result> flags = new HashMap<>();
        flags.put("JAWR_VAMPIRE", toResult(s.get(Form.JAWR_VAMPIRE)));
        flags.put("JAWR_LYCANTHROPE", toResult(s.get(Form.JAWR_WEREWOLF)));
        flags.put("GOETY_LICH", toResult(s.get(Form.GOETY_LICH)));
        flags.put("FORBIDDEN_MAGIC_LICH", toResult(s.get(Form.FORBIDDEN_MAGIC_LICH)));
        flags.put("DRAGON_SURVIVAL_DRAGON", nativeDragon(player));
        return Map.copyOf(flags);
    }

    public static void forget(UUID player) { CACHE.remove(player); }
}
