package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.core.ProgressionMath;
import io.github.heatwaveqc.tyr.progression.core.Signal;
import io.github.heatwaveqc.tyr.progression.data.HeightFallTrialSpec;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.common.util.FakePlayer;
import java.util.*;

/** Scoped native landing guard. No invulnerability flag, timer or teleport. */
public final class HeightFallTrial {
    private HeightFallTrial(){}
    private record Qualified(String path,HeightFallTrialSpec spec){}
    private record Landing(ServerPlayer player){}
    private static final ThreadLocal<Landing> LANDING=new ThreadLocal<>();
    private static boolean actor(ServerPlayer p){
        return p.isAlive()&&!(p instanceof FakePlayer)&&!p.isCreative()&&!p.isSpectator()
            &&!p.isPassenger()&&!p.isFallFlying()&&!p.getAbilities().flying&&!NewbieMode.isNewbie(p)
            &&p.getServer()!=null&&p.getServer().isSameThread()
            &&p.getServer().getPlayerList().getPlayer(p.getUUID())==p;
    }
    private static List<Qualified> qualify(ServerPlayer p,Vec3 start,double dy,boolean onGround,
                                            BlockState state,BlockPos pos){
        if(PathRegistry.heightFallTrials().isEmpty()||!onGround||start==null||!actor(p))return List.of();
        var level=p.serverLevel();
        if(!level.hasChunkAt(pos)||state.isAir()||state.getCollisionShape(level,pos).isEmpty()
            ||Math.abs(p.getY()-pos.getY())>2
            ||level.noCollision(p,p.getBoundingBox().move(0,-0.125,0)))return List.of();
        var results=new ArrayList<Qualified>();
        for(var entry:new TreeMap<>(PathRegistry.heightFallTrials()).entrySet()){
            String id=entry.getKey();
            var path=PathRegistry.get(id);
            if(path==null||PathRegistry.channel(id)==null||!PathRegistry.eligible(p,id))continue;
            var track=ProgressionApi.track(p,id);
            if(!track.joined())continue;
            for(var spec:entry.getValue()){
                var milestone=path.milestones().stream().filter(m->m.id().equals(spec.milestone())).findFirst().orElse(null);
                if(milestone==null||track.completed(milestone.id())||!ProgressionMath.canCollectMilestone(milestone,track))continue;
                if(spec.qualifies(level.dimension().location().toString(),start.y,p.getY(),p.fallDistance,dy)){
                    if(!spec.advancement().isEmpty()){
                        var advancement=p.getServer().getAdvancements().get(ResourceLocation.parse(spec.advancement()));
                        if(advancement==null||!advancement.value().criteria().containsKey(spec.advancementCriterion())){
                            ProgressionMod.LOGGER.warn("Height fall trial {} missing configured advancement criterion",spec.milestone());
                            continue;
                        }
                    }
                    results.add(new Qualified(id,spec));
                }
            }
        }
        return List.copyOf(results);
    }
    /** Runs around ServerPlayer.checkFallDamage only, so unrelated damage cannot use this guard. */
    public static void aroundLanding(ServerPlayer p,Vec3 start,double dy,boolean onGround,
                                     BlockState state,BlockPos pos,Runnable original){
        if(LANDING.get()!=null){original.run();return;}
        var trials=qualify(p,start,dy,onGround,state,pos);
        if(trials.isEmpty()){original.run();return;}
        var level=p.serverLevel();
        var at=p.position();
        LANDING.set(new Landing(p));
        try{original.run();}finally{LANDING.remove();}
        // The native landing completed without changing dimension or relocating.
        if(!actor(p)||p.serverLevel()!=level||p.position().distanceToSqr(at)>0.25)return;
        for(var trial:trials){
            var spec=trial.spec();
            ProgressionApi.recordFiltered(p,new Signal("height_fall_trial",spec.milestone(),Set.of()),
                path->path.id().equals(trial.path()));
            if(!ProgressionApi.track(p,trial.path()).completed(spec.milestone()))continue;
            // Vanilla can already have awarded this criterion; award is idempotent.
            // Never use the historical advancement receipt as proof of this trial.
            if(!spec.advancement().isEmpty()){
                var advancement=p.getServer().getAdvancements().get(ResourceLocation.parse(spec.advancement()));
                if(advancement!=null&&advancement.value().criteria().containsKey(spec.advancementCriterion()))
                    p.getAdvancements().award(advancement,spec.advancementCriterion());
            }
        }
    }
    public static void protectLanding(LivingIncomingDamageEvent event){
        var scope=LANDING.get();
        if(scope!=null&&event.getEntity()==scope.player()&&event.getSource().is(DamageTypes.FALL)
            &&event.getSource().getEntity()==null&&event.getSource().getDirectEntity()==null)
            event.setCanceled(true);
    }
}
