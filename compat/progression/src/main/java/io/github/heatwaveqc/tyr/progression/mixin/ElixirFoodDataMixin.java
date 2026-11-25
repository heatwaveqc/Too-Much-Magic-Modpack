package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.ElixirRuntime;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Server owner lookup is empty for all client/non-player FoodData. Hunger is untouched. */
@Mixin(FoodData.class)
public abstract class ElixirFoodDataMixin {
    @Shadow private float saturationLevel;
    @ModifyVariable(method="add(IF)V",at=@At("HEAD"),argsOnly=true,ordinal=0)
    private float tmm$exclusiveFoodSaturation(float amount){
        return amount>0&&ElixirRuntime.denySaturation((FoodData)(Object)this)?0:amount;
    }
    @ModifyVariable(method="setSaturation(F)V",at=@At("HEAD"),argsOnly=true,ordinal=0)
    private float tmm$exclusiveDirectSaturation(float amount){
        return amount>saturationLevel&&ElixirRuntime.denySaturation((FoodData)(Object)this)?saturationLevel:amount;
    }
}
