package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;

/** Server-owned persistent switches and finite modes for class-specific effect handlers. */
public record Modes(Map<String,String> values) {
    public static final Codec<Modes> CODEC = Codec.unboundedMap(Codec.STRING,Codec.STRING)
        .xmap(Modes::new,Modes::values);
    public Modes { values = Map.copyOf(values); }
    public static Modes empty() { return new Modes(Map.of()); }
    public String get(String key, String fallback) { return values.getOrDefault(key,fallback); }
    public Modes with(String key, String value) {
        if (value.equals(values.get(key))) return this;
        Map<String,String> next = new HashMap<>(values);
        next.put(key,value);
        return new Modes(next);
    }
}
