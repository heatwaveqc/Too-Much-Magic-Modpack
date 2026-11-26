package io.github.heatwaveqc.tyr.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.DirectDodge;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Mob.class)
public abstract class DirectMobAttackMixin {
    @WrapMethod(method="doHurtTarget")
    private boolean tmm$directDodge(Entity target,Operation<Boolean> original){
        return DirectDodge.attack((Mob)(Object)this,target,()->original.call(target));
    }
}
