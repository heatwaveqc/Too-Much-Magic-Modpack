package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.dracolich.DracolichAcquisitionGuard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Block Goety becoming-lich only when the dragon already has JAWR lichdom. */
@Pseudo
@Mixin(targets = "com.Polarice3.Goety.utils.LichdomHelper", remap = false)
public abstract class GoetyDracolichAcquisitionMixin {
    @Inject(method = "setLich(Lnet/minecraft/world/entity/player/Player;Z)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void tmm$preventSecondLich(Player player, boolean lich,
                                              CallbackInfo ci) {
        if (lich && player instanceof ServerPlayer server
                && !DracolichAcquisitionGuard.permit(server,
                DracolichAcquisitionGuard.goety(player, lich), "Goety")) {
            ci.cancel();
        }
    }
}
