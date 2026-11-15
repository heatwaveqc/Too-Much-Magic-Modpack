package io.github.heatwaveqc.tmm.transformations.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * CLIENT ONLY. Ordinary Vampirism players retain their normal blood bar.
 * When JAWR is active, its blood HUD is authoritative: suppress only
 * Vampirism's overlay, not its native blood data or skill controls.
 */
@Pseudo
@Mixin(targets = "de.teamlapen.vampirism.client.gui.overlay.BloodBarOverlay", remap = false)
public abstract class VampirismBloodBarMixin {
    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$hideAscendedMirror(GuiGraphics graphics, DeltaTracker delta, CallbackInfo ci) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        try {
            Class<?> attachment = Class.forName(
                    "dev.sterner.witchery.feature.affliction.vampire.VampirePlayerAttachment");
            Object data = attachment.getMethod("getData", Player.class).invoke(null, player);
            if (data == null) return;
            int level = ((Number) data.getClass().getMethod("getLevel").invoke(data)).intValue();
            if (level > 0) ci.cancel();
        } catch (ReflectiveOperationException | LinkageError e) {
            // No JAWR client state yet: preserve the normal Vampirism HUD.
        }
    }
}
