package io.github.heatwaveqc.tmm.transformations.core;

/**
 * Cross-mod policy independent of provider APIs.
 *
 * An enrolled class is a human discipline: it cannot acquire a new
 * supernatural species. Prior acquisitions can still advance or be cured
 * without deleting player data during migration.
 */
public final class SpeciesEnrollmentPolicy {
    private SpeciesEnrollmentPolicy() {}

    public static boolean canAcquire(boolean alreadyHasForm, boolean newbie, boolean hasJoinedProgression) {
        return alreadyHasForm || (!newbie && !hasJoinedProgression);
    }

    /** Joining a human Progression path requires verified absence of native forms. */
    public static boolean canJoinHumanPath(StateSnapshot snapshot) {
        if (snapshot == null || !snapshot.isComplete()) return false;
        for (Observation observation : snapshot.observations().values()) {
            if (observation != Observation.ABSENT) return false;
        }
        return true;
    }
}
