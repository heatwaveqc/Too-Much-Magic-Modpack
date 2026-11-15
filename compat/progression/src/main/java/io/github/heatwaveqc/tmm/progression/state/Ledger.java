package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;

/** Immutable snapshots are written back to the NeoForge player attachment. */
public record Ledger(Map<String, Track> paths) {
    public static final Codec<Ledger> CODEC =
        Codec.unboundedMap(Codec.STRING, Track.CODEC).xmap(Ledger::new, Ledger::paths);
    public Ledger { paths = Map.copyOf(paths); }
    public static Ledger empty() { return new Ledger(Map.of()); }
    public Track get(String id) { return paths.getOrDefault(id, Track.empty()); }
    public Ledger with(String id, Track state) {
        Map<String,Track> copy = new HashMap<>(paths);
        copy.put(id, state);
        return new Ledger(copy);
    }
}
