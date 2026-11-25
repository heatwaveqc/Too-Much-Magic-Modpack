package io.github.heatwaveqc.tmm.economy.runtime.enchanting;

import io.github.heatwaveqc.tmm.economy.runtime.PersonalEnchantFilterAbilityGate;
import io.github.heatwaveqc.tmm.economy.runtime.PersonalEnchantFilterService;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.zeith.improvableskills.api.registry.RegistriesIS3;

/** Register only when the reviewed native providers are present. */
public final class ArcaneDiscriminationRegistration {
    private ArcaneDiscriminationRegistration() {}

    private static final DeferredRegister<MenuType<?>> MENUS =
        DeferredRegister.create(Registries.MENU, "tmm_economy");
    public static final DeferredHolder<MenuType<?>, MenuType<ArcaneDiscriminationMenu>> MENU =
        MENUS.register("arcane_discrimination", () ->
            new MenuType<>(ArcaneDiscriminationMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus bus) {
        if (!EnchantingFeatureGate.enabled()) return;
        MENUS.register(bus);
        VirtualShelfMenus.register(bus);
        VirtualEnchantingReloads.register();
        bus.addListener(ArcaneDiscriminationRegistration::registerAbility);
        bus.addListener(ArcaneDiscriminationRegistration::registerPayload);
    }

    private static void registerAbility(RegisterEvent event) {
        event.register(RegistriesIS3.ABILITY, helper -> {
            var ability = new ArcaneDiscriminationAbility();
            helper.register(PersonalEnchantFilterAbilityGate.ABILITY_ID, ability);
            // IS3's exact onPostRegistered only sets its registered flag.
            if (!ability.registered()) ability.onPostRegistered();
        });
    }

    public record Open() implements CustomPacketPayload {
        public static final Type<Open> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("tmm_economy", "open_arcane_discrimination"));
        public static final StreamCodec<RegistryFriendlyByteBuf, Open> CODEC =
            StreamCodec.unit(new Open());
        @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
    }

    private static void registerPayload(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(Open.TYPE, Open.CODEC, (payload, context) -> {
            if (!(context.player() instanceof ServerPlayer owner)) return;
            context.enqueueWork(() -> {
                if (!owner.isAlive() || owner.isSpectator()
                    || !PersonalEnchantFilterAbilityGate.unlocked(owner)
                    || PersonalEnchantFilterService.books(owner).size() != 6
                    || owner.containerMenu instanceof ArcaneDiscriminationMenu) return;
                owner.openMenu(new SimpleMenuProvider(
                    (id, inventory, player) -> new ArcaneDiscriminationMenu(id, inventory),
                    Component.translatable("menu.tmm_economy.arcane_discrimination")));
            });
        });
    }
}
