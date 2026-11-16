package io.github.heatwaveqc.tmm.mxp.mixin;

import io.github.heatwaveqc.tmm.mxp.hooks.MxpCastHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Record undiscounted nominal price before Iron's actual mana write. */
@Mixin(targets = "io.redspace.ironsspellbooks.api.events.SpellOnCastEvent", remap = false)
public abstract class MxpIronsEventMixin {
    @Inject(method = "<init>", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$ironPriceConstructed(CallbackInfo ci) {
        MxpCastHooks.ironQuoted(this);
    }
}
