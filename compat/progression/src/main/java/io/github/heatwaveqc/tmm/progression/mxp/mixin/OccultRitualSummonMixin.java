package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.OccultRitualContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="com.klikli_dev.occultism.common.ritual.SummonRitual",remap=false)
public abstract class OccultRitualSummonMixin {
    @WrapOperation(method="spawnEntity(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$summoned(Level level,Entity entity,Operation<Boolean> original){
        boolean result=original.call(level,entity);
        OccultRitualContext.insertedSummon(this,level,entity,result);
        return result;
    }
}
