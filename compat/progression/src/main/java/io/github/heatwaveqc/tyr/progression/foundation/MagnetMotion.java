/*
 * Adapted from CyclopsMC/EverlastingAbilities, revision 53bc1f1cd9ca835e586bd0fb88d9a88afb85544e.
 * loader-common/src/main/java/org/cyclops/everlastingabilities/ability/AbilityTypeSpecialMagnetize.java
 * Original license: MIT; see third_party_licenses/EverlastingAbilities.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tyr.progression.foundation;

/** Pure attraction calculation only; callers own claim, pickup delay, entity budget, cadence and collision policy. */
public final class MagnetMotion {
    private MagnetMotion() {}
    public record Velocity(double x, double y, double z) {}
    public static Velocity toward(double dx, double dy, double dz, boolean horizontalCollision) {
        if (!Double.isFinite(dx) || !Double.isFinite(dy) || !Double.isFinite(dz))
            throw new IllegalArgumentException("Nonfinite displacement");
        double d = Math.hypot(Math.hypot(dx, dy), dz);
        if (d <= 0.5D) return new Velocity(0, 0, 0);
        double m = 1 / (2 * Math.max(1, d));
        return new Velocity(-dx * m, horizontalCollision ? 0.3 : -dy * m, -dz * m);
    }
}
