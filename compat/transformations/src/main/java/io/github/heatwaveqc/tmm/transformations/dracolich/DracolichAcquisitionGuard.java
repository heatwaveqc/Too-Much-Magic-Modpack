package io.github.heatwaveqc.tmm.transformations.dracolich;

import io.github.heatwaveqc.tmm.transformations.adapters.DragonSurvivalProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.ForbiddenMagicLichProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.GoetyLichProbe;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Server-side veto at canonical native lich entrypoints, not a delayed
 * "repair" that might consume progression or ritual costs.
 */
public final class DracolichAcquisitionGuard {
    public enum Result { ALLOW, DENY_SECOND_LICH, DENY_UNVERIFIED }

    private static final DragonSurvivalProbe DRAGON = new DragonSurvivalProbe();
    private static final GoetyLichProbe GOETY = new GoetyLichProbe();
    private static final ForbiddenMagicLichProbe FORBIDDEN = new ForbiddenMagicLichProbe();

    private DracolichAcquisitionGuard() { }

    public static Result goety(Player player, boolean becomingLich) {
        if (!becomingLich || !(player instanceof ServerPlayer serverPlayer)) {
            return Result.ALLOW; // cures and client sync must remain native
        }
        return acquisition(serverPlayer, GOETY.observe(serverPlayer),
                ModList.get().isLoaded("witchery_forbidden_magic")
                        ? FORBIDDEN.observe(serverPlayer) : Observation.ABSENT);
    }

    public static Result forbidden(ServerPlayer player, int newLevel) {
        if (newLevel <= 0) return Result.ALLOW; // JAWR cures remain native
        return acquisition(player, FORBIDDEN.observe(player),
                ModList.get().isLoaded("goety")
                        ? GOETY.observe(player) : Observation.ABSENT);
    }

    private static Result acquisition(ServerPlayer player, Observation target,
                                      Observation other) {
        if (!ModList.get().isLoaded("dragonsurvival")) return Result.ALLOW;
        Observation dragon = DRAGON.observe(player);
        if (dragon == Observation.ABSENT) return Result.ALLOW;
        if (target == Observation.PRESENT) return Result.ALLOW; // rank upgrades
        if (dragon == Observation.UNKNOWN || target == Observation.UNKNOWN
                || other == Observation.UNKNOWN) return Result.DENY_UNVERIFIED;
        return DracolichPolicy.canAcquireLich(true, other == Observation.PRESENT,
                false) ? Result.ALLOW : Result.DENY_SECOND_LICH;
    }

    public static boolean permit(ServerPlayer player, Result decision, String intended) {
        if (decision == Result.ALLOW) return true;
        String reason = decision == Result.DENY_SECOND_LICH
                ? "Dracoliches cannot acquire a second kind of lichdom. Cure the existing lichdom first."
                : "Cannot verify the native dragon/lich states; lichdom acquisition blocked safely.";
        player.sendSystemMessage(Component.literal(reason));
        return false;
    }
}
