package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;

class VengeanceRulesTest {
    @Test void naturalTraderSearchHasBoundedRetryBudget() {
        // Each native findSpawnPositionNear already tries up to ten positions;
        // cap additional calls to avoid looping around heavily signed villages.
        assertEquals(3, VengeanceRules.TRADER_POSITION_GROUPS);
        assertTrue(VengeanceRules.TRADER_POSITION_GROUPS > 0);
        assertTrue(VengeanceRules.TRADER_POSITION_GROUPS <= 3);
    }

    @Test void continuingPriestAggroDoesNotUndoAnEarnedPardon() {
        UUID offender = UUID.randomUUID();
        UUID other = UUID.randomUUID();
        assertTrue(VengeanceRules.isNewPriestTarget(null, offender));
        assertFalse(VengeanceRules.isNewPriestTarget(offender, offender));
        assertTrue(VengeanceRules.isNewPriestTarget(offender, other));
        assertFalse(VengeanceRules.isNewPriestTarget(offender, null));
    }

    @Test void onlyActualHealthLossCausesAssaultBlacklisting() {
        assertTrue(VengeanceRules.isActualHealthDamage(0.5F));
        assertTrue(VengeanceRules.isActualHealthDamage(20.0F));
        assertFalse(VengeanceRules.isActualHealthDamage(0.0F));
        assertFalse(VengeanceRules.isActualHealthDamage(-1.0F));
    }
    @Test void lavaGolemProximityUsesInclusiveEuclideanTenBlockRadius() {
        assertTrue(VengeanceRules.withinGolemVendettaRadius(0.0, 0.0, 0.0));
        assertTrue(VengeanceRules.withinGolemVendettaRadius(10.0, 0.0, 0.0));
        assertTrue(VengeanceRules.withinGolemVendettaRadius(6.0, 8.0, 0.0));
        assertFalse(VengeanceRules.withinGolemVendettaRadius(10.0001, 0.0, 0.0));
        assertFalse(VengeanceRules.withinGolemVendettaRadius(8.0, 8.0, 0.0));
    }
    @Test void heightLimitsAreInclusive() {
        assertTrue(VengeanceRules.nearSeaLevel(53, 63));
        assertTrue(VengeanceRules.nearSeaLevel(73, 63));
        assertFalse(VengeanceRules.nearSeaLevel(52, 63));
        assertFalse(VengeanceRules.nearSeaLevel(74, 63));
    }
    @Test void signTextIsCaseInsensitiveAndSupportsBrackets() {
        assertTrue(VengeanceRules.markedSign("No Soliciting"));
        assertTrue(VengeanceRules.markedSign(" [NO SOLICITING] "));
        assertFalse(VengeanceRules.markedSign("No Solicitation"));
        assertFalse(VengeanceRules.markedSign(null));
    }
}
