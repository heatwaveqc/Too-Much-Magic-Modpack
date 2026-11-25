package io.github.heatwaveqc.tmm.transformations.core;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Snapshot of actual upstream conditions. Missing probes are always UNKNOWN,
 * never silently considered ABSENT.
 */
public final class StateSnapshot {
    private final Map<ProviderState, Observation> observed;

    public StateSnapshot(Map<ProviderState, Observation> observations) {
        EnumMap<ProviderState, Observation> copy = new EnumMap<>(ProviderState.class);
        for (ProviderState state : ProviderState.values()) {
            Observation value = observations.get(state);
            copy.put(state, value == null ? Observation.UNKNOWN : value);
        }
        observed = Collections.unmodifiableMap(copy);
    }

    public Observation get(ProviderState state) {
        return observed.get(state);
    }

    public Map<ProviderState, Observation> observations() {
        return observed;
    }

    public long verifiedCount() {
        return observed.values().stream().filter(value -> value != Observation.UNKNOWN).count();
    }

    public boolean isComplete() {
        return verifiedCount() == ProviderState.values().length;
    }

    public boolean present(ProviderState state) {
        return get(state) == Observation.PRESENT;
    }
}
