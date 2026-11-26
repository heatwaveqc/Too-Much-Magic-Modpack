package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.foundation.ScopedFellActor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import java.util.function.Consumer;

/** Native TreeChop may intentionally omit its credit player. Preserve that choice;
 * carry the real felling player only for the original log's sight predicate. */
public final class TreeChopLootScope {
    private TreeChopLootScope(){}
    private static final ScopedFellActor<Entity,ServerLevel,BlockPos> SCOPE=new ScopedFellActor<>();
    public static Consumer<BlockPos> wrap(ServerPlayer player,ServerLevel level,Consumer<BlockPos> nativeBreaker){
        return pos->{
            if(player.serverLevel()!=level||player.isCreative()||player.isSpectator()||!player.isAlive()
                ||player.getServer()==null||!player.getServer().isSameThread()
                ||player.getServer().getPlayerList().getPlayer(player.getUUID())!=player){
                nativeBreaker.accept(pos);return;
            }
            SCOPE.run(player,level,pos.immutable(),()->nativeBreaker.accept(pos));
        };
    }
    static Entity sightActor(Entity nativeActor,ServerLevel level,BlockPos pos){
        return SCOPE.actor(nativeActor,level,pos);
    }
}
