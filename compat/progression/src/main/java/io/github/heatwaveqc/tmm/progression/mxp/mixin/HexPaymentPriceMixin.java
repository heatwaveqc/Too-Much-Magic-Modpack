package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.NativePaidSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets="at.petrak.hexcasting.api.casting.eval.CastingEnvironment",remap=false)
public abstract class HexPaymentPriceMixin {
    @WrapOperation(method="extractMedia",at=@At(value="INVOKE",target="Lat/petrak/hexcasting/api/casting/eval/CastingEnvironment;extractMediaEnvironment(J)J"),require=0,remap=false)
    private long tmm$price(@Coerce Object env,long cost,Operation<Long> original){
        NativePaidSources.Hex.price(env,cost);
        return original.call(env,cost);
    }
}
