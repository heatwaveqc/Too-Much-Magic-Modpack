package io.github.heatwaveqc.tmm.tradersvengence.runtime;

/** No game dependencies: executable projectile eligibility geometry. */
final class GolemRocketGeometry {
    private static final double MIN_DISTANCE_SQUARED = 100.0D;
    private GolemRocketGeometry() {}

    static boolean farEnough(double distanceSquared) {
        return distanceSquared >= MIN_DISTANCE_SQUARED;
    }

    static boolean insideRectangle(int minX, int maxX, int minZ, int maxZ,
            int x, int z) {
        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
}
