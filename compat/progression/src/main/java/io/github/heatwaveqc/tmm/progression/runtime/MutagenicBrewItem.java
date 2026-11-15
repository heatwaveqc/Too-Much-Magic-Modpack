package io.github.heatwaveqc.tmm.progression.runtime;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.Level;

/**
 * Uses PotionItem's ordinary drinking animation and POTION_CONTENTS tooltip, but
 * deliberately NEVER calls PotionItem.finishUsingItem: that method would give
 * ordinary potion effects before the mutation transfer.
 */
public final class MutagenicBrewItem extends PotionItem {
    public MutagenicBrewItem(Properties properties) { super(properties); }

    @Override public String getDescriptionId(ItemStack stack) {
        return "item.tmm_progression.mutagenic_brew";
    }

    @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                          List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack,context,tooltip,flag);
        tooltip.add(Component.translatable("tooltip.tmm_progression.mutagenic_brew")
            .withStyle(ChatFormatting.DARK_PURPLE));
    }

    @Override public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            if (!HunterMutations.drink(player,stack)) {
                player.displayClientMessage(Component.literal(
                    "No mutation progress: Hunter initiation or valid mutation effects required"),true);
                return stack; // No lost brew, no ordinary potion effects.
            }
            CriteriaTriggers.CONSUME_ITEM.trigger(player,stack);
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
            if (stack.isEmpty()) return new ItemStack(Items.GLASS_BOTTLE);
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().add(bottle)) player.drop(bottle,false);
        }
        return stack;
    }
}
