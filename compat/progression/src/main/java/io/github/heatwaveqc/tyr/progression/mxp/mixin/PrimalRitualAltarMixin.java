package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.core.PrimalRitualCatalog;
import io.github.heatwaveqc.tyr.progression.mxp.core.PrimalRitualProof;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Collection;
import java.util.UUID;

@Pseudo
@Mixin(targets="com.verdantartifice.primalmagick.common.tiles.rituals.RitualAltarTileEntity",remap=false)
public abstract class PrimalRitualAltarMixin {
    @Shadow protected boolean active;
    @Shadow protected UUID activePlayerId;
    @Shadow protected ResourceLocation activeRecipeId;
    @Shadow public abstract ItemStack getItem();
    @Unique private static final String TMM_PRIMAL="tyr_progression_primal_ritual";
    @Unique private PrimalRitualProof tmm$op;
    @Unique private PrimalRitualProof tmm$finishing;
    @Unique private ItemStack tmm$expected;
    @Unique private boolean tmm$installed;
    @Unique private static boolean tmm$supported(){
        return ModList.get().getModContainerById("primalmagick").map(c->"6.0.7".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }
    @Unique private String tmm$id(){return activeRecipeId==null?null:activeRecipeId.toString();}
    @Unique private BlockEntity tmm$block(){return (BlockEntity)(Object)this;}
    @Unique private boolean tmm$matches(PrimalRitualProof op){
        return op!=null&&op.matches(active,tmm$id(),activePlayerId);
    }
    @Unique private ItemStack tmm$expectedOutput(ServerLevel world){
        try{
            if(activeRecipeId==null)return ItemStack.EMPTY;
            var entry=PrimalRitualCatalog.lookup(tmm$id());
            var holder=world.getServer().getRecipeManager().byKey(activeRecipeId).orElse(null);
            if(entry==null||holder==null||!holder.value().getClass().getName().equals("com.verdantartifice.primalmagick.common.crafting.RitualRecipe"))return ItemStack.EMPTY;
            Object recipe=holder.value();
            Object props=MxpReflect.call(recipe,"getProps"), ingredients=MxpReflect.call(recipe,"getIngredients"),risk=MxpReflect.call(recipe,"getInstability");
            ItemStack stack=holder.value().getResultItem(world.registryAccess());
            if(stack.isEmpty()||!(props instanceof Collection<?> p)||!(ingredients instanceof Collection<?> i)||!(risk instanceof Integer n))return ItemStack.EMPTY;
            return entry.matches(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(),stack.getCount(),n,p.size(),i.size())?stack.copy():ItemStack.EMPTY;
        }catch(RuntimeException ignored){return ItemStack.EMPTY;}
    }
    @WrapMethod(method="onWandRightClick(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/InteractionResult;",require=0,remap=false)
    private InteractionResult tmm$start(ItemStack wand,Level level,Player player,BlockPos pos,Direction direction,Operation<InteractionResult> original){
        boolean wasActive=active;
        if(!wasActive)tmm$op=null;
        InteractionResult result=original.call(wand,level,player,pos,direction);
        if(!wasActive&&active&&result==InteractionResult.SUCCESS&&level instanceof ServerLevel world&&
            world.getBlockEntity(pos)==(Object)this&&player instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&
            p.getUUID().equals(activePlayerId)&&tmm$supported()&&MxpMod.active()&&!tmm$expectedOutput(world).isEmpty()){
            tmm$op=new PrimalRitualProof(tmm$id(),p.getUUID(),UUID.randomUUID(),false);
            tmm$block().setChanged();
        }
        return result;
    }
    @Inject(method="reset()V",at=@At("HEAD"),require=0,remap=false)
    private void tmm$reset(CallbackInfo ci){tmm$op=null;}
    @WrapMethod(method="finishCraft()V",require=0,remap=false)
    private void tmm$finish(Operation<Void> original){
        var op=tmm$op;tmm$op=null;
        if(tmm$finishing!=null){tmm$installed=false;original.call();return;}
        if(!tmm$matches(op)||!tmm$supported()||!MxpMod.active()||!(tmm$block().getLevel() instanceof ServerLevel world)||
            world.getBlockEntity(tmm$block().getBlockPos())!=(Object)this||!world.getServer().isSameThread()){
            original.call();return;
        }
        var expected=tmm$expectedOutput(world);
        if(expected.isEmpty()){original.call();return;}
        tmm$finishing=op;tmm$expected=expected;tmm$installed=false;
        try{
            original.call();
            if(tmm$installed&&!active&&activeRecipeId==null&&world.getBlockEntity(tmm$block().getBlockPos())==(Object)this){
                ServerPlayer p=world.getServer().getPlayerList().getPlayer(op.actor());
                RitualSettlement.settle(p,op.receipt(true,true));
            }
        }finally{tmm$finishing=null;tmm$expected=null;tmm$installed=false;}
    }
    @WrapOperation(method="lambda$finishCraft$9(Lnet/minecraft/world/item/crafting/RecipeHolder;)V",
        at=@At(value="INVOKE",target="Lcom/verdantartifice/primalmagick/common/tiles/rituals/RitualAltarTileEntity;setItem(IILnet/minecraft/world/item/ItemStack;)V"),
        require=0,allow=1,remap=false)
    private void tmm$output(@Coerce Object altar,int inventory,int slot,ItemStack stack,Operation<Void> original){
        boolean watch=altar==(Object)this&&tmm$finishing!=null&&tmm$matches(tmm$finishing)&&inventory==0&&slot==0&&
            tmm$expected!=null&&!stack.isEmpty()&&ItemStack.matches(tmm$expected,stack);
        ItemStack before=watch?getItem():ItemStack.EMPTY;
        ItemStack prior=watch?before.copy():ItemStack.EMPTY;
        original.call(altar,inventory,slot,stack);
        if(watch){
            ItemStack after=getItem();
            tmm$installed=ItemStack.matches(tmm$expected,after)&&(before!=after||!ItemStack.matches(prior,after));
        }
    }
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        if(tmm$matches(tmm$op)){
            CompoundTag data=new CompoundTag();data.putString("recipe",tmm$op.recipeId());data.putUUID("actor",tmm$op.actor());data.putUUID("operation",tmm$op.operation());
            tag.put(TMM_PRIMAL,data);
        }else tag.remove(TMM_PRIMAL);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider lookup,CallbackInfo ci){
        tmm$op=null;tmm$finishing=null;tmm$expected=null;tmm$installed=false;
        if(!active||!tmm$supported())return;
        CompoundTag data=tag.getCompound(TMM_PRIMAL);
        if(!data.hasUUID("actor")||!data.hasUUID("operation"))return;
        UUID actor=data.getUUID("actor");
        // Native 6.0.7 saves an int-array UUID but reads only a compound. Never rewrite native ownership.
        if(tag.hasUUID("ActivePlayer")&&!actor.equals(tag.getUUID("ActivePlayer")))return;
        var candidate=new PrimalRitualProof(data.getString("recipe"),actor,data.getUUID("operation"),true);
        if(tmm$matches(candidate))tmm$op=candidate;
    }
}
