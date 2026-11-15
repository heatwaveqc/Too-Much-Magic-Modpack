package io.github.heatwaveqc.tmm.transformations.mob;

/**
 * A neutral faction does not acquire an unprovoked attack target.
 * It can still retaliate. This is not immunity to damage or a tame flag.
 */
public final class MobAlliancePolicy {
    private MobAlliancePolicy() {}

    public static boolean ignoresTarget(boolean undead, boolean vampireOrLich,
            boolean forestDragonAndFae, boolean caveDragonAndDemon,
            boolean provoked, boolean exempt) {
        if (provoked || exempt) return false;
        return (undead && vampireOrLich) || forestDragonAndFae || caveDragonAndDemon;
    }
}
