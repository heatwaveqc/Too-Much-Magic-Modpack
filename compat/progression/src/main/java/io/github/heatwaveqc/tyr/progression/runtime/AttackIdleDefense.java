package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.foundation.MartialAwarenessMath;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.bus.api.EventPriority;
import java.util.*;
import java.util.function.Supplier;

/** Native Resistance projection, only during the native magic damage calculation. */
public final class AttackIdleDefense {
    private AttackIdleDefense(){}
    private static final Map<ServerPlayer,Long> LAST=new WeakHashMap<>();
    private record Scope(ServerPlayer player,int amplifier){}
    private static final ThreadLocal<Scope> SCOPED=new ThreadLocal<>();
    public static void register(){
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,AttackIdleDefense::attack);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,AttackIdleDefense::damage);
        NeoForge.EVENT_BUS.addListener(AttackIdleDefense::projectile);
        NeoForge.EVENT_BUS.addListener(AttackIdleDefense::tick);
        NeoForge.EVENT_BUS.addListener(AttackIdleDefense::login);
        NeoForge.EVENT_BUS.addListener(AttackIdleDefense::respawn);
        NeoForge.EVENT_BUS.addListener(AttackIdleDefense::logout);
        NeoForge.EVENT_BUS.addListener(AttackIdleDefense::stop);
    }
    private static boolean actor(ServerPlayer p){
        return p.getServer()!=null&&p.getServer().isSameThread()&&p.isAlive()&&!p.isCreative()&&!p.isSpectator()
            &&!NewbieMode.isNewbie(p)&&!(p instanceof net.neoforged.neoforge.common.util.FakePlayer);
    }
    private static long now(ServerPlayer p){return p.getServer().overworld().getGameTime();}
    public static void offensiveAction(ServerPlayer p){if(actor(p)){LAST.put(p,now(p));FeignDeath.offensiveAction(p);}}
    private static void attack(AttackEntityEvent e){if(e.getEntity() instanceof ServerPlayer p)offensiveAction(p);}
    private static void damage(LivingIncomingDamageEvent e){
        if(e.getSource().getEntity() instanceof ServerPlayer p&&e.getEntity()!=p&&e.getAmount()>0)offensiveAction(p);
    }
    private static void projectile(EntityJoinLevelEvent e){
        if(!e.getLevel().isClientSide()&&!e.loadedFromDisk()&&e.getEntity() instanceof AbstractArrow arrow
            &&arrow.getOwner() instanceof ServerPlayer p)offensiveAction(p);
    }
    private static void tick(PlayerTickEvent.Pre e){if(e.getEntity() instanceof ServerPlayer p&&actor(p))LAST.putIfAbsent(p,now(p));}
    private static void login(PlayerEvent.PlayerLoggedInEvent e){if(e.getEntity() instanceof ServerPlayer p)offensiveAction(p);}
    private static void respawn(PlayerEvent.PlayerRespawnEvent e){if(e.getEntity() instanceof ServerPlayer p)offensiveAction(p);}
    private static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)LAST.remove(p);}
    private static void stop(net.neoforged.neoforge.event.server.ServerStoppedEvent e){LAST.clear();}
    public static int amplifier(ServerPlayer p){
        if(!actor(p))return -1;
        long at=now(p),last=LAST.computeIfAbsent(p,k->at);
        int amp=-1;
        for(var a:EffectRuntime.active(p)){
            if(!a.effect().type().equals("attack_idle_resistance"))continue;
            var params=a.effect().params();
            if(MartialAwarenessMath.idle(at,last,MechanicalSpec.integer(params,"idle_ticks",40,1,1200)))
                amp=Math.max(amp,MechanicalSpec.integer(params,"amplifier",0,0,3));
        }
        return amp;
    }
    public static float nativeDamage(LivingEntity entity,Supplier<Float> original){
        if(!(entity instanceof ServerPlayer p))return original.get();
        int amp=amplifier(p);if(amp<0)return original.get();
        Scope prior=SCOPED.get();SCOPED.set(new Scope(p,amp));
        try{return original.get();}finally{if(prior==null)SCOPED.remove();else SCOPED.set(prior);}
    }
    public static int scopedAmplifier(LivingEntity entity,Holder<MobEffect> effect){
        var scope=SCOPED.get();
        return scope!=null&&scope.player()==entity&&effect.equals(MobEffects.DAMAGE_RESISTANCE)?scope.amplifier():-1;
    }
    public static MobEffectInstance query(LivingEntity entity,Holder<MobEffect> effect,MobEffectInstance actual){
        int amp=scopedAmplifier(entity,effect);
        if(amp<0||actual!=null&&actual.getAmplifier()>=amp)return actual;
        return new MobEffectInstance(effect,1,amp,true,false,false);
    }
}
