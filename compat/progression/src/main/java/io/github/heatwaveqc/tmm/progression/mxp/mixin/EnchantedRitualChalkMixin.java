package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.UUID;

@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.blocks.entity.GoldChalkBlockEntity",remap=false)
public abstract class EnchantedRitualChalkMixin implements E6bSpawnScope.EnchantedChalk {
    @Unique private UUID tmm$actor,tmm$operation;
    @Unique private String tmm$recipe;
    @Unique private static final String TMM_ENCHANTED="tmm_progression_enchanted_rite";
    @Unique private static boolean tmm$supported(){return ModList.get().getModContainerById("enchanted").map(c->"4.2.7".equals(c.getModInfo().getVersion().toString())).orElse(false);}
    @Unique private boolean tmm$initialising(){return Boolean.TRUE.equals(MxpReflect.field(this,"isInitialising"));}
    @Unique private void tmm$clear(){tmm$actor=null;tmm$operation=null;tmm$recipe=null;}
    @WrapMethod(method="use(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V",require=0,remap=false)
    private void tmm$use(Level world,BlockPos pos,Player player,Operation<Void> original){
        boolean prior=tmm$initialising();
        original.call(world,pos,player);
        if(prior||!tmm$initialising())return;
        tmm$clear();
        if(!tmm$supported()||!MxpMod.active()||!(world instanceof ServerLevel level)||!(player instanceof ServerPlayer p)||
            !MxpPlayerPolicy.eligible(p)||p.level()!=level||world.getBlockEntity(pos)!=(Object)this||!p.getUUID().equals(MxpReflect.field(this,"caster")))return;
        String id=E6bSpawnScope.enchantedId(level,MxpReflect.field(this,"type"));
        if(E6bSpawnCatalog.enchanted(id)!=null){tmm$actor=p.getUUID();tmm$operation=UUID.randomUUID();tmm$recipe=id;((BlockEntity)(Object)this).setChanged();}
    }
    @Override public RitualRewards.Receipt tmm$takeEnchanted(Object rite,ServerLevel level){
        UUID actor=tmm$actor,operation=tmm$operation;String id=tmm$recipe;
        tmm$clear();
        if(operation==null||actor==null||!tmm$supported()||!MxpMod.active()||
            !actor.equals(MxpReflect.field(MxpReflect.field(rite,"params"),"caster"))||
            !id.equals(E6bSpawnScope.enchantedId(level,MxpReflect.call(rite,"getType"))))return null;
        return new RitualRewards.Receipt(id,operation,actor,true,false);
    }
    @WrapOperation(method="serverTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/favouriteless/enchanted/common/blocks/entity/GoldChalkBlockEntity;)V",at=@At(value="INVOKE",target="Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite;start()V"),require=0,remap=false)
    private static void tmm$start(@Coerce Object rite,Operation<Void> original){
        if(!(MxpReflect.call(rite,"getLevel") instanceof ServerLevel level)||!(MxpReflect.call(rite,"getPos") instanceof BlockPos pos)||
            !(level.getBlockEntity(pos) instanceof E6bSpawnScope.EnchantedChalk chalk)){original.call(rite);return;}
        RitualRewards.Receipt receipt=chalk.tmm$takeEnchanted(rite,level);
        var proof=receipt==null?null:E6bSpawnCatalog.enchanted(receipt.recipeId());
        if(proof==null){original.call(rite);return;}
        int[] accepted={0};
        E6bSpawnScope.run((source,world,entity)->{if(source==rite&&world==level)accepted[0]=Math.min(proof.count(),accepted[0]+E6bSpawnScope.units(proof,source,entity));},()->original.call(rite));
        if(accepted[0]>=proof.count())RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(receipt.actor()),receipt);
    }
    @Inject(method={"cancel()V","detatch()V"},at=@At("HEAD"),require=0,remap=false)
    private void tmm$reset(CallbackInfo ci){tmm$clear();}
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        if(tmm$initialising()&&tmm$operation!=null&&tmm$actor!=null&&tmm$actor.equals(MxpReflect.field(this,"caster"))){
            CompoundTag data=new CompoundTag();data.putString("recipe",tmm$recipe);data.putUUID("actor",tmm$actor);data.putUUID("operation",tmm$operation);tag.put(TMM_ENCHANTED,data);
        }else tag.remove(TMM_ENCHANTED);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$clear();CompoundTag data=tag.getCompound(TMM_ENCHANTED);String id=data.getString("recipe");
        if(tmm$supported()&&tmm$initialising()&&E6bSpawnCatalog.enchanted(id)!=null&&data.hasUUID("actor")&&data.hasUUID("operation")&&data.getUUID("actor").equals(MxpReflect.field(this,"caster"))){
            tmm$recipe=id;tmm$actor=data.getUUID("actor");tmm$operation=data.getUUID("operation");
        }
    }
}
