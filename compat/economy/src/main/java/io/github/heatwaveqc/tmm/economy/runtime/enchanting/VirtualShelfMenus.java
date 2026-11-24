package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Two menu types under the existing optional native ability, not new skill unlocks.
 * Candidate route hooks are not yet enabled; registration alone cannot refund legacy books.
 */
public final class VirtualShelfMenus {
    private VirtualShelfMenus() {}
    private static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(Registries.MENU, "tmm_economy");
    public static final DeferredHolder<MenuType<?>, MenuType<VirtualShelfMenu>> SHELVES =
        MENUS.register("virtual_shelves", () -> new MenuType<>(VirtualShelfMenu::new, FeatureFlags.DEFAULT_FLAGS));
    public static final DeferredHolder<MenuType<?>, MenuType<PortableApothicMenu>> ENCHANTER =
        MENUS.register("portable_enchanter", () -> new MenuType<>(PortableApothicMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus) {
        if (EnchantingFeatureGate.enabled()) MENUS.register(bus);
    }

    /** Called only from the reviewed native cutover hooks, never a free client packet.
     * Migration remains unreachable until all old power routes are replaced together.
     */
    public static boolean openFromNative(ServerPlayer owner, boolean shelves) {
        if (!PortableEnchantingCutover.ready() || !VirtualShelfService.retireLegacyBooks(owner)) {
            owner.displayClientMessage(Component.translatable("menu.tmm_economy.virtual_shelves.unavailable"), true);
            return false;
        }
        return openRetired(owner, shelves);
    }

    /** Navigation of an already migrated, server-owned menu; never performs migration. */
    public static boolean openRetired(ServerPlayer owner, boolean shelves) {
        if (!PortableEnchantingCutover.ready() ||
            VirtualShelfService.items(owner).size() != VirtualShelfLayout.CAPACITY) return false;
        if (shelves) {
            return owner.openMenu(new SimpleMenuProvider(
                (id, inventory, player) -> new VirtualShelfMenu(id, inventory),
                Component.translatable("menu.tmm_economy.virtual_shelves"))).isPresent();
        }
        return owner.openMenu(new SimpleMenuProvider(
            (id, inventory, player) -> new PortableApothicMenu(id, inventory),
            Component.translatable("container.enchant"))).isPresent();
    }
}
