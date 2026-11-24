package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.ProgressionMod;
import io.github.heatwaveqc.tyr.progression.foundation.RegularHomePolicy.NativeGrant;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

/** Optional native 1.0.9 adapter. Suppress ONLY the automatic first-login
 * item, preserving every unrelated server config, stock recipe, shaft and
 * chest option. No player inventory edits and no client-supplied claims. */
final class PrefabHomeGrant {
    private PrefabHomeGrant() {}
    private static boolean warned;
    static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, PrefabHomeGrant::onStarted);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, PrefabHomeGrant::onLogin);
    }
    private static void onStarted(ServerStartedEvent event) {
        warned=false;
        suppress();
    }
    private static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        suppress();
    }
    private static boolean suppress() {
        if (!ModList.get().isLoaded("prefab")) return false;
        try {
            var mod=ModList.get().getModContainerById("prefab").orElseThrow();
            if (!mod.getModInfo().getVersion().toString().equals("1.0.9"))
                throw new IllegalStateException("Unverified Prefab version");
            Class<?> base=Class.forName("com.prefab.PrefabBase");
            Class<?> configType=Class.forName("com.prefab.config.ModConfiguration");
            Object config=base.getField("serverConfiguration").get(null);
            if (!configType.isInstance(config)) throw new IllegalStateException("Prefab server config unavailable");
            var grant=configType.getField("newPlayersGetStartingItem");
            grant.setBoolean(config,false);
            return !grant.getBoolean(config);
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ex) {
            warn(ex);
            return false;
        }
    }
    private static void warn(Throwable ex) {
        if (!warned) {
            warned=true;
            ProgressionMod.LOGGER.warn("Starter home disabled: cannot verify Prefab native automatic-grant policy",ex);
        }
    }
    static NativeGrant nativeGrant(ServerPlayer player) {
        if (!suppress()) return NativeGrant.UNKNOWN;
        try {
            Class<?> config=Class.forName("com.prefab.config.EntityPlayerConfiguration");
            Object record=config.getMethod("loadFromEntity", net.minecraft.world.entity.player.Player.class)
                .invoke(null,player);
            return config.getField("givenHouseBuilder").getBoolean(record)
                ? NativeGrant.GIVEN : NativeGrant.NOT_GIVEN;
        } catch (ReflectiveOperationException | LinkageError | RuntimeException ex) {
            warn(ex);
            return NativeGrant.UNKNOWN;
        }
    }
}
