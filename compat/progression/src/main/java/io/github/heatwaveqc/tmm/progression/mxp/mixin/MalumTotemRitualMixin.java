package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.MalumRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MalumRitualContext;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;

@Pseudo
@Mixin(targets="com.sammy.malum.common.block.curiosities.totem.TotemBaseBlockEntity",remap=false)
public abstract class MalumTotemRitualMixin {
    @Unique private static final String KEY="tmm_progression_malum_commission";
    @Unique private MalumRitualProof.Commission tmm$commission;
    @Unique private BlockEntity tmm$block(){return (BlockEntity)(Object)this;}
    @WrapMethod(method="onUseWithItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/ItemInteractionResult;",require=0,remap=false)
    private ItemInteractionResult tmm$start(Player player,ItemStack held,InteractionHand hand,Operation<ItemInteractionResult> original){
        String before=MalumRitualContext.state(this);
        boolean watch="INACTIVE".equals(before)&&MxpMod.active()&&MalumRitualContext.supported()&&player instanceof ServerPlayer p&&
            MxpPlayerPolicy.eligible(p)&&p.isAlive()&&tmm$block().getLevel() instanceof ServerLevel world&&p.level()==world&&MalumRitualContext.installed(this,world);
        ItemInteractionResult result=original.call(player,held,hand);
        String after=MalumRitualContext.state(this);
        if(!before.equals(after))tmm$commission=null;
        if(watch&&result==ItemInteractionResult.SUCCESS&&"ASSEMBLING".equals(after)){
            tmm$commission=new MalumRitualProof.Commission(player.getUUID(),UUID.randomUUID());tmm$block().setChanged();
        }
        return result;
    }
    @WrapMethod(method="serverTick(Lnet/minecraft/server/level/ServerLevel;)V",require=0,remap=false)
    private void tmm$finish(ServerLevel world,Operation<Void> original){
        var op=tmm$commission;
        boolean assembling="ASSEMBLING".equals(MalumRitualContext.state(this));
        original.call(world);
        if(op==null||op!=tmm$commission)return;
        String state=MalumRitualContext.state(this);
        if("ASSEMBLING".equals(state))return;
        tmm$commission=null;tmm$block().setChanged();
        if(!assembling||!"ACTIVE".equals(state)||!MalumRitualContext.supported()||!MalumRitualContext.installed(this,world)||!MxpMod.active())return;
        Object rite=MxpReflect.call(this,"getRite");
        String id=MalumRitualContext.id(rite);
        if(MxpReflect.call(rite,"getEffect")==null||!(MxpReflect.call(this,"getTotemHeight") instanceof Integer height)||height<=0)return;
        ServerPlayer actor=world.getServer().getPlayerList().getPlayer(op.actor());
        RitualSettlement.settle(actor,op.receipt(id,true));
    }
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        if(tmm$commission!=null&&"ASSEMBLING".equals(MalumRitualContext.state(this))){
            CompoundTag data=new CompoundTag();data.putUUID("actor",tmm$commission.actor());data.putUUID("operation",tmm$commission.operation());tag.put(KEY,data);
        }else tag.remove(KEY);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        tmm$commission=null;
        CompoundTag data=tag.getCompound(KEY);
        if(MalumRitualContext.supported()&&"ASSEMBLING".equals(MalumRitualContext.state(this))&&data.hasUUID("actor")&&data.hasUUID("operation"))
            tmm$commission=new MalumRitualProof.Commission(data.getUUID("actor"),data.getUUID("operation"));
    }
}
