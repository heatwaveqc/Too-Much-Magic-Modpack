package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.SpellMiningScope;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/** Only a returning native removal inside the authenticated per-position Fell scope pays. */
@Pseudo
@Mixin(targets="com.hollingsworth.arsnouveau.api.util.BlockUtil",remap=false)
public abstract class ArsFellRemovalMixin {
    @WrapMethod(method="destroyBlockSafelyWithoutSound(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Z)Z",remap=false)
    private static boolean tmm$fell(Level level,BlockPos pos,boolean drop,Operation<Boolean> original){
        return SpellMiningScope.remove(level,pos,()->original.call(level,pos,drop));
    }
}
