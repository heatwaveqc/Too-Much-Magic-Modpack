package io.github.heatwaveqc.tmm.progression.client;

import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.foundation.client.LavaFogHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import java.lang.ref.WeakReference;

/** Lease tied to the local player instance; stale state expires after 40 client ticks. */
@EventBusSubscriber(modid=ProgressionMod.MOD_ID,value=Dist.CLIENT)
public final class ClientVision {
    private ClientVision(){}
    private static WeakReference<LocalPlayer> owner=new WeakReference<>(null);
    private static boolean night;
    private static int lava,receivedTick,astralStage;
    public static void accept(boolean enhanced,int range,int stage){
        LocalPlayer player=Minecraft.getInstance().player;
        owner=new WeakReference<>(player);night=enhanced;lava=Math.max(0,Math.min(128,range));
        astralStage=Math.max(-1,Math.min(4,stage));
        receivedTick=player==null?0:player.tickCount;
    }
    private static boolean valid(LivingEntity entity){
        var player=Minecraft.getInstance().player;
        return player!=null&&entity==player&&owner.get()==player&&player.isAlive()&&!player.isSpectator()
            &&player.tickCount>=receivedTick&&player.tickCount-receivedTick<=40
            &&!player.hasEffect(MobEffects.BLINDNESS)&&!player.hasEffect(MobEffects.DARKNESS);
    }
    public static boolean night(LivingEntity entity){return night&&valid(entity);}
    /** A lease-bound server-projected stage, never inferred from local Astral UI. */
    public static int astralStage(){
        LocalPlayer player=Minecraft.getInstance().player;
        return player!=null&&valid(player)?astralStage:0;
    }
    @SubscribeEvent public static void lavaFog(ViewportEvent.RenderFog event){
        if(event.getCamera().getEntity() instanceof LivingEntity entity&&lava>0&&valid(entity))
            LavaFogHook.apply(event,true,lava);
    }
}
