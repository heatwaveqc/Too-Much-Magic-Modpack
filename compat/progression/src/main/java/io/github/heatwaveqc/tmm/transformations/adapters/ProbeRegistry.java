package io.github.heatwaveqc.tmm.transformations.adapters;

import io.github.heatwaveqc.tmm.transformations.TransformationsMod;
import io.github.heatwaveqc.tmm.transformations.core.Observation;
import io.github.heatwaveqc.tmm.transformations.core.ProviderState;
import io.github.heatwaveqc.tmm.transformations.core.StateSnapshot;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import net.minecraft.server.level.ServerPlayer;

/**
 * No speculative integrations are registered in the scaffold.
 * An exception or absent probe is UNKNOWN (fail closed), never ABSENT.
 */
public final class ProbeRegistry {
    private static final List<TransformationProbe> PROBES = new ArrayList<>();

    private ProbeRegistry() { }

    public static synchronized void register(TransformationProbe probe) {
        Objects.requireNonNull(probe, "probe");
        Objects.requireNonNull(probe.state(), "probe state");
        if (PROBES.stream().anyMatch(existing -> existing.state() == probe.state())) {
            throw new IllegalArgumentException("Duplicate probe: " + probe.state());
        }
        PROBES.add(probe);
    }

    public static synchronized int registeredCount() {
        return PROBES.size();
    }

    public static StateSnapshot observe(ServerPlayer player) {
        List<TransformationProbe> probes;
        synchronized (ProbeRegistry.class) {
            probes = List.copyOf(PROBES);
        }
        EnumMap<ProviderState, Observation> observations = new EnumMap<>(ProviderState.class);
        for (TransformationProbe probe : probes) {
            Observation value;
            try {
                value = probe.observe(player);
            } catch (RuntimeException ex) {
                TransformationsMod.LOGGER.error("State probe failed for {}", probe.state(), ex);
                value = Observation.UNKNOWN;
            }
            observations.put(probe.state(), value == null ? Observation.UNKNOWN : value);
        }
        return new StateSnapshot(observations);
    }
}
