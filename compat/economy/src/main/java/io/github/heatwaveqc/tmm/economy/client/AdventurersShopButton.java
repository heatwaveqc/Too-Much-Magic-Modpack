package io.github.heatwaveqc.tmm.economy.client;

import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftbquests.client.gui.quests.TabButton;
import dev.ftb.mods.ftbquests.quest.theme.property.ThemeProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

/** Open the existing personal-wallet shop as the requesting player, never as OP. */
public final class AdventurersShopButton extends TabButton {
    public AdventurersShopButton(Panel panel) {
        super(panel, Component.translatable("tmm_economy.shop.open"), ThemeProperties.SHOP_ICON.get());
    }

    @Override
    public void onClicked(MouseButton button) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return;
        playClickSound();
        connection.sendCommand("qshop open tmm");
    }
}
