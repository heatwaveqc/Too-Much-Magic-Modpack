package io.github.heatwaveqc.tmm.economy.client;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.ArcaneDiscriminationRegistration;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = "tmm_economy", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ArcaneDiscriminationScreens {
    private ArcaneDiscriminationScreens() {}
    @SubscribeEvent
    public static void register(RegisterMenuScreensEvent event) {
        if (ModList.get().isLoaded("improvableskills") && ModList.get().isLoaded("apothic_enchanting"))
            event.register(ArcaneDiscriminationRegistration.MENU.get(), ArcaneDiscriminationScreen::new);
    }
}
