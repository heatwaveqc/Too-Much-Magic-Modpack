package io.github.heatwaveqc.tmm.mxp.core;

import java.util.Set;

/** Pure policy shared by Botania's inventory/Curios payment hook and tests. */
public final class PortableManaPolicy {
    public static final double FIRST_PORTABLE_POOL = 500_000;
    private static final Set<String> ELIGIBLE = Set.of(
            "botania:mana_tablet",
            "botania:mana_ring",
            "botania:mana_ring_greater");

    private PortableManaPolicy() {}

    public static boolean eligibleStorage(String registryId) {
        return ELIGIBLE.contains(registryId);
    }

    /**
     * Only the *portable* portion of a mixed payment is rewarded; external
     * PlayerManaSources (Ars conversion, world pools) do not earn Botania MXP.
     */
    public static double eligibleNominal(double nominalCost,
                                         double portableDebit,
                                         double totalGranted) {
        if (!Double.isFinite(nominalCost) || nominalCost <= 0
                || !Double.isFinite(portableDebit) || portableDebit <= 0
                || !Double.isFinite(totalGranted) || totalGranted <= 0) return 0;
        return nominalCost * Math.min(1.0, portableDebit / totalGranted);
    }
}
