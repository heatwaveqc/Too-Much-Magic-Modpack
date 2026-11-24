package io.github.heatwaveqc.tmm.transformations.vampire;

/**
 * Strict ordinary/special blood boundary, independent of the Minecraft runtime.
 * JAWR 0.5.39 uses chicken and vampire data components on the same bottle ID.
 * Treat even a present-but-false special marker as protected (fail closed).
 */
public final class BloodBottlePolicy {
    private BloodBottlePolicy() { }

    public static boolean isOrdinary(boolean hasChickenMarker, boolean hasVampireMarker) {
        return !hasChickenMarker && !hasVampireMarker;
    }
}
