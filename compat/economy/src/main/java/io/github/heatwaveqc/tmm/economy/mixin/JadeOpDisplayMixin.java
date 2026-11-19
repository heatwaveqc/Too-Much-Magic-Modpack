package io.github.heatwaveqc.tmm.economy.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Display-only: Jade formats both current and capacity using this unit.
 * Do not modify measurements, provider payloads or any non-FE energy system. */
@Pseudo
@Mixin(targets = "snownee.jade.api.view.EnergyView", remap = false)
public abstract class JadeOpDisplayMixin {
    @ModifyVariable(method = "read(Lnet/minecraft/nbt/CompoundTag;Ljava/lang/String;)Lsnownee/jade/api/view/EnergyView;",
            at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private static String tmm$opUnit(String unit) {
        return "FE".equals(unit) || "RF".equals(unit) ? "OP" : unit;
    }
}
