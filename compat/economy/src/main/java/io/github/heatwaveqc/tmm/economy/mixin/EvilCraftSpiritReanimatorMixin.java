package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.EvilCraftReplicationPolicy;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritReanimator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Reject denied boxes before the native tick action consumes Blood, egg, or box data. */
@Pseudo
@Mixin(targets = "org.cyclops.evilcraft.blockentity.tickaction.spiritreanimator.ReanimateTickAction",
        remap = false)
public abstract class EvilCraftSpiritReanimatorMixin {
    @Inject(method = "canTick(Lorg/cyclops/evilcraft/blockentity/BlockEntitySpiritReanimator;Lnet/minecraft/world/item/ItemStack;II)Z",
            at = @At("HEAD"), cancellable = true)
    private void tmm$denyReplicationEgg(BlockEntitySpiritReanimator tile, ItemStack stack,
            int slot, int tick, CallbackInfoReturnable<Boolean> cir) {
        EntityType<?> type = tile.getEntityType();
        if (type != null && EvilCraftReplicationPolicy.blocked(type)) {
            cir.setReturnValue(false);
        }
    }
}
