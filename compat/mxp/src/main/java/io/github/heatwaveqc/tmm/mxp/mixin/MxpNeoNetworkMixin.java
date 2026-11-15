package io.github.heatwaveqc.tmm.mxp.mixin;

import io.github.heatwaveqc.tmm.mxp.hooks.MxpNeoNetworkHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Native Neo Vitae creates PostSyphon strictly after a positive EV-network debit. */
@Mixin(targets = "com.breakinblocks.neovitae.api.event.AnimaEvent$PostSyphon", remap = false)
public abstract class MxpNeoNetworkMixin {
    @Inject(method = "<init>", at = @At("RETURN"), remap = false, require = 0)
    private void tmm$networkDebited(CallbackInfo ci) {
        MxpNeoNetworkHook.onPostSyphon(this);
    }
}
