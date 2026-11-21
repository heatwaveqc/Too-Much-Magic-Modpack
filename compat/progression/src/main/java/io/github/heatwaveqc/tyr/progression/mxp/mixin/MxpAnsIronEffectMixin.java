package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import io.github.heatwaveqc.tyr.progression.mxp.MxpMod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpCastHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** ANS 3.3.5: observe the actual paid invocation only after its native effect returns. */
@Pseudo
@Mixin(targets="com.otectus.arsnspells.casting.IronsCastLifecycle",remap=false)
public abstract class MxpAnsIronEffectMixin {
    @Inject(method="finishEffect",at=@At("RETURN"),remap=false,require=0)
    private static void tmm$afterPaidEffect(CallbackInfo ci) {
        if(MxpMod.active()) MxpCastHooks.ansIronEffectFinished();
    }
}
