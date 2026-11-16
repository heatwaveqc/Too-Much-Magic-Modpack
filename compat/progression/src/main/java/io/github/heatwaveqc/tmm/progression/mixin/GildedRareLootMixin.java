package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.runtime.GildedRules;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** The rare-drop chance variant of Looting (also wrapped by Ars Nouveau). */
@Mixin(LootItemRandomChanceWithEnchantedBonusCondition.class)
public abstract class GildedRareLootMixin {
    @WrapOperation(method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int gilded$rareLootLevel(Holder<Enchantment> enchantment, LivingEntity attacker,
            Operation<Integer> original, @Local(argsOnly = true) LootContext context) {
        int actual = original.call(enchantment,attacker);
        if (!(attacker instanceof ServerPlayer player)) return actual;
        return actual + GildedRules.extraLooting(player,context);
    }
}
