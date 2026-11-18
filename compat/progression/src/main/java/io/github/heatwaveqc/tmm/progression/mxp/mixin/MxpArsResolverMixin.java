package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpCastHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Only actual Ars spending, never getResolveCost simulation. */
@Pseudo
@Mixin(targets = "com.hollingsworth.arsnouveau.api.spell.SpellResolver", remap = false)
public abstract class MxpArsResolverMixin {
    @Inject(method = "expendMana", at = @At("HEAD"), remap = false, require = 0)
    private void tmm$beforeArsDebit(CallbackInfo ci) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpCastHooks.arsSpendEntered(this);
    }

    @Inject(method = "expendMana", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$afterNativeArsDebit(CallbackInfo ci) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpCastHooks.arsNativeSpendReturned();
    }
}
