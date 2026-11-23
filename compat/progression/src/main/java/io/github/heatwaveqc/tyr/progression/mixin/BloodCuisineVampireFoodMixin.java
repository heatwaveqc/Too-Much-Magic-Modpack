package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.cuisine.BloodCuisineFoodItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * JAWR 0.5.39 VampireEvents.isSolidFood marks positive FOOD nutrition as
 * incompatible and applies 600-tick Hunger + 100-tick Nausea on item finish.
 * Only genuinely PAID cuisine food is exempt; never add it to FOODS_TAG,
 * which JAWR checks as another *penalty*, not an exemption.
 */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireEvents",
        remap = false)
public abstract class BloodCuisineVampireFoodMixin {
    @Inject(method = "isSolidFood(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"), cancellable = true, require = 0, remap = false)
    private void tmm$bloodMealsAreVampireFood(ItemStack stack,
                                                       CallbackInfoReturnable<Boolean> result) {
        if (stack.getItem() instanceof BloodCuisineFoodItem bloodFood && bloodFood.charged(stack))
            result.setReturnValue(false);
    }
}
