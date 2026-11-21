package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.MxpNeoNetworkHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Native Neo Vitae creates PostSyphon strictly after a positive EV-network debit. */
@Pseudo
@Mixin(targets = "com.breakinblocks.neovitae.api.event.AnimaEvent$PostSyphon", remap = false)
public abstract class MxpNeoNetworkMixin {
    @Inject(method = "<init>", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$networkDebited(CallbackInfo ci) {
        if (!io.github.heatwaveqc.tmm.progression.mxp.MxpMod.active()) return;
        MxpNeoNetworkHook.onPostSyphon(this);
    }
}
