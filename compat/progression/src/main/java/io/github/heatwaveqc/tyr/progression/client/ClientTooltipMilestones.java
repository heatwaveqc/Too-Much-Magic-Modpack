package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.foundation.TooltipMilestonePolicy;
import java.lang.ref.WeakReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Short-lived view of server-owned milestones. No client unlock action.
 * The weak player identity and <=40-tick lease reject stale worlds, logouts,
 * unsynchronized joins and unintended cross-player cache reuse.
 */
public final class ClientTooltipMilestones {
    private ClientTooltipMilestones() {}

    private static WeakReference<LocalPlayer> owner = new WeakReference<>(null);
    private static int receivedTick = -1;
    private static boolean stone;
    private static boolean replica;

    public static void accept(boolean projecte, boolean replication) {
        LocalPlayer player = Minecraft.getInstance().player;
        owner = new WeakReference<>(player);
        receivedTick = player == null ? -1 : player.tickCount;
        stone = player != null && projecte;
        replica = player != null && replication;
    }

    private static boolean valid() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.isAlive() && owner.get() == player
            && receivedTick >= 0 && player.tickCount >= receivedTick
            && player.tickCount - receivedTick <= 40;
    }

    /** Source-specific adapters must supply the CURRENT ProjectE knowledge test. */
    public static boolean mayShowProjecteNumber(boolean nativeKnows, boolean nativeSetting) {
        return valid() && TooltipMilestonePolicy.projecteNumeric(stone, nativeKnows, nativeSetting);
    }

    public static boolean mayShowProjecteLearned(boolean nativeSetting) {
        return valid() && TooltipMilestonePolicy.projecteLearnedIndicator(stone, nativeSetting);
    }

    public static boolean mayShowReplicationMatter(boolean nativeSetting) {
        return valid() && TooltipMilestonePolicy.replicationMatter(replica, nativeSetting);
    }
}
