package io.github.heatwaveqc.tyr.progression.client;

import io.github.heatwaveqc.tyr.progression.runtime.ClientPowerSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/** Client-only network entrypoint; all gameplay still authorized by the server. */
public final class ClientPassivePowers {
    private ClientPassivePowers() {}

    public static void accept(String dimension, String encoded) {
        LocalPlayer player = Minecraft.getInstance().player;
        ClientPowerSnapshot.accept(player, dimension, encoded);
    }
}
