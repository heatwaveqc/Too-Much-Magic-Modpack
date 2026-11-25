package io.github.heatwaveqc.tmm.economy.client;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.PortableAnvilSetupMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;

/** Explicit permanent upgrade, never an automatic consumption on opening. */
public final class PortableAnvilSetupScreen extends AnvilScreen {
    public PortableAnvilSetupScreen(AnvilMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
    @Override protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.translatable("menu.tmm_economy.portable_anvil_install"), button -> {
            if (minecraft != null && minecraft.gameMode != null)
                minecraft.gameMode.handleInventoryButtonClick(getMenu().containerId, PortableAnvilSetupMenu.INSTALL);
        }).bounds(leftPos, Math.max(0, topPos - 22), imageWidth, 20)
          .tooltip(Tooltip.create(Component.translatable("menu.tmm_economy.portable_anvil_install_hint"))).build());
    }
}
