package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VengeanceRulesTest {
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
