package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import com.klikli_dev.occultism.client.gui.IesniumAnvilMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

/** Reuse the real native Iesnium implementation and its normal client menu type.
 * Only validity changes: the installed portable upgrade replaces block proximity.
 */
public final class PortableIesniumAnvilMenu extends IesniumAnvilMenu {
    private final Player owner;
    public PortableIesniumAnvilMenu(int id, Inventory inventory) {
        super(id, inventory, ContainerLevelAccess.create(inventory.player.level(), inventory.player.blockPosition()));
        owner = inventory.player;
    }
    @Override public boolean stillValid(Player player) {
        return player == owner && player.isAlive() && !player.isSpectator()
            && (!(player instanceof ServerPlayer server) || PortableAnvilMenus.canUseUpgraded(server));
    }
}
