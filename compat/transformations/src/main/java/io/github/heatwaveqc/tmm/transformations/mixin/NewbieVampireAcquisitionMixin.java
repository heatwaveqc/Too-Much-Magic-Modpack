package io.github.heatwaveqc.tmm.transformations.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Deny new JAWR vampire levels while the player has Newbie Mode. Do not block cures. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireLeveling", remap = false)
public abstract class NewbieVampireAcquisitionMixin {
    @Inject(method = "setLevel(Lnet/minecraft/server/level/ServerPlayer;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$denyNewbieVampire(ServerPlayer player, int level, CallbackInfo ci) {
        if (level > 0 && player.getTags().contains("tmm_newbie")) {
            ci.cancel();
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "Vampirism is unavailable in Newbie Mode. Complete the Introduction graduation quest first."));
        }
    }
}
