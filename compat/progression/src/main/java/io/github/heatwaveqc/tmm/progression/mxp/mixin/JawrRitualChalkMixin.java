package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.E6bRitualCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;
import java.util.List;

@Pseudo
@Mixin(targets="dev.sterner.witchery.content.block.ritual.GoldenRitualChalkBlockEntity",remap=false)
public abstract class JawrRitualChalkMixin implements JawrRitualOutput.Observer {
    @Unique private UUID tmm$actor,tmm$operation;
    @Unique private String tmm$recipe;
    @Unique private boolean tmm$actual;
    @Unique private static final String TMM_JAWR="tmm_progression_circle_ritual";
    @Unique private static boolean tmm$supported(){return ModList.get().getModContainerById("witchery").map(c->"0.5.39+mc1.21.1".equals(c.getModInfo().getVersion().toString())).orElse(false);}
    @Unique private boolean tmm$active(){return Boolean.TRUE.equals(MxpReflect.field(this,"active"));}
    @Unique private boolean tmm$matches(String id){
        Object stored=MxpReflect.field(this,"storedRitualId"),pending=MxpReflect.field(this,"pendingRitualId");
        return id!=null&&(stored!=null||pending!=null)&&(stored==null||id.equals(stored.toString()))&&(pending==null||id.equals(pending.toString()));
    }
    @Unique private ServerLevel tmm$level(){
        if(!tmm$supported()||!MxpMod.active()||!((Object)this instanceof BlockEntity b)||!(b.getLevel() instanceof ServerLevel level)
            ||!level.getServer().isSameThread()||level.getBlockEntity(b.getBlockPos())!=b)return null;
        return level;
    }
    @Unique private void tmm$clear(){tmm$actor=null;tmm$operation=null;tmm$recipe=null;tmm$actual=false;}
    @Inject(method="beginRitual(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/crafting/RecipeHolder;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$begin(Player player,RecipeHolder<?> holder,CallbackInfo ci){
        tmm$clear();
        if(holder==null||!(player instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p)||tmm$level()!=p.level()||!tmm$active()
            ||MxpReflect.call(this,"getOwnerNotStored")!=p||MxpReflect.call(this,"getRitualRecipe")!=holder.value())return;
        String id=holder.id().toString();
        if(!tmm$matches(id)||E6bRitualCatalog.jawr(id)==null)return;
        tmm$recipe=id;tmm$actor=p.getUUID();tmm$operation=UUID.randomUUID();((BlockEntity)(Object)this).setChanged();
    }
    @WrapMethod(method="tickCurrentEvent(Lnet/minecraft/world/level/Level;)V",require=0,remap=false)
    private void tmm$tick(Level world,Operation<Void> original){
        ServerLevel level=tmm$level();
        Object event=null;
        if(tmm$operation!=null&&tmm$active()&&level==world&&tmm$matches(tmm$recipe)&&Boolean.TRUE.equals(MxpReflect.field(this,"inOutputPhase"))
            &&MxpReflect.field(this,"eventQueue") instanceof List<?> queue&&MxpReflect.field(this,"eventIndex") instanceof Integer index
            &&index>=0&&index<queue.size())event=queue.get(index);
        JawrRitualOutput.tick(this,event,level,E6bRitualCatalog.jawr(tmm$recipe),()->original.call(world));
    }
    @Override public UUID tmm$jawrActor(){return tmm$actor;}
    @Override public void tmm$markJawrOutput(){
        if(tmm$operation!=null&&tmm$active()&&tmm$matches(tmm$recipe)){tmm$actual=true;((BlockEntity)(Object)this).setChanged();}
    }
    @WrapMethod(method="finishRitual()V",require=0,remap=false)
    private void tmm$finish(Operation<Void> original){
        ServerLevel level=tmm$level();String id=tmm$recipe;UUID actor=tmm$actor,operation=tmm$operation;
        boolean valid=level!=null&&operation!=null&&actor!=null&&tmm$active()&&tmm$matches(id)&&tmm$actual
            &&Boolean.TRUE.equals(MxpReflect.field(this,"inOutputPhase"))&&MxpReflect.field(this,"outputEventsCompleted") instanceof Integer count&&count>0
            &&MxpReflect.field(this,"eventIndex") instanceof Integer index&&MxpReflect.field(this,"eventQueue") instanceof List<?> queue&&index>=queue.size();
        tmm$clear();original.call();
        if(valid&&!tmm$active()&&tmm$level()==level)
            RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(actor),new RitualRewards.Receipt(id,operation,actor,true,false));
    }
    @Inject(method="resetRitual()V",at=@At("HEAD"),require=0,remap=false)
    private void tmm$reset(CallbackInfo ci){tmm$clear();}
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        if(tmm$actor!=null&&tmm$operation!=null&&tmm$active()&&tmm$matches(tmm$recipe)){
            CompoundTag data=new CompoundTag();data.putString("recipe",tmm$recipe);data.putUUID("actor",tmm$actor);data.putUUID("operation",tmm$operation);data.putBoolean("actual",tmm$actual);tag.put(TMM_JAWR,data);
        }else tag.remove(TMM_JAWR);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$clear();CompoundTag data=tag.getCompound(TMM_JAWR);String id=data.getString("recipe");
        if(tmm$supported()&&tmm$active()&&tmm$matches(id)&&E6bRitualCatalog.jawr(id)!=null&&data.hasUUID("actor")&&data.hasUUID("operation")){
            tmm$recipe=id;tmm$actor=data.getUUID("actor");tmm$operation=data.getUUID("operation");tmm$actual=data.getBoolean("actual");
        }
    }
}
