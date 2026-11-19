package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.PsiCastHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Pseudo
@Mixin(targets="vazkii.psi.api.spell.SpellCastEvent",remap=false)
public abstract class PsiSpellCastEventMixin {
    @Inject(method="<init>",at=@At("RETURN"),require=0,remap=false)
    private void tmm$completed(CallbackInfo ci){PsiCastHooks.complete(this);}
}
