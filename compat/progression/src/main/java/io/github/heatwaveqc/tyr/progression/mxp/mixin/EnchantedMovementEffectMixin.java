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
@Pseudo
@Mixin(targets={"net.favouriteless.enchanted.common.enchanted.circle_magic.rites.ImprisonmentRite","net.favouriteless.enchanted.common.enchanted.circle_magic.rites.SanctityRite"},remap=false)
public abstract class EnchantedMovementEffectMixin{
    @WrapOperation(method="onTick(Lnet/favouriteless/enchanted/common/enchanted/circle_magic/rites/Rite$RiteParams;)Z",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;addDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"),require=0,remap=false)
    private void tmm$movement(Entity entity,Vec3 delta,Operation<Void> original){
        Vec3 before=entity.getDeltaMovement();original.call(entity,delta);
        if(entity.level() instanceof ServerLevel level&&!entity.isRemoved())EnchantedCurseWitness.effect(this,level,"enchanted:movement",before.distanceToSqr(entity.getDeltaMovement())>1.0e-8);
    }
}
