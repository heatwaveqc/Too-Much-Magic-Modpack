package io.github.heatwaveqc.tmm.transformations.vampire;

import static io.github.heatwaveqc.tmm.transformations.core.Observation.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VampireOwnershipTest {
    @Test void singleProvidersOwnTheirMechanics() {
        for (var subsystem : VampireSubsystem.values()) {
            assertEquals(VampireOwner.VAMPIRISM, VampireOwnership.target(PRESENT, ABSENT, subsystem));
            assertEquals(VampireOwner.JAWR, VampireOwnership.target(ABSENT, PRESENT, subsystem));
        }
    }

    @Test void combinedStateHasExactlyOneSurvivalOwnerAndSeparateProgression() {
        assertEquals(VampireOwner.JAWR,
                VampireOwnership.target(PRESENT, PRESENT, VampireSubsystem.BLOOD_STORAGE));
        assertEquals(VampireOwner.JAWR,
                VampireOwnership.target(PRESENT, PRESENT, VampireSubsystem.VANILLA_FOOD_EXHAUSTION));
        assertEquals(VampireOwner.VAMPIRISM,
                VampireOwnership.target(PRESENT, PRESENT, VampireSubsystem.GENUINE_DEATH_INTERCEPTION));
        assertEquals(VampireOwner.JAWR,
                VampireOwnership.target(PRESENT, PRESENT, VampireSubsystem.PASSIVE_HEALING));
        assertEquals(VampireOwner.SEPARATE_NATIVE_PROGRESSIONS,
                VampireOwnership.target(PRESENT, PRESENT, VampireSubsystem.PROGRESSION));
        assertEquals(VampireOwner.BRIDGE_REQUIRED,
                VampireOwnership.target(PRESENT, PRESENT, VampireSubsystem.TEMPORARY_BAT_FORM));
    }

    @Test void unknownStateNeverAuthorizesAnOwner() {
        for (var subsystem : VampireSubsystem.values()) {
            assertEquals(VampireOwner.UNVERIFIED, VampireOwnership.target(UNKNOWN, PRESENT, subsystem));
            assertEquals(VampireOwner.UNVERIFIED, VampireOwnership.target(PRESENT, UNKNOWN, subsystem));
        }
    }
}
