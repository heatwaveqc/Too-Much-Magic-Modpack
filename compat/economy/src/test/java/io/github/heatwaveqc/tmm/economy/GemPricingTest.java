package io.github.heatwaveqc.tmm.economy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class GemPricingTest {
    @Test void commonRareAndMinPurity() {
        assertEquals(2, GemPricing.seedDustUnits(0, 100, 100, 2, 2));
        assertEquals(4, GemPricing.seedDustUnits(0, 50, 100, 2, 2));
        assertEquals(64, GemPricing.seedDustUnits(4, 50, 100, 2, 7));
        assertEquals(10, GemPricing.seedDustUnits(0, 100, 100, 2, 10));
    }
    @Test void upgradeConsumesTwoGemsAndBothSides() {
        assertEquals(300, GemPricing.upgrade(100, 20, 80));
        assertEquals(5, GemPricing.guaranteedDustUnits(2, 1, 3));
        assertThrows(IllegalArgumentException.class, () -> GemPricing.guaranteedDustUnits(2, 1, 6));
    }
    @Test void invalidAndOverflowFailClosed() {
        assertThrows(IllegalArgumentException.class, () -> GemPricing.seedDustUnits(0, 0, 100, 2, 2));
        assertThrows(ArithmeticException.class, () -> GemPricing.upgrade(Long.MAX_VALUE, 1, 1));
    }
}
