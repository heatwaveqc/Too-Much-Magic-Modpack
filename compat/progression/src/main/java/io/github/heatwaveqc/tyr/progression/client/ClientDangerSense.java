package io.github.heatwaveqc.tyr.progression.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import java.lang.ref.WeakReference;
import java.util.*;

public final class ClientDangerSense {
    private ClientDangerSense(){}
    private static WeakReference<LocalPlayer> owner=new WeakReference<>(null);
    private static Set<UUID> ids=Set.of();
    private static int received=-1;
    public static void accept(Set<UUID> next,boolean alert){
        var p=Minecraft.getInstance().player;
        owner=new WeakReference<>(p);ids=p==null?Set.of():Set.copyOf(next);received=p==null?-1:p.tickCount;
        if(p!=null&&alert&&!ids.isEmpty()){
            p.playSound(SoundEvents.NOTE_BLOCK_BASS.value(),0.65f,0.85f);
            p.playSound(SoundEvents.NOTE_BLOCK_BIT.value(),0.5f,1.25f);
        }
    }
    public static boolean highlighted(Entity e){
        if(!e.level().isClientSide())return false;
        var p=Minecraft.getInstance().player;
        return e.level().isClientSide()&&p!=null&&p==owner.get()&&p.level()==e.level()&&p.isAlive()
            &&!p.isCreative()&&!p.isSpectator()&&e.isAlive()&&received>=0&&p.tickCount>=received
            &&p.tickCount-received<=40&&ids.contains(e.getUUID());
    }
}
