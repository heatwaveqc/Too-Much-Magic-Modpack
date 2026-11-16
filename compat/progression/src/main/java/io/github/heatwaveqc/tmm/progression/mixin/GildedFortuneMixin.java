package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.runtime.GildedRules;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Adds to Fortune inside its native ore formula; no second loot roll or item mutation. */
@Mixin(ApplyBonusCount.class)
public abstract class GildedFortuneMixin {
    @ModifyArg(method = "run",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$Formula;calculateNewCount(Lnet/minecraft/util/RandomSource;II)I"),
        index = 2)
    private int gilded$fortuneLevel(int actual, @Local(argsOnly = true) LootContext context) {
        if (!(context.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof ServerPlayer player)) return actual;
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool == null || tool.isEmpty()) return actual;
        return actual + GildedRules.extraFortune(player,tool,context);
    }
}
