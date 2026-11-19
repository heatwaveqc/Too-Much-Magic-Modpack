package io.github.heatwaveqc.tmm.tradersvengence.client;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = TradersVengence.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class NpcPistolClientHooks {
    private NpcPistolClientHooks() {}
    @SubscribeEvent public static void models(ModelEvent.RegisterAdditional event) {
        event.register(NpcPistolLayer.MODEL);
    }
    @SubscribeEvent public static void layers(EntityRenderersEvent.AddLayers event) {
        VillagerRenderer villagers = event.getRenderer(EntityType.VILLAGER);
        if (villagers != null) villagers.addLayer(new NpcPistolLayer<>(villagers));
        WanderingTraderRenderer traders = event.getRenderer(EntityType.WANDERING_TRADER);
        if (traders != null) traders.addLayer(new NpcPistolLayer<>(traders));
    }
}
