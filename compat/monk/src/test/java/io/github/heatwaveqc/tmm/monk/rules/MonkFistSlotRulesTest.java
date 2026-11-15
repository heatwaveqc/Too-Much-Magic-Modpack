package io.github.heatwaveqc.tmm.monk.rules;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/** No item deletion, partial transfer, or reserved index insertion in future GUI/pickup adapters. */
final class MonkFistSlotRulesTest {
    @Test void monkReservesFirstHotbarPositionWithoutChangingNonMonkInventories() {
        assertEquals(0, MonkFistSlotRules.DEFAULT_FIST_INDEX);
        assertEquals(9, MonkFistSlotRules.HOTBAR_SIZE);
        assertEquals(36, MonkFistSlotRules.PLAYER_STORAGE_SIZE);
        for (int inventoryIndex = 0; inventoryIndex < 36; inventoryIndex++) {
            assertTrue(MonkFistSlotRules.mayInsert(false, 0, inventoryIndex),
                    "Non-Monks retain all nine vanilla slots");
            assertEquals(inventoryIndex != 0,
                    MonkFistSlotRules.mayInsert(true, 0, inventoryIndex),
                    "Monk cannot put items in numeral-1 fist slot");
        }
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.mayInsert(true, 8, 0),
                "The fist cannot migrate to a different hotbar position");
        assertFalse(MonkFistSlotRules.mayInsert(true, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> MonkFistSlotRules.mayInsert(true, 9, 0));
        assertThrows(IllegalArgumentException.class, () -> MonkFistSlotRules.mayInsert(false, -1, 0));
    }

    @Test void firstSlotReservationRelocatesWholeStackOnlyIntoRealPermittedSpace() {
        int[] capacity = new int[36];
        capacity[0] = 64; // The reserved fist slot is NEVER available storage.
        capacity[4] = 5;  // Compatible stack merge.
        capacity[23] = 64; // Ordinary empty slot.
        var relocation = MonkFistSlotRules.planLosslessRelocation(30, 0, capacity);
        assertTrue(relocation.isPresent());
        int[] allocation = relocation.orElseThrow();
        assertEquals(0, allocation[0]);
        assertEquals(5, allocation[4]);
        assertEquals(25, allocation[23]);
        assertEquals(30, Arrays.stream(allocation).sum());
    }

    @Test void fullInventoryDeclinesReservationWithoutLossOrPartialMove() {
        int[] capacity = new int[36];
        capacity[0] = 64;
        assertTrue(MonkFistSlotRules.planLosslessRelocation(1, 0, capacity).isEmpty());
        capacity[2] = 4;
        assertTrue(MonkFistSlotRules.planLosslessRelocation(5, 0, capacity).isEmpty(),
                "A four-item partial move must not be applied to a five-item stack");
        assertTrue(MonkFistSlotRules.planLosslessRelocation(4, 0, capacity).isPresent());
    }

    @Test void invalidCapacityCannotDestroyItems() {
        int[] capacity = new int[36];
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(0, 0, capacity));
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(10, 9, capacity));
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(10, 0, new int[35]));
        capacity[0] = -2;
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(10, 0, capacity));
    }

    @Test void fistReservationCannotBeReconfiguredAwayFromTheFirstHotbarSlot() {
        int[] capacities = new int[36];
        capacities[0] = 64;
        capacities[1] = 64;
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(24, 1, capacities));
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.mayInsert(true, 1, 1));
        assertTrue(MonkFistSlotRules.planLosslessRelocation(24, 0, capacities).isPresent());
    }
}
