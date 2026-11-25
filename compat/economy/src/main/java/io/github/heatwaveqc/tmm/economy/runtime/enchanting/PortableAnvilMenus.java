package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.AbilitiesIS;

/** Native Portable Anvil owner, not another unlock or XP ledger. */
public final class PortableAnvilMenus {
    private PortableAnvilMenus() {}
    private static final String KEY = "tmm_economy:portable_iesnium_anvil";
    private static final ResourceLocation ITEM = ResourceLocation.fromNamespaceAndPath("occultism", "iesnium_anvil");
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, "tmm_economy");
    public static final DeferredHolder<MenuType<?>, MenuType<AnvilMenu>> SETUP =
        MENUS.register("portable_anvil_setup", () -> new MenuType<AnvilMenu>(PortableAnvilSetupMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus) {
        if (EnchantingFeatureGate.portableAnvilEnabled()) MENUS.register(bus);
    }

    private static PlayerSkillData data(ServerPlayer owner) {
        if (owner == null || !owner.isAlive() || owner.isSpectator()
            || !EnchantingFeatureGate.portableAnvilEnabled()
            || !AbilitiesIS.ANVIL.registered()) return null;
        var data = PlayerDataManager.getDataFor(owner);
        return data != null && data.persistedData != null && data.hasAbility(AbilitiesIS.ANVIL) ? data : null;
    }

    private enum State { UNINSTALLED, INSTALLED, INVALID }
    private static State state(PlayerSkillData data) {
        if (data == null) return State.INVALID;
        if (!data.persistedData.contains(KEY)) return State.UNINSTALLED;
        if (!data.persistedData.contains(KEY, Tag.TAG_BYTE)) return State.INVALID;
        byte value = data.persistedData.getByte(KEY);
        return value == 1 ? State.INSTALLED : value == 0 ? State.UNINSTALLED : State.INVALID;
    }

    public static boolean canUseSetup(ServerPlayer owner) { return state(data(owner)) == State.UNINSTALLED; }
    public static boolean canUseUpgraded(ServerPlayer owner) { return state(data(owner)) == State.INSTALLED; }

    /** Return false only to preserve the native route when this optional feature cannot operate. */
    public static boolean open(ServerPlayer owner) {
        var state = state(data(owner));
        if (state == State.INVALID) return false;
        if (state == State.INSTALLED) {
            owner.openMenu(new SimpleMenuProvider((id, inv, player) -> new PortableIesniumAnvilMenu(id, inv),
                Component.translatable("menu.tmm_economy.portable_iesnium_anvil")));
        } else {
            owner.openMenu(new SimpleMenuProvider((id, inv, player) -> new PortableAnvilSetupMenu(id, inv),
                Component.translatable("container.repair")));
        }
        return true;
    }

    public static boolean install(ServerPlayer owner, PortableAnvilSetupMenu menu) {
        var data = data(owner);
        if (owner.containerMenu != menu) return false;
        ItemStack input = menu.getSlot(0).getItem();
        boolean exactItem = !input.isEmpty() && ITEM.equals(BuiltInRegistries.ITEM.getKey(input.getItem()));
        boolean clear = menu.getSlot(1).getItem().isEmpty() && menu.getSlot(2).getItem().isEmpty() && menu.getCarried().isEmpty();
        if (!PortableAnvilInstallPolicy.canInstall(data != null, state(data) == State.UNINSTALLED,
            exactItem, input.getCount(), clear)) return false;
        // All checks precede the server-thread mutation. Creative consumes the item too.
        // The normal native menu owns the input; removed() returns any remainder.
        input.shrink(1);
        data.persistedData.putBoolean(KEY, true);
        menu.getSlot(0).setChanged();
        data.sync();
        menu.broadcastChanges();
        owner.displayClientMessage(Component.translatable("message.tmm_economy.portable_iesnium_installed"), true);
        // A failed screen open cannot charge again; next native activation uses the stored flag.
        open(owner);
        return true;
    }
}
