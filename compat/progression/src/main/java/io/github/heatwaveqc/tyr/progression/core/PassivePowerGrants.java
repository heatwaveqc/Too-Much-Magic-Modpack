package io.github.heatwaveqc.tyr.progression.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Immutable, source-keyed grants. A power is active while at least one
 * authorized source owns it. Replacing one source does not revoke another.
 *
 * No potion effects, extra wallet, or path-owned persistence: the server
 * projects these grants from existing durable Progression state.
 */
public record PassivePowerGrants(Map<String, Set<String>> sources) {
    private static final String ID = "[a-z0-9_.-]+:[a-z0-9_./-]+";
    public static final int MAX_POWERS = 64;
    public static final int MAX_SOURCES_PER_POWER = 32;

    public PassivePowerGrants {
        if (sources == null || sources.size() > MAX_POWERS)
            throw new IllegalArgumentException("Invalid passive power count");
        var safe = new HashMap<String, Set<String>>();
        sources.forEach((power, grantors) -> {
            validate(power);
            if (grantors == null || grantors.size() > MAX_SOURCES_PER_POWER)
                throw new IllegalArgumentException("Invalid grant sources for " + power);
            var copy = Set.copyOf(grantors);
            for (String source : copy) validate(source);
            if (!copy.isEmpty()) safe.put(power, copy);
        });
        sources = Map.copyOf(safe);
    }

    public static PassivePowerGrants empty() {
        return new PassivePowerGrants(Map.of());
    }

    public static void validate(String name) {
        if (name == null || name.length() > 128 || !name.matches(ID))
            throw new IllegalArgumentException("Invalid namespaced ability/source: " + name);
    }

    public boolean active(String power) {
        return sources.containsKey(power);
    }

    public Set<String> activeIds() {
        return sources.keySet();
    }

    /** Full source replacement for one power, not a global revoke. */
    public PassivePowerGrants withSources(String power, Set<String> grantors) {
        validate(power);
        var updated = new HashMap<>(sources);
        if (grantors == null || grantors.isEmpty()) updated.remove(power);
        else updated.put(power, Set.copyOf(grantors));
        return new PassivePowerGrants(updated);
    }

    /** Safe, bounded client projection that never includes source identities. */
    public String encodeActive() {
        return String.join("\n", activeIds().stream().sorted().toList());
    }

    public static Set<String> decodeActive(String encoded) {
        if (encoded == null || encoded.isEmpty()) return Set.of();
        if (encoded.length() > 8192) throw new IllegalArgumentException("Oversized passive power packet");
        var names = encoded.split("\n", -1);
        if (names.length > MAX_POWERS) throw new IllegalArgumentException("Too many passive powers");
        var result = new HashSet<String>();
        for (String name : names) {
            validate(name);
            if (!result.add(name))
                throw new IllegalArgumentException("Duplicate passive power " + name);
        }
        return Set.copyOf(result);
    }
}
