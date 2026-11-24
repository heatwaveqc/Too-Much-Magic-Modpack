package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import dev.shadowsoffire.apothic_enchanting.payloads.StatsPayload;
import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentMenu;
import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableItemHandler;
import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableStats;
import java.util.Arrays;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Candidate native portable menu. Native Apothic selection, infusion, clues, cost,
 * seed, fuel and item handling stay in the superclass. Only virtual stats, validity,
 * menu identity, navigation and transient lapis ownership differ.
 */
public final class PortableApothicMenu extends ApothEnchantmentMenu {
    public static final int SHELF_SETTINGS = 122;
    private final EnchantmentTableItemHandler portableFuel;
    private EnchantmentTableStats previewStats;
    private float previewCap = Float.NaN;
    private long previewReload = -1;

    public PortableApothicMenu(int id, Inventory inventory) {
        this(id, inventory, new EnchantmentTableItemHandler());
    }

    private PortableApothicMenu(int id, Inventory inventory, EnchantmentTableItemHandler fuel) {
        super(id, inventory, inventory.player instanceof ServerPlayer
            ? ContainerLevelAccess.create(inventory.player.level(), inventory.player.blockPosition())
            : ContainerLevelAccess.NULL, fuel, inventory.player.blockPosition());
        portableFuel = fuel;
    }

    @Override public MenuType<?> getType() { return VirtualShelfMenus.ENCHANTER.get(); }

    @Override public boolean stillValid(Player owner) {
        if (owner != player || !owner.isAlive() || owner.isSpectator()) return false;
        return !(owner instanceof ServerPlayer server) ||
            VirtualShelfService.items(server).size() == VirtualShelfLayout.CAPACITY;
    }

    @Override public void gatherStats() {
        if (!(player instanceof ServerPlayer owner)) return;
        stats = VirtualShelfService.stats(owner).orElse(EnchantmentTableStats.INVALID);
        if (owner.containerMenu == this) PacketDistributor.sendToPlayer(owner, new StatsPayload(stats));
    }

    @Override public void slotsChanged(Container changed) {
        if (!(player instanceof ServerPlayer owner)) return;
        // Only the native enchant-input container drives previews.
        if (slots.isEmpty() || changed != getSlot(0).container) return;
        var current = VirtualShelfService.stats(owner);
        if (current.isEmpty()) {
            stats = EnchantmentTableStats.INVALID;
            previewStats = null;
            Arrays.fill(costs, 0);
            Arrays.fill(enchantClue, -1);
            Arrays.fill(levelClue, -1);
            if (owner.containerMenu == this) PacketDistributor.sendToPlayer(owner, new StatsPayload(stats));
            broadcastChanges();
            return;
        }
        super.slotsChanged(changed);
        previewStats = stats;
        previewCap = stats.eterna(owner);
        previewReload = VirtualEnchantingReloads.revision();
    }

    @Override public boolean clickMenuButton(Player clicker, int button) {
        if (!(clicker instanceof ServerPlayer owner) || owner != player ||
            owner.containerMenu != this || !stillValid(owner)) return false;
        if (button == SHELF_SETTINGS)
            return getCarried().isEmpty() && VirtualShelfMenus.openRetired(owner, true);
        if (button < 0 || button >= 3) return false;
        var current = VirtualShelfService.stats(owner);
        if (current.isEmpty() || !current.get().equals(previewStats) ||
            Float.compare(current.get().eterna(owner), previewCap) != 0 ||
            previewReload != VirtualEnchantingReloads.revision()) {
            slotsChanged(getSlot(0).container);
            broadcastChanges();
            return false;
        }
        return super.clickMenuButton(owner, button);
    }

    @Override public void removed(Player owner) {
        // Native superclass returns the enchanting input and cursor. Unlike a
        // physical table, our fuel handler has no block attachment to retain lapis.
        super.removed(owner);
        if (owner instanceof ServerPlayer && portableFuel != null) {
            ItemStack remaining = portableFuel.extractItem(0, Integer.MAX_VALUE, false);
            if (!remaining.isEmpty()) clearContainer(owner, new SimpleContainer(remaining));
        }
    }
}
