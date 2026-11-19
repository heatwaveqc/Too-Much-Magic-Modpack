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

    public static final double PSI_STARTING_POOL=5000.0;
    // Owner-approved EMC-derived reference (2026-11-18):
    // 5 mB ordinary EvilCraft blood = 1 EMC = 1 base MXP;
    // 50,000 native Hex Media = 32 EMC (ordinary amethyst shard).
    // 1,000 MXP * 50,000 Media / 32 EMC = 1,562,500 Media.
    // Fixed policy reference; never derived from live ProjectE prices.
    public static final double HEX_EMC_MEDIA_REFERENCE = 1_562_500.0;
    // Previously approved basic Blood Extractor reference (5,000 mB).
    public static final double EVILCRAFT_PORTABLE_BLOOD = 5_000.0;
    public static final double ROOTS_STARTING_TERRA=40.0;
    public static final double EMBERS_FIRST_JAR=2000.0;
    // Owner approved both EMC-derived references 2026-11-18.
    public static final double GOETY_EMC_SOUL_REFERENCE=500.0;
    public static final double PRIMAL_EMC_CENTIMANA_REFERENCE=50_000.0;
    private ResourceBaselines() {}
}
