package io.github.heatwaveqc.tmm.economy;

import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfLayout;
import io.github.heatwaveqc.tmm.economy.runtime.enchanting.VirtualShelfLayout.Occupied;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class VirtualShelfLayoutTest {
    private static List<Occupied<String>> empty() {
        return new ArrayList<>(Collections.nCopies(VirtualShelfLayout.CAPACITY, null));
    }

    @Test void shelvesAlwaysOccupyDistinctPositions() {
        var slots = empty();
        for (int i = 0; i < 32; i++) {
            assertEquals(i, VirtualShelfLayout.destination(slots, "shelf", false).orElseThrow());
            slots.set(i, new Occupied<>("shelf", 1, false));
        }
        assertTrue(VirtualShelfLayout.destination(slots, "shelf", false).isEmpty());
        slots.set(12, null);
        assertEquals(12, VirtualShelfLayout.destination(slots, "shelf", false).orElseThrow());
    }

    @Test void candlesCompactBeforeUsingEmptyPosition() {
        var slots = empty();
        slots.set(2, new Occupied<>("white", 2, true));
        assertEquals(2, VirtualShelfLayout.destination(slots, "white", true).orElseThrow());
        assertEquals(0, VirtualShelfLayout.destination(slots, "red", true).orElseThrow());
        // Identical color but different data components must never be conflated.
        assertEquals(0, VirtualShelfLayout.destination(slots, "white/custom-name", true).orElseThrow());
        slots.set(2, new Occupied<>("white", 4, true));
        assertEquals(0, VirtualShelfLayout.destination(slots, "white", true).orElseThrow());
    }

    @Test void fullInventoryCanCompleteExistingClusterButNotOpenAnother() {
        var slots = empty();
        for (int i = 0; i < 32; i++) slots.set(i, new Occupied<>("shelf", 1, false));
        slots.set(10, new Occupied<>("white", 3, true));
        assertEquals(10, VirtualShelfLayout.destination(slots, "white", true).orElseThrow());
        assertTrue(VirtualShelfLayout.destination(slots, "red", true).isEmpty());
        slots.set(10, new Occupied<>("white", 4, true));
        assertTrue(VirtualShelfLayout.destination(slots, "white", true).isEmpty());
    }

    @Test void fullCandleLayoutHoldsExactly128RealItems() {
        var slots = empty();
        for (int n = 0; n < 128; n++) {
            int i = VirtualShelfLayout.destination(slots, "white", true).orElseThrow();
            int count = slots.get(i) == null ? 0 : slots.get(i).count();
            slots.set(i, new Occupied<>("white", count + 1, true));
        }
        assertTrue(VirtualShelfLayout.destination(slots, "white", true).isEmpty());
        assertEquals(128, slots.stream().mapToInt(Occupied::count).sum());
        slots.set(4, null);
        assertEquals(4, VirtualShelfLayout.destination(slots, "white", true).orElseThrow());
    }

    @Test void legacyRefundRejectsImpossibleOrCorruptNumericPower() {
        for (int n = 0; n <= 15; n++) assertEquals(n, VirtualShelfLayout.legacyBooks(n).orElseThrow());
        for (float invalid : new float[] {-1, 16, 1.5f, Float.NaN, Float.POSITIVE_INFINITY})
            assertTrue(VirtualShelfLayout.legacyBooks(invalid).isEmpty());
    }

    @Test void invalidCapacityAndCountsFailClosed() {
        assertThrows(IllegalArgumentException.class, () -> VirtualShelfLayout.destination(List.of(), "x", false));
        assertThrows(IllegalArgumentException.class, () -> new Occupied<>("x", 2, false));
        assertThrows(IllegalArgumentException.class, () -> new Occupied<>("x", 5, true));
    }
}
