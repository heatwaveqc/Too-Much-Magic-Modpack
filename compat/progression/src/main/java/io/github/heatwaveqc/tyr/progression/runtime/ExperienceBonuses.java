package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.server.level.ServerPlayer;

/** Additive native practice bonuses on actual positive grants, never on MXP, points or refunds. */
public final class ExperienceBonuses{
    private ExperienceBonuses(){}
    public static long apply(ServerPlayer player,String economy,long base){
        if(base<=0||economy.equals("tyr_progression:mxp"))return base;
        double rate=0;
        for(var active:EffectRuntime.active(player)){
            var fx=active.effect();
            if(fx.type().equals("experience_gain_bonus")&&fx.selector().equals(economy))rate+=fx.amountPerRank()*active.rank();
        }
        return Math.min(1_000_000L,base+Math.round(base*Math.min(2,Math.max(0,rate))));
    }
}
