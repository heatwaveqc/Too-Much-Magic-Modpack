package io.github.heatwaveqc.tmm.economy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CharmPotionEligibilityTest {
    private boolean permits(int count, boolean instant, boolean nativeBaseAllowed,
            boolean customOnly, boolean ordinary, boolean denied, boolean carrier, boolean components) {
        return CharmPotionEligibility.permits(count, instant, nativeBaseAllowed, customOnly,
                ordinary, denied, carrier, components);
    }

    @Test void customOnlySingleOrdinaryEffectAllowed() {
        assertTrue(permits(1, false, true, true, true, false, true, true));
    }

    @Test void registeredNativeSingleEffectAllowed() {
        assertTrue(permits(1, false, true, false, false, false, true, true));
    }

    @Test void customOnlyUnvettedModdedEffectIsDenied() {
        assertFalse(permits(1, false, true, true, false, false, true, true));
    }

    @Test void emptyOrMultiEffectDenied() {
        assertFalse(permits(0, false, true, true, true, false, true, true));
        assertFalse(permits(2, false, true, true, true, false, true, true));
    }

    @Test void instantEffectDenied() {
        assertFalse(permits(1, true, true, true, true, false, true, true));
    }

    @Test void blacklistedBaseAndSpecialEffectsDenied() {
        assertFalse(permits(1, false, false, false, true, false, true, true));
        assertFalse(permits(1, false, true, true, true, true, true, true));
    }

    @Test void foreignPayloadAndMultiDoseCarrierDenied() {
        assertFalse(permits(1, false, true, true, true, false, false, true));
        assertFalse(permits(1, false, true, true, true, false, true, false));
    }
}
