package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.data.TrainingXpSpec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Server-only bridge between verified gameplay adapters and the universal
 * configured economy. Each source is its own independent XP category.
 */
public final class TrainingRewards {
    private TrainingRewards() {}
    public static boolean eligible(ServerPlayer player,String path) {
        return player!=null && player.getServer()!=null && player.getServer().isSameThread()
            && !(player instanceof FakePlayer) && !player.isCreative() && !player.isSpectator()
            && !NewbieMode.isNewbie(player)
            && player.getServer().getPlayerList().getPlayer(player.getUUID())==player
            && ProgressionApi.track(player,path).joined() && PathRegistry.eligible(player,path);
    }
    public static boolean award(ServerPlayer player,String path,TrainingXpSpec rates,
                                String category,BigDecimal nominal,boolean fixed,
                                String permanentReceipt) {
        if(!eligible(player,path)||nominal==null||nominal.signum()<=0
                ||rates==null||!PathRegistry.economyEligible(player,rates.system()))
            return false;
        String tx="training:"+UUID.randomUUID();
        return ProgressionApi.awardConfiguredXp(player,rates.system(),
            "tyr_progression:training_"+category,tx,permanentReceipt,nominal,fixed);
    }
    public static void mining(ServerPlayer player,double hardness) {
        for(Map.Entry<String,TrainingXpSpec> row:PathRegistry.trainingXp().entrySet())
            award(player,row.getKey(),row.getValue(),"mining",row.getValue().mining(hardness),false,null);
    }
    public static void sprint(ServerPlayer player,long fullBlocks) {
        if(fullBlocks<=0)return;
        for(var row:PathRegistry.trainingXp().entrySet())
            award(player,row.getKey(),row.getValue(),"sprint",
                row.getValue().sprint().multiply(BigDecimal.valueOf(fullBlocks)),false,null);
    }
    public static void kill(ServerPlayer player,double baselineHealth,boolean strict) {
        for(var row:PathRegistry.trainingXp().entrySet())
            award(player,row.getKey(),row.getValue(),"combat",
                row.getValue().kill(baselineHealth,strict),false,null);
    }
}
