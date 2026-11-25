package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.SummonEntitiesRitualEvent",remap=false)
public abstract class JawrRitualEntityMixin {
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);JawrRitualOutput.spawned(this,level,entity,accepted);return accepted;
    }
}
