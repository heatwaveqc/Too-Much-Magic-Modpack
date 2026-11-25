package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.NativePaidSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

/** Scope only the native immediate consume-media side effect, and retain the original cost. */
@Pseudo
@Mixin(targets="at.petrak.hexcasting.api.casting.eval.sideeffects.OperatorSideEffect$ConsumeMedia",remap=false)
public abstract class HexPaidMediaMixin {
    @WrapOperation(method="performEffect",at=@At(value="INVOKE",target="Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;extractMedia(J)J"),require=0,remap=false)
    private long tmm$receipt(@Coerce Object env,long cost,Operation<Long> original){
        return NativePaidSources.Hex.cast(env,cost,()->original.call(env,cost));
    }
}
