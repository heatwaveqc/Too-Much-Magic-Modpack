package io.github.heatwaveqc.traders.vengeance.client;

import io.github.heatwaveqc.traders.vengeance.TradersVengeance;
import io.github.heatwaveqc.traders.vengeance.runtime.GolemRocketTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = TradersVengeance.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class GolemRocketClientHooks {
    private GolemRocketClientHooks() {}

    @SubscribeEvent public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GolemRocketTypes.ROCKET.get(), GolemRocketRenderer::new);
    }

    @SubscribeEvent public static void registerModel(ModelEvent.RegisterAdditional event) {
        event.register(GolemRocketRenderer.MODEL);
    }
}
