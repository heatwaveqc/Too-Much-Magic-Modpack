package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

/**
 * Read-only observer for Vampirism 1.10.x on NeoForge 1.21.1.
 *
 * Source-backed API: VampirismAPI.factionPlayerHandler(Player),
 * IFactionPlayerHandler.getCurrentFaction(), IFaction.getID(), and
 * VReference IDs vampirism:vampire and vampirism:hunter.
 * Reflection keeps the optional provider off the bridge compile classpath.
 * An upstream jar mismatch stays UNKNOWN, not an invented observation.
 */
public final class VampirismFactionProbe implements TransformationProbe {
    private final ProviderState state;
    private final String factionId;

    public VampirismFactionProbe(ProviderState state) {
        if (state != ProviderState.VAMPIRISM_VAMPIRE && state != ProviderState.VAMPIRISM_HUNTER) {
            throw new IllegalArgumentException("Unsupported Vampirism faction probe: " + state);
        }
        this.state = state;
        this.factionId = state == ProviderState.VAMPIRISM_VAMPIRE ? "vampirism:vampire" : "vampirism:hunter";
    }

    @Override
    public ProviderState state() {
        return state;
    }

    @Override
    public Observation observe(ServerPlayer player) {
        if (!ModList.get().isLoaded("vampirism")) {
            return Observation.UNKNOWN;
        }
        try {
            Class<?> api = Class.forName("de.teamlapen.vampirism.api.VampirismAPI");
            Object handler = api.getMethod("factionPlayerHandler", Player.class).invoke(null, player);
            if (handler == null) return Observation.UNKNOWN;
            Object faction = handler.getClass().getMethod("getCurrentFaction").invoke(handler);
            if (faction == null) return Observation.ABSENT;
            Object id = faction.getClass().getMethod("getID").invoke(faction);
            if (!(id instanceof ResourceLocation location)) return Observation.UNKNOWN;
            return factionId.equals(location.toString()) ? Observation.PRESENT : Observation.ABSENT;
        } catch (ReflectiveOperationException | LinkageError | SecurityException ex) {
            Throwable cause = ex instanceof InvocationTargetException invocation && invocation.getCause() != null
                    ? invocation.getCause() : ex;
            TransformationsMod.LOGGER.warn("Could not read Vampirism faction state ({}); returning UNKNOWN", state, cause);
            return Observation.UNKNOWN;
        }
    }
}
