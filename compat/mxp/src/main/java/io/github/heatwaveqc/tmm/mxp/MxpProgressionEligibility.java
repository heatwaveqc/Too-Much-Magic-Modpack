package io.github.heatwaveqc.tmm.mxp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

/**
 * Single, server-authoritative Newbie-to-MXP boundary.
 *
 * Mxp is independently compilable, so the shared Progression API is resolved
 * reflectively rather than linking against a second implementation of modes.
 * Failed API negotiation is NOT permission to award experience.
 */
public final class MxpProgressionEligibility {
    private static volatile Method newbieGate;

    private MxpProgressionEligibility() {}

    public static boolean canEarn(ServerPlayer player) {
        if (player == null || player.getTags().contains("tmm_newbie")) return false;
        if (!ModList.get().isLoaded("tmm_progression")) return true;
        try {
            Method method = newbieGate;
            if (method == null) {
                method = Class.forName(
                    "io.github.heatwaveqc.tmm.progression.runtime.NewbieMode"
                ).getMethod("canEarnMxp", ServerPlayer.class);
                newbieGate = method;
            }
            return Boolean.TRUE.equals(method.invoke(null, player));
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failed) {
            // The integration is mandatory whenever Progression is present.
            // An incompatible ABI or unavailable mode ledger must fail closed.
            return false;
        }
    }
}
