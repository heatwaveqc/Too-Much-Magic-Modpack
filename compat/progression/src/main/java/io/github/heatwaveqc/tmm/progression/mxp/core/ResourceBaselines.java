package io.github.heatwaveqc.tmm.progression.mxp.core;

/**
 * Frozen first-access reference values for MXP resource normalization.
 * Neo Vitae 1.21.1 BloodOrbStats data map: weak (tier 0) orb
 * tier=0, fluidCapacity=4000, animaCapacity=5000, fillRate=2.
 */
public final class ResourceBaselines {
    public static final double ARS_STARTING_MANA = 100.0;
    public static final double IRONS_STARTING_MANA = 100.0;
    public static final double BOTANIA_PORTABLE_MANA = 500_000.0;
    public static final double NEO_TIER_ZERO_ANIMA_EV = 5_000.0;
    // Initial owner-approved JAWR blood-normalization target; confirm the
    // actual 0.5.39 player reservoir on a disposable server before enabling a hook.
    public static final double JAWR_INITIAL_BLOOD = 900.0;

    private ResourceBaselines() {}
}
