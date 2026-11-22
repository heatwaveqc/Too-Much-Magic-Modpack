package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.runtime.blood.BloodCauldronTransferPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BloodCauldronTransferPolicyTest {
    @Test
    void handlesBothOwnerObservedOrdinaryBloodSources() {
        org.junit.jupiter.api.Assertions.assertTrue(
            BloodCauldronTransferPolicy.supportedSource("hexerei:blood_fluid"));
        org.junit.jupiter.api.Assertions.assertTrue(
            BloodCauldronTransferPolicy.supportedSource("evilcraft:blood"));
    }

    @Test
    void ignoresAllOtherNativeAndExceptionalFluids() {
        org.junit.jupiter.api.Assertions.assertFalse(
            BloodCauldronTransferPolicy.supportedSource("irons_spellbooks:blood"));
        org.junit.jupiter.api.Assertions.assertFalse(
            BloodCauldronTransferPolicy.supportedSource("neovitae:essentia_vitae_source"));
        org.junit.jupiter.api.Assertions.assertFalse(
            BloodCauldronTransferPolicy.supportedSource("minecraft:water"));
        org.junit.jupiter.api.Assertions.assertFalse(
            BloodCauldronTransferPolicy.supportedSource(null));
    }

    @Test
    void emptySourceDoesNothing() {
        assertEquals(0, BloodCauldronTransferPolicy.transferable(0, 8000));
    }

    @Test
    void fullDestinationDoesNothing() {
        assertEquals(0, BloodCauldronTransferPolicy.transferable(2000, 0));
    }

    @Test
    void ordinaryFullBucketPreservesAmount() {
        assertEquals(1000, BloodCauldronTransferPolicy.transferable(1000, 2000));
    }

    @Test
    void partialCauldronTransfersOnlyAvailableBlood() {
        assertEquals(250, BloodCauldronTransferPolicy.transferable(250, 8000));
    }

    @Test
    void partialExtractorTransfersOnlyAvailableSpace() {
        assertEquals(350, BloodCauldronTransferPolicy.transferable(1750, 350));
    }

    @Test
    void unrealisticNegativeHandlerReportsFailClosed() {
        assertEquals(0, BloodCauldronTransferPolicy.transferable(-1, 1000));
        assertEquals(0, BloodCauldronTransferPolicy.transferable(1000, -1));
    }
}
