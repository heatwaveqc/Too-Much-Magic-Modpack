package io.github.heatwaveqc.tmm.economy.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageNestingPolicyTest {
    @Test
    void riskyEmbeddedSourceOnlyRejectedByEmbeddedDestination() {
        assertTrue(StorageNestingPolicy.rejects(
            StorageNestingPolicy.Source.EMBEDDED_RISK,
            StorageNestingPolicy.Destination.ITEM_EMBEDDED, false));
        assertFalse(StorageNestingPolicy.rejects(
            StorageNestingPolicy.Source.EMBEDDED_RISK,
            StorageNestingPolicy.Destination.EXTERNAL_REFERENCE, false));
        assertFalse(StorageNestingPolicy.rejects(
            StorageNestingPolicy.Source.EMBEDDED_RISK,
            StorageNestingPolicy.Destination.ORDINARY, false));
    }

    @Test
    void referenceBackedHandlesAllowedInEmbeddedBags() {
        assertFalse(StorageNestingPolicy.rejects(
            StorageNestingPolicy.Source.EXTERNAL_REFERENCE,
            StorageNestingPolicy.Destination.ITEM_EMBEDDED, false));
    }

    @Test
    void ordinaryItemsAreNotRejected() {
        assertFalse(StorageNestingPolicy.rejects(
            StorageNestingPolicy.Source.ORDINARY,
            StorageNestingPolicy.Destination.ITEM_EMBEDDED, false));
    }

    @Test
    void nativeRestrictionsAlwaysWin() {
        for (StorageNestingPolicy.Source source : StorageNestingPolicy.Source.values()) {
            for (StorageNestingPolicy.Destination destination : StorageNestingPolicy.Destination.values()) {
                assertTrue(StorageNestingPolicy.rejects(source, destination, true));
            }
        }
    }
}
