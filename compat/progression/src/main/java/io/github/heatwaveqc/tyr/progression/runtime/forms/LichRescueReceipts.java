package io.github.heatwaveqc.tyr.progression.runtime.forms;

import io.github.heatwaveqc.tyr.progression.foundation.LichSkillMath;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import java.util.Collections;
import java.util.WeakHashMap;

/** Observe only provider-native rescue completion; does not intercept/cancel death itself. */
public final class LichRescueReceipts {
    private static final ThreadLocal<Receipt> ACTIVE=new ThreadLocal<>();
    private static final java.util.Set<LivingDeathEvent> SEEN=Collections.newSetFromMap(new WeakHashMap<>());
    private static final class Receipt{
        final LivingDeathEvent event;
        final ServerPlayer player;
        final boolean forbidden;
        boolean nativeRescue,paid,valid=true;
        Receipt(LivingDeathEvent e,ServerPlayer p,boolean f){event=e;player=p;forbidden=f;}
    }
    private LichRescueReceipts(){}
    public static void observe(LivingDeathEvent e,boolean forbidden,Runnable original){
        Receipt old=ACTIVE.get();
        if(old!=null){old.valid=false;original.run();return;}
        if(e.isCanceled()||!(e.getEntity() instanceof ServerPlayer p)||!LichEffects.eligible(p)||
            (forbidden?!LichEffects.forbidden(p):!LichEffects.goety(p)||!LichEffects.arca(p))){
            original.run();return;
        }
        Receipt r=new Receipt(e,p,forbidden);ACTIVE.set(r);
        try{
            original.run();
            if(r.valid&&LichSkillMath.rescue(false,e.isCanceled(),r.nativeRescue,r.paid,p.getHealth())&&p.isAlive()){
                synchronized(SEEN){if(!SEEN.add(e))return;}
                LichEffects.rescued(p);
            }
        }finally{ACTIVE.remove();}
    }
    public static void arcaTeleport(Player p,boolean success){
        Receipt r=ACTIVE.get();if(r==null||r.forbidden||r.player!=p)return;
        if(r.nativeRescue)r.valid=false;
        r.nativeRescue=success;
    }
    public static void arcaDebit(Player p,int cost,Runnable original){
        Receipt r=ACTIVE.get();
        if(r==null||r.forbidden||r.player!=p||!r.nativeRescue){original.run();return;}
        int before=LichEffects.souls(p);
        original.run();
        int after=LichEffects.souls(p);
        if(r.paid||before<0||after<0||after>=before||cost<=0||before-after>cost)r.valid=false;
        else r.paid=true;
    }
    /** Exact native onPhylacterySave is downstream of nonnull drainNewestSoul,
     * successful health restoration and native slot/anchor handling. */
    public static void phylactery(ServerPlayer p){
        Receipt r=ACTIVE.get();if(r==null||!r.forbidden||r.player!=p)return;
        if(r.nativeRescue)r.valid=false;
        r.nativeRescue=true;r.paid=true;
    }
}
