package io.github.heatwaveqc.tmm.transformations.vampire;

import net.neoforged.neoforge.common.ModConfigSpec;

/** Intentionally disabled until disposable-world integration validation. */
public final class VampireChassisConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_CHASSIS = BUILDER
            .comment("EXPERIMENTAL: enable ascended JAWR-owned blood and targeted cross-mod interception in test worlds only.")
            .define("experimentalVampireChassis", false);
    public static final ModConfigSpec.BooleanValue AUTOMATIC_DUAL_ACQUISITION = BUILDER
            .comment("EXPERIMENTAL: Lilith/JAWR acquisition also enrolls Vampirism; ordinary Vampirism infection never enrolls JAWR.")
            .define("experimentalAutomaticDualAcquisition", false);
    public static final ModConfigSpec.BooleanValue ALLOW_UNVERIFIED_OTHER_PROVIDERS = BUILDER
            .comment("DANGEROUS TEST ONLY: allow vampire acquisition while other unimplemented transformation probes are UNKNOWN. Do not use in permanent saves.")
            .define("experimentalAllowUnverifiedOtherProviders", false);
    public static final ModConfigSpec.BooleanValue OPERATOR_CURE = BUILDER
            .comment("UNSAFE FOR VALUABLE SAVES: operator-only coordinated cure, with partial rollback and admin hold on failure.")
            .define("experimentalOperatorCure", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private VampireChassisConfig() { }

    public static boolean chassis() { return ENABLE_CHASSIS.get(); }
    public static boolean acquisition() { return chassis() && AUTOMATIC_DUAL_ACQUISITION.get(); }
    public static boolean cure() { return chassis() && OPERATOR_CURE.get(); }
    public static boolean allowUnverifiedProviders() { return chassis() && ALLOW_UNVERIFIED_OTHER_PROVIDERS.get(); }
}
