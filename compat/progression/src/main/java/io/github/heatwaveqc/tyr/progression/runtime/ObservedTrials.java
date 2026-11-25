package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.*;
import io.github.heatwaveqc.tyr.progression.data.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import java.util.*;

/** Transient observations are scoped to a continuously eligible server actor and trial. */
public final class ObservedTrials {
    private ObservedTrials(){}
    private record Key(String path,String milestone){}
    private record ArrowSample(double previous,double closest,long tick){}
    private static final class Session{
        final ObservedTrialSpec spec;
        final Object catalog;
        long tick=Long.MIN_VALUE;
        Vec3 previous;
        int duration;
        double distance;
        final Map<UUID,ArrowSample> arrows=new HashMap<>();
        Session(ObservedTrialSpec spec,Object catalog){this.spec=spec;this.catalog=catalog;}
    }
    private static final Map<UUID,Map<Key,Session>> ACTIVE=new HashMap<>();
    private static boolean active(ServerPlayer p,String path,ObservedTrialSpec s){
        if(!TrainingRewards.eligible(p,path)||!p.serverLevel().dimension().location().toString().equals(s.dimension()))return false;
        var def=PathRegistry.get(path);
        if(def==null||PathRegistry.channel(path)==null)return false;
        var track=ProgressionApi.track(p,path);
        var m=def.milestones().stream().filter(x->x.id().equals(s.milestone())).findFirst().orElse(null);
        return m!=null&&!track.completed(m.id())&&ProgressionMath.canCollectMilestone(m,track);
    }
    public static void tick(ServerPlayer p){
        var catalog=PathRegistry.observedTrials();
        if(catalog.isEmpty()){forget(p);return;}
        var sessions=ACTIVE.computeIfAbsent(p.getUUID(),k->new HashMap<>());
        var eligible=new HashSet<Key>();
        // Take eligibility snapshot before any signal completes a prerequisite.
        for(var row:catalog.entrySet())for(var spec:row.getValue()){
            if(active(p,row.getKey(),spec))eligible.add(new Key(row.getKey(),spec.milestone()));
        }
        sessions.keySet().retainAll(eligible);
        for(var row:catalog.entrySet())for(var spec:row.getValue()){
            var key=new Key(row.getKey(),spec.milestone());
            if(!eligible.contains(key))continue;
            long tick=p.serverLevel().getGameTime();
            var session=sessions.get(key);
            if(session==null||session.catalog!=catalog||!session.spec.equals(spec)||session.tick!=tick-1){
                session=new Session(spec,catalog);sessions.put(key,session);
            }
            session.tick=tick;
            boolean complete=switch(spec.kind()){
                case "underwater" -> timed(session,p.isUnderWater()&&!p.isPassenger(),spec.ticks());
                case "moon_boat" -> timed(session,moon(p,spec.lookCosine()),spec.ticks());
                case "fire_walk" -> fire(p,session);
                case "arrow_dodge" -> {arrows(p,key,session);yield false;}
                default -> false;
            };
            if(complete){
                ProgressionApi.recordFiltered(p,new Signal("observed_trial",spec.milestone(),Set.of()),
                    path->path.id().equals(key.path()));
                if(spec.kind().equals("underwater")&&ProgressionApi.track(p,key.path()).completed(spec.milestone()))
                    p.setAirSupply(p.getMaxAirSupply());
                sessions.remove(key);
            }
        }
        if(sessions.isEmpty())ACTIVE.remove(p.getUUID());
    }
    private static boolean timed(Session s,boolean condition,int target){
        s.duration=condition?s.duration+1:0;
        return s.duration>=target;
    }
    private static boolean moon(ServerPlayer p,double cosine){
        long time=Math.floorMod(p.level().getDayTime(),24000L);
        if(!(p.getVehicle() instanceof Boat)||time<13000||time>23000
            ||p.level().isRaining()||!p.level().canSeeSky(p.blockPosition().above()))return false;
        double angle=p.level().getTimeOfDay(1f)*2*Math.PI;
        Vec3 moon=new Vec3(Math.sin(angle),-Math.cos(angle),0);
        return p.getLookAngle().dot(moon)>=cosine;
    }
    private static boolean fire(ServerPlayer p,Session s){
        Vec3 pos=p.position(),prior=s.previous;
        boolean valid=p.onGround()&&!p.isPassenger()&&!p.isFallFlying()&&!p.getAbilities().flying
            &&p.serverLevel().getBlockState(p.blockPosition()).is(BlockTags.FIRE);
        s.previous=valid?pos:null;
        if(!valid||prior==null){s.distance=0;return false;}
        double dy=Math.abs(pos.y-prior.y),dx=pos.x-prior.x,dz=pos.z-prior.z;
        double d=Math.hypot(dx,dz);
        if(d>1.25||dy>0.6){s.distance=0;return false;}
        s.distance+=d;
        return s.distance>=s.spec.distance();
    }
    private static void arrows(ServerPlayer p,Key key,Session s){
        var center=p.getBoundingBox().getCenter();
        var nearby=p.serverLevel().getEntitiesOfClass(AbstractArrow.class,p.getBoundingBox().inflate(8),
            a->a.isAlive()&&!(a instanceof ThrownTrident)&&a.getOwner() instanceof Mob owner
                &&Selectors.entity(s.spec.shooter(),owner));
        if(nearby.size()>128){s.arrows.clear();return;}
        var seen=new HashSet<UUID>();
        for(var a:nearby){
            UUID id=a.getUUID();seen.add(id);
            Vec3 offset=center.subtract(a.position()),velocity=a.getDeltaMovement();
            double distance=offset.length(),speed=velocity.length();
            var old=s.arrows.get(id);
            if(speed<0.05){s.arrows.remove(id);continue;}
            double approach=offset.dot(velocity.scale(1/speed));
            if(old==null){
                // Only initially aimed, actually approaching projectiles from a mob targeting this player.
                if(a.getOwner() instanceof Mob owner&&owner.getTarget()==p&&approach>0
                    &&offset.subtract(velocity.scale(approach/speed)).length()<=s.spec.radius())
                    s.arrows.put(id,new ArrowSample(distance,distance,s.tick));
                continue;
            }
            double closest=Math.min(old.closest(),distance);
            if(old.tick()!=s.tick-1){s.arrows.remove(id);continue;}
            if(distance>old.previous()+0.05&&approach<0&&closest<=s.spec.radius()){
                s.arrows.remove(id);
                ProgressionApi.recordFiltered(p,new Signal("arrow_dodged",key.milestone(),Set.of(),id),
                    path->path.id().equals(key.path()));
            }else s.arrows.put(id,new ArrowSample(distance,closest,s.tick));
        }
        s.arrows.keySet().retainAll(seen);
    }
    /** Reset on actual damaging arrow impact, not a client-reported hit or canceled damage. */
    public static void hit(LivingDamageEvent.Post event){
        if(!(event.getEntity() instanceof ServerPlayer p)||event.getNewDamage()<=0
            ||!(event.getSource().getDirectEntity() instanceof AbstractArrow arrow)
            ||arrow instanceof ThrownTrident||!(arrow.getOwner() instanceof Mob owner))return;
        var sessions=ACTIVE.getOrDefault(p.getUUID(),Map.of());
        for(var path:PathRegistry.observedTrials().entrySet())for(var spec:path.getValue()){
            if(!spec.kind().equals("arrow_dodge")||!Selectors.entity(spec.shooter(),owner)
                ||!active(p,path.getKey(),spec))continue;
            var s=sessions.get(new Key(path.getKey(),spec.milestone()));
            if(s!=null)s.arrows.clear();
            ProgressionApi.resetArrowTrial(p,path.getKey(),spec.milestone());
        }
    }
    public static void forget(ServerPlayer p){ACTIVE.remove(p.getUUID());}
}
