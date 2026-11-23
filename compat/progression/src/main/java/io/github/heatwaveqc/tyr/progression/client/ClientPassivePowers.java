package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.core.PassivePowerGrants;
import io.github.heatwaveqc.tyr.progression.core.PassivePowerLease;
import java.lang.ref.WeakReference;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * Read-only local-player lease, not an authority. A stale packet cannot grant
 * another player power; a lost server packet expires after 40 client ticks.
 */
public final class ClientPassivePowers {
    private ClientPassivePowers() {}
    private static WeakReference<LocalPlayer> owner = new WeakReference<>(null);
    private static Set<String> active = Set.of();
    private static int receivedAt = -1;

    public static void accept(String dimension, String encoded) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null
                || !player.level().dimension().location().toString().equals(dimension)) {
            clear();
            return;
        }
        Set<String> parsed;
        try { parsed = PassivePowerGrants.decodeActive(encoded); }
        catch (IllegalArgumentException invalid) { clear(); return; }
        owner = new WeakReference<>(player);
        active = parsed;
        receivedAt = player.tickCount;
    }

    public static boolean active(LivingEntity entity, String power) {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && entity == player && owner.get() == player
                && player.isAlive() && !player.isSpectator() && !player.isCreative()
                && PassivePowerLease.valid(receivedAt, player.tickCount)
                && active.contains(power);
    }

    public static void clear() {
        owner = new WeakReference<>(null);
        active = Set.of();
        receivedAt = -1;
    }
}
