package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.runtime.GildedRules;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Prevent only new unprovoked alarms; never reset existing mob anger memories. */
@Mixin(PiglinAi.class)
public abstract class GildedPiglinMixin {
    @Inject(method = "angerNearbyPiglins", at = @At("HEAD"), cancellable = true)
    private static void gilded$ignoreContainerAndGoldAlarm(Player player, boolean requiresSight, CallbackInfo ci) {
        if (player instanceof ServerPlayer server && GildedRules.joined(server)) ci.cancel();
    }
    @Inject(method = "isWearingGold", at = @At("HEAD"), cancellable = true)
    private static void gilded$goldByKinship(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (target instanceof ServerPlayer server && GildedRules.joined(server))
            cir.setReturnValue(true);
    }
}
