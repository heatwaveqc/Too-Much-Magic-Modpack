package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

/** Cancel ONLY duplicate passive healing and sun damage in dual vampires. */
public final class JAWRDefenseEvents {
    private JAWRDefenseEvents() { }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        if (!ModList.get().isLoaded("witchery")) return;
        for (String eventName : new String[]{
                "dev.sterner.witchery.core.api.event.VampireAfflictionEvent$SunDamageApplied",
                "dev.sterner.witchery.core.api.event.VampireAfflictionEvent$BloodHealing"
        }) {
            try {
                Class<? extends Event> event = Class.forName(eventName).asSubclass(Event.class);
                NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, false, (Class) event,
                        (java.util.function.Consumer<Event>) JAWRDefenseEvents::onEvent);
                TransformationsMod.LOGGER.info("Bound optional JAWR vampire defense event {}", eventName);
            } catch (ReflectiveOperationException | LinkageError e) {
                TransformationsMod.LOGGER.error("JAWR exact-binary defense event unavailable: {}", eventName, e);
            }
        }
    }

    private static void onEvent(Event event) {
        if (!VampireChassisConfig.chassis()) return;
        try {
            Object raw = event.getClass().getMethod("getPlayer").invoke(event);
            if (raw instanceof ServerPlayer player && VampireChassisRuntime.isDual(player)) {
                ((ICancellableEvent) event).setCanceled(true);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            TransformationsMod.LOGGER.error("Unable to arbitrate JAWR vampire event {}", event.getClass(), e);
        }
    }
}
