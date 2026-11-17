package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.runtime.VirtualElytra;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Based on Iron's Angel Wings pattern for MC 1.21.1 NeoForge.
 *
 * ItemStack's ordinary Elytra predicates are evaluated first. Our synced
 * permission marker is additive, including when the chest slot contains
 * armor rather than an Elytra. Works both client-side (jump-to-glide) and
 * server-side (continued vanilla flight); no alternate glide physics.
 */
@Mixin(value = IItemStackExtension.class, remap = false, priority = 1000)
public interface VirtualElytraItemExtensionMixin {
    @Inject(method = "canElytraFly", at = @At("RETURN"), cancellable = true, remap = false)
    default void tmm$canElytraFly(LivingEntity wearer, CallbackInfoReturnable<Boolean> cir) {
        if (VirtualElytra.canGlide(wearer)) cir.setReturnValue(true);
    }

    @Inject(method = "elytraFlightTick", at = @At("RETURN"), cancellable = true, remap = false)
    default void tmm$elytraFlightTick(LivingEntity wearer, int flightTicks,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (VirtualElytra.canGlide(wearer)) cir.setReturnValue(true);
    }
}
