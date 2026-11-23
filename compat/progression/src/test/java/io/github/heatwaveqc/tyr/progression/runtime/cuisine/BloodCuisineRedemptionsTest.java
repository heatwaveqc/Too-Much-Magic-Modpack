package io.github.heatwaveqc.tyr.progression.runtime.cuisine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;

class BloodCuisineRedemptionsTest {
    @Test void oneNativeBloodCreditPerCraftReceiptAcrossCopies() {
        BloodCuisineRedemptions ledger = new BloodCuisineRedemptions();
        UUID crafted = UUID.randomUUID();
        assertFalse(ledger.spent(crafted));
        assertTrue(ledger.redeem(crafted));
        assertTrue(ledger.spent(crafted));
        assertFalse(ledger.redeem(crafted));
        assertTrue(ledger.spent(null));
        assertFalse(ledger.redeem(null));
    }

    @Test void independentCraftsKeepIndependentCredits() {
        BloodCuisineRedemptions ledger = new BloodCuisineRedemptions();
        UUID one = UUID.randomUUID(), two = UUID.randomUUID();
        assertTrue(ledger.redeem(one));
        assertTrue(ledger.redeem(two));
        assertFalse(ledger.redeem(one));
        assertFalse(ledger.redeem(two));
    }
}
