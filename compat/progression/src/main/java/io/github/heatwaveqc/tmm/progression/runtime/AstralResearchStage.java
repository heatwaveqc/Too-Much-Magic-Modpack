package io.github.heatwaveqc.tmm.progression.runtime;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.foundation.AstralDarknessPolicy;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;

/**
 * Optional, read-only bridge to Astral Sorcery's authoritative personal research.
 * Do not use advancements, altar proximity or a client-reported tier as substitutes.
 * No binary or compile-time dependency on Astral Sorcery is introduced.
 */
public final class AstralResearchStage {
    private static Method progressMethod;
    private static Method tierMethod;
    private static boolean failed;

    private AstralResearchStage() {}

    public static int stage(ServerPlayer player) {
        if (NewbieMode.isNewbie(player)) return AstralDarknessPolicy.NEWBIE;
        if (!ModList.get().isLoaded("astralsorcery")) return AstralDarknessPolicy.REGULAR;
        if (failed) return AstralDarknessPolicy.REGULAR;
        try {
            resolveOnce();
            Object progress = progressMethod.invoke(null, player, LogicalSide.SERVER);
            if (progress == null) return AstralDarknessPolicy.REGULAR;
            Object tier = tierMethod.invoke(progress);
            return AstralDarknessPolicy.researchTier(tier instanceof Enum<?> value ? value.name() : null);
        } catch (ReflectiveOperationException | LinkageError | ClassCastException exception) {
            failed = true;
            ProgressionMod.LOGGER.warn("Astral research API unavailable; keeping ordinary Regular darkness until a compatible API is present", exception);
            return AstralDarknessPolicy.REGULAR;
        }
    }

    private static synchronized void resolveOnce() throws ReflectiveOperationException {
        if (progressMethod != null) return;
        Class<?> research = Class.forName("hellfirepvp.astralsorcery.common.research.ResearchManager");
        Class<?> data = Class.forName("hellfirepvp.astralsorcery.common.research.PlayerProgress");
        // Version-matched public 1.21.1 source: getProgress(Player, LogicalSide)
        Method getter = research.getMethod("getProgress", net.minecraft.world.entity.player.Player.class, LogicalSide.class);
        Method tier = data.getMethod("getTierReached");
        tierMethod = tier;
        progressMethod = getter;
    }
}
