package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.EvilCraftReplicationPolicy;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Add the shared tag to EvilCraft's native blacklist without changing its own deny rules. */
@Pseudo
@Mixin(targets = "org.cyclops.evilcraft.entity.monster.EntityVengeanceSpirit", remap = false)
public abstract class EvilCraftVengeanceSpiritMixin {
    @Inject(method = "canSustain(Lnet/minecraft/world/entity/LivingEntity;)Z",
            at = @At("HEAD"), cancellable = true)
    private static void tmm$denyReplicationSpirit(LivingEntity entity,
            CallbackInfoReturnable<Boolean> cir) {
        if (EvilCraftReplicationPolicy.blocked(entity.getType())) {
            cir.setReturnValue(false);
        }
    }
}
