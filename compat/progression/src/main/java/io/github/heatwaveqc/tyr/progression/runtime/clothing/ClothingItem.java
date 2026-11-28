package io.github.heatwaveqc.tyr.progression.runtime.clothing;

import net.minecraft.core.Holder;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import io.github.heatwaveqc.tyr.progression.foundation.TrainingWeight;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;

/** Native armor-slot equip behavior, without any armor attributes, durability,
 * enchantability or repair. No client classes on the common registration path. */
public final class ClothingItem extends ArmorItem {
    public ClothingItem(Holder<ArmorMaterial> material, Type type) {
        super(material, type, new Properties().stacksTo(1));
    }

    @Override public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return ItemAttributeModifiers.EMPTY;
    }
    @Override public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack,context,tooltip,flag);
        var tier=stack.getOrDefault(ClothingComponents.TRAINING_WEIGHT.get(),TrainingWeight.NORMAL);
        if(tier!=TrainingWeight.NORMAL&&ClothingItems.martialStyle(stack).isPresent())
            tooltip.add(Component.translatable("tooltip.tyr_progression.training_weight",
                    Component.translatable("tooltip.tyr_progression.training_weight."+tier.name().toLowerCase(java.util.Locale.ROOT)),
                    tier.multiplier().toPlainString(),Math.round(-tier.speed()*100)));
    }
    @Override public boolean isEnchantable(ItemStack stack) { return false; }
    @Override public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }
    @Override public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) { return false; }
    @Override public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) { return false; }
    @Override public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) { return false; }
}
