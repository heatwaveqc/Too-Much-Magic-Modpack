package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.Locale;

/**
 * Owner-approved personal night-sight curve for More Darkness (issue #264).
 *
 * <p>No world-light, skylight, mob-spawning, gamma or potion changes. The rendered
 * RGB lightmap is blended only where sky light exists and only during nighttime.
 * All five Astral tiers originate on the logical server.
 */
public final class AstralDarknessPolicy {
    private AstralDarknessPolicy() {}

    public static final int NEWBIE = -1;
    public static final int REGULAR = 0;
    public static final int ILLUMINATION = 1;
    public static final int RESONANCE = 2;
    public static final int LUMINANCE = 3;
    public static final int RADIANCE = 4;

    public static int researchTier(String tier) {
        if (tier == null) return REGULAR;
        return switch (tier.toLowerCase(Locale.ROOT)) {
            case "illumination" -> ILLUMINATION;
            case "resonance" -> RESONANCE;
            case "luminance" -> LUMINANCE;
            case "radiance" -> RADIANCE;
            default -> REGULAR; // SHIMMER, absent provider and unknown names
        };
    }

    public static int stage(boolean newbie, String personalResearchTier) {
        return newbie ? NEWBIE : researchTier(personalResearchTier);
    }

    /** Skip the distance-crush shader once ordinary visibility is restored. */
    public static boolean suppressDistanceVeil(int stage) {
        return stage == NEWBIE || stage >= RESONANCE;
    }

    /**
     * Perceptual blend applied after More Darkness calculates a lightmap pixel.
     * Packed channels are Minecraft NativeImage's low-byte red, green, blue.
     *
     * @param vanilla original pixel before More Darkness's rescaling
     * @param darkened native More Darkness result
     * @param skyIndex lightmap skylight row (0 = no sky light)
     * @param phaseDayAngle 0..1 time-of-day angle, night centered on 0.5
     * @param moonBrightness native moon brightness 0..1
     */
    public static int color(int vanilla, int darkened, int skyIndex, float phaseDayAngle,
                            float moonBrightness, int stage) {
        if (stage == NEWBIE) return vanilla;
        if (stage <= REGULAR || skyIndex <= 0) return darkened;

        float night = nightWeight(phaseDayAngle);
        if (night <= 0) return darkened;
        float moon = clamp(moonBrightness, 0, 1);
        int target;
        if (stage == ILLUMINATION) {
            // Always noticeably darker than vanilla, but less severe than native darkness.
            target = blend(darkened, vanilla, 0.44f);
        } else if (stage == RESONANCE) {
            // Very close to vanilla at full moon; still slightly dimmer at new moon.
            target = blend(darkened, vanilla, 0.90f + moon * 0.10f);
        } else {
            // Supernatural sensitivity amplifies existing light, never creates light
            // from zero, and remains below fullbright. Higher moon = easier sight.
            float boost = (stage >= RADIANCE ? 0.32f : 0.15f) * (0.60f + moon * 0.40f);
            target = brighten(vanilla, boost);
        }
        return blend(darkened, target, night);
    }

    /** A narrow smooth dawn/dusk transition prevents lightmap jumps. */
    public static float nightWeight(float timeOfDay) {
        if (!Float.isFinite(timeOfDay)) return 0;
        float distance = Math.abs(timeOfDay - 0.5f);
        if (distance <= 0.20f) return 1;
        if (distance >= 0.25f) return 0;
        float t = (0.25f - distance) / 0.05f;
        return t * t * (3 - 2 * t);
    }

    private static float clamp(float v, float low, float high) {
        return Float.isFinite(v) ? Math.max(low, Math.min(high, v)) : low;
    }

    private static int blend(int from, int to, float amount) {
        float t = clamp(amount, 0, 1);
        int out = 0xFF000000;
        for (int shift : new int[]{0, 8, 16}) {
            int a = (from >>> shift) & 255;
            int b = (to >>> shift) & 255;
            int v = Math.round(a + (b - a) * t);
            out |= (v & 255) << shift;
        }
        return out;
    }

    private static int brighten(int vanilla, float amount) {
        int out = 0xFF000000;
        for (int shift : new int[]{0, 8, 16}) {
            float channel = ((vanilla >>> shift) & 255) / 255f;
            float lifted = channel + amount * (1f - channel) * (float)Math.sqrt(channel);
            out |= Math.round(clamp(lifted, 0, 1) * 255f) << shift;
        }
        return out;
    }
}
