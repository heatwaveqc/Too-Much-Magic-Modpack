package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.client.ClientVision;
import io.github.heatwaveqc.tmm.progression.foundation.AstralDarknessPolicy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Optional integration with More Darkness 1.21.1's native lightmap rescaler.
 * Runs AFTER its own target luminance and eye-adaptation calculations, so our
 * server-owned personal Astral stage does not rewrite vendor config or gamma.
 *
 * @Pseudo and optional client mixin config allow Progression without More Darkness.
 * The exact published 2.0.1 NeoForge bytecode still needs in-pack acceptance.
 */
@Pseudo
@Mixin(targets = "com.payangar.moredarkness.darkness.DarknessCalculator", remap = false)
public abstract class MoreDarknessLightmapMixin {
    @Inject(method = "darken(III)I", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
    private static void tmm$applyPersonalAstralNightSight(int vanilla, int blockIndex, int skyIndex,
                                                         CallbackInfoReturnable<Integer> cir) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) return;
        // Native Night Vision, water vision, and optical-debuff semantics win.
        if (client.player.hasEffect(MobEffects.NIGHT_VISION)
                || (client.player.hasEffect(MobEffects.CONDUIT_POWER) && client.player.getWaterVision() > 0)
                || client.player.hasEffect(MobEffects.BLINDNESS)
                || client.player.hasEffect(MobEffects.DARKNESS)) return;
        int stage = ClientVision.astralStage();
        if (stage == AstralDarknessPolicy.REGULAR) return;
        int result = AstralDarknessPolicy.color(
                vanilla, cir.getReturnValueI(), skyIndex,
                client.level.getTimeOfDay(0), client.level.getMoonBrightness(), stage);
        cir.setReturnValue(result);
    }
}
