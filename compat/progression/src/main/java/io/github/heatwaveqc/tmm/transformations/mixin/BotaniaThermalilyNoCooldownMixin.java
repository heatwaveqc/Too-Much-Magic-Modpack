package io.github.heatwaveqc.tmm.transformations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Botania Revived 2.0.2 Thermalily: remove only the cooldown between
 * burns. FluidGeneratorBlockEntity still enforces burnTime == 0 and
 * getMana() < getMaxMana() before consuming a lava source.
 *
 * This preserves the original 600-tick burn and mana-per-tick values,
 * native storage, and all other generating flowers. A direct override
 * of getCooldownTime is smaller and safer than modifying the shared
 * fluid-consumption loop.
 */
@Pseudo
@Mixin(targets = "velrondevs.botania.common.block.flower.generating.ThermalilyBlockEntity",
       remap = false)
public abstract class BotaniaThermalilyNoCooldownMixin {
    @Inject(method = "getCooldownTime(Z)I", at = @At("HEAD"),
            cancellable = true, remap = false, require = 1)
    private void tmm$noThermalilyCooldown(boolean finishedPrevious,
                                           CallbackInfoReturnable<Integer> result) {
        result.setReturnValue(0);
    }
}
