package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Translate JAWR blood transactions only for players with both native vampire flags. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.blood_pool.BloodPoolHandler", remap = false)
public abstract class JAWRBloodPoolMixin {
    @Inject(method = "increaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void tmm$increase(LivingEntity entity, int amount, CallbackInfo ci) {
        if (entity instanceof ServerPlayer p && VampireChassisRuntime.interceptJAWRBlood(p, amount, true))
            ci.cancel();
    }

    @Inject(method = "decreaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void tmm$decrease(LivingEntity entity, int amount, CallbackInfo ci) {
        if (entity instanceof ServerPlayer p && VampireChassisRuntime.interceptJAWRBlood(p, amount, false))
            ci.cancel();
    }

    @Inject(method = "tickBloodRegen(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$passiveRegen(LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer p && VampireChassisRuntime.isDual(p)) ci.cancel();
    }
}
