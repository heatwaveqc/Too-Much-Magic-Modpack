package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.foundation.FlyingKickMath;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import java.util.Comparator;
import java.util.function.BooleanSupplier;

/** A scoped native attack conversion, not an additional attack packet or cooldown. */
public final class FlyingKick {
    private FlyingKick(){}
    private static final ThreadLocal<Attempt> CURRENT=new ThreadLocal<>();
    private static final class Attempt {
        final ServerPlayer player;
        final LivingEntity primary;
        EffectRuntime.Active selected;
        Vec3 impact;
        float raw;
        boolean hurtSeen, committed;
        Attempt(ServerPlayer player,LivingEntity primary){this.player=player;this.primary=primary;}
    }
    public static void register(){NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST,FlyingKick::critical);}
    private static boolean actor(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
            &&!(p instanceof FakePlayer)&&p.getMainHandItem().isEmpty();
    }
    /** Scope lasts exactly one native Player.attack invocation, even on exception. */
    public static void attack(Player player,Entity target,Runnable original){
        Attempt outer=CURRENT.get();
        if(outer!=null){
            CURRENT.remove();
            try{original.run();}finally{CURRENT.set(outer);}
            return;
        }
        if(!(player instanceof ServerPlayer p)||!(target instanceof LivingEntity living)||!actor(p)){
            original.run();return;
        }
        Attempt a=new Attempt(p,living);
        CURRENT.set(a);
        try{
            original.run();
            if(a.committed&&actor(p)&&p.level()==living.level())release(a);
        }finally{CURRENT.remove();}
    }
    private static double radius(EffectRuntime.Active a){
        return FlyingKickMath.radius(MechanicalSpec.number(a.effect().params(),"base_radius",0,0,16),
            a.effect().amountPerRank(),a.rank());
    }
    private static boolean clearBelow(ServerPlayer p,double blocks){
        var b=p.getBoundingBox();
        // Only real collision geometry under the feet; not fall distance, entities or a ray through air.
        var below=new AABB(b.minX,b.minY-blocks+1e-5,b.minZ,b.maxX,b.minY-1e-5,b.maxZ);
        return !p.level().getBlockCollisions(p,below).iterator().hasNext();
    }
    private static void critical(CriticalHitEvent event){
        Attempt a=CURRENT.get();
        if(a==null||a.selected!=null||event.getEntity()!=a.player||event.getTarget()!=a.primary
            ||!actor(a.player)||!a.primary.isAlive()||!event.isVanillaCritical()||!event.isCriticalHit())return;
        // Respect earlier mod denial; do not force an airborne crit into existence.
        a.selected=EffectRuntime.active(a.player).stream().filter(x->x.effect().type().equals("flying_kick")
            &&Selectors.entity(x.effect().selector(),a.primary)&&MechanicalEffects.areaAllowed(a.player,a.primary)
            &&FlyingKickMath.eligible(event.isVanillaCritical(),event.isCriticalHit(),a.player.getDeltaMovement().y,
                clearBelow(a.player,MechanicalSpec.number(x.effect().params(),"min_clearance",1,1,4)))
            &&radius(x)>0).max(Comparator.comparingDouble(FlyingKick::radius).thenComparing(EffectRuntime.Active::id)).orElse(null);
        if(a.selected==null)return;
        event.setDamageMultiplier(1.0F);
        event.setDisableSweep(true);
    }
    /** Wrap only the original target.hurt call; capture unmodified native damage, never reapply modified damage. */
    public static boolean primary(Entity target,DamageSource source,float amount,BooleanSupplier original){
        Attempt a=CURRENT.get();
        if(a==null||a.selected==null||a.hurtSeen||target!=a.primary||source.getEntity()!=a.player
            ||source.getDirectEntity()!=a.player||!source.is(DamageTypes.PLAYER_ATTACK))return original.getAsBoolean();
        a.hurtSeen=true;
        a.impact=a.primary.position();
        a.raw=amount;
        float health=a.primary.getHealth(),absorption=a.primary.getAbsorptionAmount();
        boolean accepted=original.getAsBoolean();
        a.committed=accepted&&Float.isFinite(amount)&&amount>0
            &&(a.primary.getHealth()<health||a.primary.getAbsorptionAmount()<absorption);
        return accepted;
    }
    private static boolean secondary(Attempt a,LivingEntity t,double radius){
        ServerPlayer p=a.player;
        return t!=p&&t!=a.primary&&t.isAlive()&&!(t instanceof Player)
            &&!(t instanceof TamableAnimal animal&&animal.isTame())
            &&!(t instanceof OwnableEntity own&&own.getOwnerUUID()!=null)
            &&!p.isAlliedTo(t)&&!t.isAlliedTo(p)
            &&Selectors.entity(a.selected.effect().selector(),t)
            &&FlyingKickMath.inRadius(t.position().distanceToSqr(a.impact),radius)
            &&MechanicalEffects.areaAllowed(p,t)&&p.hasLineOfSight(t);
    }
    private static void release(Attempt a){
        var p=a.player;var level=p.serverLevel();double r=radius(a.selected);
        var center=a.impact;
        var box=new AABB(center.x-r,center.y-r,center.z-r,center.x+r,center.y+r,center.z+r);
        var targets=level.getEntitiesOfClass(LivingEntity.class,box,t->secondary(a,t,r));
        // Each victim receives exactly one ordinary player-attack pipeline; no Player.attack recursion.
        for(var t:targets){
            if(!actor(p)||p.level()!=level)break;
            if(secondary(a,t,r))t.hurt(p.damageSources().playerAttack(p),a.raw);
        }
        level.playSound(null,center.x,center.y,center.z,SoundEvents.PLAYER_ATTACK_SWEEP,SoundSource.PLAYERS,1F,0.85F);
        for(int i=0;i<16;i++){
            double angle=i*Math.PI/8;
            level.sendParticles(ParticleTypes.POOF,center.x+Math.cos(angle)*r,center.y+0.25,center.z+Math.sin(angle)*r,1,0,0,0,0);
        }
    }
}
