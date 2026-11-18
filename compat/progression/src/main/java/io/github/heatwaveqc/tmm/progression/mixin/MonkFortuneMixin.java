package io.github.heatwaveqc.tmm.progression.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.heatwaveqc.tmm.progression.runtime.NativeLootBonuses;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Bare Monk fist behaves like a Fortune-enchanted mining tool inside the
 * ORIGINAL loot roll. Same target as the experimental Gilded virtual-Fortune
 * mixin; both are additive and their faction conditions are distinct.
 */
@Mixin(ApplyBonusCount.class)
public abstract class MonkFortuneMixin {
    @ModifyArg(method = "run",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$Formula;calculateNewCount(Lnet/minecraft/util/RandomSource;II)I"),
            index = 2)
    private int tmm$barehandFortune(int actual, @Local(argsOnly = true) LootContext context) {
        if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof ServerPlayer player))
            return actual;
        return actual + NativeLootBonuses.monkFortune(player, context) + NativeLootBonuses.configuredFortune(player,context);
    }
}
