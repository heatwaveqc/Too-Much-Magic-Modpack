package io.github.heatwaveqc.tmm.economy.client;

import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentScreen;
import net.minecraft.world.inventory.EnchantmentMenu;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableApothicMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/** Native Apothic UI, with the same bookshelf action ID as IS3's original UI. */
public final class PortableApothicScreen extends ApothEnchantmentScreen {
    public PortableApothicScreen(EnchantmentMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
    @Override protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.translatable("menu.tmm_economy.virtual_shelves.settings"), button -> {
            if (minecraft != null && minecraft.gameMode != null)
                minecraft.gameMode.handleInventoryButtonClick(getMenu().containerId, PortableApothicMenu.SHELF_SETTINGS);
        }).bounds(leftPos + 8, topPos + 4, 48, 18).build());
    }
}
