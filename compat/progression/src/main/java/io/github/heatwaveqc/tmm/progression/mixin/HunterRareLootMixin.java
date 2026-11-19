package io.github.heatwaveqc.tmm.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.heatwaveqc.tmm.progression.runtime.NativeLootBonuses;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Looting-sensitive rare chances; no duplicate loot-table execution. */
@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
public abstract class HunterRareLootMixin {
    @WrapOperation(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int tmm$favoredLootingChance(Holder<Enchantment> enchantment, LivingEntity attacker,
                Operation<Integer> original, @Local(argsOnly = true) LootContext context) {
        int nativeLevel = original.call(enchantment, attacker);
        if (!enchantment.is(Enchantments.LOOTING) || !(attacker instanceof ServerPlayer player))
            return nativeLevel;
        return nativeLevel + NativeLootBonuses.hunterLooting(player, context) + NativeLootBonuses.configuredLooting(player,context);
    }
}
