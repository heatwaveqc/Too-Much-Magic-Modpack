package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.UUID;

class BloodCuisineRedemptionsTest {
    @Test void oneGlobalNativeBloodCreditPerCraftReceiptAcrossCopies() {
        var receipts = new HashSet<UUID>();
        UUID crafted = UUID.randomUUID();
        assertTrue(BloodCuisinePolicy.redeem(receipts, crafted));
        assertFalse(BloodCuisinePolicy.redeem(receipts, crafted));
        assertTrue(receipts.contains(crafted));
        assertFalse(BloodCuisinePolicy.redeem(receipts, null));
        assertFalse(BloodCuisinePolicy.redeem(null, crafted));
    }

    @Test void distinctCraftsHaveIndependentSpendRights() {
        var receipts = new HashSet<UUID>();
        UUID one = UUID.randomUUID(), two = UUID.randomUUID();
        assertTrue(BloodCuisinePolicy.redeem(receipts, one));
        assertTrue(BloodCuisinePolicy.redeem(receipts, two));
        assertFalse(BloodCuisinePolicy.redeem(receipts, one));
        assertFalse(BloodCuisinePolicy.redeem(receipts, two));
    }
}
