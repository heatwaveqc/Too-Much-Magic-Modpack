package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * JAWR remains untouched as the canonical owner of blood and hunger.
 * Mirror only its actual completed changes into Vampirism's internal
 * compatibility meter; do not cancel native JAWR deductions or regeneration.
 */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.blood_pool.BloodPoolHandler", remap = false)
public abstract class JAWRBloodPoolMixin {
    @Inject(method = "increaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At("RETURN"), remap = false)
    private static void tmm$afterGain(LivingEntity entity, int amount, CallbackInfo ci) {
        if (entity instanceof ServerPlayer sp) VampireChassisRuntime.onJAWRBloodChanged(sp);
    }

    @Inject(method = "decreaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At("RETURN"), remap = false)
    private static void tmm$afterSpend(LivingEntity entity, int amount, CallbackInfo ci) {
        if (entity instanceof ServerPlayer sp) VampireChassisRuntime.onJAWRBloodChanged(sp);
    }

    @Inject(method = "tickBloodRegen(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("RETURN"), remap = false)
    private void tmm$afterRegen(LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer sp) VampireChassisRuntime.onJAWRBloodChanged(sp);
    }
}
