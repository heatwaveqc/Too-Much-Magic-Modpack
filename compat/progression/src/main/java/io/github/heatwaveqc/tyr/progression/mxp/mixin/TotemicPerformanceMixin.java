package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.E6bRitualCatalog;
import io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.LinkedHashSet;
import java.util.UUID;

/** Actual accepted music contribution and the noncanceled native performance transition. */
@Pseudo
@Mixin(targets="pokefenn.totemic.block.totem.entity.StateStartup",remap=false)
public abstract class TotemicPerformanceMixin {
    @Unique private UUID tmm$operation;
    @Unique private final LinkedHashSet<UUID> tmm$performers=new LinkedHashSet<>();
    @Unique private int tmm$beforeMusic=-1;
    @Unique private boolean tmm$spent;
    @Unique private static final String TMM_TOTEMIC="tyr_progression_performance";
    @Unique private static boolean tmm$supported(){
        return ModList.get().getModContainerById("totemic").map(c->"1.21.1-0.12.18".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }
    @Unique private BlockEntity tmm$tile(){return MxpReflect.field(this,"tile") instanceof BlockEntity b?b:null;}
    @Unique private String tmm$id(){
        Object id=MxpReflect.call(MxpReflect.call(this,"getCeremony"),"getRegistryName");
        return id==null?null:id.toString();
    }
    @Unique private ServerLevel tmm$live(){
        BlockEntity b=tmm$tile();
        return tmm$supported()&&MxpMod.active()&&b!=null&&b.getLevel() instanceof ServerLevel level&&level.getServer().isSameThread()
            &&level.getBlockEntity(b.getBlockPos())==b&&MxpReflect.call(b,"getTotemState")==this?level:null;
    }
    @Inject(method="acceptMusic(Lpokefenn/totemic/api/music/MusicInstrument;ILnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/Entity;)Lpokefenn/totemic/api/music/MusicAcceptor$MusicResult;",at=@At("HEAD"),require=0,remap=false)
    private void tmm$before(@Coerce Object instrument,int amount,Vec3 pos,Entity source,CallbackInfoReturnable<Object> cir){
        tmm$beforeMusic=-1;
        if(!tmm$spent&&amount>0&&source instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&tmm$live()==p.level()
            &&E6bRitualCatalog.totemic(tmm$id())&&MxpReflect.call(this,"getMusic",instrument) instanceof Integer n)tmm$beforeMusic=n;
    }
    @Inject(method="acceptMusic(Lpokefenn/totemic/api/music/MusicInstrument;ILnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/Entity;)Lpokefenn/totemic/api/music/MusicAcceptor$MusicResult;",at=@At("RETURN"),require=0,remap=false)
    private void tmm$after(@Coerce Object instrument,int amount,Vec3 pos,Entity source,CallbackInfoReturnable<Object> cir){
        int before=tmm$beforeMusic;tmm$beforeMusic=-1;
        if(before<0||tmm$spent||!(source instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p)||tmm$live()!=p.level()
            ||!Boolean.TRUE.equals(MxpReflect.call(cir.getReturnValue(),"isSuccess"))||!(MxpReflect.call(this,"getMusic",instrument) instanceof Integer after)||after<=before)return;
        if(tmm$operation==null)tmm$operation=UUID.randomUUID();
        if(tmm$performers.size()<128&&tmm$performers.add(p.getUUID()))tmm$tile().setChanged();
    }
    @WrapOperation(method="tick()V",at=@At(value="INVOKE",target="Lpokefenn/totemic/block/totem/entity/StateStartup;startCeremony()V"),require=0,remap=false)
    private void tmm$success(@Coerce Object self,Operation<Void> original){
        ServerLevel level=tmm$live(); String id=tmm$id();UUID operation=tmm$operation;
        boolean valid=self==this&&!tmm$spent&&level!=null&&operation!=null&&E6bRitualCatalog.totemic(id);
        tmm$spent=true;
        var actors=java.util.List.copyOf(tmm$performers);tmm$performers.clear();tmm$operation=null;
        original.call(self);
        BlockEntity b=tmm$tile();Object state=MxpReflect.call(b,"getTotemState");
        if(!valid||b==null||b.getLevel()!=level||level.getBlockEntity(b.getBlockPos())!=b||state==null
            ||!state.getClass().getName().equals("pokefenn.totemic.block.totem.entity.StateCeremonyEffect"))return;
        b.setChanged();
        for(UUID actor:actors){
            ServerPlayer player=level.getServer().getPlayerList().getPlayer(actor);
            if(MxpPlayerPolicy.eligible(player)&&player.level()==level)
                RitualSettlement.settle(player,new RitualRewards.Receipt(id,operation,actor,true,false));
        }
    }
    @Inject(method="save(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        String id=tmm$id();
        if(!tmm$spent&&tmm$operation!=null&&E6bRitualCatalog.totemic(id)&&!tmm$performers.isEmpty()){
            CompoundTag data=new CompoundTag();data.putUUID("operation",tmm$operation);data.putString("ceremony",id);
            data.putString("performers",String.join(",",tmm$performers.stream().map(UUID::toString).toList()));tag.put(TMM_TOTEMIC,data);
        }else tag.remove(TMM_TOTEMIC);
    }
    @Inject(method="load(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$operation=null;tmm$performers.clear();tmm$spent=false;tmm$beforeMusic=-1;
        CompoundTag data=tag.getCompound(TMM_TOTEMIC);String id=tmm$id(),actors=data.getString("performers");
        if(!tmm$supported()||!E6bRitualCatalog.totemic(id)||!id.equals(data.getString("ceremony"))||!data.hasUUID("operation")||actors.isEmpty()||actors.length()>128*37)return;
        try{for(String actor:actors.split(","))tmm$performers.add(UUID.fromString(actor));
            if(tmm$performers.size()<=128)tmm$operation=data.getUUID("operation");else tmm$performers.clear();
        }catch(IllegalArgumentException ignored){tmm$performers.clear();tmm$operation=null;}
    }
}
