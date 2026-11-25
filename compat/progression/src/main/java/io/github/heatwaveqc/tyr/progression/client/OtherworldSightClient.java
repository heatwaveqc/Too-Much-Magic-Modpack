package io.github.heatwaveqc.tyr.progression.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Local texture-only lease using Occultism's actual uncovered property and tier.
 * Pinned release 1.227.0 source, commit 990303ea95ba057791b7d407d6cac5151f66d799.
 * Native renderer retains ownership of its own revealed positions.
 */
public final class OtherworldSightClient {
    private OtherworldSightClient(){}
    private record Native(Class<?> type,BooleanProperty property,Method tier,Method level){}
    private static Native nativeApi;
    private static boolean attempted;
    private static ClientLevel world;
    private static int lastTier;
    private static final Map<BlockPos,Block> OWNED=new HashMap<>();
    private static Native api(){
        if(attempted)return nativeApi;
        attempted=true;
        try{
            Class<?> type=Class.forName("com.klikli_dev.occultism.common.block.otherworld.IOtherworldBlock");
            var tier=type.getMethod("getTier");
            nativeApi=new Native(type,(BooleanProperty)type.getField("UNCOVERED").get(null),
                tier,tier.getReturnType().getMethod("getLevel"));
        }catch(ReflectiveOperationException|LinkageError|ClassCastException failure){
            io.github.heatwaveqc.tyr.progression.ProgressionMod.LOGGER.warn("Occultism Otherworld sight API unavailable",failure);
        }
        return nativeApi;
    }
    private static int tier(Native api,Block block){
        if(!api.type().isInstance(block))return 0;
        try{return (Integer)api.level().invoke(api.tier().invoke(block));}
        catch(ReflectiveOperationException|RuntimeException failure){return 0;}
    }
    private static boolean near(BlockPos center,BlockPos pos){
        return Math.abs(center.getX()-pos.getX())<=10&&Math.abs(center.getY()-pos.getY())<=10
            &&Math.abs(center.getZ()-pos.getZ())<=10;
    }
    public static void tick(Player entity,Set<BlockPos> nativeUncovered){
        var mc=Minecraft.getInstance();
        if(entity!=mc.player||mc.level==null)return;
        var level=mc.level;
        if(world!=level){OWNED.clear();world=level;lastTier=0;}
        int grant=ClientVision.otherworldTier(entity);
        if(grant==0&&OWNED.isEmpty()){lastTier=0;return;}
        var api=api();if(api==null)return;
        var center=entity.blockPosition();
        // Loss, tier change and expiry clean up immediately. Never reveal unloaded chunks.
        var iterator=OWNED.entrySet().iterator();
        while(iterator.hasNext()){
            var entry=iterator.next();var pos=entry.getKey();
            if(!level.hasChunkAt(pos)){iterator.remove();continue;}
            var state=level.getBlockState(pos);
            if(state.getBlock()!=entry.getValue()||!state.hasProperty(api.property())){
                iterator.remove();continue;
            }
            if(grant==0||!near(center,pos)||tier(api,state.getBlock())>grant){
                if(!nativeUncovered.contains(pos)&&state.getValue(api.property()))
                    level.setBlock(pos,state.setValue(api.property(),false),Block.UPDATE_IMMEDIATE);
                iterator.remove();
            }
        }
        if(grant>0&&(grant!=lastTier||entity.tickCount%5==0)){
            for(var cursor:BlockPos.betweenClosed(center.offset(-10,-10,-10),center.offset(10,10,10))){
                if(level.isOutsideBuildHeight(cursor)||!level.hasChunkAt(cursor))continue;
                var state=level.getBlockState(cursor);int blockTier=tier(api,state.getBlock());
                if(blockTier<1||blockTier>grant||!state.hasProperty(api.property())||state.getValue(api.property()))continue;
                var pos=cursor.immutable();
                if(level.setBlock(pos,state.setValue(api.property(),true),Block.UPDATE_IMMEDIATE))
                    OWNED.put(pos,state.getBlock());
            }
        }
        lastTier=grant;
    }
}
