package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.event.level.BlockEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

/** Synchronous native producer scope. Never infer a real owner from a FakePlayer UUID. */
public final class SpellMiningScope {
    private SpellMiningScope(){}
    private record Receipt(BlockEvent.BreakEvent event,BlockState original,double hardness,long tick){}
    private record Frame(ServerLevel level,ServerPlayer owner,BlockPos pos,boolean hammer,
                         Map<Long,Receipt> receipts,Frame parent){}
    private static final ThreadLocal<Frame> CURRENT=new ThreadLocal<>();
    private static ServerPlayer owner(Entity actor,ServerLevel level){
        return actor instanceof ServerPlayer p&&p.serverLevel()==level
            &&VerifiedMiningXp.participates(p)?p:null;
    }
    private static Frame find(ServerLevel level,BlockPos pos){
        for(Frame f=CURRENT.get();f!=null;f=f.parent())
            if(f.level()==level&&(f.pos()==null||f.pos().equals(pos)))return f;
        return null;
    }
    public static boolean captures(ServerLevel level,BlockPos pos){return find(level,pos)!=null;}
    public static boolean active(ServerLevel level){
        for(Frame f=CURRENT.get();f!=null;f=f.parent())if(f.level()==level)return true;
        return false;
    }
    private static void scoped(Entity actor,Level level,BlockPos pos,boolean hammer,Runnable action){
        if(!(level instanceof ServerLevel server)){action.run();return;}
        Frame old=CURRENT.get();
        CURRENT.set(new Frame(server,owner(actor,server),pos==null?null:pos.immutable(),hammer,new HashMap<>(),old));
        try{action.run();}finally{if(old==null)CURRENT.remove();else CURRENT.set(old);}
    }
    public static boolean claim(Entity actor,Level level,BlockPos pos,BooleanSupplier action){
        boolean[] result={false};scoped(actor,level,pos,false,()->result[0]=action.getAsBoolean());return result[0];
    }
    public static void fellPosition(Entity actor,Level level,BlockPos pos,Runnable action){
        scoped(actor,level,pos,false,action);
    }
    public static void hammer(Entity actor,Level level,Runnable action){
        scoped(actor,level,null,true,action);
    }
    public static boolean breakBlock(Entity actor,ServerLevel level,BlockPos pos,BooleanSupplier action){
        boolean[] result={false};scoped(actor,level,pos,false,()->result[0]=remove(level,pos,action));
        return result[0];
    }
    /** Called only at a native boolean removal boundary, never at glyph resolve or a claim probe. */
    public static boolean remove(Level level,BlockPos pos,BooleanSupplier action){
        if(!(level instanceof ServerLevel server))return action.getAsBoolean();
        Frame f=find(server,pos);
        if(f==null||f.owner()==null||f.hammer()||!server.hasChunkAt(pos))return action.getAsBoolean();
        BlockState original=server.getBlockState(pos);
        double hardness=original.getDestroySpeed(server,pos);
        long tick=server.getGameTime();
        boolean accepted=action.getAsBoolean();
        if(accepted)VerifiedMiningXp.spellBreakCommitted(f.owner(),server,pos,original,hardness,tick);
        return accepted;
    }
    /** Spectral Hammer posts a native BreakEvent with its retained actual owner before removal. */
    public static void observe(BlockEvent.BreakEvent event){
        if(!(event.getLevel() instanceof ServerLevel level))return;
        Frame f=find(level,event.getPos());
        if(f==null||!f.hammer()||f.owner()==null||event.getPlayer()!=f.owner()
            ||event.isCanceled()||event.getState().isAir()||!level.hasChunkAt(event.getPos()))return;
        double hardness=event.getState().getDestroySpeed(level,event.getPos());
        if(!Double.isFinite(hardness)||hardness<0)return;
        // Native iteration is synchronous; each receipt is consumed by the ensuing dropResources.
        f.receipts().put(event.getPos().asLong(),new Receipt(event,event.getState(),hardness,level.getGameTime()));
    }
    public static void hammerCommitted(Level level,BlockPos pos,BlockState original){
        if(!(level instanceof ServerLevel server))return;
        Frame f=find(server,pos);
        if(f==null||!f.hammer()||f.owner()==null)return;
        Receipt r=f.receipts().remove(pos.asLong());
        if(r==null||r.event().isCanceled()||!r.original().equals(original))return;
        VerifiedMiningXp.spellBreakCommitted(f.owner(),server,pos,r.original(),r.hardness(),r.tick());
    }
}
