package io.github.heatwaveqc.tyr.progression.runtime.resources;

import io.github.heatwaveqc.tyr.progression.core.ProgressionApi;
import io.github.heatwaveqc.tyr.progression.data.PathRegistry;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.runtime.NewbieMode;
import io.github.heatwaveqc.tyr.progression.state.ProgressionAttachments;
import io.github.heatwaveqc.tyr.progression.network.PathResourceNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import java.util.*;

/** Only trusted committed server operations can change a personal meter. */
public final class PathResources {
    private PathResources(){}
    private static final Map<UUID,List<PathResourceNetwork.Meter>> LAST=new HashMap<>();
    public static void register(){
        NeoForge.EVENT_BUS.addListener(PathResources::tick);
        NeoForge.EVENT_BUS.addListener(PathResources::clonePlayer);
        NeoForge.EVENT_BUS.addListener(PathResources::login);
        NeoForge.EVENT_BUS.addListener(PathResources::respawn);
        NeoForge.EVENT_BUS.addListener(PathResources::dimension);
        NeoForge.EVENT_BUS.addListener(PathResources::logout);
    }
    private static boolean actor(ServerPlayer p){return MxpPlayerPolicy.eligible(p)&&p.isAlive()&&!NewbieMode.isNewbie(p);}
    private static boolean joined(ServerPlayer p,ResourceCatalog.Spec s){
        return PathRegistry.get(s.path())!=null&&ProgressionApi.track(p,s.path()).joined();
    }
    private static boolean active(ServerPlayer p,ResourceCatalog.Spec s){
        return joined(p,s)&&PathRegistry.eligible(p,s.path());
    }
    private static int capacity(ServerPlayer p,ResourceCatalog.Spec s){
        int rank=s.capacitySkill().isEmpty()?0:ProgressionApi.track(p,s.path()).skillRank(s.capacitySkill());
        return s.capacity(rank);
    }
    private static ResourceState state(ServerPlayer p){return p.getData(ProgressionAttachments.PATH_RESOURCES.get());}
    private static int value(ServerPlayer p,ResourceCatalog.Spec s){
        return ResourcePolicy.clamp(state(p).balances().getOrDefault(s.id(),s.initial()),capacity(p,s));
    }
    /** Read-only condition. Only membership (not recursive eligibility) is evaluated here. */
    public static OptionalInt value(ServerPlayer p,String id){
        var s=ResourceCatalog.current().get(id);
        return s==null||NewbieMode.isNewbie(p)||!joined(p,s)?OptionalInt.empty():OptionalInt.of(value(p,s));
    }
    /** Operator read/commit surface, separate from gameplay reward receipts. */
    public record AdminSnapshot(String id,int value,int capacity,int initial,Integer stored,boolean joined,boolean active){}
    public static AdminSnapshot adminSnapshot(ServerPlayer p,String id){
        var s=ResourceCatalog.current().get(id);
        if(s==null)throw new IllegalArgumentException("Unknown configured resource "+id);
        return new AdminSnapshot(id,value(p,s),capacity(p,s),s.initial(),
            state(p).balances().get(id),joined(p,s),active(p,s));
    }
    public static void adminSet(ServerPlayer p,AdminSnapshot expected,int amount){
        if(p.getServer()==null||!p.getServer().isSameThread())throw new IllegalStateException("Server thread required");
        var current=adminSnapshot(p,expected.id());
        if(!current.equals(expected))throw new IllegalStateException("Resource state changed");
        if(!current.joined())throw new IllegalArgumentException("Resource owner path not joined");
        if(amount<0||amount>current.capacity())throw new IllegalArgumentException("Resource outside current capacity");
        if(!settle(p,List.of(new ResourcePolicy.Change(current.id(),current.initial(),current.capacity(),
                ResourcePolicy.Operation.SET,amount)),null))
            throw new IllegalStateException("Resource edit rejected");
        sync(p,true);
    }
    public static void adminRefresh(ServerPlayer p){sync(p,true);}
    private static ResourcePolicy.Change change(ServerPlayer p,ResourceCatalog.Spec s,ResourceCatalog.Rule r,int units){
        return new ResourcePolicy.Change(s.id(),s.initial(),capacity(p,s),r.operation(),
            (long)r.amount()+(long)r.perUnit()*units);
    }
    /** Event must come from a reviewed successful native/server adapter. The source
     * supplies integer event units and a stable operation identity; no client packet
     * accepts either. All matching rule conditions read the pre-event state.
     * Recent dedup is bounded, not an indefinite replay guarantee. */
    public static boolean committed(ServerPlayer p,String event,String transaction,int units){
        if(!actor(p)||!ResourcePolicy.validId(event)||transaction==null||transaction.isBlank()
                ||transaction.length()>180||units<0||units>ResourcePolicy.MAX)return false;
        String receipt=event+"|"+transaction;
        var changes=new ArrayList<ResourcePolicy.Change>();
        for(var s:sorted()){
            if(!active(p,s))continue;
            for(var r:s.rules())
                if(r.event().equals(event)&&r.when().test(p))changes.add(change(p,s,r,units));
        }
        return settle(p,changes,receipt);
    }
    /** Trusted direct spend for a reviewed atomic engine action. Caller must
     * establish its world-action preconditions; no arbitrary world action here. */
    public static boolean spend(ServerPlayer p,String id,int amount,String transaction){
        var s=ResourceCatalog.current().get(id);
        if(!actor(p)||s==null||!active(p,s)||amount<1||amount>ResourcePolicy.MAX
                ||transaction==null||transaction.isBlank()||transaction.length()>180)return false;
        return settle(p,List.of(new ResourcePolicy.Change(id,s.initial(),capacity(p,s),
            ResourcePolicy.Operation.SPEND,amount)),"spend|"+id+"|"+transaction);
    }
    private static boolean settle(ServerPlayer p,List<ResourcePolicy.Change> changes,String receipt){
        var before=state(p);
        var result=before.settle(changes,receipt);
        if(result.isEmpty())return false;
        if(result.get()!=before)p.setData(ProgressionAttachments.PATH_RESOURCES.get(),result.get());
        return true;
    }
    private static List<ResourceCatalog.Spec> sorted(){
        return ResourceCatalog.current().values().stream().sorted(Comparator.comparing(ResourceCatalog.Spec::id)).toList();
    }
    private static void tick(PlayerTickEvent.Post event){
        if(!(event.getEntity() instanceof ServerPlayer p))return;
        if(ResourceCatalog.current().isEmpty()){
            if(LAST.containsKey(p.getUUID()))sync(p,false);
            return;
        }
        if(actor(p)){
            long now=p.server.overworld().getGameTime();
            for(var s:sorted()){
                if(!active(p,s))continue;
                int cap=capacity(p,s);
                // Discard excess when a capacity upgrade is lost; dormant pools stay saved.
                Integer stored=state(p).balances().get(s.id());
                if(stored!=null&&stored>cap)
                    settle(p,List.of(new ResourcePolicy.Change(s.id(),s.initial(),cap,ResourcePolicy.Operation.SET,cap)),null);
                if(now%s.intervalTicks()!=0)continue;
                var changes=new ArrayList<ResourcePolicy.Change>();
                for(var r:s.rules())if(r.event().equals("interval")&&r.when().test(p))changes.add(change(p,s,r,0));
                settle(p,changes,null);
            }
        }
        if(p.tickCount%5==0)sync(p,false);
    }
    private static void sync(ServerPlayer p,boolean force){
        var meters=new ArrayList<PathResourceNetwork.Meter>();
        if(actor(p))for(var s:sorted()){
            if(!s.hud()||!active(p,s))continue;
            meters.add(new PathResourceNetwork.Meter(s.title(),value(p,s),capacity(p,s),s.color(),s.divisor()));
            if(meters.size()==PathResourceNetwork.MAX_METERS)break;
        }
        var next=List.copyOf(meters);
        var previous=LAST.put(p.getUUID(),next);
        if(force||!next.equals(previous)||(!next.isEmpty()&&p.tickCount%20==0))
            PathResourceNetwork.send(p,next);
    }
    private static void clonePlayer(PlayerEvent.Clone event){
        if(!(event.getEntity() instanceof ServerPlayer p)||!(event.getOriginal() instanceof ServerPlayer old))return;
        var prior=state(old);
        if(event.isWasDeath()){
            var reset=new HashSet<String>();
            ResourceCatalog.current().values().forEach(s->{if(s.resetOnDeath())reset.add(s.id());});
            prior=prior.reset(reset);
        }
        p.setData(ProgressionAttachments.PATH_RESOURCES.get(),prior);
        LAST.remove(p.getUUID());
    }
    private static void login(PlayerEvent.PlayerLoggedInEvent e){if(e.getEntity() instanceof ServerPlayer p)sync(p,true);}
    private static void respawn(PlayerEvent.PlayerRespawnEvent e){if(e.getEntity() instanceof ServerPlayer p)sync(p,true);}
    private static void dimension(PlayerEvent.PlayerChangedDimensionEvent e){if(e.getEntity() instanceof ServerPlayer p)sync(p,true);}
    private static void logout(PlayerEvent.PlayerLoggedOutEvent e){LAST.remove(e.getEntity().getUUID());}
}
