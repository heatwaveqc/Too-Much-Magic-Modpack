package io.github.heatwaveqc.tmm.friendship;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/** In-place crafting attribution; placed flowers are never rebound by recipient/placer. */
public final class FriendshipFlowerItem extends BlockItem {
    public FriendshipFlowerItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        if (!level.isClientSide()) FriendshipData.craftedBy(stack, player);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (FriendshipData.bound(context.getItemInHand()) == null) {
            Player player = context.getPlayer();
            if (player != null && !context.getLevel().isClientSide()) {
                player.displayClientMessage(Component.translatable("message.tmm_friendship_garden.unbound"), true);
            }
            return InteractionResult.FAIL;
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        FriendshipFlower flower = FriendshipData.bound(stack);
        if (flower != null) {
            tooltip.add(Component.literal("Crafted by " + flower.crafterDisplayName()).withStyle(ChatFormatting.LIGHT_PURPLE));
            tooltip.add(Component.literal("Original: " + flower.originalFlowerId()).withStyle(ChatFormatting.GRAY));
            if (flag.isAdvanced()) {
                tooltip.add(Component.literal(flower.crafterId().toString()).withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Component.translatable("message.tmm_friendship_garden.unbound").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
