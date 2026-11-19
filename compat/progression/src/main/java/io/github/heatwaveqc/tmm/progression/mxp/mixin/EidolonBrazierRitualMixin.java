package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tmm.progression.mxp.MxpMod;
import io.github.heatwaveqc.tmm.progression.mxp.core.E6bSpawnCatalog;
import io.github.heatwaveqc.tmm.progression.mxp.core.RitualRewards;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
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
import java.util.UUID;

@Pseudo
@Mixin(targets="alexthw.eidolon_repraised.common.tile.BrazierTileEntity",remap=false)
public abstract class EidolonBrazierRitualMixin implements E6bSpawnScope.Observer {
    @Unique private UUID tmm$actor,tmm$operation;
    @Unique private String tmm$recipe;
    @Unique private int tmm$accepted;
    @Unique private static final String TMM_EIDOLON="tmm_progression_brazier_rite";
    @Unique private static boolean tmm$supported(){return ModList.get().getModContainerById("eidolon_repraised").map(c->"0.5.0.8".equals(c.getModInfo().getVersion().toString())).orElse(false);}
    @Unique private boolean tmm$burning(){return Boolean.TRUE.equals(MxpReflect.field(this,"burning"));}
    @Unique private Object tmm$ritual(){return MxpReflect.field(this,"ritual");}
    @Unique private ServerLevel tmm$level(){
        return tmm$supported()&&MxpMod.active()&&(Object)this instanceof BlockEntity b&&b.getLevel() instanceof ServerLevel level&&level.getServer().isSameThread()&&level.getBlockEntity(b.getBlockPos())==b?level:null;
    }
    @Unique private void tmm$clear(){tmm$actor=null;tmm$operation=null;tmm$recipe=null;tmm$accepted=0;}
    @Unique private void tmm$manual(boolean prior,Player player){
        if(prior||!tmm$burning()||!(player instanceof ServerPlayer p)||!MxpPlayerPolicy.eligible(p)||tmm$level()!=p.level())return;
        tmm$clear();tmm$actor=p.getUUID();tmm$operation=UUID.randomUUID();((BlockEntity)(Object)this).setChanged();
    }
    @WrapMethod(method="onActivated(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/ItemInteractionResult;",require=0,remap=false)
    private ItemInteractionResult tmm$activate(BlockState state,BlockPos pos,Player player,InteractionHand hand,Operation<ItemInteractionResult> original){
        boolean prior=tmm$burning();ItemInteractionResult result=original.call(state,pos,player,hand);tmm$manual(prior,player);return result;
    }
    @WrapMethod(method="startBurning(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",require=0,remap=false)
    private void tmm$ignite(Player player,Level level,BlockPos pos,Operation<Void> original){
        boolean prior=tmm$burning();original.call(player,level,pos);tmm$manual(prior,player);
    }
    @Inject(method={"startBurning()V","extinguish()V"},at=@At("HEAD"),require=0,remap=false)
    private void tmm$reset(CallbackInfo ci){tmm$clear();}
    @Inject(method="lambda$tick$3(Lalexthw/eidolon_repraised/recipe/RitualRecipe;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$selected(@Coerce Object recipe,CallbackInfo ci){
        if(tmm$operation==null||!tmm$burning())return;
        String id=E6bSpawnScope.eidolonId(tmm$level(),recipe);
        if(!E6bSpawnScope.eidolonNativeMatches(E6bSpawnCatalog.eidolon(id),tmm$ritual())){tmm$clear();return;}
        tmm$recipe=id;tmm$accepted=0;((BlockEntity)(Object)this).setChanged();
    }
    @WrapMethod(method="tick()V",require=0,remap=false)
    private void tmm$tick(Operation<Void> original){
        E6bSpawnScope.run(tmm$operation!=null&&tmm$burning()&&tmm$level()!=null?this:null,original::call);
    }
    @Override public void tmm$spawned(Object ritual,ServerLevel level,Entity entity){
        if(tmm$operation==null||tmm$ritual()!=ritual||tmm$level()!=level||!tmm$burning())return;
        var proof=E6bSpawnCatalog.eidolon(tmm$recipe);
        if(!E6bSpawnScope.eidolonNativeMatches(proof,ritual))return;
        tmm$accepted=Math.min(proof.count(),tmm$accepted+E6bSpawnScope.units(proof,ritual,entity));((BlockEntity)(Object)this).setChanged();
    }
    @Override public void tmm$timeChanged(Object ritual,ServerLevel level){
        if(tmm$operation==null||tmm$ritual()!=ritual||tmm$level()!=level||!tmm$burning())return;
        var proof=E6bSpawnCatalog.eidolon(tmm$recipe);
        if(E6bSpawnScope.timeProof(proof)&&E6bSpawnScope.eidolonNativeMatches(proof,ritual)){
            tmm$accepted=1;((BlockEntity)(Object)this).setChanged();
        }
    }
    @WrapMethod(method="complete()V",require=0,remap=false)
    private void tmm$complete(Operation<Void> original){
        ServerLevel level=tmm$level();String id=tmm$recipe;UUID actor=tmm$actor,operation=tmm$operation;
        var proof=E6bSpawnCatalog.eidolon(id);
        boolean valid=level!=null&&actor!=null&&operation!=null&&tmm$burning()&&proof!=null&&tmm$accepted>=proof.count()&&E6bSpawnScope.eidolonNativeMatches(proof,tmm$ritual());
        tmm$clear();original.call();
        if(valid&&tmm$level()==level)RitualSettlement.settle(level.getServer().getPlayerList().getPlayer(actor),new RitualRewards.Receipt(id,operation,actor,true,false));
    }
    @Inject(method="saveAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$save(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        if(tmm$burning()&&tmm$operation!=null&&tmm$actor!=null){
            CompoundTag data=new CompoundTag();data.putUUID("actor",tmm$actor);data.putUUID("operation",tmm$operation);
            if(tmm$recipe!=null){data.putString("recipe",tmm$recipe);data.putInt("accepted",tmm$accepted);}
            tag.put(TMM_EIDOLON,data);
        }else tag.remove(TMM_EIDOLON);
    }
    @Inject(method="loadAdditional(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/core/HolderLookup$Provider;)V",at=@At("RETURN"),require=0,remap=false)
    private void tmm$load(CompoundTag tag,HolderLookup.Provider registries,CallbackInfo ci){
        tmm$clear();CompoundTag data=tag.getCompound(TMM_EIDOLON);String id=data.getString("recipe");
        if(!tmm$supported()||!tmm$burning()||!data.hasUUID("actor")||!data.hasUUID("operation"))return;
        var proof=E6bSpawnCatalog.eidolon(id);
        if(!id.isEmpty()&&!E6bSpawnScope.eidolonNativeMatches(proof,tmm$ritual()))return;
        tmm$actor=data.getUUID("actor");tmm$operation=data.getUUID("operation");tmm$recipe=id.isEmpty()?null:id;
        tmm$accepted=proof==null?0:Math.max(0,Math.min(proof.count(),data.getInt("accepted")));
    }
}
