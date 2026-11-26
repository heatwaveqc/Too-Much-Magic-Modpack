package io.github.heatwaveqc.tyr.progression.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.network.AbilityNetwork;
import io.github.heatwaveqc.tyr.progression.network.ProgressionMenuNetwork;
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
    private static final String CATEGORY = "key.categories.tyr_progression";
    public static final KeyMapping SKILLS = key("skills",GLFW.GLFW_KEY_K);
    public static final KeyMapping BLINK = key("blink",GLFW.GLFW_KEY_V);
    public static final KeyMapping ARMOR = key("armor",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping TREASURE = key("treasure",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping GLIDE = key("glide",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping ORDEAL = key("ordeal",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping FEIGN_DEATH = key("feign_death",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping NIGHT_VISION=key("enhanced_night_vision",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping LAVA_VISION=key("lava_vision",GLFW.GLFW_KEY_UNKNOWN);
    public static final KeyMapping CHARM=key("charm",GLFW.GLFW_KEY_C);
    public static final KeyMapping TAME=new KeyMapping("key.tyr_progression.tame",
            InputConstants.Type.MOUSE,GLFW.GLFW_MOUSE_BUTTON_RIGHT,CATEGORY);
    private static KeyMapping key(String name,int code) {
        return new KeyMapping("key.tyr_progression."+name,
            InputConstants.Type.KEYSYM,code,CATEGORY);
    }
    @SubscribeEvent public static void register(RegisterKeyMappingsEvent event) {
        event.register(SKILLS);
        event.register(BLINK);
        event.register(ARMOR);
        event.register(TREASURE);
        event.register(GLIDE);
        event.register(ORDEAL);
        event.register(FEIGN_DEATH);
        event.register(NIGHT_VISION);
        event.register(LAVA_VISION);
        event.register(CHARM);
        event.register(TAME);
    }
    @EventBusSubscriber(modid = ProgressionMod.MOD_ID,value = Dist.CLIENT)
    public static final class Game {
        private Game() {}
        private static net.minecraft.client.player.LocalPlayer lastPlayer;
        private static boolean nativeTaming;
        @SubscribeEvent public static void tick(ClientTickEvent.Post event) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) { lastPlayer=null; return; }
            // When the bindings coincide, keep the actual vanilla interaction pipeline.
            boolean nativeUse=!TAME.isUnbound() && TAME.getKey().equals(minecraft.options.keyUse.getKey())
                    && TAME.getKeyModifier()==minecraft.options.keyUse.getKeyModifier();
            if(lastPlayer!=minecraft.player || nativeTaming!=nativeUse){
                lastPlayer=minecraft.player; nativeTaming=nativeUse;
                PacketDistributor.sendToServer(new AbilityNetwork.Action(nativeUse?"tame_native_on":"tame_native_off"));
            }
            if (minecraft.screen == null) {
                while (SKILLS.consumeClick()) {
                    minecraft.setScreen(new ProgressionScreen(""));
                }
            }
            if (minecraft.screen != null) {
                while(CHARM.consumeClick()){}
                while(TAME.consumeClick()){}
                while(FEIGN_DEATH.consumeClick()){}
                return;
            }
            send(CHARM,"charm");
            while(TAME.consumeClick()) {
                if(!nativeUse && minecraft.player.getMainHandItem().isEmpty())
                    PacketDistributor.sendToServer(new AbilityNetwork.Action("tame"));
            }
            send(BLINK,"blink");
            send(ARMOR,"armor");
            send(TREASURE,"treasure");
            send(GLIDE,"glide");
            send(ORDEAL,"ordeal");
            send(FEIGN_DEATH,"feign_death");
            send(NIGHT_VISION,"enhanced_night_vision");
            send(LAVA_VISION,"lava_vision");
        }
        @SubscribeEvent public static void skillKey(net.neoforged.neoforge.client.event.InputEvent.Key event){
            Minecraft mc=Minecraft.getInstance();
            if(mc.player==null||mc.getConnection()==null||mc.screen!=null||event.getAction()!=GLFW.GLFW_PRESS)return;
            SkillKeybindings.activate(event.getKey(),event.getModifiers());
        }
        private static void send(KeyMapping key,String action) {
            while (key.consumeClick()) PacketDistributor.sendToServer(new AbilityNetwork.Action(action));
        }
    }
}
