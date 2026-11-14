package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Event-driven shadow update at *native mutations*, not a polling rewrite. */
@Pseudo
@Mixin(targets = "de.teamlapen.vampirism.entity.player.vampire.BloodStats", remap = false)
public abstract class VampirismBloodStatsMixin {
    @Shadow @Final private Player player;

    @Inject(method = "onUpdate()Z", at = @At("RETURN"), remap = false)
    private void tmm$afterExhaustion(CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof ServerPlayer sp && cir.getReturnValue())
            VampireChassisRuntime.onNativeBloodChanged(sp);
    }

    @Inject(method = "addBlood(IF)I", at = @At("RETURN"), remap = false)
    private void tmm$afterFeeding(int amount, float saturation, CallbackInfoReturnable<Integer> cir) {
        if (player instanceof ServerPlayer sp) VampireChassisRuntime.onNativeBloodChanged(sp);
    }

    @Inject(method = "removeBlood(IZ)Z", at = @At("RETURN"), remap = false)
    private void tmm$afterSpend(int amount, boolean partial, CallbackInfoReturnable<Boolean> cir) {
        if (player instanceof ServerPlayer sp) VampireChassisRuntime.onNativeBloodChanged(sp);
    }
}
