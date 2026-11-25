package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tyr.progression.state.CombatProof;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.data.TrainingXpSpec;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import java.math.BigDecimal;
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
            && player.isAlive()
            && player.getData(ProgressionAttachments.LEDGER.get()).get(path).joined()
            && PathRegistry.eligible(player,path);
    }
    public static boolean award(ServerPlayer player,String path,TrainingXpSpec rates,
                                String category,BigDecimal nominal,boolean fixed,
                                String permanentReceipt) {
        if(!eligible(player,path)||nominal==null||nominal.signum()<=0
                ||rates==null)
            return false;
        String tx="training:"+UUID.randomUUID();
        return ProgressionApi.awardConfiguredXp(player,rates.system(),
            "tyr_progression:training_"+category,tx,permanentReceipt,nominal,fixed);
    }
    public static io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate candidate(
            TrainingXpSpec rates,String category,BigDecimal nominal,boolean fixed,String receipt){
        return new io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate(
                rates.system(),"tyr_progression:training_"+category,receipt,nominal,fixed);
    }

    private static void batch(ServerPlayer player,String category,
                              java.util.function.Function<TrainingXpSpec,BigDecimal> nominal) {
        var candidates=new java.util.ArrayList<io.github.heatwaveqc.tyr.progression.data.XpSourceRouting.Candidate>();
        for(var row:PathRegistry.trainingXp().entrySet()){
            if(!eligible(player,row.getKey()))continue;
            var rates=row.getValue();
            candidates.add(candidate(rates,category,nominal.apply(rates),false,null));
        }
        ProgressionApi.awardSourceBatch(player,"training:"+UUID.randomUUID(),candidates);
    }
    public static void mining(ServerPlayer player,double hardness) {
        batch(player,"mining",rates->rates.mining(hardness));
    }
    public static void sprint(ServerPlayer player,long fullBlocks) {
        if(fullBlocks<=0)return;
        batch(player,"sprint",rates->rates.sprint().multiply(BigDecimal.valueOf(fullBlocks)));
    }
    public static void kill(ServerPlayer player,double baselineHealth,
                            CombatProof proof,boolean directFistFinisher,long tick) {
        batch(player,"combat",rates->rates.kill(baselineHealth,directFistFinisher &&
                proof.strict(player.getUUID().toString(),tick,rates.strictWindow())));
    }
}
