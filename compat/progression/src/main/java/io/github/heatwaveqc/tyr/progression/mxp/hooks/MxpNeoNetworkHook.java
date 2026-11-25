package io.github.heatwaveqc.tyr.progression.mxp.hooks;

import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.MxpService;
import io.github.heatwaveqc.tyr.progression.mxp.MxpSourceConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.UUID;
import io.github.heatwaveqc.tyr.progression.mxp.core.PaymentReceiptPolicy;

/**
 * Neo Vitae 1.1.53: exclusively owner-linked Anima EV-network syphons.
 * No XP for added EV, health-payment substitutions, item charges, off-network
 * effects or network capacity changes. Manual and automated network syphons
 * both qualify for the online owner; PostSyphon is emitted after the debit.
 */
public final class MxpNeoNetworkHook {
    private MxpNeoNetworkHook() {}

    public static void onPostSyphon(Object nativeEvent) {
        double base = MxpSourceConfig.NEO_EV_NETWORK_POOL.get();
        if (!Double.isFinite(base) || base <= 0) return;
        Object owner = MxpReflect.call(nativeEvent, "getOwnerId");
        if (!(owner instanceof UUID ownerId)) return;
        double debited = MxpReflect.number(MxpReflect.call(nativeEvent, "getActualAmount"));
        // Native upkeep multipliers can make actualAmount exceed the ticket's
        // original amount. The positive committed debit is authoritative.
        if (!PaymentReceiptPolicy.positive(debited)) return;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null || !server.isSameThread()) return;
        ServerPlayer player = server.getPlayerList().getPlayer(ownerId);
        if (!MxpPlayerPolicy.eligible(player)) return;
        // This method is called ONLY from the actual native Anima.PostSyphon,
        // not an arbitrary mana/health/status resource or a mana-gain event.
        // Credit every actual positive native network syphon, independent of
        // the caller. Do not infer spend from periodic net balance differences.
        try {
            MxpService.resource(player, "neovitae:ev_network",
                    "neovitae:network:" + UUID.randomUUID(),
                    debited, base, debited);
        } catch (RuntimeException e) {
            MxpMod.LOGGER.warn("Neo Vitae network MXP observation rejected: {}", e.toString(), e);
        }
    }
}
