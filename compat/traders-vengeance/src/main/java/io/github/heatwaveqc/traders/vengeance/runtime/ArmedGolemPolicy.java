package io.github.heatwaveqc.traders.vengeance.runtime;

/** Fail closed: only golems observed emerging from vanilla villager summoning. */
public final class ArmedGolemPolicy {
    private ArmedGolemPolicy() {}
    public static boolean shouldArm(boolean villagerSpawn, boolean playerCreated) {
        return villagerSpawn && !playerCreated;
    }
}
