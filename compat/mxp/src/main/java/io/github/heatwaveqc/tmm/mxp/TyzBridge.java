package io.github.heatwaveqc.tmm.mxp;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * No compile-time Tyz JAR required. Uses its source-verified 6.4.2 public API.
 * Observe the ACTUAL credited XP, not merely a void addXP() invocation:
 * other mods may cancel/modify SkillXPChangeEvent.
 */
public final class TyzBridge {
    private static final String API = "com.tyzsskills.api.TyzsSkillsAPI";

    private TyzBridge() {}

    public static float deliver(ServerPlayer player, float requested) {
        if (requested <= 0 || !Float.isFinite(requested)) return 0;
        if (!ModList.get().isLoaded("tyzs_skills"))
            throw new IllegalStateException("MXP requires Tyz's Skills 6.4.2");

        try {
            Class<?> api = Class.forName(API);
            Object xp = api.getMethod("xp").invoke(null);
            Object level = api.getMethod("level").invoke(null);
            Method getXP = xp.getClass().getMethod("getXP", ServerPlayer.class);
            Method getLevel = level.getClass().getMethod("getLevel", ServerPlayer.class);
            Method addXP = xp.getClass().getMethod("addXP",
                    ServerPlayer.class, float.class, boolean.class, boolean.class);

            float beforeXP = ((Number) getXP.invoke(xp, player)).floatValue();
            int beforeLevel = ((Number) getLevel.invoke(level, player)).intValue();
            // applyLimits=false is deliberate: our MXP divisor is the only
            // progression scaling. A server config must disable Tyz's native
            // activity XP; this call does not create vanilla Minecraft XP.
            addXP.invoke(xp, player, requested, true, false);
            float afterXP = ((Number) getXP.invoke(xp, player)).floatValue();
            int afterLevel = ((Number) getLevel.invoke(level, player)).intValue();

            double observed = (afterLevel - beforeLevel) * 1000.0
                    + (afterXP - beforeXP);
            if (observed < -0.02 || observed > requested + 0.1) {
                throw new IllegalStateException("Tyz XP diverged during MXP award: requested="
                    + requested + " observed=" + observed + ". Check the loaded level-pool "
                    + "and interfering XP/level listeners. Pause live MXP awards.");
            }
            if (observed <= 0) return 0;
            if (observed < requested - 0.1)
                MxpMod.LOGGER.warn("MXP partial Tyz delivery: requested={}, actual={}",
                        requested, observed);
            return (float) Math.min(requested, observed);
        } catch (ReflectiveOperationException | LinkageError ex) {
            Throwable cause = ex instanceof InvocationTargetException invoked
                    ? invoked.getTargetException() : ex;
            throw new IllegalStateException("Tyz 6.4.2 API bridge failed", cause);
        }
    }
}
