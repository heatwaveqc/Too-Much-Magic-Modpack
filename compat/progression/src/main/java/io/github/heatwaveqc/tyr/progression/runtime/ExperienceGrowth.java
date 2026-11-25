package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.GrowthFactorMath;
import net.minecraft.server.level.ServerPlayer;

/**
 * Data-defined, per-player MXP growth reduction. An active purchased skill
 * contributes only while its owning Path and effect conditions are eligible.
 * This has no ownership of the MXP wallet, no side effect, and no stored XP.
 */
public final class ExperienceGrowth {
    private ExperienceGrowth() {}

    public static double effective(ServerPlayer player, double configuredGrowth) {
        return effective(player,configuredGrowth,"tyr_progression:mxp");
    }

    /** Only eligible live effects targeting this configured XP system apply. */
    public static double effective(ServerPlayer player,double configuredGrowth,String systemId) {
        double reduction=0;
        for(EffectRuntime.Active active:EffectRuntime.active(player)){
            if(!"xp_growth_reduction".equals(active.effect().type()))continue;
            String target=active.effect().params().getOrDefault("xp_system","");
            if(!target.isEmpty()&&!target.equals(systemId)&&!target.equals("*"))continue;
            reduction+=active.effect().amountPerRank()*active.rank();
        }
        return GrowthFactorMath.effective(configuredGrowth,reduction);
    }
}
