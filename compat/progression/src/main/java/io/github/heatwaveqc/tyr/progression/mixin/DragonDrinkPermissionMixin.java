package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.forms.DragonDietHook;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Hook after item-level diet selection, not a global food component mutation. */
@Mixin(value=IItemStackExtension.class,remap=false)
public interface DragonDrinkPermissionMixin {
    @ModifyReturnValue(method="getFoodProperties",at=@At("RETURN"),require=0,remap=false)
    private FoodProperties tmm$authorizedDragonDrink(FoodProperties original,LivingEntity entity){
        return DragonDietHook.restore((ItemStack)(Object)this,entity,original);
    }
}
