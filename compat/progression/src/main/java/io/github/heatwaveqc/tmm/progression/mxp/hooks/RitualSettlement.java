package io.github.heatwaveqc.tmm.progression.mxp.hooks;

import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.MxpService;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import net.minecraft.server.level.ServerPlayer;

/** Shared native-only success settlement. Does not start, tick, or modify any ritual. */
public final class RitualSettlement {
    private RitualSettlement(){}
    public static boolean settle(ServerPlayer player,RitualRewards.Receipt receipt){
        if(receipt==null||!MxpPlayerPolicy.eligible(player)||!player.getUUID().equals(receipt.actor())||!MxpMod.active())return false;
        var rule=receipt.rule();
        if(rule==null)return false;
        if(rule.band().repeat()==0&&MxpService.inspect(player).firstCompletions().contains("ritual:"+rule.family()))return false;
        try {
            return MxpService.ritual(player,rule.family(),receipt.transaction(),rule.band().first(),rule.band().repeat()).accepted();
        } catch(RuntimeException e){
            MxpMod.LOGGER.error("MXP ritual settlement failed ({})",receipt.recipeId(),e);
            return false;
        }
    }
}
