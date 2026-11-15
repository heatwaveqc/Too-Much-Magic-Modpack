package io.github.heatwaveqc.tmm.transformations.core;

/**
 * Safety policy for the operator-only two-native-action pilot.
 * No native faction/player state is ever mutated by this decision.
 *
 * A complete snapshot is NOT required: unrelated providers lack probes and
 * remain UNKNOWN. Only directly observed JAWR dual afflictions authorize
 * these limited ability experiments, never a real species transition.
 */
public final class AbominationPilotPolicy {
    private AbominationPilotPolicy() { }

    public enum Verdict {
        DISABLED, UNKNOWN_AFFLICTION, MISSING_DUAL_AFFLICTION, HUNTER_CONFLICT, READY
    }

    public static Verdict assess(boolean enabled, Observation jawrVampire,
                                 Observation jawrWerewolf, Observation hunter) {
        if (!enabled) return Verdict.DISABLED;
        if (jawrVampire == null || jawrWerewolf == null
                || jawrVampire == Observation.UNKNOWN
                || jawrWerewolf == Observation.UNKNOWN) {
            return Verdict.UNKNOWN_AFFLICTION;
        }
        if (hunter == Observation.PRESENT) return Verdict.HUNTER_CONFLICT;
        if (jawrVampire != Observation.PRESENT || jawrWerewolf != Observation.PRESENT) {
            return Verdict.MISSING_DUAL_AFFLICTION;
        }
        // Unknown Hunter is deliberately NOT promoted to ABSENT; the runtime
        // independently requires the pinned Vampirism mod and verifies its
        // faction is not Hunter. This class alone does not authorize actions.
        return Verdict.READY;
    }

    public static long nextReadyTick(long now, int nativeCooldown) {
        return now + Math.max(20, Math.min(nativeCooldown, 20 * 60));
    }
}
