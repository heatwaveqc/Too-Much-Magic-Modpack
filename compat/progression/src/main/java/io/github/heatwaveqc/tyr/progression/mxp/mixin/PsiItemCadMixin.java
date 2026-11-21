package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import io.github.heatwaveqc.tyr.progression.mxp.hooks.PsiCastHooks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Source-verified Psi 110 standard CAD cast boundaries, optional on other installations. */
@Pseudo
@Mixin(targets="vazkii.psi.common.item.ItemCAD",remap=false)
public abstract class PsiItemCadMixin {
    @WrapOperation(method="cast",at=@At(value="INVOKE",target="Lvazkii/psi/api/spell/PreSpellCastEvent;getCost()I"),require=0,remap=false)
    private static int tmm$postedCost(@Coerce Object event,Operation<Integer> original){
        return PsiCastHooks.price(event,original.call(event));
    }
    @WrapOperation(method="cast",at=@At(value="INVOKE",target="Lvazkii/psi/common/core/handler/PlayerData;deductPsi(IIZ)V"),require=0,remap=false)
    private static void tmm$observeDebit(@Coerce Object data,int psi,int cooldown,boolean sync,Operation<Void> original){
        var before=PsiCastHooks.before(data,psi);
        original.call(data,psi,cooldown,sync);
        PsiCastHooks.after(data,psi,before);
    }
    @Inject(method="cast",at=@At("RETURN"),require=0,remap=false)
    private static void tmm$clearCast(CallbackInfoReturnable<?> cir){PsiCastHooks.clear();}
}
