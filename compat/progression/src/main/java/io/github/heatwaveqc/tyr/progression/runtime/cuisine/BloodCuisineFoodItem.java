package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

/**
 * Preserve each source sausage/pie slice's native FOOD component and effects.
 * Normal people receive that original nourishment; JAWR vampires receive
 * exactly the paid blood instead of an extra ordinary hunger/saturation refill.
 */
public final class BloodCuisineFoodItem extends Item {
    private final BloodCuisinePolicy.Product product;

    public BloodCuisineFoodItem(BloodCuisinePolicy.Product product, Properties properties) {
        super(properties);
        this.product = product;
    }

    public boolean charged(ItemStack stack) {
        return BloodCuisinePolicy.validFoodPayload(product, BloodCuisinePayload.credit(stack));
    }

    @Override public InteractionResultHolder<ItemStack> use(Level level, Player player,
                                                             InteractionHand hand) {
        // Food must remain usable by full-hunger JAWR vampires; native food
        // values and consumable status effects stay on the output stack.
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        boolean paid = charged(stack);
        ServerPlayer player = entity instanceof ServerPlayer p ? p : null;
        boolean vampire = player != null && BloodCuisineNative.isVampire(player);
        int beforeFood = vampire ? player.getFoodData().getFoodLevel() : 0;
        float beforeSaturation = vampire ? player.getFoodData().getSaturationLevel() : 0.0f;
        int credit = paid ? BloodCuisinePayload.credit(stack) : 0;

        // If native blood cannot be credited, preserve the paid food instead
        // of silently converting an expensive filled bottle into 0 blood.
        if (vampire && paid && !BloodCuisineNative.canCreditNative()) return stack;
        ItemStack after = super.finishUsingItem(stack, level, entity);
        if (vampire && paid) {
            FoodData food = player.getFoodData();
            food.setFoodLevel(beforeFood);
            food.setSaturation(beforeSaturation);
            if (!player.getAbilities().instabuild) BloodCuisineNative.credit(player, credit);
        }
        return after;
    }
}
