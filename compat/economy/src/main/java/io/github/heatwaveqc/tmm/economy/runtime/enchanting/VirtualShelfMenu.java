package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import io.github.heatwaveqc.tmm.economy.state.VirtualShelfState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Read-only slot projection; all actual item movement is through VirtualShelfService.
 * The standard menu click protocol supplies only a slot/action, not trusted stacks.
 */
public final class VirtualShelfMenu extends AbstractContainerMenu {
    public static final int SLOTS = VirtualShelfState.SLOTS;
    public static final int BACK = 1;
    public static final int SHELF_X = 17, SHELF_Y = 25;
    public static final int PLAYER_Y = 143, HOTBAR_Y = 201;
    private final Player owner;
    private final SimpleContainer display = new SimpleContainer(SLOTS);

    public VirtualShelfMenu(int id, Inventory inventory) {
        super(VirtualShelfMenus.SHELVES.get(), id);
        owner = inventory.player;
        for (int i = 0; i < SLOTS; i++) {
            addSlot(new Slot(display, i, SHELF_X + 18 * (i % 8), SHELF_Y + 18 * (i / 8)) {
                @Override public boolean mayPlace(ItemStack stack) { return false; }
                @Override public boolean mayPickup(Player player) { return false; }
            });
        }
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, PLAYER_Y + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col, 8 + col * 18, HOTBAR_Y));
        refresh();
    }

    private void refresh() {
        if (!(owner instanceof ServerPlayer server)) return;
        var items = VirtualShelfService.items(server);
        for (int i = 0; i < SLOTS; i++) {
            ItemStack next = items.size() == SLOTS ? items.get(i) : ItemStack.EMPTY;
            if (!ItemStack.matches(display.getItem(i), next)) display.setItem(i, next.copy());
        }
    }

    public int occupied() {
        int count = 0;
        for (int i = 0; i < SLOTS; i++) if (!display.getItem(i).isEmpty()) count++;
        return count;
    }

    @Override public boolean stillValid(Player player) {
        if (player != owner || !player.isAlive() || player.isSpectator()) return false;
        return !(player instanceof ServerPlayer server) ||
            VirtualShelfService.items(server).size() == SLOTS;
    }

    @Override public void broadcastChanges() {
        refresh();
        super.broadcastChanges();
    }

    @Override public ItemStack quickMoveStack(Player player, int index) { return ItemStack.EMPTY; }

    @Override public void clicked(int slotId, int button, ClickType type, Player player) {
        if (!(player instanceof ServerPlayer server) || owner != player ||
            server.containerMenu != this || !stillValid(player)) return;
        if (getCarried().isEmpty() && button == 0 &&
            (type == ClickType.PICKUP || type == ClickType.QUICK_MOVE) &&
            slotId >= 0 && slotId < slots.size()) {
            if (slotId < SLOTS) VirtualShelfService.withdraw(server, slotId);
            else VirtualShelfService.deposit(server, getSlot(slotId).getContainerSlot());
        }
        // Never run vanilla drag/swap/clone/drop/cursor movement against the projection.
        refresh();
        broadcastFullState();
    }

    @Override public boolean clickMenuButton(Player player, int id) {
        if (id != BACK || player != owner || !(player instanceof ServerPlayer server) ||
            server.containerMenu != this || !stillValid(player) || !getCarried().isEmpty()) return false;
        return VirtualShelfMenus.openRetired(server, false);
    }
}
