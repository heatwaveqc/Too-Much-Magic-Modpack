package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.JawrBrazierCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpPlayerPolicy;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RitualSettlement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Optional;
import java.util.UUID;
import java.util.LinkedHashSet;

@Pseudo
@Mixin(targets="dev.sterner.witchery.content.block.brazier.BrazierBlockEntity",remap=false)
public abstract class JawrBrazierRitualMixin {
    @Unique private UUID tmm$actor,tmm$operation;
    @Unique private String tmm$recipe;
    @Unique private LinkedHashSet<String> tmm$ignitionMatches;
    @Unique private Object tmm$nativeRecipe,tmm$passive;
    @Unique private boolean tmm$outcome,tmm$paid;
    @Unique private static final String TMM_BRAZIER="tmm_progression_brazier_ritual";
    @Unique private boolean tmm$supported(){
        return ModList.get().getModContainerById("witchery").map(c->"0.5.39+mc1.21.1".equals(c.getModInfo().getVersion().toString())).orElse(false);
    }
    @Unique private boolean tmm$active(){return Boolean.TRUE.equals(MxpReflect.field(this,"active"));}
    @Unique private ServerLevel tmm$level(){
        return tmm$supported()&&MxpMod.active()&&(Object)this instanceof BlockEntity b&&b.getLevel() instanceof ServerLevel l&&
            l.getServer().isSameThread()&&l.getBlockEntity(b.getBlockPos())==b?l:null;
    }
    @Unique private void tmm$resetWitness(){tmm$nativeRecipe=null;tmm$passive=null;tmm$outcome=false;tmm$paid=false;}
    @Unique private void tmm$clear(){tmm$actor=null;tmm$operation=null;tmm$recipe=null;tmm$resetWitness();}
    @Unique private void tmm$dirty(){((BlockEntity)(Object)this).setChanged();}
    @WrapMethod(method="handleIgnition(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/ItemInteractionResult;",require=0,remap=false)
    private ItemInteractionResult tmm$ignite(Player player,ItemStack item,Operation<ItemInteractionResult> original){
        boolean prior=tmm$active();
        boolean observe=!prior&&player instanceof ServerPlayer p&&MxpPlayerPolicy.eligible(p)&&tmm$level()==p.level();
        var matches=observe?new LinkedHashSet<String>():null;
        tmm$ignitionMatches=matches;
        ItemInteractionResult result;
        try{result=original.call(player,item);}finally{tmm$ignitionMatches=null;}
        if(!prior){tmm$clear();
            if(observe&&result==ItemInteractionResult.SUCCESS&&tmm$active()&&matches.size()==1){
                String recipe=matches.iterator().next();
                if(JawrBrazierCatalog.lookup(recipe)!=null){tmm$actor=player.getUUID();tmm$operation=UUID.randomUUID();tmm$recipe=recipe;tmm$dirty();}
            }
        }else if(!tmm$active()){tmm$clear();tmm$dirty();}
        return result;
    }
    @WrapOperation(method="handleIgnition(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/ItemInteractionResult;",at=@At(value="INVOKE",target="Lnet/minecraft/world/item/crafting/RecipeManager$CachedCheck;getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;"),require=0,remap=false)
    private Optional<?> tmm$candidate(RecipeManager.CachedCheck<?,?> check,RecipeInput input,Level level,Operation<Optional<?>> original){
        Optional<?> result=original.call(check,input,level);
        if(tmm$ignitionMatches!=null&&result!=null&&result.orElse(null) instanceof RecipeHolder<?> holder)tmm$ignitionMatches.add(holder.id().toString());
        return result;
    }
    @WrapMethod(method="tickPassive(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",require=0,remap=false)
    private void tmm$passive(ServerLevel level,BlockPos pos,Operation<Void> original){
        tmm$resetWitness();try{original.call(level,pos);}finally{tmm$resetWitness();}
    }
    @WrapMethod(method="tickSummoning(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",require=0,remap=false)
    private void tmm$summoning(ServerLevel level,BlockPos pos,Operation<Void> original){
        tmm$resetWitness();try{original.call(level,pos);}finally{tmm$resetWitness();}
    }
    @WrapOperation(method={"tickPassive(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V","tickSummoning(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V"},at=@At(value="INVOKE",target="Lnet/minecraft/world/item/crafting/RecipeHolder;value()Lnet/minecraft/world/item/crafting/Recipe;"),require=0,remap=false)
    private Recipe<?> tmm$selected(RecipeHolder<?> holder,Operation<Recipe<?>> original){
        Recipe<?> result=original.call(holder);
        if(tmm$operation!=null&&tmm$active()&&tmm$level()!=null){
            var rule=JawrBrazierCatalog.lookup(tmm$recipe);
            if(rule==null||!holder.id().toString().equals(tmm$recipe)||!Integer.valueOf(rule.altarPower()).equals(MxpReflect.call(result,"getAltarPower"))){tmm$clear();tmm$dirty();}
            else tmm$nativeRecipe=result;
        }
        return result;
    }
    @WrapOperation(method="tickPassive(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",at=@At(value="INVOKE",target="Ldev/sterner/witchery/core/api/BrazierPassive;onStartBrazier(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ldev/sterner/witchery/content/block/brazier/BrazierBlockEntity;)Z"),require=0,remap=false)
    private boolean tmm$startPassive(@Coerce Object passive,Level world,BlockPos pos,@Coerce Object brazier,Operation<Boolean> original){
        boolean result=original.call(passive,world,pos,brazier);
        var proof=JawrBrazierCatalog.lookup(tmm$recipe);
        if(result&&brazier==this&&proof!=null&&proof.passive()&&tmm$nativeRecipe!=null&&world==tmm$level()
            &&passive==MxpReflect.field(this,"brazierPassive")&&proof.outcome().equals(passive.getClass().getName())){
            tmm$passive=passive;tmm$outcome=true;
        }
        return result;
    }
    @WrapOperation(method="performSummoning(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ldev/sterner/witchery/content/recipe/brazier/BrazierSummoningRecipe;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(Level world,Entity entity,Operation<Boolean> original){
        boolean result=original.call(world,entity);
        var proof=JawrBrazierCatalog.lookup(tmm$recipe);
        if(result&&proof!=null&&!proof.passive()&&tmm$nativeRecipe!=null&&world==tmm$level()&&entity instanceof LivingEntity&&!(entity instanceof Player)
            &&proof.outcome().equals(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))tmm$outcome=true;
        return result;
    }
    @WrapOperation(method={"tickPassive(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V","performSummoning(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ldev/sterner/witchery/content/recipe/brazier/BrazierSummoningRecipe;)V"},at=@At(value="INVOKE",target="Ldev/sterner/witchery/core/api/AltarPowerConsumer;consumePower$default(Ldev/sterner/witchery/core/api/AltarPowerConsumer;IZILjava/lang/Object;)Z"),require=0,remap=false)
    private boolean tmm$power(@Coerce Object consumer,int amount,boolean flag,int mask,Object marker,Operation<Boolean> original){
        boolean result=original.call(consumer,amount,flag,mask,marker);
        var proof=JawrBrazierCatalog.lookup(tmm$recipe);
        if(result&&consumer==this&&tmm$nativeRecipe!=null&&proof!=null&&amount==proof.altarPower())tmm$paid=true;
        return result;
    }
    @Unique private RitualRewards.Receipt tmm$take(boolean passive){
        var proof=JawrBrazierCatalog.lookup(tmm$recipe);
        return proof!=null&&proof.passive()==passive&&tmm$level()!=null&&tmm$active()&&tmm$actor!=null&&tmm$operation!=null&&tmm$nativeRecipe!=null&&tmm$outcome&&tmm$paid
            ?new RitualRewards.Receipt(tmm$recipe,tmm$operation,tmm$actor,true,false):null;
    }
    @WrapMethod(method="completePassive()V",require=0,remap=false)
    private void tmm$completePassive(Operation<Void> original){
        var receipt=tmm$take(true);Object passive=tmm$passive;ServerLevel level=tmm$level();
        tmm$clear();original.call();if(receipt!=null){tmm$dirty();
            if(tmm$level()==level&&tmm$active()&&passive!=null&&MxpReflect.field(this,"brazierPassive")==passive)
                RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(receipt.actor()),receipt);}
    }
    @WrapMethod(method="completeSummoning(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",require=0,remap=false)
    private void tmm$completeSummoning(Level world,BlockPos pos,Operation<Void> original){
        var receipt=tmm$take(false);ServerLevel level=tmm$level();tmm$clear();original.call(world,pos);
        if(receipt!=null){tmm$dirty();if(world==level&&tmm$level()==level&&!tmm$active())
            RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(receipt.actor()),receipt);}
    }
    @Inject(method="setActive(Z)V",at=@At("HEAD"),require=0,remap=false)
    private void tmm$deactivate(boolean active,CallbackInfo ci){if(!active&&tmm$operation!=null){tmm$clear();tmm$dirty();}}
    @Inject(method="tick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",at=@At("HEAD"),require=0,remap=false)
    private void tmm$inactive(Level world,BlockPos pos,BlockState state,CallbackInfo ci){if(world instanceof ServerLevel&&!tmm$active()&&tmm$operation!=null){tmm$clear();tmm$dirty();}}
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider regs,CallbackInfo ci){
        if(tmm$active()&&tmm$actor!=null&&tmm$operation!=null&&JawrBrazierCatalog.lookup(tmm$recipe)!=null){
            CompoundTag data=new CompoundTag();data.putString("recipe",tmm$recipe);data.putUUID("actor",tmm$actor);data.putUUID("operation",tmm$operation);tag.put(TMM_BRAZIER,data);
        }else tag.remove(TMM_BRAZIER);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider regs,CallbackInfo ci){
        tmm$clear();CompoundTag data=tag.getCompound(TMM_BRAZIER);String id=data.getString("recipe");
        if(tmm$supported()&&tmm$active()&&JawrBrazierCatalog.lookup(id)!=null&&data.hasUUID("actor")&&data.hasUUID("operation")){
            tmm$actor=data.getUUID("actor");tmm$operation=data.getUUID("operation");tmm$recipe=id;
        }
    }
}
