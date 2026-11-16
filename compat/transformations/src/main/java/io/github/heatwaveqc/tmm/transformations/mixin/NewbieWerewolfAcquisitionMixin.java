package io.github.heatwaveqc.tmm.transformations.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** JAWR lycanthrope: intercept authoritative positive level writes, not only infection items. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.werewolf.WerewolfLeveling", remap = false)
public abstract class NewbieWerewolfAcquisitionMixin {
    @Inject(method = "setLevel(Lnet/minecraft/server/level/ServerPlayer;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$denyNewbieWerewolf(ServerPlayer player, int level, CallbackInfo ci) {
        if (level > 0 && player.getTags().contains("tmm_newbie")) {
            ci.cancel();
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                "Lycanthropy is unavailable in Newbie Mode. Complete the Introduction graduation quest first."));
        }
    }
}
