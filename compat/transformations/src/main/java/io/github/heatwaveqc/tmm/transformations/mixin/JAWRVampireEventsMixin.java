package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireForestShelter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.DamageTypeTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Avoid competing vanilla FoodData writes and duplicate passive garlic/fire. */
@Pseudo
@Mixin(targets = "dev.sterner.witchery.feature.affliction.vampire.VampireEvents", remap = false)
public abstract class JAWRVampireEventsMixin {
    @Inject(method = "handleSunExposure(Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$shadeInVampireForest(ServerPlayer player, CallbackInfo ci) {
        if (VampireForestShelter.isSheltered(player)) {
            // JAWR's own shelter routine clears any accumulated sun exposure.
            ((JAWRVampireSunAccessor) (Object) this).tmm$decreaseSunTick(player);
            ci.cancel();
        }
    }

    @Inject(method = "tickGarlicExposure(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$garlic(LivingEntity living, CallbackInfo ci) {
        if (living instanceof ServerPlayer p && VampireChassisRuntime.isDual(p)) ci.cancel();
    }

    @Inject(method = "handleHurt(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/damagesource/DamageSource;F)F",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$fire(LivingEntity living, DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        if (living instanceof ServerPlayer p && VampireChassisRuntime.isDual(p)
                && source.is(DamageTypeTags.IS_FIRE)) {
            // Vampirism supplies its own fire vulnerability; don't double it.
            cir.setReturnValue(amount);
        }
    }
}
