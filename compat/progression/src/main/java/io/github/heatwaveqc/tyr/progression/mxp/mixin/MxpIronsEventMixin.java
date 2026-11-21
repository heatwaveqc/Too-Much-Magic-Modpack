package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpCastHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Record undiscounted nominal price before Iron's actual mana write. */
@Pseudo
@Mixin(targets = "io.redspace.ironsspellbooks.api.events.SpellOnCastEvent", remap = false)
public abstract class MxpIronsEventMixin {
    @Inject(method = "<init>", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$ironPriceConstructed(CallbackInfo ci) {
        if (!io.github.heatwaveqc.tyr.progression.mxp.MxpMod.active()) return;
        MxpCastHooks.ironQuoted(this);
    }
    @Inject(method="getManaCost",at=@At("RETURN"),remap=false,require=0,cancellable=true)
    private void tmm$discountNativeCost(org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(MxpCastHooks.ironCost(this,cir.getReturnValue()));
    }
}
