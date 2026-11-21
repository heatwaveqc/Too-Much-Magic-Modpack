/*
 * Adapted from Shadows-of-Fire/Apotheosis, revision 84e27e7031c7e2ab2f2e8328daf491c88203fcbc.
 * src/main/java/dev/shadowsoffire/apotheosis/attachments/AttributeToggles.java
 * Original license: MIT; see third_party_licenses/Apotheosis.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tyr.progression.foundation;

/** Compute a suppression cap without changing unrelated attribute modifiers.
 * No global attribute hook; caller must first establish explicit authorization.
 */
public final class AttributeCap {
    private AttributeCap() {}
    public static double suppressBonuses(double actualValue, double retainedCap) {
        if (!Double.isFinite(actualValue) || !Double.isFinite(retainedCap))
            throw new IllegalArgumentException("Nonfinite attribute value");
        return Math.min(actualValue, retainedCap);
    }
}
