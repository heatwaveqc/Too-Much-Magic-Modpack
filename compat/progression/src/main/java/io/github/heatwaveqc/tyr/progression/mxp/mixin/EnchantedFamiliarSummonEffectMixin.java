package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.EnchantedCurseWitness;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Pseudo
@Mixin(targets="net.favouriteless.enchanted.common.enchanted.circle_magic.rites.SummonFamiliarRite",remap=false)
public abstract class EnchantedFamiliarSummonEffectMixin{
    @Unique private Entity tmm$spawned;
    @WrapOperation(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$summon(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);
        if(accepted&&EnchantedCurseWitness.effectEligible(this,level,"enchanted:familiar_summon"))tmm$spawned=entity;
        return accepted;
    }
    @WrapOperation(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;teleportTo(DDD)V"),require=0,remap=false)
    private void tmm$move(Entity entity,double x,double y,double z,Operation<Void> original){
        var before=entity.position();original.call(entity,x,y,z);
        if(entity.level() instanceof ServerLevel level)EnchantedCurseWitness.effect(this,level,"enchanted:familiar_summon",!entity.isRemoved()&&before.distanceToSqr(entity.position())>1.0e-6&&EnchantedCurseWitness.familiar(this,level,entity));
    }
    @Inject(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At("HEAD"),require=0,remap=false)
    private void tmm$begin(@Coerce Object params,CallbackInfoReturnable<Boolean> cir){tmm$spawned=null;}
    @Inject(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At("RETURN"),require=0,remap=false)
    private void tmm$end(@Coerce Object params,CallbackInfoReturnable<Boolean> cir){
        Entity entity=tmm$spawned;tmm$spawned=null;
        if(entity!=null&&entity.level() instanceof ServerLevel level)EnchantedCurseWitness.effect(this,level,"enchanted:familiar_summon",!entity.isRemoved()&&EnchantedCurseWitness.familiar(this,level,entity));
    }
}
