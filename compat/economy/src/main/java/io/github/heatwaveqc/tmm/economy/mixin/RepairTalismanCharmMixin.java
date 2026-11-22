package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.ProjectECharmRepairPolicy;
import moze_intel.projecte.gameObjs.items.RepairTalisman;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Extend only ProjectE's private Repair Talisman predicate.
 * Player swing protection and every inventory/Curios/bag/chest/pedestal caller
 * continue through ProjectE's native predicate and one-durability repair loop.
 */
@Mixin(value = RepairTalisman.class, remap = false)
public abstract class RepairTalismanCharmMixin {
    @Inject(method = "lambda$static$0", at = @At("RETURN"), cancellable = true)
    private static void tmm$allowValidPotionCharm(ItemStack stack, Void ignored,
            CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && ProjectECharmRepairPolicy.permits(stack)) {
            cir.setReturnValue(true);
        }
    }
}
