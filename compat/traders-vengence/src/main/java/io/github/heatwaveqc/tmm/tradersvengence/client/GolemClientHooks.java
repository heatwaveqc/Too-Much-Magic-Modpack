package io.github.heatwaveqc.tmm.tradersvengence.client;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import io.github.heatwaveqc.tmm.tradersvengence.network.GolemVisualState;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@EventBusSubscriber(modid = TradersVengence.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class GolemClientHooks {
    @SubscribeEvent public static void definitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GolemMinigunLayer.LAYER, GolemMinigunModel::createBodyLayer);
    }
    @SubscribeEvent public static void layers(EntityRenderersEvent.AddLayers event) {
        IronGolemRenderer renderer = event.getRenderer(EntityType.IRON_GOLEM);
        if (renderer != null) renderer.addLayer(new GolemMinigunLayer(renderer, event.getEntityModels()));
    }

    @EventBusSubscriber(modid = TradersVengence.MOD_ID, value = Dist.CLIENT)
    public static final class Lifecycle {
        @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
            GolemVisualState.clear();
        }
        @SubscribeEvent public static void leave(EntityLeaveLevelEvent event) {
            if (event.getLevel().isClientSide()) GolemVisualState.remove(event.getEntity().getUUID());
        }
    }
}
