package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.client.ClientVision;
import io.github.heatwaveqc.tmm.progression.foundation.AstralDarknessPolicy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * More Darkness has a separate post-processing distance veil. Suppress it
 * for Newbies and Resonance+; otherwise natural night sight could brighten
 * terrain while the shader still artificially crushes visibility at 8 blocks.
 * Keep native eye adaptation / dazzle for Regular and Astral players.
 */
@Pseudo
@Mixin(targets = "com.payangar.moredarkness.darkness.PerceptionEffects", remap = false)
public abstract class MoreDarknessPerceptionMixin {
    @Inject(method = "processFrame(Lnet/minecraft/client/Minecraft;F)V",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private static void tmm$skipNewbieDarkPerception(Minecraft client, float tickDelta, CallbackInfo ci) {
        if (ClientVision.astralStage() == AstralDarknessPolicy.NEWBIE) ci.cancel();
    }

    @Inject(method = "processDarkSight(Lnet/minecraft/client/Minecraft;F)V",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private static void tmm$preserveAstralRangeAndPotions(Minecraft client, float tickDelta, CallbackInfo ci) {
        if (client.player == null) return;
        if (client.player.hasEffect(MobEffects.NIGHT_VISION)
                || (client.player.hasEffect(MobEffects.CONDUIT_POWER) && client.player.getWaterVision() > 0)
                || AstralDarknessPolicy.suppressDistanceVeil(ClientVision.astralStage())) {
            ci.cancel();
        }
    }
}
