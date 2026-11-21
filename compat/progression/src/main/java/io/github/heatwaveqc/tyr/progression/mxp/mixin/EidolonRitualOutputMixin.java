package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.E6bSpawnScope;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
@Pseudo
@Mixin(targets={"alexthw.eidolon_repraised.common.ritual.CraftingRitual","alexthw.eidolon_repraised.common.ritual.SummonRitual"},remap=false)
public abstract class EidolonRitualOutputMixin{
    @WrapOperation(method="start(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lalexthw/eidolon_repraised/api/ritual/Ritual$RitualResult;",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(Level level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);E6bSpawnScope.spawned(this,level,entity,accepted);return accepted;
    }
}
