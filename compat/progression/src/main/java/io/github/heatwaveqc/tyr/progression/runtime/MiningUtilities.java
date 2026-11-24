package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.foundation.BoundedVein;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/** Server-owned mining/scan. Native block destruction is the only secondary break path. */
public final class MiningUtilities {
    private MiningUtilities(){}
    @FunctionalInterface public interface Guard { boolean permits(ServerPlayer player,BlockPos pos,boolean reveal); }
    private static final List<Guard> GUARDS=new CopyOnWriteArrayList<>();
    public static void registerGuard(Guard guard){GUARDS.add(Objects.requireNonNull(guard));}
    private record Receipt(ServerLevel level,BlockPos pos,BlockState state,ItemStack hand,String effect,long tick,BlockDropsEvent event){}
    private static final Map<UUID,Receipt> PENDING=new HashMap<>();
    private static final Map<UUID,Long> NEXT=new HashMap<>();
    private static final Set<UUID> RUNNING=new HashSet<>();
    private static final ThreadLocal<NativeBreak> NATIVE=new ThreadLocal<>();
    private static final class NativeBreak {
        final ServerPlayer player;final BlockPos pos;final ItemStack hand;
        Receipt receipt;boolean bulk;
        NativeBreak(ServerPlayer p,BlockPos pos){player=p;this.pos=pos.immutable();hand=p.getMainHandItem();}
    }
    private static boolean usable(ServerPlayer p){return p.isAlive()&&!p.isCreative()&&!p.isSpectator()&&!NewbieMode.isNewbie(p)
        &&p.getServer()!=null&&p.getServer().getPlayerList().getPlayer(p.getUUID())==p;}
    private static boolean allowed(ServerPlayer p,ServerLevel level,BlockPos pos,boolean reveal){
        if(!level.hasChunkAt(pos)||level.isOutsideBuildHeight(pos)||!level.getWorldBorder().isWithinBounds(pos)||!level.mayInteract(p,pos))return false;
        for(var guard:GUARDS)try{if(!guard.permits(p,pos,reveal))return false;}catch(RuntimeException|LinkageError bad){return false;}
        return true;
    }
    private static boolean eligible(EffectRuntime.Active a,ServerPlayer p,BlockState state,ItemStack hand){
        var fx=a.effect();
        return fx.type().equals("vein_mining")&&state.is(Tags.Blocks.ORES)&&Selectors.block(fx.selector(),state)
            &&(fx.emptyHand()?hand.isEmpty():!hand.isEmpty()&&Selectors.item(fx.weapon(),hand))
            &&(!MechanicalSpec.bool(fx.params(),"sneak_only",true)||p.isCrouching());
    }
    /** Own only the outer native player break, never a nested modded bulk break or a simulated drop. */
    public static boolean nativeBreak(ServerPlayer p,BlockPos pos,java.util.function.BooleanSupplier original){
        if(RUNNING.contains(p.getUUID())||!usable(p))return original.getAsBoolean();
        NativeBreak prior=NATIVE.get();
        if(prior!=null){
            if(prior.player==p)prior.bulk=true;
            return original.getAsBoolean();
        }
        var level=p.serverLevel();
        if(!level.hasChunkAt(pos)||!level.getBlockState(pos).is(Tags.Blocks.ORES)
            ||EffectRuntime.active(p).stream().noneMatch(a->eligible(a,p,level.getBlockState(pos),p.getMainHandItem())&&a.effect().amountPerRank()>0))
            return original.getAsBoolean();
        var root=new NativeBreak(p,pos);NATIVE.set(root);
        try{
            boolean result=original.getAsBoolean();
            if(result&&!root.bulk&&root.receipt!=null&&!root.receipt.event().isCanceled()
                &&p.serverLevel()==level&&level.hasChunkAt(pos)&&level.getBlockState(pos).getBlock()!=root.receipt.state().getBlock())
                PENDING.putIfAbsent(p.getUUID(),root.receipt);
            return result;
        }finally{NATIVE.remove();}
    }
    public static void observe(BlockDropsEvent event){
        if(event.isCanceled()||!(event.getBreaker() instanceof ServerPlayer p)||!usable(p)||RUNNING.contains(p.getUUID()))return;
        var root=NATIVE.get();
        if(root==null||root.player!=p){
            // An additional ore drop outside the native stack before execution also invalidates a queued origin.
            if(event.getState().is(Tags.Blocks.ORES))PENDING.remove(p.getUUID());
            return;
        }
        if(!root.pos.equals(event.getPos())||root.receipt!=null){root.bulk=true;return;}
        if(root.bulk||PENDING.containsKey(p.getUUID())||event.getBlockEntity()!=null||!event.getState().is(Tags.Blocks.ORES))return;
        var level=event.getLevel();long now=level.getGameTime();
        if(p.serverLevel()!=level||now<NEXT.getOrDefault(p.getUUID(),0L))return;
        var hand=root.hand;
        if(p.getMainHandItem()!=hand||hand.isEmpty()!=event.getTool().isEmpty()||!hand.isEmpty()&&hand.getItem()!=event.getTool().getItem())return;
        if(p.distanceToSqr(event.getPos().getX()+.5,event.getPos().getY()+.5,event.getPos().getZ()+.5)>64)return;
        if(!allowed(p,level,event.getPos(),false))return;
        for(var a:EffectRuntime.active(p))if(eligible(a,p,event.getState(),hand)&&a.effect().amountPerRank()>0){
            root.receipt=new Receipt(level,event.getPos().immutable(),event.getState(),hand,a.id(),now,event);return;
        }
    }
    public static void forget(ServerPlayer p){PENDING.remove(p.getUUID());NEXT.remove(p.getUUID());RUNNING.remove(p.getUUID());}
    public static void tick(ServerPlayer p){
        Receipt receipt=PENDING.remove(p.getUUID());
        if(receipt!=null)mine(p,receipt);
        if(!usable(p)||p.tickCount%20!=0)return;
        // One scan per player interval, even if several trees provide the mechanic.
        for(var a:EffectRuntime.active(p)){
            var fx=a.effect();if(!fx.type().equals("ore_sense"))continue;
            int interval=MechanicalSpec.integer(fx.params(),"interval_ticks",60,40,200);
            if(p.tickCount%(((interval+19)/20)*20)!=0)continue;
            scan(p,a);break;
        }
    }
    private static void mine(ServerPlayer p,Receipt r){
        ServerLevel level=r.level();long now=level.getGameTime();
        if(!usable(p)||p.serverLevel()!=level||now<r.tick()||now-r.tick()>2||r.event().isCanceled()
            ||!level.hasChunkAt(r.pos())||level.getBlockState(r.pos()).getBlock()==r.state().getBlock()
            ||p.getMainHandItem()!=r.hand()||now<NEXT.getOrDefault(p.getUUID(),0L))return;
        var a=EffectRuntime.active(p).stream().filter(x->x.id().equals(r.effect())&&eligible(x,p,r.state(),r.hand())).findFirst().orElse(null);
        if(a==null)return;
        var fx=a.effect();
        int budget=(int)MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),MechanicalSpec.integer(fx.params(),"max_blocks",16,1,32));
        int radius=MechanicalSpec.integer(fx.params(),"radius",6,1,8);
        if(budget<1||!RUNNING.add(p.getUUID()))return;
        NEXT.put(p.getUUID(),now+MechanicalSpec.integer(fx.params(),"cooldown_ticks",20,5,200));
        try{
            BoundedVein.walk(r.pos(),budget,1+6*(budget+1),pos->{
                var neighbors=new ArrayList<BlockPos>(6);
                for(Direction d:Direction.values())neighbors.add(pos.relative(d));
                return neighbors;
            },pos->{
                if(!usable(p)||p.serverLevel()!=level||p.getMainHandItem()!=r.hand()||pos.distSqr(r.pos())>radius*radius
                    ||p.distanceToSqr(pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5)>64||!allowed(p,level,pos,false))return false;
                var state=level.getBlockState(pos);
                if(state.getBlock()!=r.state().getBlock()||level.getBlockEntity(pos)!=null||!eligible(a,p,state,r.hand())
                    ||state.getDestroySpeed(level,pos)<0||!state.canHarvestBlock(level,pos,p)
                    ||(!r.hand().isEmpty()&&!r.hand().isCorrectToolForDrops(state)))return false;
                // Every removal goes through normal protection, tool wear, events, XP and loot.
                boolean result=p.gameMode.destroyBlock(pos);
                return result&&level.getBlockState(pos).getBlock()!=state.getBlock();
            });
        }finally{RUNNING.remove(p.getUUID());}
    }
    private record Seen(BlockPos pos,double distance){}
    private static void scan(ServerPlayer p,EffectRuntime.Active a){
        var level=p.serverLevel();var center=p.blockPosition();var fx=a.effect();
        int radius=(int)MechanicalSpec.scaled(fx.amountPerRank(),a.rank(),12);
        if(radius<1)return;
        int cap=MechanicalSpec.integer(fx.params(),"max_targets",8,1,16);
        var nearest=new PriorityQueue<Seen>(Comparator.comparingDouble(Seen::distance).reversed());
        for(var cursor:BlockPos.betweenClosed(center.offset(-radius,-radius,-radius),center.offset(radius,radius,radius))){
            double d=center.distSqr(cursor);
            if(d>radius*radius||level.isOutsideBuildHeight(cursor)||!level.hasChunkAt(cursor))continue;
            if(nearest.size()==cap&&nearest.peek().distance()<=d)continue;
            var state=level.getBlockState(cursor);
            if(!state.is(Tags.Blocks.ORES)||!Selectors.block(fx.selector(),state)||!allowed(p,level,cursor,true))continue;
            if(nearest.size()==cap)nearest.remove();
            nearest.add(new Seen(cursor.immutable(),d));
        }
        if(nearest.isEmpty()){p.displayClientMessage(Component.literal("Ore sense: no matching ore nearby"),true);return;}
        var sorted=nearest.stream().sorted(Comparator.comparingDouble(Seen::distance)).toList();
        for(var seen:sorted){
            var pos=seen.pos();
            level.sendParticles(p,ParticleTypes.END_ROD,true,pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5,3,.2,.2,.2,.01);
        }
        var first=sorted.getFirst().pos();
        // A short directional cue near the owner remains useful when the ore is behind rock.
        var eye=p.getEyePosition();var vector=net.minecraft.world.phys.Vec3.atCenterOf(first).subtract(eye).normalize();
        for(int n=1;n<=4;n++){
            var point=eye.add(vector.scale(n*.4));
            level.sendParticles(p,ParticleTypes.END_ROD,true,point.x,point.y,point.z,1,0,0,0,0);
        }
        p.displayClientMessage(Component.literal("Ore sense: nearest at "+first.getX()+", "+first.getY()+", "+first.getZ()),true);
    }
}
