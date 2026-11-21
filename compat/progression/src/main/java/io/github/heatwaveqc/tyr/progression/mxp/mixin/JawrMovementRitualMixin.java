package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets={"dev.sterner.witchery.content.ritual_event.PushMobsRitualEvent","dev.sterner.witchery.content.ritual_event.PullMobsRitualEvent"},remap=false)
public abstract class JawrMovementRitualMixin{
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"),require=0,remap=false)
    private void tmm$velocity(LivingEntity entity,Vec3 value,Operation<Void> original){
        Vec3 before=entity.getDeltaMovement();original.call(entity,value);
        if(entity.level() instanceof ServerLevel level&&!entity.isRemoved())JawrRitualOutput.changed(this,level,before.distanceToSqr(entity.getDeltaMovement())>1.0e-8);
    }
}
