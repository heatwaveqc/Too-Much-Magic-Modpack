/*
 * Adapted from Direwolf20-MC/JustDireThings, revision 506416a9c7675cacc246a47d2b0c9d6b27eff9a8.
 * src/main/java/com/direwolf20/justdirethings/mixin/LightTextureMixin.java
 * Original license: MIT; see third_party_licenses/JustDireThings.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
/*
 * Adapted from Creators-of-Create/Create, revision 37ea4afcfe5bf85875d4b0749798a45330070a1f.
 * src/main/java/com/simibubi/create/foundation/events/ClientEvents.java
 * Original license: MIT; see third_party_licenses/Create.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tmm.progression.foundation;

/** Extracted vision policy. Call sites must supply authenticated, synced eligibility. */
public final class VisionMath {
    private VisionMath() {}
    public static boolean hasNightVision(boolean vanillaHasEffect, boolean enhanced) {
        return enhanced || vanillaHasEffect;
    }
    public static float nightVisionScale(float vanillaScale, boolean enhanced) {
        return enhanced ? 1f : vanillaScale;
    }
    public record Fog(float near, float far) {}
    public static Fog lavaFog(float far) {
        if (!Float.isFinite(far) || far <= 0 || far > 128)
            throw new IllegalArgumentException("Invalid lava vision range");
        return new Fog(-4.0f, far);
    }
}
