package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import io.github.heatwaveqc.tyr.progression.mxp.core.ForgeRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;
import java.util.Optional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets="com.stal111.forbidden_arcanus.common.block.entity.forge.ritual.RitualManager",remap=false)
public abstract class ForgeRitualManagerMixin implements ForgeRitualCompletion.Provider {
    @Unique private static final String TMM_OPERATION="tyr_progression_ritual_operation";
    @Unique private UUID tmm$operation;
    @Unique private Object tmm$finishing;
    @Unique private RitualRewards.Receipt tmm$candidate;
    @Unique private int tmm$beforeTier;
    @Unique private ForgeRitualCompletion tmm$pending;

    @Inject(method="startRitual(Lnet/minecraft/server/level/ServerPlayer;Lcom/stal111/forbidden_arcanus/common/block/entity/forge/essence/EssencesStorage;)Z",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$started(ServerPlayer actor,@Coerce Object storage,CallbackInfoReturnable<Boolean> cir){
        if(!Boolean.TRUE.equals(cir.getReturnValue()))return;
        tmm$operation=null;
        if(!ForgeRitualCompletion.supported()||!MxpPlayerPolicy.eligible(actor))return;
        Object data=MxpReflect.field(this,"activeRitualData");
        if(!actor.getUUID().equals(MxpReflect.call(data,"getStartedBy"))||
            !(MxpReflect.call(data,"getRitualId") instanceof ResourceLocation id)||RitualRewards.lookup(id.toString())==null)return;
        tmm$operation=UUID.randomUUID();
        if(MxpReflect.field(this,"level") instanceof ServerLevel level&&MxpReflect.field(this,"pos") instanceof BlockPos pos){
            var block=level.getBlockEntity(pos);
            if(block!=null)block.setChanged();
        }
    }
    @Inject(method="reset()V",at=@At("HEAD"),require=0,remap=false)
    private void tmm$reset(CallbackInfo ci){tmm$operation=null;}
    @Inject(method="save(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/nbt/CompoundTag;",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfoReturnable<CompoundTag> cir){
        CompoundTag result=cir.getReturnValue();
        if(result==null)return;
        if(tmm$operation!=null&&MxpReflect.field(this,"activeRitualData")!=null)result.putUUID(TMM_OPERATION,tmm$operation);
        else result.remove(TMM_OPERATION);
    }
    @Inject(method="load(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$pending=null;tmm$candidate=null;tmm$finishing=null;tmm$operation=null;
        if(ForgeRitualCompletion.supported()&&MxpReflect.field(this,"activeRitualData")!=null&&tag.hasUUID(TMM_OPERATION))
            tmm$operation=tag.getUUID(TMM_OPERATION);
    }
    @Inject(method="tick()Ljava/util/Optional;",at=@At("HEAD"),require=0,remap=false)
    private void tmm$beginTick(CallbackInfoReturnable<Optional<ItemStack>> cir){
        tmm$pending=null;tmm$candidate=null;tmm$finishing=null;
    }
    @Inject(method="finishRitual(Lcom/stal111/forbidden_arcanus/common/block/entity/forge/ritual/ActiveRitualData;)Lnet/minecraft/world/item/ItemStack;",
        at=@At("HEAD"),require=0,remap=false)
    private void tmm$finishBegin(@Coerce Object data,CallbackInfoReturnable<ItemStack> cir){
        tmm$candidate=null;tmm$finishing=null;
        if(!ForgeRitualCompletion.supported()||tmm$operation==null||data==null||
            MxpReflect.field(this,"activeRitualData")!=data)return;
        if(!(MxpReflect.call(data,"getRitualId") instanceof ResourceLocation id)||
            !(MxpReflect.call(data,"getStartedBy") instanceof UUID actor))return;
        Object requirements=MxpReflect.call(MxpReflect.call(data,"getRitual"),"requirements");
        Object required=MxpReflect.call(MxpReflect.call(requirements,"tier"),"tier");
        if(!(required instanceof Integer n)||!ForgeRitualProof.matchesTier(id.toString(),n))return;
        tmm$beforeTier=ForgeRitualCompletion.tier(this);
        tmm$finishing=data;
        tmm$candidate=new RitualRewards.Receipt(id.toString(),tmm$operation,actor,true,false);
    }
    @Inject(method="finishRitual(Lcom/stal111/forbidden_arcanus/common/block/entity/forge/ritual/ActiveRitualData;)Lnet/minecraft/world/item/ItemStack;",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$finishEnd(@Coerce Object data,CallbackInfoReturnable<ItemStack> cir){
        var candidate=tmm$candidate;Object finishing=tmm$finishing;
        tmm$candidate=null;tmm$finishing=null;
        ItemStack result=cir.getReturnValue();
        if(candidate!=null&&data==finishing&&result!=null)
            tmm$pending=new ForgeRitualCompletion(candidate,result.copy(),tmm$beforeTier);
    }
    @Override public ForgeRitualCompletion tmm$takeForgeReceipt(){
        var result=tmm$pending;tmm$pending=null;return result;
    }
}
