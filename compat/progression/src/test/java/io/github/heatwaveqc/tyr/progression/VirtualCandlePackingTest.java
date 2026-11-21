package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.runtime.enchanting.VirtualCandlePacking;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

final class VirtualCandlePackingTest {
    @Test void exactOwnerExamplesPackIntoRealOneToFourCandleStates() {
        assertEquals(1, VirtualCandlePacking.positions(1));
        assertEquals(1, VirtualCandlePacking.positions(4));
        assertEquals(2, VirtualCandlePacking.positions(5));
        assertEquals(2, VirtualCandlePacking.positions(8));
        assertEquals(java.util.List.of(
                new VirtualCandlePacking.Cluster("minecraft:white_candle", 4),
                new VirtualCandlePacking.Cluster("minecraft:white_candle", 1)),
            VirtualCandlePacking.pack(Map.of("minecraft:white_candle", 5)));
    }

    @Test void colorsNeverShareACluster() {
        var packed = VirtualCandlePacking.pack(Map.of(
            "minecraft:red_candle", 3,
            "minecraft:white_candle", 2));
        assertEquals(2, packed.size());
        assertEquals("minecraft:red_candle", packed.get(0).candleType());
        assertEquals("minecraft:white_candle", packed.get(1).candleType());
    }

    @Test void emptyCountsUseNoPositionsAndCapacityFailsClosed() {
        assertTrue(VirtualCandlePacking.pack(Map.of("minecraft:candle", 0)).isEmpty());
        assertThrows(IllegalArgumentException.class,
            () -> VirtualCandlePacking.pack(Map.of("minecraft:candle", 129)));
        assertThrows(IllegalArgumentException.class,
            () -> VirtualCandlePacking.pack(Map.of("minecraft:candle", -1)));
    }
}
