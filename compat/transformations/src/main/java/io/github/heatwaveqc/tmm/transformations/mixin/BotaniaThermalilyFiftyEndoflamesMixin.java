package io.github.heatwaveqc.tmm.transformations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Exactly 50 continuously burning Endoflames worth of mana generation.
 *
 * Pinned Botania Revived 2.0.2:
 *   Endoflame: 3 mana every 2 ticks = 1.5 mana/tick.
 *   Thermalily: 45 mana every tick (native, equivalent to 30 Endoflames).
 *
 * Owner's revised target: 75 mana/tick = 50 Endoflames.
 *
 * Modify only the manaPerTick field read in the shared fluid generator's
 * server-side burn branch, and only on actual Thermalily instances. Keep
 * getGenerationDelay() at its native 1, preserving the 600-tick burn, 750
 * mana buffer, zero-cooldown override, and other generating flowers.
 *
 * At continuous output, one 600-tick burn yields 45,000 mana per lava
 * source. Actual exported mana may be lower if mana storage backs up.
 */
@Pseudo
@Mixin(targets = "velrondevs.botania.common.block.flower.generating.FluidGeneratorBlockEntity",
       remap = false)
public abstract class BotaniaThermalilyFiftyEndoflamesMixin {
    private static final String THERMALILY_CLASS =
            "velrondevs.botania.common.block.flower.generating.ThermalilyBlockEntity";
    private static final int TARGET_MANA_PER_TICK = 75;

    @ModifyExpressionValue(
            method = "tickFlower()V",
            at = @At(value = "FIELD",
                    target = "Lvelrondevs/botania/common/block/flower/generating/FluidGeneratorBlockEntity;manaPerTick:I",
                    remap = false),
            remap = false,
            require = 1)
    private int tmm$fiftyEndoflamesPerThermalily(int originalManaPerTick) {
        return getClass().getName().equals(THERMALILY_CLASS)
                ? TARGET_MANA_PER_TICK : originalManaPerTick;
    }
}
