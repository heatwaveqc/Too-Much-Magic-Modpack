package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import io.github.heatwaveqc.tmm.economy.runtime.PersonalEnchantFilterService;
import io.github.heatwaveqc.tmm.economy.state.PersonalEnchantFilterState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Six read-only projections of native persisted escrow, not a second inventory.
 * Every actual mutation uses the existing exact-stack server service. Normal
 * slot synchronization transmits projections; client clicks never simulate
 * inventory movement. No cursor deposit, drag, hotbar swap, drop or clone path.
 */
public final class ArcaneDiscriminationMenu extends AbstractContainerMenu {
    public static final int FILTER_SLOTS = PersonalEnchantFilterState.SLOTS;
    public static final int FILTER_X = 35;
    public static final int FILTER_Y = 26;
    public static final int PLAYER_Y = 83;
    public static final int HOTBAR_Y = 141;
    private final Player owner;
    private final SimpleContainer display = new SimpleContainer(FILTER_SLOTS);

    public ArcaneDiscriminationMenu(int id, Inventory inventory) {
        super(ArcaneDiscriminationRegistration.MENU.get(), id);
        owner = inventory.player;
        for (int i = 0; i < FILTER_SLOTS; i++)
            addSlot(new Slot(display, i, FILTER_X + 18 * i, FILTER_Y) {
                @Override public boolean mayPlace(ItemStack stack) { return false; }
            });
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inventory, 9 + row * 9 + col, 8 + col * 18, PLAYER_Y + row * 18));
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inventory, col, 8 + col * 18, HOTBAR_Y));
        refresh();
    }

    private void refresh() {
        if (!(owner instanceof ServerPlayer player)) return;
        var books = PersonalEnchantFilterService.books(player);
        for (int i = 0; i < FILTER_SLOTS; i++) {
            ItemStack next = books.size() == FILTER_SLOTS ? books.get(i) : ItemStack.EMPTY;
            if (!ItemStack.matches(display.getItem(i), next))
                display.setItem(i, next.copy());
        }
    }

    @Override public boolean stillValid(Player player) {
        if (player != owner || !player.isAlive() || player.isSpectator()) return false;
        return !(player instanceof ServerPlayer server)
            || PersonalEnchantFilterService.books(server).size() == FILTER_SLOTS;
    }

    @Override public void broadcastChanges() {
        refresh();
        super.broadcastChanges();
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override public void clicked(int slotId, int button, ClickType type, Player player) {
        if (!(player instanceof ServerPlayer server) || player != owner
            || server.containerMenu != this || !stillValid(player)
            || !getCarried().isEmpty() || button != 0
            || (type != ClickType.PICKUP && type != ClickType.QUICK_MOVE)
            || slotId < 0 || slotId >= slots.size()) return;
        if (slotId < FILTER_SLOTS) {
            PersonalEnchantFilterService.withdraw(server, slotId);
        } else {
            var books = PersonalEnchantFilterService.books(server);
            if (books.size() != FILTER_SLOTS) return;
            for (int i = 0; i < FILTER_SLOTS; i++) {
                if (books.get(i).isEmpty()) {
                    PersonalEnchantFilterService.deposit(server, getSlot(slotId).getContainerSlot(), i);
                    break;
                }
            }
        }
        // Server also repairs projections after refused full-inventory/invalid clicks.
        refresh();
        broadcastFullState();
    }
}
