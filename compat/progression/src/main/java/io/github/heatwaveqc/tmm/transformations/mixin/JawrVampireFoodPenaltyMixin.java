package io.github.heatwaveqc.tmm.transformations.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.heatwaveqc.tyr.progression.runtime.forms.JawrDietPenaltyAdapter;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Exactly pinned JAWR 0.5.39:
 * VampireEvents.onItemUseFinish(Finish) consults isSolidFood(ItemStack)
 * before applying Hunger/Nausea. Modify ONLY that one decision, not the
 * finished item stack, native blood pool, event propagation or effects.
 * The optional mixin plugin disables this on any unapproved JAWR version.
 */
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireEvents",
        remap = false, priority = 900)
public abstract class JawrVampireFoodPenaltyMixin {
    @ModifyExpressionValue(
        method = "onItemUseFinish",
        at = @At(value = "INVOKE",
            target = "Ldev/sterner/witchery/feature/affliction/vampire/VampireEvents;isSolidFood(Lnet/minecraft/world/item/ItemStack;)Z"),
        require = 1, remap = false)
    private boolean tmm$approvedFoodWithoutPenalty(boolean nativeSolidFood,
            LivingEntityUseItemEvent.Finish event) {
        return nativeSolidFood && !JawrDietPenaltyAdapter.suppress(event);
    }
}
