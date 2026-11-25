package io.github.heatwaveqc.tmm.economy.client;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.ArcaneDiscriminationRegistration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.EnchantingFeatureGate;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = "tmm_economy", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ArcaneDiscriminationScreens {
    private ArcaneDiscriminationScreens() {}
    @SubscribeEvent
    public static void register(RegisterMenuScreensEvent event) {
        if (EnchantingFeatureGate.enabled()) {
            event.register(ArcaneDiscriminationRegistration.MENU.get(), ArcaneDiscriminationScreen::new);
            event.register(io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfMenus.SHELVES.get(), VirtualShelfScreen::new);
            event.<net.minecraft.world.inventory.EnchantmentMenu, PortableApothicScreen>register(
                io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfMenus.ENCHANTER.get(), PortableApothicScreen::new);
        }
    }
}
