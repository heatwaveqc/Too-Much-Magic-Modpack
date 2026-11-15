package io.github.heatwaveqc.tmm.monk.rules;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

/** No item deletion, partial transfer, or reserved index insertion in future GUI/pickup adapters. */
final class MonkFistSlotRulesTest {
    @Test void monkReservesNinthHotbarPositionWithoutChangingNonMonkInventories() {
        assertEquals(8, MonkFistSlotRules.DEFAULT_FIST_INDEX);
        assertEquals(9, MonkFistSlotRules.HOTBAR_SIZE);
        assertEquals(36, MonkFistSlotRules.PLAYER_STORAGE_SIZE);
        for (int inventoryIndex = 0; inventoryIndex < 36; inventoryIndex++) {
            assertTrue(MonkFistSlotRules.mayInsert(false, 8, inventoryIndex),
                    "Non-Monks retain their complete vanilla hotbar and inventory");
            assertEquals(inventoryIndex != 8,
                    MonkFistSlotRules.mayInsert(true, 8, inventoryIndex),
                    "Monk cannot put items in the reserved fist slot");
        }
        assertTrue(MonkFistSlotRules.mayInsert(true, 0, 8),
                "Server-chosen alternate slot should not also reserve the old position");
        assertFalse(MonkFistSlotRules.mayInsert(true, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> MonkFistSlotRules.mayInsert(true, 9, 8));
        assertThrows(IllegalArgumentException.class, () -> MonkFistSlotRules.mayInsert(false, -1, 8));
    }

    @Test void initialReservationRelocatesWholeStackOnlyIntoRealPermittedSpace() {
        int[] capacity = new int[36];
        capacity[8] = 64; // An empty reserved slot is NEVER available storage.
        capacity[4] = 5;  // Compatible stack merge.
        capacity[23] = 64; // Ordinary empty slot.
        var relocation = MonkFistSlotRules.planLosslessRelocation(30, 8, capacity);
        assertTrue(relocation.isPresent());
        int[] allocation = relocation.orElseThrow();
        assertEquals(0, allocation[8]);
        assertEquals(5, allocation[4]);
        assertEquals(25, allocation[23]);
        assertEquals(30, Arrays.stream(allocation).sum());
    }

    @Test void fullInventoryDeclinesReservationWithoutLossOrPartialMove() {
        int[] capacity = new int[36];
        capacity[8] = 64;
        assertTrue(MonkFistSlotRules.planLosslessRelocation(1, 8, capacity).isEmpty());
        capacity[2] = 4;
        assertTrue(MonkFistSlotRules.planLosslessRelocation(5, 8, capacity).isEmpty(),
                "A four-item partial move must not be applied to a five-item stack");
        assertTrue(MonkFistSlotRules.planLosslessRelocation(4, 8, capacity).isPresent());
    }

    @Test void invalidCapacityCannotDestroyItems() {
        int[] capacity = new int[36];
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(0, 8, capacity));
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(10, 9, capacity));
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(10, 8, new int[35]));
        capacity[0] = -2;
        assertThrows(IllegalArgumentException.class, () ->
                MonkFistSlotRules.planLosslessRelocation(10, 8, capacity));
    }

    @Test void otherHotbarIndexCanBeReservedWithoutSpecialCasingFistToSlotEight() {
        int[] capacities = new int[36];
        capacities[8] = 64;
        capacities[1] = 64;
        var allocation = MonkFistSlotRules.planLosslessRelocation(24, 1, capacities).orElseThrow();
        assertEquals(0, allocation[1]);
        assertEquals(24, allocation[8]);
    }
}
