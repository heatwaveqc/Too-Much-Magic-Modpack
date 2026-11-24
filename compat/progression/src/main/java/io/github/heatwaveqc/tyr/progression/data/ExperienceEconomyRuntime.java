package io.github.heatwaveqc.tyr.progression.data;

import java.util.Objects;

/**
 * Coherent snapshot of the *validated* server economy configuration.
 * Levels, wallet views, award divisors and purchase eligibility read the same
 * installed catalog; reload swaps it only after PathRegistry accepts all data.
 */
public final class ExperienceEconomyRuntime {
    private ExperienceEconomyRuntime() {}

    private static volatile ExperienceEconomyConfig.Catalog active =
            ExperienceEconomyConfig.tmmDefault();

    public static ExperienceEconomyConfig.Catalog current() {
        return active;
    }

    public static void install(ExperienceEconomyConfig.Catalog next) {
        active=Objects.requireNonNull(next,"validated XP catalog");
    }
}
