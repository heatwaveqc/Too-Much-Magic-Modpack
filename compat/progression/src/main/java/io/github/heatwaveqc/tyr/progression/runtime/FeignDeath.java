package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.*;

/** Playing dead affects combat targeting, never real death, invulnerability, or health. */
public final class FeignDeath {
    private FeignDeath(){}
    private record Session(Vec3 origin,Object level){}
    private static final Map<ServerPlayer,Session> ACTIVE=new WeakHashMap<>();
    public static void register(){
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,FeignDeath::targetChange);
        NeoForge.EVENT_BUS.addListener(FeignDeath::tick);
        NeoForge.EVENT_BUS.addListener(FeignDeath::login);
        NeoForge.EVENT_BUS.addListener(FeignDeath::dimension);
        NeoForge.EVENT_BUS.addListener(FeignDeath::respawn);
        NeoForge.EVENT_BUS.addListener(FeignDeath::logout);
        NeoForge.EVENT_BUS.addListener(FeignDeath::stop);
    }
    public static boolean validPose(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
            &&!(p instanceof net.neoforged.neoforge.common.util.FakePlayer)
            &&p.onGround()&&!p.isPassenger()&&!p.isFallFlying()&&!p.getAbilities().flying
            &&!p.isSwimming()&&!p.isInWater()&&!p.isInLava()&&!p.isSleeping();
    }
    private static int radius(ServerPlayer p){
        return EffectRuntime.active(p).stream().filter(a->a.effect().type().equals("feign_death"))
            .mapToInt(a->MechanicalSpec.integer(a.effect().params(),"aggro_radius",64,8,96)).max().orElse(0);
    }
    public static boolean active(ServerPlayer p){
        var s=ACTIVE.get(p);
        return s!=null&&s.level()==p.level()&&validPose(p)&&p.position().distanceToSqr(s.origin())<=0.0025
            &&radius(p)>0;
    }
    static void activationChanged(ServerPlayer p){
        int radius=radius(p);
        if(radius<=0||!validPose(p)){end(p);return;}
        ACTIVE.put(p,new Session(p.position(),p.level()));
        dump(p,radius);
    }
    static void offensiveAction(ServerPlayer p){if(ACTIVE.containsKey(p))end(p);}
    private static void end(ServerPlayer p){
        ACTIVE.remove(p);SkillActivation.stopEffect(p,"feign_death");
    }
    private static boolean ordinary(Mob mob,ServerPlayer p){
        return mob.isAlive()&&!StunningStrike.boss(mob)&&!mob.isAlliedTo(p)&&!p.isAlliedTo(mob)
            &&!Selectors.entity("#tyr_progression:feign_death_immune",mob);
    }
    public static boolean hidesFrom(Mob mob,ServerPlayer p){return ordinary(mob,p)&&active(p);}
    private static boolean attackMemory(Mob mob,ServerPlayer p){
        var brain=mob.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)
            &&brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null)==p;
    }
    private static boolean angryAt(Mob mob,ServerPlayer p){
        var brain=mob.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.ANGRY_AT)
            &&brain.getMemory(MemoryModuleType.ANGRY_AT).orElse(null)!=null
            &&brain.getMemory(MemoryModuleType.ANGRY_AT).orElseThrow().equals(p.getUUID());
    }
    private static void dump(ServerPlayer p,int radius){
        var mobs=p.serverLevel().getEntitiesOfClass(Mob.class,p.getBoundingBox().inflate(radius),
            mob->ordinary(mob,p)&&mob.distanceToSqr(p)<=radius*radius
                &&(mob.getTarget()==p||attackMemory(mob,p)||angryAt(mob,p)||mob.getLastHurtByMob()==p
                   ||mob instanceof NeutralMob n&&p.getUUID().equals(n.getPersistentAngerTarget())));
        for(var mob:mobs){
            if(mob.getTarget()==p){mob.setTarget(null);mob.getNavigation().stop();}
            var brain=mob.getBrain();
            if(attackMemory(mob,p))brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            if(angryAt(mob,p))brain.eraseMemory(MemoryModuleType.ANGRY_AT);
            if(mob.getLastHurtByMob()==p)mob.setLastHurtByMob(null);
            if(mob instanceof NeutralMob n&&p.getUUID().equals(n.getPersistentAngerTarget())){
                n.setPersistentAngerTarget(null);n.setRemainingPersistentAngerTime(0);
            }
        }
    }
    private static void targetChange(LivingChangeTargetEvent event){
        if(event.getEntity() instanceof Mob mob&&event.getNewAboutToBeSetTarget() instanceof ServerPlayer p&&hidesFrom(mob,p))
            event.setNewAboutToBeSetTarget(null);
    }
    private static void tick(PlayerTickEvent.Pre e){
        if(!(e.getEntity() instanceof ServerPlayer p)||!ACTIVE.containsKey(p))return;
        if(!active(p)){end(p);return;}
        if(p.tickCount%5==0)dump(p,radius(p));
    }
    private static void login(PlayerEvent.PlayerLoggedInEvent e){if(e.getEntity() instanceof ServerPlayer p)end(p);}
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p)end(p);}
    private static void respawn(PlayerEvent.PlayerRespawnEvent e){if(e.getEntity() instanceof ServerPlayer p)end(p);}
    private static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)end(p);}
    private static void stop(net.neoforged.neoforge.event.server.ServerStoppedEvent e){ACTIVE.clear();}
}
