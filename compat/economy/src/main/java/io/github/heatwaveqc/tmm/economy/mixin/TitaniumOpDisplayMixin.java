package io.github.heatwaveqc.tmm.economy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Titanium 4.0.52's shared energy tooltip, including Replication machines.
 * Only the second literal is the formatted measurement. Preserve the header,
 * locale-sensitive numbers, formatting, and all energy calculations. */
@Pseudo
@Mixin(targets = "com.hrznstudio.titanium.client.screen.addon.EnergyBarScreenAddon", remap = false)
public abstract class TitaniumOpDisplayMixin {
    @ModifyArg(method = "getTooltip(II)Ljava/util/List;",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Component;literal(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 1), index = 0)
    private static String tmm$opSuffix(String text) {
        return text.endsWith(" FE") || text.endsWith(" RF")
                ? text.substring(0, text.length() - 2) + "OP" : text;
    }
}
