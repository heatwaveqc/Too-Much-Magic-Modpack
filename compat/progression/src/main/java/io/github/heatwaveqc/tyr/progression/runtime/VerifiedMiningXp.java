package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Pre-break captures actor and original hardness; only verified removal pays. */
public final class VerifiedMiningXp {
    private VerifiedMiningXp() {}
    private record Pending(ServerLevel level,BlockPos pos,BlockState state,
                           boolean barehand,double hardness,long tick) {}
    private static final Map<UUID,Map<Long,Pending>> PENDING=new HashMap<>();
    private static final int MAX_PER_PLAYER=8192;

    public static boolean participates(ServerPlayer player) {
        for(String path:PathRegistry.trainingXp().keySet())
            if(TrainingRewards.eligible(player,path)
                    && PathRegistry.trainingXp().get(path).hardness().signum()>0)return true;
        return false;
    }
    public static void beforeBreak(BlockEvent.BreakEvent event){
        if(event.isCanceled()||!(event.getPlayer() instanceof ServerPlayer player)
                ||!(event.getLevel() instanceof ServerLevel level)
                ||!participates(player)||event.getState().isAir())return;
        BlockPos pos=event.getPos();
        if(!level.hasChunkAt(pos))return;
        double hardness=event.getState().getDestroySpeed(level,pos);
        if(!Double.isFinite(hardness)||hardness<0)return;
        var queue=PENDING.computeIfAbsent(player.getUUID(),ignored->new HashMap<>());
        if(queue.size()>=MAX_PER_PLAYER&&!queue.containsKey(pos.asLong()))return;
        queue.put(pos.asLong(),new Pending(level,pos.immutable(),event.getState(),
            player.getMainHandItem().isEmpty(),hardness,level.getGameTime()));
    }
    public static void drops(BlockDropsEvent event){
        if(event.isCanceled()||!(event.getBreaker() instanceof ServerPlayer player)
                ||!participates(player))return;
        var level=event.getLevel();BlockPos pos=event.getPos();
        if(!level.hasChunkAt(pos)||event.getState().isAir()
                ||!removed(level.getBlockState(pos)))return;
        double hardness=event.getState().getDestroySpeed(level,pos);
        boolean bare=event.getTool().isEmpty()&&player.getMainHandItem().isEmpty();
        credit(player,level,pos,event.getState(),hardness,bare,level.getGameTime());
    }
    private static boolean removed(BlockState state){
        return state.isAir()||!state.getFluidState().isEmpty();
    }
    private static void credit(ServerPlayer player,ServerLevel level,BlockPos pos,
                               BlockState state,double hardness,boolean barehand,long originalTick){
        if(!barehand||!Double.isFinite(hardness)||hardness<0
            ||!RecentBlockCredits.first(player.getUUID(),
                level.dimension().location().toString(),pos.asLong(),originalTick))return;
        ProgressionApi.record(player,Selectors.blockBreak(state));
        ProgressionApi.record(player,Selectors.barehandBlockBreak(state));
        TrainingRewards.mining(player,hardness);
    }
    public static void tick(ServerPlayer player){
        Map<Long,Pending> entries=PENDING.remove(player.getUUID());
        if(entries==null)return;
        for(Pending item:List.copyOf(entries.values())){
            ServerLevel level=item.level();long now=level.getGameTime();
            if(!participates(player)||player.serverLevel()!=level
                    ||now<item.tick()||now-item.tick()>3||!level.hasChunkAt(item.pos())
                    ||!removed(level.getBlockState(item.pos())))continue;
            credit(player,level,item.pos(),item.state(),item.hardness(),item.barehand(),item.tick());
        }
    }
    /**
     * Trusted server-only adapter for a spell or modded AoE producer which has
     * independently verified player ownership and original state. Distinct from
     * barehand trial counting and from MXP for consumed magical resources.
     * An unremoved/protected block is never credited.
     */
    public static boolean spellBreakCommitted(ServerPlayer owner,ServerLevel level,
            BlockPos pos,BlockState original,long originalTick){
        if(owner==null||level==null||original==null||original.isAir()
                ||owner.serverLevel()!=level||!participates(owner)
                ||!level.hasChunkAt(pos)||!removed(level.getBlockState(pos))
                ||level.getGameTime()<originalTick||level.getGameTime()-originalTick>3)return false;
        double hardness=original.getDestroySpeed(level,pos);
        if(!Double.isFinite(hardness)||hardness<0
                ||!RecentBlockCredits.first(owner.getUUID(),
                    level.dimension().location().toString(),pos.asLong(),originalTick))return false;
        ProgressionApi.record(owner,Selectors.blockBreak(original));
        TrainingRewards.mining(owner,hardness);
        return true;
    }

    public static void forget(ServerPlayer player){PENDING.remove(player.getUUID());}
}
