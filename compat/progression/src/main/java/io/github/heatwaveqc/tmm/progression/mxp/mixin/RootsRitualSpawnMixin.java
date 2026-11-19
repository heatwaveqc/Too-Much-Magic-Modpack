package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof;
import io.github.heatwaveqc.tmm.progression.mxp.core.RootsRitualProof.Kind;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.RootsRitualContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
@Pseudo
@Mixin(targets={"elucent.rootsclassic.ritual.rituals.RitualCrafting","elucent.rootsclassic.ritual.rituals.RitualEngravedSword","elucent.rootsclassic.ritual.rituals.RitualSummoning"},remap=false)
public abstract class RootsRitualSpawnMixin {
    @WrapOperation(method="doEffect(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/Container;Ljava/util/List;Lnet/minecraft/nbt/CompoundTag;)V",at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawned(Level world,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(world,entity);RootsRitualContext.spawned(this,world,entity,accepted);return accepted;
    }
}
