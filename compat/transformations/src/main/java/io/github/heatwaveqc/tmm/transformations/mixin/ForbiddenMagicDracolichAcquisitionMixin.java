package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichAcquisitionGuard;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** JAWR 0.5.37 canonical setLevel: prevent a Goety Dracolich from gaining lichdom. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichdomLeveling",
        remap = false)
public abstract class ForbiddenMagicDracolichAcquisitionMixin {
    @Inject(method = "setLevel(Lnet/minecraft/server/level/ServerPlayer;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$preventSecondLich(ServerPlayer player, int level,
                                       CallbackInfo ci) {
        if (level > 0 && !DracolichAcquisitionGuard.permit(player,
                DracolichAcquisitionGuard.forbidden(player, level),
                "Forbidden Magic")) {
            ci.cancel();
        }
    }
}
