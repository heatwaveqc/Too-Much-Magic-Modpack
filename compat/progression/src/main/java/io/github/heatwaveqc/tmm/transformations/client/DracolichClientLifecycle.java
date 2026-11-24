package io.github.heatwaveqc.tmm.transformations.client;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.network.DracolichVisualState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = TransformationsMod.MOD_ID, value = Dist.CLIENT)
public final class DracolichClientLifecycle {
    private DracolichClientLifecycle() { }

    @SubscribeEvent
    public static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        DracolichVisualState.clear();
    }
}
