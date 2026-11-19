package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.MalumRitualCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.MalumRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MalumRitualContext;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Pseudo
@Mixin(targets="com.sammy.malum.common.block.curiosities.soul_brazier.SoulBrazierBlockEntity",remap=false)
public abstract class MalumGeasRitualMixin {
    @Unique private static final String KEY="tmm_progression_malum_binding";
    @Unique private static final String HANDLER="com.sammy.malum.core.handlers.GeasEffectHandler";
    @Unique private MalumRitualProof.Binding tmm$binding;
    @Unique private final Set<UUID> tmm$consent=new HashSet<>();
    @Unique private MalumRitualProof.Binding tmm$finishing;
    @Unique private Set<UUID> tmm$sacrifices=Set.of(),tmm$optedIn=Set.of();
    @Unique private final Set<UUID> tmm$applied=new HashSet<>();
    @Unique private BlockEntity tmm$block(){return (BlockEntity)(Object)this;}
    @Unique private boolean tmm$bindingState(){return "BINDING".equals(MalumRitualContext.state(this));}
    @Unique private void tmm$clear(){tmm$binding=null;tmm$consent.clear();}
    @WrapOperation(method="onUse(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/ItemInteractionResult;",
        at=@At(value="INVOKE",target="Lcom/sammy/malum/common/block/curiosities/soul_brazier/SoulBrazierBlockEntity;attemptSoulbinding(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Z"),require=0,allow=1,remap=false)
    private boolean tmm$manualStart(@Coerce Object tile,ServerLevel world,Player player,ItemStack stack,Operation<Boolean> original){
        String before=MalumRitualContext.state(this);
        boolean watch=tile==(Object)this&&"IDLE".equals(before)&&MxpMod.active()&&MalumRitualContext.supported()&&
            player instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&p.isAlive()&&p.level()==world&&MalumRitualContext.installed(this,world)&&
            !stack.isEmpty()&&"malum:ether".equals(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        int count=stack.getCount();
        String effect=watch?MalumRitualContext.eligibleEffect(this,world):null;
        boolean accepted=original.call(tile,world,player,stack);
        if(!before.equals(MalumRitualContext.state(this)))tmm$clear();
        if(watch&&accepted&&tmm$bindingState()&&effect!=null&&effect.equals(MalumRitualContext.eligibleEffect(this,world))&&stack.getCount()==count-1){
            tmm$binding=new MalumRitualProof.Binding(player.getUUID(),UUID.randomUUID(),effect);tmm$block().setChanged();
        }
        return accepted;
    }
    @WrapOperation(method="onUse(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/ItemInteractionResult;",
        at=@At(value="INVOKE",target="Lcom/sammy/malum/common/block/curiosities/soul_brazier/SoulBrazierBlockEntity;addSacrifice(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)Z"),require=0,allow=1,remap=false)
    private boolean tmm$manualJoin(@Coerce Object tile,ServerLevel world,LivingEntity target,Operation<Boolean> original){
        boolean accepted=original.call(tile,world,target);
        if(accepted&&tile==(Object)this&&tmm$binding!=null&&tmm$bindingState()&&target instanceof ServerPlayer p&&p.isAlive()&&
            MxpPlayerPolicy.eligible(p)&&p.level()==world&&MalumRitualContext.installed(this,world)&&MalumRitualContext.sacrifices(this).contains(p.getUUID())){
            tmm$consent.add(p.getUUID());tmm$block().setChanged();
        }
        return accepted;
    }
    @WrapMethod(method="serverTick(Lnet/minecraft/server/level/ServerLevel;)V",require=0,remap=false)
    private void tmm$tick(ServerLevel world,Operation<Void> original){
        original.call(world);
        if(tmm$binding!=null&&(!tmm$bindingState()||!tmm$binding.matches(MalumRitualContext.eligibleEffect(this,world)))){
            tmm$clear();tmm$block().setChanged();
        }
    }
    @WrapMethod(method="completeSoulBinding(Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V",require=0,remap=false)
    private void tmm$finish(ServerLevel world,List<LivingEntity> targets,Operation<Void> original){
        if(tmm$finishing!=null){tmm$applied.clear();tmm$finishing=null;original.call(world,targets);return;}
        var op=tmm$binding;
        boolean watch=op!=null&&tmm$bindingState()&&MalumRitualContext.supported()&&MxpMod.active()&&MalumRitualContext.installed(this,world)&&
            op.matches(MalumRitualContext.eligibleEffect(this,world))&&MxpReflect.field(this,"progress") instanceof Integer ticks&&ticks>600;
        tmm$sacrifices=watch?MalumRitualContext.sacrifices(this):Set.of();
        tmm$optedIn=watch?Set.copyOf(tmm$consent):Set.of();
        tmm$finishing=watch?op:null;tmm$applied.clear();tmm$clear();
        try{
            original.call(world,targets);
            if(watch&&tmm$finishing==op&&"IDLE".equals(MalumRitualContext.state(this))&&MalumRitualContext.installed(this,world)){
                for(UUID id:tmm$applied){
                    ServerPlayer p=world.getServer().getPlayerList().getPlayer(id);
                    RitualSettlement.settle(p,op.receipt(id,tmm$sacrifices,tmm$optedIn,true,true));
                }
            }
        }finally{
            tmm$finishing=null;tmm$sacrifices=Set.of();tmm$optedIn=Set.of();tmm$applied.clear();tmm$block().setChanged();
        }
    }
    @WrapOperation(method="completeSoulBinding(Lnet/minecraft/server/level/ServerLevel;Ljava/util/List;)V",
        at=@At(value="INVOKE",target="Lcom/sammy/malum/core/handlers/GeasEffectHandler;addGeasEffect(Lnet/minecraft/world/entity/LivingEntity;Lcom/sammy/malum/core/systems/geas/GeasEffectType;)Z"),require=0,allow=1,remap=false)
    private boolean tmm$apply(LivingEntity target,@Coerce Object effect,Operation<Boolean> original){
        var op=tmm$finishing;
        boolean watch=op!=null&&target instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&p.isAlive()&&
            tmm$sacrifices.contains(p.getUUID())&&(op.initiator().equals(p.getUUID())||tmm$optedIn.contains(p.getUUID()))&&
            op.matches(MalumRitualContext.id(effect))&&Boolean.FALSE.equals(MxpReflect.staticCall(HANDLER,"hasGeasEffect",target,effect));
        boolean accepted=original.call(target,effect);
        if(watch&&accepted&&tmm$finishing==op&&Boolean.TRUE.equals(MxpReflect.staticCall(HANDLER,"hasGeasEffect",target,effect)))tmm$applied.add(target.getUUID());
        return accepted;
    }
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        if(tmm$binding!=null&&tmm$bindingState()){
            CompoundTag data=new CompoundTag();data.putUUID("initiator",tmm$binding.initiator());data.putUUID("operation",tmm$binding.operation());data.putString("effect",tmm$binding.effect());
            ListTag opted=new ListTag();for(UUID id:tmm$consent)opted.add(NbtUtils.createUUID(id));data.put("participants",opted);tag.put(KEY,data);
        }else tag.remove(KEY);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        tmm$clear();tmm$finishing=null;tmm$applied.clear();tmm$optedIn=Set.of();tmm$sacrifices=Set.of();
        CompoundTag data=tag.getCompound(KEY);
        if(!MalumRitualContext.supported()||!tmm$bindingState()||!data.hasUUID("initiator")||!data.hasUUID("operation"))return;
        var op=new MalumRitualProof.Binding(data.getUUID("initiator"),data.getUUID("operation"),data.getString("effect"));
        if(!op.matches(op.effect()))return;
        tmm$binding=op;
        Set<UUID> nativeIds=MalumRitualContext.sacrifices(this);
        for(Tag entry:data.getList("participants",Tag.TAG_INT_ARRAY)){
            try{UUID id=NbtUtils.loadUUID(entry);if(nativeIds.contains(id))tmm$consent.add(id);}catch(IllegalArgumentException ignored){}
        }
    }
}
