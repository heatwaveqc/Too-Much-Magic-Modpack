package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpCastHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Complete only after a returning Iron's cast, not merely its prepayment event. */
@Pseudo
@Mixin(targets = "io.redspace.ironsspellbooks.api.spells.AbstractSpell", remap = false)
public abstract class MxpIronsCastMixin {
    @Inject(method = "castSpell", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$ironCastReturned(CallbackInfo ci) {
        if (!io.github.heatwaveqc.tyr.progression.mxp.MxpMod.active()) return;
        MxpCastHooks.ironCastReturned();
    }
}
