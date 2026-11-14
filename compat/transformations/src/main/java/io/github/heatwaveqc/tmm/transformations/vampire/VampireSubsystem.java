package io.github.heatwaveqc.tmm.transformations.vampire;

/** Persistent physiology is shared; these mechanics must have singular owners. */
public enum VampireSubsystem {
    BLOOD_STORAGE,
    PASSIVE_BLOOD_DRAIN,
    VANILLA_FOOD_EXHAUSTION,
    PASSIVE_HEALING,
    SUN_DAMAGE,
    GARLIC_AND_FIRE_DAMAGE,
    GENUINE_DEATH_INTERCEPTION,
    VISUAL_BLOOD_HUD,
    TEMPORARY_BAT_FORM,
    PROGRESSION
}
