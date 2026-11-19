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
@Mixin(targets="com.rekindled.embers.compat.curios.CuriosCompat",remap=false)
public abstract class EmberCuriosPaymentMixin {
    @WrapOperation(method="removeEmber",at=@At(value="INVOKE",target="Lcom/rekindled/embers/api/power/IEmberCapability;removeAmount(DZ)D"),require=0,remap=false)
    private static double tmm$curioDebit(@Coerce Object cap,double request,boolean remove,Operation<Double> original){
        double before=NativePaidSources.Embers.before(cap);
        double result=original.call(cap,request,remove);
        NativePaidSources.Embers.removed(cap,request,remove,result,before);
        return result;
    }
}
