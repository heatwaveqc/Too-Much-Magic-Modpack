package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.GoetyRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.GoetyRitualContext;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.GoetyStormReceipt;
import net.minecraft.world.entity.Entity;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity",remap=false)
public abstract class GoetyRitualAltarMixin {
    @Unique private static final String TMM_GOETY="tyr_progression_goety_ritual";
    @Unique private GoetyRitualProof tmm$op,tmm$pending;
    @Unique private boolean tmm$stopping;
    @Unique private Entity tmm$delayed;
    @Unique private String tmm$id(){return GoetyRitualContext.currentId(this);}
    @Unique private UUID tmm$owner(){return GoetyRitualContext.owner(this);}
    @Unique private BlockEntity tmm$block(){return (BlockEntity)(Object)this;}
    @WrapMethod(method="activate(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/core/Direction;)Z",require=0,remap=false)
    private boolean tmm$start(Level level,BlockPos pos,Player player,InteractionHand hand,Direction face,Operation<Boolean> original){
        var handler=GoetyRitualContext.handler(this);
        boolean manual=tmm$id()==null&&handler!=null&&handler.getStackInSlot(0).isEmpty()&&level instanceof ServerLevel&&level.getBlockEntity(pos)==(Object)this&&
            player instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&!player.getItemInHand(hand).isEmpty()&&MxpMod.active();
        if(tmm$id()==null)tmm$op=null;
        boolean result=original.call(level,pos,player,hand,face);
        if(manual&&result&&tmm$id()!=null&&player.getUUID().equals(tmm$owner())&&handler.getStackInSlot(0).getCount()==1&&GoetyRitualContext.eligible(this,(ServerLevel)level)){
            tmm$op=new GoetyRitualProof(tmm$id(),player.getUUID(),UUID.randomUUID());tmm$block().setChanged();
        }
        return result;
    }
    @WrapMethod(method="stopRitual(Z)V",require=0,remap=false)
    private void tmm$stop(boolean finished,Operation<Void> original){
        if(tmm$stopping){tmm$op=null;tmm$pending=null;tmm$delayed=null;original.call(finished);return;}
        tmm$stopping=true;tmm$pending=null;tmm$delayed=null;
        try{
            original.call(finished);
            var pending=tmm$pending;
            if(finished&&pending!=null&&tmm$id()==null&&tmm$owner()==null&&tmm$block().getLevel() instanceof ServerLevel world&&world.getBlockEntity(tmm$block().getBlockPos())==(Object)this){
                ServerPlayer actor=world.getServer().getPlayerList().getPlayer(pending.actor());
                if(tmm$delayed==null)RitualSettlement.settle(actor,pending.receipt(true,true));
                else if(tmm$delayed instanceof GoetyStormReceipt sink&&world.getEntity(tmm$delayed.getUUID())==tmm$delayed&&MxpPlayerPolicy.eligible(actor))sink.tmm$armStorm(pending);
            }
        }finally{tmm$stopping=false;tmm$pending=null;tmm$op=null;tmm$delayed=null;}
    }
    @WrapOperation(method="stopRitual(Z)V",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/common/ritual/Ritual;finish(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V"),require=0,allow=1,remap=false)
    private void tmm$nativeFinish(@Coerce Object ritual,Level level,BlockPos pos,@Coerce Object altar,Player actor,ItemStack item,Operation<Void> original){
        var op=tmm$op;tmm$op=null;
        var result=GoetyRitualContext.finish(ritual,altar,level,pos,actor,item,altar==(Object)this&&tmm$stopping?op:null,()->original.call(ritual,level,pos,altar,actor,item));
        if(result.successful()){tmm$pending=op;tmm$delayed=result.delayed();}
    }
    @Inject(method="clearRitual()V",at=@At("HEAD"),require=0,remap=false)
    private void tmm$clear(CallbackInfo ci){tmm$op=null;}
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        if(tmm$op!=null&&tmm$op.matches(tmm$id(),tmm$owner())){
            CompoundTag data=new CompoundTag();data.putString("recipe",tmm$op.recipeId());data.putUUID("actor",tmm$op.actor());data.putUUID("operation",tmm$op.operation());tag.put(TMM_GOETY,data);
        }else tag.remove(TMM_GOETY);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        tmm$op=null;tmm$pending=null;tmm$stopping=false;tmm$delayed=null;
        CompoundTag data=tag.getCompound(TMM_GOETY);
        if(!data.hasUUID("actor")||!data.hasUUID("operation"))return;
        var candidate=new GoetyRitualProof(data.getString("recipe"),data.getUUID("actor"),data.getUUID("operation"));
        if(candidate.matches(tmm$id(),tmm$owner())&&GoetyRitualContext.supported(candidate.recipeId()))tmm$op=candidate;
    }
}
