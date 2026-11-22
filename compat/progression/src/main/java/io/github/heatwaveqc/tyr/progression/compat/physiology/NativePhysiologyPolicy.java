package io.github.heatwaveqc.tyr.progression.compat.physiology;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Path/native-physiology compatibility, independent of any optional mod classes.
 *
 * <p>This policy deliberately does NOT arbitrate one native form against another:
 * existing JAWR/lich/dragon transitions and their native costs are a separate,
 * provider-specific decision. Nor does it perform graduation or Path enrollment.
 * It answers only whether a specialist Path can coexist with observed physiology.
 */
public final class NativePhysiologyPolicy {
    private NativePhysiologyPolicy() {}

    public enum Form {
        JAWR_VAMPIRE, JAWR_WEREWOLF, GOETY_LICH, FORBIDDEN_MAGIC_LICH,
        CAVE_DRAGON, FOREST_DRAGON, SEA_DRAGON
    }

    /** ABSENT includes a provider mod that is not installed; UNKNOWN means a loaded
     * provider could not be reliably observed. Never map UNKNOWN to ABSENT. */
    public enum Presence { PRESENT, ABSENT, UNKNOWN }

    public enum Decision { ALLOW, DENY_NEWBIE, DENY_UNVERIFIED, DENY_INCOMPATIBLE }

    /** An immutable, complete record of all supported provider states. */
    public record Snapshot(Map<Form, Presence> states) {
        public Snapshot {
            Objects.requireNonNull(states, "states");
            EnumMap<Form, Presence> verified = new EnumMap<>(Form.class);
            for (Form form : Form.values()) {
                verified.put(form, Objects.requireNonNull(
                    states.get(form), "missing observation: " + form));
            }
            states = Map.copyOf(verified);
        }

        public Presence get(Form form) {
            return states.get(Objects.requireNonNull(form, "form"));
        }

        public boolean hasUnknown() {
            return states.values().stream().anyMatch(p -> p == Presence.UNKNOWN);
        }

        public static Snapshot allAbsent() {
            EnumMap<Form, Presence> absent = new EnumMap<>(Form.class);
            for (Form form : Form.values()) absent.put(form, Presence.ABSENT);
            return new Snapshot(absent);
        }
    }

    /**
     * Explicit Path identifiers, not display names. Rules are immutable and
     * supplied by Progression's data-driven compatibility registry.
     * The three approved cross-Dragon pairings belong in that registry;
     * no generic "any dragon" wildcard or assumption from a themed Path name.
     */
    public record Rules(Set<String> baselinePaths, Map<String, Set<Form>> allowedPairs) {
        public Rules {
            Objects.requireNonNull(baselinePaths, "baselinePaths");
            Objects.requireNonNull(allowedPairs, "allowedPairs");
            baselinePaths = Set.copyOf(baselinePaths);
            Map<String, Set<Form>> copy = new java.util.HashMap<>();
            for (var entry : allowedPairs.entrySet()) {
                String path = Objects.requireNonNull(entry.getKey(), "Path ID");
                if (path.isBlank() || baselinePaths.contains(path))
                    throw new IllegalArgumentException("Invalid specialist Path ID: " + path);
                var forms = Objects.requireNonNull(entry.getValue(), "allowed forms");
                copy.put(path, forms.isEmpty()
                    ? Set.of() : Set.copyOf(EnumSet.copyOf(forms)));
            }
            allowedPairs = Map.copyOf(copy);
        }

        public boolean isBaseline(String path) {
            return baselinePaths.contains(path);
        }

        public boolean permits(String path, Form form) {
            return allowedPairs.getOrDefault(path, Set.of()).contains(form);
        }
    }

    /**
     * A Path join must satisfy every currently present native form.
     * Newbie transitions are owned by the personal graduation API, not this
     * compatibility checker; it never grants enrollment on its own.
     */
    public static Decision mayJoin(boolean newbie, String requestedPath,
                                   Snapshot nativeForms, Rules rules) {
        Objects.requireNonNull(requestedPath, "requestedPath");
        Objects.requireNonNull(nativeForms, "nativeForms");
        Objects.requireNonNull(rules, "rules");
        if (newbie) return Decision.DENY_NEWBIE;
        // Normie is a baseline, NOT a second enrolled specialist.
        if (rules.isBaseline(requestedPath)) return Decision.ALLOW;
        if (nativeForms.hasUnknown()) return Decision.DENY_UNVERIFIED;
        for (Form form : Form.values()) {
            if (nativeForms.get(form) == Presence.PRESENT
                    && !rules.permits(requestedPath, form))
                return Decision.DENY_INCOMPATIBLE;
        }
        return Decision.ALLOW;
    }

    /**
     * A new native-form acquisition must satisfy every joined specialist Path.
     * This checks Path compatibility only; native-vs-native combinations, ritual
     * conditions and provider attachment writes are never authorized here.
     */
    public static Decision mayAcquire(boolean newbie, Set<String> joinedPaths,
                                      Snapshot before, Form acquiring, Rules rules) {
        Objects.requireNonNull(joinedPaths, "joinedPaths");
        Objects.requireNonNull(before, "before");
        Objects.requireNonNull(acquiring, "acquiring");
        Objects.requireNonNull(rules, "rules");
        // Native level upgrades are not new initiations; preserve native progression.
        if (before.get(acquiring) == Presence.PRESENT) return Decision.ALLOW;
        if (newbie) return Decision.DENY_NEWBIE;
        if (before.hasUnknown()) return Decision.DENY_UNVERIFIED;
        for (String path : joinedPaths) {
            if (!rules.isBaseline(path) && !rules.permits(path, acquiring))
                return Decision.DENY_INCOMPATIBLE;
        }
        return Decision.ALLOW;
    }
}
