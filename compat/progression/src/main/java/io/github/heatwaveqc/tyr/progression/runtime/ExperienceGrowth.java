package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.server.level.ServerPlayer;

/** Compatibility entry point for existing callers; source-specific settlement
 * uses ExperienceModifiers.earn so the actual source identity is available. */
public final class ExperienceGrowth {
    private ExperienceGrowth() {}

    public static double effective(ServerPlayer player, double configuredGrowth) {
        return effective(player,configuredGrowth,"tyr_progression:mxp");
    }

    public static double effective(ServerPlayer player,double configuredGrowth,String systemId) {
        return ExperienceModifiers.growth(player, configuredGrowth, systemId, "");
    }
}
