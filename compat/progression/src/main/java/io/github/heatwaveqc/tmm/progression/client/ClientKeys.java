package io.github.heatwaveqc.tmm.progression.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.heatwaveqc.tmm.progression.ProgressionMod;
import io.github.heatwaveqc.tmm.progression.network.AbilityNetwork;
import io.github.heatwaveqc.tmm.progression.network.ProgressionMenuNetwork;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

/** Optional keybinds can be freely rebound in Controls; all gameplay is validated on server. */
@EventBusSubscriber(modid = ProgressionMod.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ClientKeys {
    private ClientKeys() {}
    private static final String CATEGORY = "key.categories.tmm_progression";
    public static final KeyMapping SKILLS = key("skills",GLFW.GLFW_KEY_K);
    public static final KeyMapping BLINK = key("blink",GLFW.GLFW_KEY_V);
    public static final KeyMapping ARMOR = key("armor",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping TREASURE = key("treasure",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping GLIDE = key("glide",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping ORDEAL = key("ordeal",GLFW.GLFW_KEY_UNKNOWN);
    private static KeyMapping key(String name,int code) {
        return new KeyMapping("key.tmm_progression."+name,
            InputConstants.Type.KEYSYM,code,CATEGORY);
    }
    @SubscribeEvent public static void register(RegisterKeyMappingsEvent event) {
        event.register(SKILLS);
        event.register(BLINK);
        event.register(ARMOR);
        event.register(TREASURE);
        event.register(GLIDE);
        event.register(ORDEAL);
    }
    @EventBusSubscriber(modid = ProgressionMod.MOD_ID,value = Dist.CLIENT)
    public static final class Game {
        private Game() {}
        @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) return;
            if (minecraft.screen == null) {
                while (SKILLS.consumeClick()) {
                    minecraft.setScreen(new ProgressionScreen("tmm_progression:monk"));
                }
            }
            if (minecraft.screen != null) return;
            send(BLINK,"blink");
            send(ARMOR,"armor");
            send(TREASURE,"treasure");
            send(GLIDE,"glide");
            send(ORDEAL,"ordeal");
        }
        private static void send(KeyMapping key,String action) {
            while (key.consumeClick()) PacketDistributor.sendToServer(new AbilityNetwork.Action(action));
        }
    }
}
