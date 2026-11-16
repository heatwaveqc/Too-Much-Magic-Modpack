package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.runtime.GildedRules;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.function.Consumer;

/**
 * Pre-filters individual durability damage points, then leaves vanilla Unbreaking to run.
 * Product of survival probabilities equals the natural additive virtual Unbreaking rate.
 * The player-acceptance test must verify armor and mainhand wear call paths.
 */
@Mixin(ItemStack.class)
public abstract class GildedDurabilityMixin {
    @ModifyVariable(method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
        at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int gilded$virtualUnbreaking(int damage, ServerLevel level, ServerPlayer owner, Consumer<Item> onBreak) {
        if (owner == null || damage < 1) return damage;
        ItemStack stack = (ItemStack)(Object)this;
        int extra = GildedRules.extraUnbreaking(owner,stack);
        if (extra == 0) return damage;
        int physical = stack.getEnchantmentLevel(level.registryAccess()
            .registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.UNBREAKING));
        double before = stack.getItem() instanceof ArmorItem
            ? 0.6 + 0.4 / (physical + 1.0) : 1.0 / (physical + 1.0);
        double after = stack.getItem() instanceof ArmorItem
            ? 0.6 + 0.4 / (physical + extra + 1.0) : 1.0 / (physical + extra + 1.0);
        double probability = Math.min(1.0,after / before);
        int remaining = 0;
        for (int i=0; i<damage; i++) if (level.random.nextDouble() < probability) remaining++;
        return remaining;
    }
}
