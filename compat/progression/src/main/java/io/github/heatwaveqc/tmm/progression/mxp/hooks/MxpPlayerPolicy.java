package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import net.minecraft.server.level.ServerPlayer;

/** Actual connected survival/adventure server actor, never fake/offline/client. */
public final class MxpPlayerPolicy {
    private MxpPlayerPolicy() {}
    public static boolean eligible(ServerPlayer p) {
        return p != null && p.server.isSameThread() && !p.isCreative() && !p.isSpectator()
            && p.server.getPlayerList().getPlayer(p.getUUID()) == p;
    }
}
