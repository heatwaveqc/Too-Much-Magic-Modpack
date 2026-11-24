package io.github.heatwaveqc.tmm.economy.mixin;

import io.github.heatwaveqc.tmm.economy.storage.PortableStorageSafety;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Extra Botany 1.0.4 creates 24 plain Slots for its item-backed Mini Handbag.
 * Vanilla Slot.mayPlace does not delegate to Container.canPlaceItem, so a
 * container-level guard alone does NOT cover GUI clicks or quick-move.
 *
 * Only replace the 24 vanilla bag slots in this one menu, after construction.
 * Preserve original slot indices and delegate native restrictions, including
 * mayPickup. Armor/player slots remain untouched. Both server and client menus
 * use the same cheap predicate; source stacks are never consumed or rewritten.
 */
@Pseudo
@Mixin(targets = "velrondevs.extrabotany.client.gui.handbag.MiniHandbagMenu", remap = false)
public abstract class PortableStorageMiniHandbagMenuMixin extends AbstractContainerMenu {
    protected PortableStorageMiniHandbagMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL"), require = 1, remap = false)
    private void tmm$guardNativeBagSlots(int windowId, Inventory inventory,
                                         ItemStack bag, CallbackInfo ci) {
        // Exact Extra Botany 1.0.4 layout: 4 armor, then 24 handbag slots.
        final int first = 4;
        final int end = 28;
        for (int menuSlotIndex = first; menuSlotIndex < end; menuSlotIndex++) {
            Slot previous = this.slots.get(menuSlotIndex);
            Slot guarded = new Slot(previous.container, previous.getContainerSlot(),
                                    previous.x, previous.y) {
                @Override
                public boolean mayPlace(ItemStack source) {
                    return previous.mayPlace(source)
                        && !PortableStorageSafety.rejectsEmbeddedDestination(source);
                }

                @Override
                public boolean mayPickup(Player player) {
                    return previous.mayPickup(player);
                }
            };
            guarded.index = previous.index;
            this.slots.set(menuSlotIndex, guarded);
        }
    }
}
