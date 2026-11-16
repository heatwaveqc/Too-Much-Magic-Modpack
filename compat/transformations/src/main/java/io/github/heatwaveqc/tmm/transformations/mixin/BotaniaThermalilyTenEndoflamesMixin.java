package io.github.heatwaveqc.tmm.transformations.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Thermalily balance target: one burning Thermalily == ten burning Endoflames.
 *
 * Pinned Botania Revived 2.0.2 sources:
 *   Endoflame: +3 mana every 2 ticks = 1.5 mana/tick.
 *   Thermalily: +45 mana every getGenerationDelay() ticks.
 *   Target: +45 every 3 ticks = 15 mana/tick = 10 Endoflames.
 *
 * Only Thermalily changes. The shared fluid generator still provides the
 * original 600-tick burn, 750-mana local buffer, and mana-network transfer.
 * Works together with the separate no-cooldown Thermalily mixin.
 */
@Pseudo
@Mixin(targets = "velrondevs.botania.common.block.flower.generating.FluidGeneratorBlockEntity",
       remap = false)
public abstract class BotaniaThermalilyTenEndoflamesMixin {
    private static final String THERMALILY_CLASS =
            "velrondevs.botania.common.block.flower.generating.ThermalilyBlockEntity";
    private static final int THERMALILY_GENERATION_INTERVAL_TICKS = 3;

    @Inject(method = "getGenerationDelay()I", at = @At("HEAD"),
            cancellable = true, remap = false, require = 1)
    private void tmm$tenEndoflamesPerThermalily(CallbackInfoReturnable<Integer> result) {
        if (getClass().getName().equals(THERMALILY_CLASS)) {
            result.setReturnValue(THERMALILY_GENERATION_INTERVAL_TICKS);
        }
    }
}
