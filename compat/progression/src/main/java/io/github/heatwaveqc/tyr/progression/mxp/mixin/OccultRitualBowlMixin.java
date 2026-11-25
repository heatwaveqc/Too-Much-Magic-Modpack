package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets="com.klikli_dev.occultism.common.blockentity.GoldenSacrificialBowlBlockEntity",remap=false)
public abstract class OccultRitualBowlMixin {
    @Unique private static final String TMM_OPERATION="tyr_progression_ritual_operation";
    @Unique private UUID tmm$operation;
    @Unique private ServerPlayer tmm$manualActor;

    // The exact native right-click branch sets castingPlayer then inserts the bound book;
    // automation also inserts but never traverses this manual scope.
    @WrapOperation(method="activate(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/core/Direction;)Z",
        at=@At(value="INVOKE",target="Lnet/neoforged/neoforge/items/ItemStackHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"),require=0,remap=false)
    private ItemStack tmm$manualInsert(ItemStackHandler handler,int slot,ItemStack book,boolean simulate,Operation<ItemStack> original){
        ServerPlayer outer=tmm$manualActor;
        tmm$manualActor=null;
        if(!simulate&&slot==0&&OccultRitualContext.supported()&&MxpMod.active()&&
            MxpReflect.field(this,"castingPlayer") instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p))
            tmm$manualActor=p;
        try{return original.call(handler,slot,book,simulate);}
        finally{tmm$manualActor=outer;}
    }
    @Inject(method="startRitual(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/crafting/RecipeHolder;)Z",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$start(ServerPlayer actor,ItemStack activation,RecipeHolder<?> recipe,CallbackInfoReturnable<Boolean> cir){
        if(!Boolean.TRUE.equals(cir.getReturnValue()))return;
        tmm$operation=null;
        if(actor==null||actor!=tmm$manualActor||!MxpPlayerPolicy.eligible(actor)||
            !actor.getUUID().equals(MxpReflect.field(this,"castingPlayerId"))||
            !Boolean.TRUE.equals(MxpReflect.field(this,"ritualActive")))return;
        // Native start/handler may have already consumed the passed stack reference; verify the installed book.
        if(!(MxpReflect.field(this,"itemStackHandler") instanceof ItemStackHandler handler)||
            !OccultRitualContext.eligibleRecipe(recipe,handler.getStackInSlot(0)))return;
        tmm$operation=UUID.randomUUID();
        if((Object)this instanceof BlockEntity block)block.setChanged();
    }

    @WrapOperation(method="stopRitual(ZZ)V",at=@At(value="INVOKE",target="Lcom/klikli_dev/occultism/common/ritual/Ritual;finish(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/klikli_dev/occultism/common/blockentity/GoldenSacrificialBowlBlockEntity;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;)V"),require=0,remap=false)
    private void tmm$finish(@Coerce Object ritual,Level level,BlockPos pos,@Coerce Object bowl,ServerPlayer actor,ItemStack book,Operation<Void> original){
        UUID token=tmm$operation;tmm$operation=null;
        OccultRitualContext.finish(ritual,bowl,level,pos,actor,book,bowl==this?token:null,()->original.call(ritual,level,pos,bowl,actor,book));
    }
    @Inject(method="stopRitual(ZZ)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$stop(boolean finished,boolean showInterrupted,CallbackInfo ci){tmm$operation=null;}
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        if(tmm$operation!=null&&Boolean.TRUE.equals(MxpReflect.field(this,"ritualActive")))tag.putUUID(TMM_OPERATION,tmm$operation);
        else tag.remove(TMM_OPERATION);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",
        at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$operation=null;tmm$manualActor=null;
        if(OccultRitualContext.supported()&&Boolean.TRUE.equals(MxpReflect.field(this,"ritualActive"))&&
            MxpReflect.field(this,"castingPlayerId") instanceof UUID&&tag.hasUUID(TMM_OPERATION))tmm$operation=tag.getUUID(TMM_OPERATION);
    }
}
