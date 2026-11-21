package io.github.heatwaveqc.tyr.progression.client.mixin;

import io.github.heatwaveqc.tyr.progression.client.ClientKeyDefaults;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Earlier than Default Options' POST_LOAD application and auto-save.
 * Must NOT use the current options.txt during the first client tick: by then
 * it can contain keys not present before the launch.
 */
@Mixin(Options.class)
public abstract class OptionsOriginalKeyCaptureMixin {
    @Inject(method = "load()V", at = @At("HEAD"))
    private void tmm$capturePlayerBindingsBeforeDefaultOptions(CallbackInfo ci) {
        ClientKeyDefaults.captureOriginalOptions(((Options) (Object) this).getFile().toPath());
    }
}
