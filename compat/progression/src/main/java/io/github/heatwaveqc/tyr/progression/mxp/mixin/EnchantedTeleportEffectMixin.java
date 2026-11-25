package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.EnchantedCurseWitness;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.portal.DimensionTransition;
@Pseudo
@Mixin(targets={"net.favouriteless.enchanted.common.enchanted.circle_magic.rites.TransposeEntityRite"},remap=false)
public abstract class EnchantedTeleportEffectMixin{
    @WrapOperation(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;teleportTo(DDD)V"),require=0,remap=false)
    private void tmm$move(Entity entity,double x,double y,double z,Operation<Void> original){
        Vec3 before=entity.position();original.call(entity,x,y,z);
        if(entity.level() instanceof ServerLevel level&&!entity.isRemoved())EnchantedCurseWitness.effect(this,level,"enchanted:teleport",before.distanceToSqr(entity.position())>1.0e-6&&entity.position().distanceToSqr(new Vec3(x,y,z))<1.0);
    }
    @WrapOperation(method="onStart(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;changeDimension(Lnet/minecraft/world/level/portal/DimensionTransition;)Lnet/minecraft/world/entity/Entity;"),require=0,remap=false)
    private Entity tmm$dimension(Entity entity,DimensionTransition transition,Operation<Entity> original){
        var before=entity.level();Entity result=original.call(entity,transition);
        if(result!=null&&result.level() instanceof ServerLevel level&&!result.isRemoved())EnchantedCurseWitness.effect(this,level,"enchanted:teleport",before!=level&&result.getUUID().equals(entity.getUUID())&&level.getEntity(result.getUUID())==result);
        return result;
    }
}
