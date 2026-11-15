package io.github.heatwaveqc.tmm.transformations.vampire;

import io.github.heatwaveqc.tmm.transformations.core.Observation;

/**
 * Target ownership contract, NOT proof of runtime arbitration.
 * If both vampire systems are active, source-specific hooks and resource
 * translations are necessary. The bridge does not silently claim success.
 */
public final class VampireOwnership {
    private VampireOwnership() { }

    public static VampireOwner target(Observation vampirism, Observation jawr, VampireSubsystem system) {
        if (vampirism == null || jawr == null || system == null
                || vampirism == Observation.UNKNOWN || jawr == Observation.UNKNOWN) {
            return VampireOwner.UNVERIFIED;
        }
        if (vampirism == Observation.ABSENT && jawr == Observation.ABSENT) {
            return VampireOwner.UNVERIFIED;
        }
        if (vampirism == Observation.PRESENT && jawr == Observation.ABSENT) {
            return VampireOwner.VAMPIRISM;
        }
        if (vampirism == Observation.ABSENT && jawr == Observation.PRESENT) {
            return VampireOwner.JAWR;
        }
        return switch (system) {
            case BLOOD_STORAGE, PASSIVE_BLOOD_DRAIN, VANILLA_FOOD_EXHAUSTION,
                 PASSIVE_HEALING, VISUAL_BLOOD_HUD -> VampireOwner.JAWR;
            case GENUINE_DEATH_INTERCEPTION -> VampireOwner.VAMPIRISM;
            case PROGRESSION -> VampireOwner.SEPARATE_NATIVE_PROGRESSIONS;
            case SUN_DAMAGE, GARLIC_AND_FIRE_DAMAGE, TEMPORARY_BAT_FORM ->
                    VampireOwner.BRIDGE_REQUIRED;
        };
    }
}
