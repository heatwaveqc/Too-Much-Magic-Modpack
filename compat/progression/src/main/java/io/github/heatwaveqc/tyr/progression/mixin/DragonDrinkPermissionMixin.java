package io.github.heatwaveqc.tyr.progression.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.heatwaveqc.tyr.progression.runtime.forms.DragonDietHook;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Dragon Survival 2.0.74 injects into IItemExtension.getFoodProperties,
 * not IItemStackExtension.getFoodProperties. Run after its default-priority
 * modifier so we can inspect the selected *native* dragon result.
 *
 * The native item delegate, consumption and effects are never changed.
 */
@Mixin(value = IItemExtension.class, priority = 900, remap = false)
public interface DragonDrinkPermissionMixin {
    @ModifyReturnValue(method = "getFoodProperties", at = @At("RETURN"),
        require = 1, remap = false)
    private FoodProperties tmm$approvedDragonFood(FoodProperties nativeResult,
                                                  ItemStack stack, LivingEntity entity) {
        return DragonDietHook.restore(stack, entity, nativeResult);
    }
}
