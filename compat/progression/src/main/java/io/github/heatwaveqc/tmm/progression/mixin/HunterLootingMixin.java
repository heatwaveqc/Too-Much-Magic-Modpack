package io.github.heatwaveqc.tmm.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.heatwaveqc.tmm.progression.runtime.NativeLootBonuses;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Add favored-prey Looting I inside the original vanilla mob count formula. */
@Mixin(EnchantedCountIncreaseFunction.class)
public abstract class HunterLootingMixin {
    @WrapOperation(method = "run",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int tmm$favoredLootingCount(Holder<Enchantment> enchantment, LivingEntity attacker,
                Operation<Integer> original, @Local(argsOnly = true) LootContext context) {
        int nativeLevel = original.call(enchantment, attacker);
        if (!enchantment.is(Enchantments.MOB_LOOTING) || !(attacker instanceof ServerPlayer player))
            return nativeLevel;
        return nativeLevel + NativeLootBonuses.hunterLooting(player, context);
    }
}
