package io.github.heatwaveqc.tyr.progression.core;

import java.util.Set;

/** Exact event identity + server-resolved tag membership, never client-provided. */
public record Signal(String event, String target, Set<String> tags) {
    public Signal { tags = Set.copyOf(tags); }
    public static Signal external(String id) { return new Signal("external",id,Set.of()); }
    public boolean matches(String kind, String selector) {
        if (!event.equals(kind)) return false;
        if ("*".equals(selector)) return true;
        return selector.startsWith("#")
            ? tags.contains(selector.substring(1))
            : target.equals(selector);
    }
}
