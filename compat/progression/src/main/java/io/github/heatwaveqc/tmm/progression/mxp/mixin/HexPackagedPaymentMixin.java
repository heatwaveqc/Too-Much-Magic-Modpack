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
@Mixin(targets="at.petrak.hexcasting.api.casting.eval.env.PackagedItemCastEnv",remap=false)
public abstract class HexPackagedPaymentMixin {
    @WrapOperation(method="extractMediaEnvironment",at=@At(value="INVOKE",target="Lat/petrak/hexcasting/api/addldata/ADMediaHolder;withdrawMedia(JZ)J"),require=0,remap=false)
    private long tmm$paid(@Coerce Object holder,long cost,boolean simulate,Operation<Long> original){
        double before=NativePaidSources.Hex.before(holder);
        long result=original.call(holder,cost,simulate);
        NativePaidSources.Hex.removed(holder,cost,result,before,simulate);
        return result;
    }
}
