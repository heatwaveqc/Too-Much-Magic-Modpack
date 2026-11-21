package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.JawrRitualOutput;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import java.util.UUID;
@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.BindFamiliarRitualEvent",remap=false)
public abstract class JawrFamiliarBindRitualMixin {
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Ldev/sterner/witchery/feature/familiar/FamiliarHandler;bindOwnerAndFamiliar(Lnet/minecraft/server/level/ServerLevel;Ljava/util/UUID;Lnet/minecraft/world/entity/LivingEntity;)V"),require=0,remap=false)
    private void tmm$bind(@Coerce Object handler,ServerLevel level,UUID owner,LivingEntity entity,Operation<Void> original){
        boolean eligible=JawrRitualOutput.familiarOwner(this,level,owner)&&Boolean.FALSE.equals(MxpReflect.call(handler,"isBound",level,entity));
        original.call(handler,level,owner,entity);
        JawrRitualOutput.changed(this,level,eligible&&Boolean.TRUE.equals(MxpReflect.call(handler,"isBound",level,entity)));
    }
}
