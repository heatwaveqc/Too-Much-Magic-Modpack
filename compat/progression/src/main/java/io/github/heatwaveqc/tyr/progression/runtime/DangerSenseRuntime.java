package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.foundation.MartialAwarenessMath;
import io.github.heatwaveqc.tyr.progression.network.DangerSenseNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import java.util.*;

/** Personal, server-observed targeting; no global mob effect or scoreboard edits. */
public final class DangerSenseRuntime {
    private DangerSenseRuntime(){}
    private static final Map<ServerPlayer,MartialAwarenessMath.ThreatState> LAST=new WeakHashMap<>();
    public static void register(){
        NeoForge.EVENT_BUS.addListener(DangerSenseRuntime::tick);
        NeoForge.EVENT_BUS.addListener(DangerSenseRuntime::dimension);
        NeoForge.EVENT_BUS.addListener(DangerSenseRuntime::respawn);
        NeoForge.EVENT_BUS.addListener(DangerSenseRuntime::logout);
        NeoForge.EVENT_BUS.addListener(DangerSenseRuntime::stop);
    }
    private static boolean actor(ServerPlayer p){
        return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
            &&!(p instanceof net.neoforged.neoforge.common.util.FakePlayer);
    }
    private static boolean targeting(Mob mob,ServerPlayer p){
        if(!mob.isAlive()||mob.isAlliedTo(p))return false;
        if(mob.getTarget()==p)return true;
        var brain=mob.getBrain();
        return brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)
            &&brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null)==p;
    }
    private record Rule(String selector,double radius,int cooldown){}
    private static void tick(PlayerTickEvent.Post e){
        if(!(e.getEntity() instanceof ServerPlayer p)||p.tickCount%5!=0)return;
        var rules=new ArrayList<Rule>();
        if(actor(p))for(var a:EffectRuntime.active(p)){
            if(!a.effect().type().equals("danger_sense"))continue;
            rules.add(new Rule(a.effect().selector(),MechanicalSpec.scaled(a.effect().amountPerRank(),a.rank(),64),
                MechanicalSpec.integer(a.effect().params(),"alert_cooldown_ticks",100,20,1200)));
        }
        var prior=LAST.get(p);
        if(rules.isEmpty()){
            if(prior!=null){DangerSenseNetwork.send(p,Set.of(),false);LAST.remove(p);}
            return;
        }
        double radius=rules.stream().mapToDouble(Rule::radius).max().orElse(0);
        int cooldown=rules.stream().mapToInt(Rule::cooldown).max().orElse(100);
        var found=p.serverLevel().getEntitiesOfClass(Mob.class,p.getBoundingBox().inflate(radius),
            mob->targeting(mob,p)&&rules.stream().anyMatch(rule->mob.distanceToSqr(p)<=rule.radius()*rule.radius()
                &&Selectors.entity(rule.selector(),mob)));
        var ids=new LinkedHashSet<UUID>();
        found.stream().sorted(Comparator.comparingDouble(mob->mob.distanceToSqr(p)))
            .limit(DangerSenseNetwork.MAX_TARGETS).forEach(mob->ids.add(mob.getUUID()));
        var transition=MartialAwarenessMath.observe(prior==null?MartialAwarenessMath.ThreatState.empty():prior,
            ids,p.getServer().overworld().getGameTime(),cooldown);
        LAST.put(p,transition.state());
        if(prior==null||!prior.targets().equals(ids)||transition.alert()||p.tickCount%20==0)
            DangerSenseNetwork.send(p,ids,transition.alert());
    }
    private static void clear(ServerPlayer p){
        var prior=LAST.remove(p);if(prior!=null)DangerSenseNetwork.send(p,Set.of(),false);
    }
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p)clear(p);}
    private static void respawn(PlayerEvent.PlayerRespawnEvent e){if(e.getEntity() instanceof ServerPlayer p)clear(p);}
    private static void logout(PlayerEvent.PlayerLoggedOutEvent e){if(e.getEntity() instanceof ServerPlayer p)LAST.remove(p);}
    private static void stop(net.neoforged.neoforge.event.server.ServerStoppedEvent e){LAST.clear();}
}
