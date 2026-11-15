package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Exact-binary JAWR 0.5.39 setLevel and increaseLevel. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireLeveling", remap = false)
public abstract class JAWRVampireLevelingMixin {
    @Inject(method = "setLevel(Lnet/minecraft/server/level/ServerPlayer;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$beforeJAWRLevel(ServerPlayer player, int level, CallbackInfo ci) {
        if (!VampireChassisRuntime.onJAWRSetLevel(player, level)) ci.cancel();
    }

    @Inject(method = "setLevel(Lnet/minecraft/server/level/ServerPlayer;I)V",
            at = @At("RETURN"), remap = false)
    private void tmm$afterJAWRLevel(ServerPlayer player, int level, CallbackInfo ci) {
        VampireChassisRuntime.afterJAWRSetLevel(player);
    }

    @Inject(method = "increaseLevel(Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At("RETURN"), remap = false)
    private void tmm$afterJAWRIncrease(ServerPlayer player, CallbackInfo ci) {
        VampireChassisRuntime.onJAWRLevelAdvanced(player);
    }
}
