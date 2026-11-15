package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;

/** Immutable snapshots are written back to the NeoForge player attachment. */
public record Ledger(Map<String, Track> paths) {
    private static final Codec<Track> TRACK_CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.BOOL.fieldOf("joined").forGetter(Track::joined),
        Codec.LONG.fieldOf("xp").forGetter(Track::xp),
        Codec.INT.fieldOf("rank").forGetter(Track::rank),
        Codec.INT.fieldOf("points").forGetter(Track::points),
        Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("skills").forGetter(Track::skills),
        Codec.STRING.listOf().fieldOf("milestones").forGetter(Track::milestones),
        Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("counters").forGetter(Track::counters)
    ).apply(i, Track::new));

    public static final Codec<Ledger> CODEC =
        Codec.unboundedMap(Codec.STRING, TRACK_CODEC).xmap(Ledger::new, Ledger::paths);
    public Ledger { paths = Map.copyOf(paths); }
    public static Ledger empty() { return new Ledger(Map.of()); }
    public Track get(String id) { return paths.getOrDefault(id, Track.empty()); }
    public Ledger with(String id, Track state) {
        Map<String,Track> copy = new HashMap<>(paths);
        copy.put(id, state);
        return new Ledger(copy);
    }
}
