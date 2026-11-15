package io.github.heatwaveqc.tmm.transformations.botania;

/**
 * A Thermalily should never consume a fresh lava source unless it can
 * actually start a new burn. Full buffers and a running burn already block
 * collection in Botania Revived. Its shared fluid-generator base additionally
 * collects lava while on cooldown, wasting it.
 *
 * Keep the decision in a pure helper for unit testing.
 */
public final class ThermalilyLavaPolicy {
    private ThermalilyLavaPolicy() {}

    public static boolean mayCollectNewLava(int currentMana, int maxMana,
                                             int burnTime, int cooldown) {
        return currentMana < maxMana && burnTime <= 0 && cooldown <= 0;
    }
}
