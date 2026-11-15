package io.github.heatwaveqc.tmm.transformations.lich;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Read-only native state diagnostics for /tmmtransform archlich.
 * Does not enroll, resurrect, move or change a player's upstream state.
 */
public final class ArchlichDiagnostics {
    private ArchlichDiagnostics() { }

    public static String describe(ServerPlayer player) {
        boolean enabled = ArchlichResurrectionConfig.enabled();
        if (!ModList.get().isLoaded("goety")
                || !ModList.get().isLoaded("witchery_forbidden_magic")) {
            return "Archlich priority=" + enabled
                    + "; Goety and Forbidden Magic must both be loaded.";
        }
        try {
            Class<?> goetyType = Class.forName("com.Polarice3.Goety.utils.LichdomHelper");
            boolean goety = (Boolean) goetyType.getMethod("isLich", Player.class)
                    .invoke(null, player);
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichPlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class)
                    .invoke(null, player);
            if (data == null) return "Archlich: Forbidden Magic attachment unavailable";
            boolean forbidden = (Boolean) data.getClass().getMethod("isLich")
                    .invoke(data);
            int level = ((Number) data.getClass().getMethod("getLevel")
                    .invoke(data)).intValue();
            Class<?> poolType = Class.forName(
                    "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichdomSoulPoolHandler");
            Object pool = poolType.getField("INSTANCE").get(null);
            int souls = ((Number) poolType.getMethod(
                    "getCurrentSouls", ServerPlayer.class).invoke(pool, player)).intValue();
            boolean willPrioritize = enabled
                    && ArchlichResurrectionPolicy.prioritizePhylactery(
                    goety, forbidden, level, souls, false);
            return "Archlich priority=" + enabled
                    + "; native Goety lich=" + goety
                    + ", Forbidden Magic lich=" + forbidden
                    + ", Forbidden Magic level=" + level
                    + ", charged phylactery souls=" + souls
                    + "; next death priority=" + (willPrioritize
                    ? "JAWR phylactery (Goety Arca only if JAWR does not cancel)"
                    : "unmodified native Goety/JAWR handlers");
        } catch (ReflectiveOperationException | LinkageError | RuntimeException failure) {
            return "Archlich native-state diagnostics unavailable: "
                    + failure.getClass().getSimpleName() + ": "
                    + failure.getMessage();
        }
    }
}
