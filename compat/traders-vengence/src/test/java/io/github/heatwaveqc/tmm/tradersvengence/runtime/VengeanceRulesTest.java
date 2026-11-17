package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.UUID;

class VengeanceRulesTest {
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
