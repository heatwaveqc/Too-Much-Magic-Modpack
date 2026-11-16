package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.core.ProgressionApi;
import io.github.heatwaveqc.tmm.progression.data.PathRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Configurable manual enrollment or server-verified Monk sunset contemplation. */
public final class Enrollment {
    private Enrollment() {}
    private static final Map<UUID,Integer> sunsetTicks = new HashMap<>();

    public static boolean enrollManual(ServerPlayer player, String path) {
        if (!"manual".equals(PathRegistry.enrollment(path))
                || player.isCreative() || player.isSpectator()) return false;
        return ProgressionApi.join(player,path);
    }

    public static void tick(ServerPlayer player) {
        String path = "tmm_progression:monk";
        if (!"sunset".equals(PathRegistry.enrollment(path))
                || ProgressionApi.track(player,path).joined()) {
            sunsetTicks.remove(player.getUUID());
            return;
        }
        long time = player.level().getDayTime() % 24000L;
        var pos = player.blockPosition();
        boolean valid = !player.isCreative() && !player.isSpectator()
            && player.level().dimension() == Level.OVERWORLD
            && time >= 12000 && time <= 13000
            && !player.level().isRainingAt(pos)
            && player.level().canSeeSky(pos.above())
            && player.getLookAngle().x < -0.70
            && Math.abs(player.getLookAngle().y) < 0.45
            && player.getMainHandItem().isEmpty();
        if (valid) {
            for (var stack : player.getArmorSlots()) if (!stack.isEmpty()) valid = false;
        }
        int ticks = valid ? sunsetTicks.getOrDefault(player.getUUID(),0)+1 : 0;
        if (ticks == 0) sunsetTicks.remove(player.getUUID());
        else sunsetTicks.put(player.getUUID(),ticks);
        if (ticks > 0 && ticks % 20 == 0)
            player.displayClientMessage(Component.literal("First Sunset: " + ticks + "/200"),true);
        if (ticks >= 200) {
            sunsetTicks.remove(player.getUUID());
            if (ProgressionApi.join(player,path)) {
                ProgressionApi.recordExternal(player,"tmm_progression:monk_first_sunset");
                player.displayClientMessage(Component.literal("First Sunset complete: Monk cultivation unlocked"),false);
            }
        }
    }

    public static void clear(ServerPlayer player) { sunsetTicks.remove(player.getUUID()); }
}
