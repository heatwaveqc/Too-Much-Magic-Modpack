package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.EvilCraftReplicationPolicy;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.cyclops.evilcraft.blockentity.BlockEntitySpiritFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Reject denied boxes before the native tick action can consume Blood or emit loot. */
@Pseudo
@Mixin(targets = "org.cyclops.evilcraft.blockentity.tickaction.spiritfurnace.BoxCookTickAction",
        remap = false)
public abstract class EvilCraftSpiritFurnaceMixin {
    @Inject(method = "canTick(Lorg/cyclops/evilcraft/blockentity/BlockEntitySpiritFurnace;Lnet/minecraft/world/item/ItemStack;II)Z",
            at = @At("HEAD"), cancellable = true)
    private void tmm$denyReplicationLoot(BlockEntitySpiritFurnace tile, ItemStack stack,
            int slot, int tick, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = tile.getEntity();
        if (entity != null && EvilCraftReplicationPolicy.blocked(entity.getType())) {
            cir.setReturnValue(false);
        }
    }
}
