package io.github.heatwaveqc.tmm.economy;

/** Mod-independent decision core so negative cases remain ordinary unit tests. */
final class CharmRepairEligibility {
    private CharmRepairEligibility() {
    }

    static boolean permits(boolean potionCharm, boolean damageable, int damage,
            boolean exactlyOneEffect, boolean instantEffect, boolean modeChanger) {
        return potionCharm && damageable && damage > 0 && exactlyOneEffect
                && !instantEffect && !modeChanger;
    }
}
