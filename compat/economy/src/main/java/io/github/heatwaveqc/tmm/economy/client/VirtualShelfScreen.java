package io.github.heatwaveqc.tmm.economy.client;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/** Eight columns x four rows; native item tooltip and count rendering. */
public final class VirtualShelfScreen extends AbstractContainerScreen<VirtualShelfMenu> {
    public VirtualShelfScreen(VirtualShelfMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 225;
        inventoryLabelY = 131;
    }

    @Override protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.translatable("menu.tmm_economy.virtual_shelves.back"), button -> {
            if (minecraft != null && minecraft.gameMode != null)
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, VirtualShelfMenu.BACK);
        }).bounds(leftPos + 112, topPos + 110, 56, 18).build());
    }

    @Override protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        g.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFFC6C6C6);
        g.fill(leftPos, topPos, leftPos + imageWidth, topPos + 1, 0xFFFFFFFF);
        g.fill(leftPos, topPos, leftPos + 1, topPos + imageHeight, 0xFFFFFFFF);
        g.fill(leftPos, topPos + imageHeight - 1, leftPos + imageWidth, topPos + imageHeight, 0xFF555555);
        g.fill(leftPos + imageWidth - 1, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF555555);
        for (var slot : menu.slots) {
            int x = leftPos + slot.x, y = topPos + slot.y;
            g.fill(x - 1, y - 1, x + 17, y + 17, 0xFF555555);
            g.fill(x, y, x + 17, y + 17, 0xFFFFFFFF);
            g.fill(x, y, x + 16, y + 16, 0xFF8B8B8B);
        }
    }

    @Override protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        super.renderLabels(g, mouseX, mouseY);
        g.drawString(font, Component.translatable("menu.tmm_economy.virtual_shelves.count", menu.occupied(), VirtualShelfMenu.SLOTS), 8, 15, 0x404040, false);
        g.drawString(font, Component.translatable("menu.tmm_economy.virtual_shelves.deposit"), 8, 101, 0x404040, false);
        g.drawString(font, Component.translatable("menu.tmm_economy.virtual_shelves.withdraw"), 8, 113, 0x404040, false);
    }

    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
    }
}
