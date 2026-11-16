package io.github.heatwaveqc.tmm.progression.mixin;

import io.github.heatwaveqc.tmm.progression.runtime.ClassAbilities;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Only automatic player-inventory insertion is restricted. Inventory#setItem
 * (used by deliberate container clicks, hotbar swaps and the user's own
 * placement) remains untouched.
 *
 * The normal auto-add methods go through getFreeSlot/getSlotWithRemainingSpace.
 * Direct add(0,stack) is redirected through vanilla add(stack) to preserve
 * vanilla partial-stack behavior without filling the reserved first slot.
 */
@Mixin(Inventory.class)
public abstract class InventoryReservationMixin {
    @Shadow @Final public NonNullList<ItemStack> items;
    @Shadow @Final public Player player;
    @Shadow public int selected;

    private boolean tmm$reserved() {
        return player instanceof ServerPlayer server
            && ClassAbilities.unlocked(server,"reserved_fist");
    }

    @Inject(method="getFreeSlot",at=@At("HEAD"),cancellable=true)
    private void tmm$skipReservedFreeSlot(CallbackInfoReturnable<Integer> cir) {
        if (!tmm$reserved()) return;
        for (int slot=1;slot<items.size();slot++) {
            if (items.get(slot).isEmpty()) {
                cir.setReturnValue(slot);
                return;
            }
        }
        cir.setReturnValue(-1);
    }

    @Inject(method="getSlotWithRemainingSpace",at=@At("HEAD"),cancellable=true)
    private void tmm$skipReservedStackMerge(ItemStack incoming,
                                             CallbackInfoReturnable<Integer> cir) {
        if (!tmm$reserved()) return;
        if (selected > 0 && selected < items.size() && tmm$mergeable(items.get(selected),incoming)) {
            cir.setReturnValue(selected);
            return;
        }
        Inventory inventory=(Inventory)(Object)this;
        if (tmm$mergeable(inventory.getItem(Inventory.SLOT_OFFHAND),incoming)) {
            cir.setReturnValue(Inventory.SLOT_OFFHAND);
            return;
        }
        for (int slot=1;slot<items.size();slot++) {
            if (tmm$mergeable(items.get(slot),incoming)) {
                cir.setReturnValue(slot);
                return;
            }
        }
        cir.setReturnValue(-1);
    }

    private static boolean tmm$mergeable(ItemStack existing,ItemStack incoming) {
        return !existing.isEmpty() && ItemStack.isSameItemSameComponents(existing,incoming)
            && existing.isStackable() && existing.getCount()<existing.getMaxStackSize();
    }

    @Inject(method="add(ILnet/minecraft/world/item/ItemStack;)Z",at=@At("HEAD"),cancellable=true)
    private void tmm$redirectExplicitAutoInsert(int slot,ItemStack incoming,
                                                 CallbackInfoReturnable<Boolean> cir) {
        if (slot!=0 || !tmm$reserved()) return;
        // "add" preserves the residual stack on failure. This is not the manual
        // inventory slot setter; never discard or clone the original item.
        cir.setReturnValue(((Inventory)(Object)this).add(incoming));
    }
}
