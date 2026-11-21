package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.foundation.AstralDarknessPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class AstralDarknessPolicyTest {
    private static final int VANILLA = 0xFF303030;
    private static final int DARK = 0xFF080808;

    private static int brightness(int packed) { return packed & 0xFF; }

    @Test void tiersArePersonalAndOrdered() {
        assertEquals(-1, AstralDarknessPolicy.stage(true, "radiance"));
        assertEquals(0, AstralDarknessPolicy.stage(false, null));
        assertEquals(0, AstralDarknessPolicy.stage(false, "SHIMMER"));
        assertEquals(1, AstralDarknessPolicy.stage(false, "Illumination"));
        assertEquals(2, AstralDarknessPolicy.stage(false, "resonance"));
        assertEquals(3, AstralDarknessPolicy.stage(false, "luminance"));
        assertEquals(4, AstralDarknessPolicy.stage(false, "radiance"));
        assertEquals(0, AstralDarknessPolicy.stage(false, "unknown"));
    }

    @Test void unlitCavesStayDarkExceptNewbieVanillaBaseline() {
        for (int tier = 0; tier <= 4; tier++) {
            assertEquals(DARK, AstralDarknessPolicy.color(VANILLA, DARK, 0, 0.5f, 1f, tier));
        }
        assertEquals(VANILLA, AstralDarknessPolicy.color(VANILLA, DARK, 0, 0.5f, 0f, -1));
    }

    @Test void nightSightLadderIsMonotonicAndStopsShortOfFullbright() {
        int previous = brightness(DARK);
        for (int tier = 0; tier <= 4; tier++) {
            int pixel = AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.5f, 0.5f, tier);
            int current = brightness(pixel);
            assertTrue(current >= previous, "tier " + tier + " must not dim the player");
            assertTrue(current < 255, "never fullbright");
            previous = current;
        }
        assertTrue(brightness(AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.5f, 0.5f, 1))
            < brightness(VANILLA), "Illumination remains darker than vanilla");
        int resonance = brightness(AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.5f, 1f, 2));
        assertEquals(brightness(VANILLA), resonance, "Resonance at full moon is exactly vanilla");
        assertTrue(brightness(AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.5f, 0.5f, 3))
            > brightness(VANILLA), "Luminance enhances natural light");
    }

    @Test void moonPhaseAndDawnTransitionsArePreserved() {
        for (int tier = 1; tier <= 4; tier++) {
            int newMoon = AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.5f, 0f, tier);
            int fullMoon = AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.5f, 1f, tier);
            assertTrue(brightness(fullMoon) > brightness(newMoon), "Moon contrast at tier " + tier);
        }
        assertEquals(DARK, AstralDarknessPolicy.color(VANILLA, DARK, 15, 0.0f, 1f, 4));
        assertEquals(0f, AstralDarknessPolicy.nightWeight(0.25f));
        assertEquals(1f, AstralDarknessPolicy.nightWeight(0.50f));
        assertTrue(AstralDarknessPolicy.nightWeight(0.225f) > 0);
        assertTrue(AstralDarknessPolicy.nightWeight(0.225f) < 1);
    }

    @Test void actualVisionAndDistanceVeilRemainDistinct() {
        assertTrue(AstralDarknessPolicy.suppressDistanceVeil(-1));
        assertFalse(AstralDarknessPolicy.suppressDistanceVeil(0));
        assertFalse(AstralDarknessPolicy.suppressDistanceVeil(1));
        assertTrue(AstralDarknessPolicy.suppressDistanceVeil(2));
        assertTrue(AstralDarknessPolicy.suppressDistanceVeil(4));
        // A real potion's render path bypasses this policy in the client hook.
        // Neither the policy nor the server bridge grants a MobEffect.
    }
}
