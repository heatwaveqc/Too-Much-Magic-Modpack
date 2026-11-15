package io.github.heatwaveqc.tmm.transformations.lich;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Archlich-only native resurrection dispatch.
 *
 * Goety 3.2.2 and Forbidden Magic 0.5.37 both subscribe to LivingDeathEvent
 * at normal priority. We run FIRST at HIGHEST and call the verified
 * Forbidden Magic native onDeath handler only when a valid charged
 * phylactery exists. That handler spends one soul, handles the teleport/
 * health effects and credits phylactery progression, then cancels the
 * SAME event. By NeoForge's normal cancelled-event filtering, Goety and
 * Forbidden Magic's later normal listeners do not run a second save.
 *
 * If there is no charged phylactery, we intentionally do NOTHING: Goety's
 * original normal-priority Arca handler remains the fallback. Pure liches
 * are never intercepted. No third-party state is overwritten.
 *
 * WARNING: Version-specific reflective bridge. Never extrapolate this
 * contract to another JAWR/Goety build without inspecting its methods.
 */
public final class ArchlichResurrectionEvents {
    private static final String FORBIDDEN_ATTACHMENT =
            "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichPlayerAttachment";
    private static final String FORBIDDEN_POOL =
            "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichdomSoulPoolHandler";
    private static final String FORBIDDEN_EVENTS =
            "dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichdomEvents";
    private static final String GOETY_HELPER =
            "com.Polarice3.Goety.utils.LichdomHelper";

    private ArchlichResurrectionEvents() { }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,
                ArchlichResurrectionEvents::onLivingDeath);
    }

    private static void onLivingDeath(LivingDeathEvent event) {
        if (!ArchlichResurrectionConfig.enabled()
                || event.isCanceled()
                || !(event.getEntity() instanceof ServerPlayer player)
                || player.level().isClientSide()
                || !ModList.get().isLoaded("goety")
                || !ModList.get().isLoaded("witchery_forbidden_magic")) {
            return;
        }

        // Important: preflight BEFORE the native debit. Never infer
        // Archlich status from mod presence or a persisted TMM marker.
        try {
            Class<?> goety = Class.forName(GOETY_HELPER);
            boolean goetyLich = asBoolean(goety.getMethod("isLich", Player.class)
                    .invoke(null, player));

            Class<?> attachment = Class.forName(FORBIDDEN_ATTACHMENT);
            Object data = attachment.getMethod("getData", Player.class)
                    .invoke(null, player);
            if (data == null) return;
            boolean forbiddenLich = asBoolean(data.getClass()
                    .getMethod("isLich").invoke(data));
            int level = asNumber(data.getClass().getMethod("getLevel")
                    .invoke(data));

            if (!goetyLich || !forbiddenLich || level < 2) return;

            Class<?> poolClass = Class.forName(FORBIDDEN_POOL);
            Object pool = poolClass.getField("INSTANCE").get(null);
            int availableSouls = asNumber(poolClass.getMethod(
                    "getCurrentSouls", ServerPlayer.class).invoke(pool, player));

            if (!ArchlichResurrectionPolicy.prioritizePhylactery(
                    goetyLich, forbiddenLich, level, availableSouls,
                    event.isCanceled())) {
                return; // Goety owns the normal fallback event.
            }

            // Verified JNI signatures from the exact 0.5.37 JAR, not a
            // guessed reimplementation of drainNewestSoul().
            Class<?> events = Class.forName(FORBIDDEN_EVENTS);
            Method nativeSave = events.getMethod("onDeath",
                    LivingDeathEvent.class, LivingEntity.class);
            nativeSave.invoke(null, event, player);

            if (event.isCanceled()) {
                TransformationsMod.LOGGER.info(
                        "Archlich phylactery saved {}; Goety Arca was not charged",
                        player.getGameProfile().getName());
            } else {
                // A conflicting/removed phylactery is possible between
                // preflight and debit. No event was canceled: let Goety act.
                TransformationsMod.LOGGER.warn(
                        "Archlich phylactery preflight was positive but did not cancel death for {}; "
                        + "deferred to native Goety fallback",
                        player.getGameProfile().getName());
            }
        } catch (InvocationTargetException failure) {
            // A native handler may have partially mutated its soul pool.
            // This is a hard QA failure; do not attempt a second manual save.
            TransformationsMod.LOGGER.error(
                    "Archlich native phylactery handler failed for {}; "
                    + "disable experimental Archlich bridge and inspect save/log",
                    player.getGameProfile().getName(), failure.getCause());
        } catch (ReflectiveOperationException | LinkageError | SecurityException
                 | IllegalArgumentException failure) {
            // Missing/incompatible version: do not guess at provider state,
            // spend souls or suppress either native handler.
            TransformationsMod.LOGGER.error(
                    "Archlich native API contract unavailable for {}; "
                    + "leaving original death handlers untouched",
                    player.getGameProfile().getName(), failure);
        }
    }

    private static boolean asBoolean(Object value) {
        if (value instanceof Boolean result) return result;
        throw new IllegalStateException("Unexpected native lich flag: " + value);
    }

    private static int asNumber(Object value) {
        if (value instanceof Number number) return number.intValue();
        throw new IllegalStateException("Unexpected native lich number: " + value);
    }
}
