package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Only JAWR sunlight is suppressed in Vampirism's vampire faction biome.
 * Do NOT cancel JAWR's normal healing or hunger. Other sun exposure stays
 * native to both mods pending a live double-damage audit.
 */
public final class JAWRDefenseEvents {
    private static final TagKey<Biome> VAMPIRE_FOREST = TagKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("vampirism", "has_faction/vampire"));

    private JAWRDefenseEvents() { }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        if (!ModList.get().isLoaded("witchery")) return;
        try {
            Class<? extends Event> event = Class.forName(
                    "dev.sterner.witchery.core.api.event.VampireAfflictionEvent$SunDamageApplied")
                    .asSubclass(Event.class);
            NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, false, (Class) event,
                    (java.util.function.Consumer<Event>) JAWRDefenseEvents::onSun);
            TransformationsMod.LOGGER.info("Registered JAWR vampire-forest sunlight exception");
        } catch (ReflectiveOperationException | LinkageError e) {
            TransformationsMod.LOGGER.error("JAWR sunlight exception event unavailable", e);
        }
    }

    private static void onSun(Event event) {
        try {
            Object raw = event.getClass().getMethod("getPlayer").invoke(event);
            if (raw instanceof ServerPlayer player
                    && player.level().getBiome(player.blockPosition()).is(VAMPIRE_FOREST)
                    && event instanceof ICancellableEvent cancellable) {
                cancellable.setCanceled(true);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            TransformationsMod.LOGGER.error("Unable to evaluate JAWR vampire-forest sun exception", e);
        }
    }
}
