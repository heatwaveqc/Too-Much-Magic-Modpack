package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import io.github.heatwaveqc.tmm.progression.runtime.forms.LichSoulCapacity;
@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.capabilities.soulenergy.SEImp",remap=false)
public abstract class GoetyArcaCapacityMixin {
    @ModifyExpressionValue(method="increaseSE(I)Z",at=@At(value="INVOKE",target="Lnet/neoforged/neoforge/common/ModConfigSpec$ConfigValue;get()Ljava/lang/Object;"),require=0,allow=2,remap=false)
    private Object tmm$ownerCap(Object cap){return LichSoulCapacity.capRead(cap,this);}
}
