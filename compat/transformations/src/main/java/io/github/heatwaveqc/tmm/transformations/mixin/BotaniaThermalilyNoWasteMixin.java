package io.github.heatwaveqc.tmm.transformations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tmm.transformations.botania.ThermalilyLavaPolicy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Botania Revived 2.0.2 FluidGeneratorBlockEntity.tickFlower contains:
 *
 * if (burnTime == 0 && getMana() < getMaxMana()) {
 *     // Finds and REMOVES a source even when cooldown > 0
 * }
 *
 * For Thermalily only, mask the one getMana() value in the collector
 * condition during cooldown. This leaves all mana storage and tick
 * behavior unchanged and does not affect Hydroangeas.
 *
 * This is a narrow port-specific mixin and must be tested against the
 * pinned Botania Revived 2.0.2 binary before promotion.
 */
@Pseudo
@Mixin(targets = "velrondevs.botania.common.block.flower.generating.FluidGeneratorBlockEntity", remap = false)
public abstract class BotaniaThermalilyNoWasteMixin {
    private static final String THERMALILY_CLASS =
            "velrondevs.botania.common.block.flower.generating.ThermalilyBlockEntity";

    @Shadow(remap = false) protected int burnTime;
    @Shadow(remap = false) protected int cooldown;

    @ModifyExpressionValue(
            method = "tickFlower()V",
            at = @At(value = "INVOKE",
                    target = "Lvelrondevs/botania/common/block/flower/generating/FluidGeneratorBlockEntity;getMana()I",
                    remap = false),
            remap = false,
            require = 1)
    private int tmm$maskManaOnlyWhenThermalilyCooling(int actualMana) {
        if (getClass().getName().equals(THERMALILY_CLASS)
                && !ThermalilyLavaPolicy.mayCollectNewLava(
                    actualMana, Integer.MAX_VALUE, burnTime, cooldown)) {
            // The actual max-mana guard is native; this mixin only blocks
            // additional lava intake during cooldown.
            return Integer.MAX_VALUE;
        }
        return actualMana;
    }
}
