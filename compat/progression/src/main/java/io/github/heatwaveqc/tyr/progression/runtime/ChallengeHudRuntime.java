package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.ChallengeHudSpec;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.network.ChallengeHudNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import java.util.*;

/** Personal read-only challenge projection, refreshed on changes and heartbeat. */
public final class ChallengeHudRuntime {
    private ChallengeHudRuntime(){}
    private static final Map<UUID,List<ChallengeHudSpec.Row>> LAST=new HashMap<>();
    public static void register(){
        NeoForge.EVENT_BUS.addListener(ChallengeHudRuntime::tick);
        NeoForge.EVENT_BUS.addListener(ChallengeHudRuntime::login);
        NeoForge.EVENT_BUS.addListener(ChallengeHudRuntime::respawn);
        NeoForge.EVENT_BUS.addListener(ChallengeHudRuntime::dimension);
        NeoForge.EVENT_BUS.addListener(ChallengeHudRuntime::logout);
        NeoForge.EVENT_BUS.addListener(ChallengeHudRuntime::stopped);
    }
    private static void tick(PlayerTickEvent.Post e){
        if(e.getEntity() instanceof ServerPlayer p&&p.tickCount%5==0)sync(p,false);
    }
    private static void sync(ServerPlayer p,boolean force){
        var catalog=PathRegistry.challengeHuds();
        if(catalog.isEmpty()&&!LAST.containsKey(p.getUUID()))return;
        var rows=new ArrayList<ChallengeHudSpec.Row>();
        if(p.isAlive()&&!p.isSpectator()&&!p.isCreative()&&!NewbieMode.isNewbie(p)){
            for(var entry:new TreeMap<>(catalog).entrySet()){
                var path=PathRegistry.get(entry.getKey());
                if(path==null||!PathRegistry.eligible(p,path.id()))continue;
                var track=ProgressionApi.track(p,path.id());
                for(var row:ChallengeHudSpec.select(path,track,entry.getValue())){
                    if(rows.size()==ChallengeHudSpec.MAX_ROWS)break;
                    rows.add(row);
                }
                if(rows.size()==ChallengeHudSpec.MAX_ROWS)break;
            }
        }
        var next=List.copyOf(rows);
        var previous=LAST.put(p.getUUID(),next);
        if(force||!next.equals(previous)||(!next.isEmpty()&&p.tickCount%20==0))
            ChallengeHudNetwork.send(p,next);
        if(catalog.isEmpty())LAST.remove(p.getUUID());
    }
    private static void login(PlayerEvent.PlayerLoggedInEvent e){if(e.getEntity() instanceof ServerPlayer p)sync(p,true);}
    private static void respawn(PlayerEvent.PlayerRespawnEvent e){if(e.getEntity() instanceof ServerPlayer p)sync(p,true);}
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p)sync(p,true);}
    private static void logout(PlayerEvent.PlayerLoggedOutEvent e){LAST.remove(e.getEntity().getUUID());}
    private static void stopped(ServerStoppedEvent e){LAST.clear();}
}
