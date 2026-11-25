package io.github.heatwaveqc.tmm.economy.client;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.ArcaneDiscriminationMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/** Ordinary vanilla container input and synchronization, six explicit book slots. */
public final class ArcaneDiscriminationScreen extends AbstractContainerScreen<ArcaneDiscriminationMenu> {
    public ArcaneDiscriminationScreen(ArcaneDiscriminationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 165;
        inventoryLabelY = 71;
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
        g.drawString(font, Component.translatable("menu.tmm_economy.arcane_discrimination.deposit"), 8, 47, 0x404040, false);
        g.drawString(font, Component.translatable("menu.tmm_economy.arcane_discrimination.withdraw"), 8, 58, 0x404040, false);
    }

    @Override public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
    }
}
