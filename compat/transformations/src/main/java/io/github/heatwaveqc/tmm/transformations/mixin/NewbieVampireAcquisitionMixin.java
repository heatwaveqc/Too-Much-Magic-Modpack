package io.github.heatwaveqc.tmm.transformations.mixin;

import net.minecraft.server.level.ServerPlayer;
import io.github.heatwaveqc.tmm.transformations.core.NativeSpeciesJoinGuard;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** JAWR vampire entrypoint: enforce shared acquisition policy; cures remain untouched. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireLeveling", remap = false)
public abstract class NewbieVampireAcquisitionMixin {
    @Inject(method = "setLevel(Lnet/minecraft/server/level/ServerPlayer;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$denyNewbieVampire(ServerPlayer player, int level, CallbackInfo ci) {
        if (level > 0 && !NativeSpeciesJoinGuard.permit(
                player, ProviderState.JAWR_VAMPIRE, "Vampire")) {
            ci.cancel();
        }
    }
}
