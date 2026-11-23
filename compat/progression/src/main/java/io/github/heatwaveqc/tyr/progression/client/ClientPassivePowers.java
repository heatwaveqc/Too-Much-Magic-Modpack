package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.runtime.ClientPowerSnapshot;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/** Client-only network entrypoint; all gameplay still authorized by the server. */
@EventBusSubscriber(modid = ProgressionMod.MOD_ID, value = Dist.CLIENT)
public final class ClientPassivePowers {
    private ClientPassivePowers() {}

    public static void accept(String dimension, String encoded) {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientPowerSnapshot.accept(player, dimension, encoded);
    }

    @SubscribeEvent public static void login(ClientPlayerNetworkEvent.LoggingIn event) {
        ClientPowerSnapshot.clear();
    }
    @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientPowerSnapshot.clear();
    }
    @SubscribeEvent public static void clonePlayer(ClientPlayerNetworkEvent.Clone event) {
        ClientPowerSnapshot.clear();
    }
}
