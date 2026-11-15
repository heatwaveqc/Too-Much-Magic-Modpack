package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Registers on the actual Vampirism 1.10.13 NeoForge faction events. No hard
 * provider linkage in the standalone addon; class signatures are jar-verified.
 */
public final class VampireFactionEvents {
    private VampireFactionEvents() { }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        if (!ModList.get().isLoaded("vampirism")) return;
        try {
            Class<? extends Event> pre = (Class<? extends Event>) Class.forName(
                    "de.teamlapen.vampirism.api.event.PlayerFactionEvent$FactionLevelChangePre")
                    .asSubclass(Event.class);
            Class<? extends Event> post = (Class<? extends Event>) Class.forName(
                    "de.teamlapen.vampirism.api.event.PlayerFactionEvent$FactionLevelChanged")
                    .asSubclass(Event.class);
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, false, (Class) pre,
                    (java.util.function.Consumer<Event>) VampireFactionEvents::onPre);
            NeoForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, (Class) post,
                    (java.util.function.Consumer<Event>) VampireFactionEvents::onPost);
            TransformationsMod.LOGGER.info("Bound pinned Vampirism faction pre/post event observers");
        } catch (ReflectiveOperationException | LinkageError | ClassCastException ex) {
            TransformationsMod.LOGGER.error("Failed registering pinned Vampirism integration", ex);
        }
    }

    private static ServerPlayer player(Event event) throws ReflectiveOperationException {
        Object handler = event.getClass().getMethod("getPlayer").invoke(event);
        Object entity = handler.getClass().getMethod("asEntity").invoke(handler);
        return entity instanceof ServerPlayer sp ? sp : null;
    }

    private static String factionId(Object faction) throws ReflectiveOperationException {
        if (faction == null) return "";
        Object id = faction.getClass().getMethod("getID").invoke(faction);
        return id instanceof ResourceLocation rl ? rl.toString() : "";
    }

    private static void onPre(Event event) {
        if (!VampireChassisConfig.chassis()) return;
        try {
            ServerPlayer p = player(event);
            if (p == null || VampireChassisRuntime.inTransaction()) return;
            String old = factionId(event.getClass().getMethod("getCurrentFaction").invoke(event));
            String next = factionId(event.getClass().getMethod("getNewFaction").invoke(event));
            int nextLevel = ((Number) event.getClass().getMethod("getNewLevel").invoke(event)).intValue();
            boolean fromVampire = old.equals("vampirism:vampire");
            boolean toVampire = next.equals("vampirism:vampire") && nextLevel > 0;
            if (fromVampire && !toVampire
                    && !VampireChassisRuntime.preflightVampirismCure(p)) {
                ((ICancellableEvent) event).setCanceled(true);
                p.sendSystemMessage(Component.literal(
                        "Full vampire cure refused: could not preflight JAWR removal."));
            }
            // Ordinary human -> Vampirism must NOT initialize JAWR.
        } catch (ReflectiveOperationException | RuntimeException e) {
            TransformationsMod.LOGGER.error("Vampirism pre-faction hook failed", e);
            if (event instanceof ICancellableEvent cancel) cancel.setCanceled(true);
        }
    }

    private static void onPost(Event event) {
        if (!VampireChassisConfig.chassis()) return;
        try {
            ServerPlayer p = player(event);
            if (p == null || VampireChassisRuntime.inTransaction()) return;
            String old = factionId(event.getClass().getMethod("getOldFaction").invoke(event));
            String next = factionId(event.getClass().getMethod("getCurrentFaction").invoke(event));
            if (old.equals("vampirism:vampire") && !next.equals("vampirism:vampire")) {
                VampireChassisRuntime.afterVampirismCure(p);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            TransformationsMod.LOGGER.error("Vampirism cure synchronization failed", e);
        }
    }
}
