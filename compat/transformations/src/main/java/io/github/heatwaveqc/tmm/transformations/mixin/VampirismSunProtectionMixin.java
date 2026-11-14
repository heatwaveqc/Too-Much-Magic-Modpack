package io.github.heatwaveqc.tmm.transformations.mixin;

import io.github.heatwaveqc.tmm.transformations.vampire.JAWRVampireProtection;
import io.github.heatwaveqc.tmm.transformations.vampire.VampireChassisRuntime;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Respect JAWR's empowered Sunstone in Vampirism's sun-exposure check. */
@Pseudo
@Mixin(targets = "de.teamlapen.vampirism.entity.player.vampire.VampirePlayer", remap = false)
public abstract class VampirismSunProtectionMixin {
    @Inject(method = "isGettingSundamage(Lnet/minecraft/world/level/LevelAccessor;Z)Z",
            at = @At("RETURN"), cancellable = true, remap = false)
    private void tmm$sunstone(LevelAccessor world, boolean refresh, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        try {
            Object raw = this.getClass().getMethod("asEntity").invoke(this);
            if (raw instanceof ServerPlayer player
                    && VampireChassisRuntime.isDual(player)
                    && JAWRVampireProtection.empoweredSunstone(player)) cir.setReturnValue(false);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // Failed optional protection lookup never disables vanilla sun exposure.
        }
    }
}
