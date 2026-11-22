package io.github.heatwaveqc.tmm.transformations.mixin;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import io.github.heatwaveqc.tmm.transformations.core.NativeSpeciesJoinGuard;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Dragon Survival species entrypoint: enforce shared acquisition policy without blocking cures. */
@Pseudo
@Mixin(targets = "by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateHandler", remap = false)
public abstract class NewbieDragonAcquisitionMixin {
    @Inject(method = "setSpecies(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/Holder;Z)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private void tmm$denyNewbieDragon(Player player, Holder<?> species, boolean savedForSoul, CallbackInfo ci) {
        if (species != null && player instanceof ServerPlayer server
                && !NativeSpeciesJoinGuard.permitDragon(server,
                    species.unwrapKey().map(key -> key.location()).orElse(null))) {
            ci.cancel();
        }
    }
}
