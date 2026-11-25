package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.zeith.improvableskills.client.gui.abil.anvil.AnvilMenuPortable;

/** Normal native portable anvil, with one deliberate server-authoritative install action. */
public final class PortableAnvilSetupMenu extends AnvilMenuPortable {
    public static final int INSTALL = 123;
    private final Player owner;
    public PortableAnvilSetupMenu(int id, Inventory inventory) {
        super(id, inventory);
        owner = inventory.player;
    }
    @Override public MenuType<?> getType() { return PortableAnvilMenus.SETUP.get(); }
    @Override public boolean stillValid(Player player) {
        return player == owner && player.isAlive() && !player.isSpectator()
            && (!(player instanceof ServerPlayer server) || PortableAnvilMenus.canUseSetup(server));
    }
    @Override public boolean clickMenuButton(Player player, int button) {
        if (button != INSTALL) return super.clickMenuButton(player, button);
        if (!(player instanceof ServerPlayer server) || !stillValid(player) || server.containerMenu != this) return false;
        return PortableAnvilMenus.install(server, this);
    }
}
