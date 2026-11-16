package io.github.heatwaveqc.tmm.transformations.dracolich;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.adapters.DragonSurvivalProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.ForbiddenMagicLichProbe;
import io.github.heatwaveqc.tmm.transformations.adapters.GoetyLichProbe;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Attempt Dragon Survival's original on-death ability at HIGHEST priority
 * for a Dracolich, before either lich source can spend resurrection fuel.
 *
 * Dragon Survival registers OnDeath.trigger at NORMAL priority. Its native
 * method heals the player with passive dragon abilities and cancels the
 * death event if healed. A narrow mixin suppresses its later duplicate call
 * for this same event; no other dragon or lich is affected.
 */
public final class DracolichDragonFirstEvents {
    private static final DragonSurvivalProbe DRAGON = new DragonSurvivalProbe();
    private static final GoetyLichProbe GOETY = new GoetyLichProbe();
    private static final ForbiddenMagicLichProbe FORBIDDEN = new ForbiddenMagicLichProbe();
    private static final Map<LivingDeathEvent, Boolean> DISPATCHED =
            Collections.synchronizedMap(new WeakHashMap<>());

    private DracolichDragonFirstEvents() { }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST,
                DracolichDragonFirstEvents::onDeath);
    }

    private static void onDeath(LivingDeathEvent event) {
        if (event.isCanceled() || !(event.getEntity() instanceof ServerPlayer player)
                || DRAGON.observe(player) != Observation.PRESENT) return;
        Observation goety = GOETY.observe(player);
        Observation forbidden = FORBIDDEN.observe(player);
        if (goety == Observation.UNKNOWN || forbidden == Observation.UNKNOWN) return;
        if (!DracolichPolicy.isDracolich(DracolichPolicy.resolve(true,
                goety == Observation.PRESENT, forbidden == Observation.PRESENT))) return;

        try {
            Class<?> trigger = Class.forName(
                    "by.dragonsurvivalteam.dragonsurvival.registry.dragon.ability.activation.trigger.OnDeath");
            try {
                trigger.getMethod("trigger", LivingDeathEvent.class).invoke(null, event);
            } finally {
                // Whether native dragon rescue worked or not, don't fire its
                // same passive abilities again at NORMAL priority. If dragon
                // saved the player, canceled-event semantics skip lich costs.
                DISPATCHED.put(event, true);
            }
        } catch (InvocationTargetException e) {
            TransformationsMod.LOGGER.error("Native Dragon Survival death passive failed "
                    + "for Dracolich {}; leaving lich fallback available",
                    player.getGameProfile().getName(), e.getCause());
        } catch (ReflectiveOperationException | LinkageError | RuntimeException e) {
            TransformationsMod.LOGGER.error("Dragon Survival OnDeath API unavailable "
                    + "for Dracolich {}; leaving original listeners in place",
                    player.getGameProfile().getName(), e);
        }
    }

    /** Called at Dragon Survival's native NORMAL-priority OnDeath.trigger entrypoint. */
    public static boolean consumeNativeRepeat(LivingDeathEvent event) {
        return DISPATCHED.remove(event) != null;
    }
}
