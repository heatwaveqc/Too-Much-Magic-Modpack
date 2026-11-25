package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.EvilCraftReplicationPolicy;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Prevent naturally initialized spirits from choosing a prohibited inner monster. */
@Pseudo
@Mixin(targets = "org.cyclops.evilcraft.entity.monster.EntityVengeanceSpiritData", remap = false)
public abstract class EvilCraftRandomSpiritMixin {
    @Inject(method = "getRandomInnerEntity(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/entity/EntityType;",
            at = @At("HEAD"), cancellable = true)
    private static void tmm$chooseAllowedInner(RandomSource random,
            CallbackInfoReturnable<EntityType<?>> cir) {
        cir.setReturnValue(EvilCraftReplicationPolicy.randomAllowedMonster(random));
    }
}
