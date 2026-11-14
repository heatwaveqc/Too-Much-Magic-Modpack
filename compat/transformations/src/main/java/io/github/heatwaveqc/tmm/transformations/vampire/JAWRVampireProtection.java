package io.github.heatwaveqc.tmm.transformations.vampire;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/** Exact JAWR 0.5.39 empowered Sunstone pendant check; no fabricated item IDs. */
public final class JAWRVampireProtection {
    private JAWRVampireProtection() { }

    public static boolean empoweredSunstone(ServerPlayer player) {
        if (!VampireChassisRuntime.isDual(player)) return false;
        try {
            Object pendant = Class.forName("dev.sterner.witchery.content.item.SunstonePendantItem")
                    .getField("Companion").get(null);
            Object result = pendant.getClass().getMethod("hasEmpoweredPendant", Player.class)
                    .invoke(pendant, player);
            return result instanceof Boolean yes && yes;
        } catch (ReflectiveOperationException | LinkageError e) {
            return false;
        }
    }
}
