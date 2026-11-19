package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RootsRitualContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import java.util.List;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets="elucent.rootsclassic.block.altar.AltarBlockEntity",remap=false)
public abstract class RootsRitualAltarMixin implements RootsRitualContext.Altar {
    @Unique private static final String TMM_ROOTS="tmm_progression_roots_ritual";
    @Unique private RootsRitualProof.Operation tmm$rootsOperation;
    @Unique private RitualRewards.Receipt tmm$rootsPendingReceipt;
    @WrapMethod(method="activate(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/ItemInteractionResult;",require=0,remap=false)
    private ItemInteractionResult tmm$activate(Level world,BlockPos pos,BlockState state,Player player,InteractionHand hand,ItemStack stack,BlockHitResult hit,Operation<ItemInteractionResult> original){
        int before=RootsRitualContext.progress(this);
        boolean manual=before==0&&world instanceof ServerLevel&&world.getBlockEntity(pos)==(Object)this&&hand==InteractionHand.MAIN_HAND&&stack.isEmpty()&&player.isShiftKeyDown()&&
            player instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&RootsRitualContext.supported()&&MxpMod.active();
        if(before==0){tmm$rootsOperation=null;tmm$rootsPendingReceipt=null;}
        ItemInteractionResult result=original.call(world,pos,state,player,hand,stack,hit);
        if(manual&&result==ItemInteractionResult.SUCCESS&&RootsRitualProof.started(before,RootsRitualContext.progress(this))){
            try{
                var holder=RootsRitualContext.current(this);
                if(RootsRitualContext.eligibleRecipe(holder,(ServerLevel)world)!=null){
                    tmm$rootsOperation=new RootsRitualProof.Operation(holder.id().toString(),player.getUUID(),UUID.randomUUID());
                    if((Object)this instanceof BlockEntity block)block.setChanged();
                }
            }catch(RuntimeException ignored){tmm$rootsOperation=null;}
        }
        return result;
    }
    @Inject(method="serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lelucent/rootsclassic/block/altar/AltarBlockEntity;)V",at=@At("HEAD"),require=0,remap=false)
    private static void tmm$startTick(Level world,BlockPos pos,BlockState state,@Coerce Object altar,CallbackInfo ci){
        if(altar instanceof RootsRitualContext.Altar provider){
            provider.tmm$takeRootsPending();
            if(RootsRitualContext.progress(altar)<=0||RootsRitualContext.current(altar)==null)provider.tmm$takeRootsOperation();
        }
    }
    @WrapOperation(method="serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lelucent/rootsclassic/block/altar/AltarBlockEntity;)V",at=@At(value="INVOKE",target="Lelucent/rootsclassic/recipe/RitualRecipe;doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;Ljava/util/List;)V"),require=0,remap=false)
    private static void tmm$effect(@Coerce Object recipe,Level world,BlockPos pos,Container inventory,List<ItemStack> incenses,Operation<Void> original){
        Object altar=world.getBlockEntity(pos);
        if(altar instanceof RootsRitualContext.Altar provider){
            var receipt=RootsRitualContext.finish(altar,recipe,world,pos,provider.tmm$takeRootsOperation(),()->original.call(recipe,world,pos,inventory,incenses));
            provider.tmm$rootsPending(receipt);
        }else original.call(recipe,world,pos,inventory,incenses);
    }
    @WrapOperation(method="serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lelucent/rootsclassic/block/altar/AltarBlockEntity;)V",at=@At(value="INVOKE",target="Lelucent/rootsclassic/block/altar/AltarBlockEntity;emptyAltar()V"),require=0,remap=false)
    private static void tmm$empty(@Coerce Object altar,Operation<Void> original){
        RitualRewards.Receipt receipt=altar instanceof RootsRitualContext.Altar provider?provider.tmm$takeRootsPending():null;
        original.call(altar);RootsRitualContext.settled(altar,receipt);
    }
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        var op=tmm$rootsOperation;
        if(op!=null&&RootsRitualContext.progress(this)>0&&RootsRitualContext.currentId(this,op.recipeId())){
            CompoundTag data=new CompoundTag();data.putString("recipe",op.recipeId());data.putUUID("actor",op.actor());data.putUUID("operation",op.transaction());tag.put(TMM_ROOTS,data);
        }else tag.remove(TMM_ROOTS);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$rootsOperation=null;tmm$rootsPendingReceipt=null;
        if(!RootsRitualContext.supported()||RootsRitualContext.progress(this)<=0)return;
        CompoundTag data=tag.getCompound(TMM_ROOTS);
        try{
            if(data.hasUUID("actor")&&data.hasUUID("operation")&&RootsRitualContext.currentId(this,data.getString("recipe")))
                tmm$rootsOperation=new RootsRitualProof.Operation(data.getString("recipe"),data.getUUID("actor"),data.getUUID("operation"));
        }catch(RuntimeException ignored){tmm$rootsOperation=null;}
        // Native 1.5.10 does not restore its active server recipe; never synthesize or restart it.
    }
    @Override public RootsRitualProof.Operation tmm$takeRootsOperation(){var op=tmm$rootsOperation;tmm$rootsOperation=null;return op;}
    @Override public void tmm$rootsPending(RitualRewards.Receipt receipt){tmm$rootsPendingReceipt=receipt;}
    @Override public RitualRewards.Receipt tmm$takeRootsPending(){var receipt=tmm$rootsPendingReceipt;tmm$rootsPendingReceipt=null;return receipt;}
}
