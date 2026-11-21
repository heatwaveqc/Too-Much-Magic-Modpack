package io.github.heatwaveqc.tmm.progression;

import io.github.heatwaveqc.tmm.progression.foundation.RegularHomePolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegularHomePolicyTest {
    @Test
    void exactlyNineteenThemeChoicesWithNativeGenericSeparate() {
        assertEquals(18, RegularHomePolicy.CUSTOM.size());
        assertEquals(18, java.util.Set.copyOf(RegularHomePolicy.CUSTOM).size());
        assertTrue(RegularHomePolicy.choice("generic"));
        for (String id : RegularHomePolicy.CUSTOM) assertTrue(RegularHomePolicy.choice(id));
        assertFalse(RegularHomePolicy.choice("generic_basic"));
        assertFalse(RegularHomePolicy.choice("house_basic"));
        assertFalse(RegularHomePolicy.choice("adventurers_inn/../../"));
        assertFalse(RegularHomePolicy.choice("not_in_catalog"));
    }

    @Test
    void newbieCannotReserveAndRegularMayReserveExactlyOnce() {
        assertFalse(RegularHomePolicy.canUse(false, "available"));
        assertNull(RegularHomePolicy.reserve(false, "available", "generic"));
        assertEquals("reserved:witch_cottage",
            RegularHomePolicy.reserve(true, "available", "witch_cottage"));
        assertNull(RegularHomePolicy.reserve(true, "reserved:witch_cottage", "generic"));
        assertNull(RegularHomePolicy.reserve(true, "spent:witch_cottage", "witch_cottage"));
        assertNull(RegularHomePolicy.reserve(true, "available", "evil_uploaded_nbt"));
    }

    @Test
    void successfulConstructionIsPermanentButCleanFailureAllowsRetry() {
        String reservation = RegularHomePolicy.reserve(true, "available", "hearthkeeper");
        assertEquals("available", RegularHomePolicy.finish(reservation, "hearthkeeper", false));
        String spent = RegularHomePolicy.finish(reservation, "hearthkeeper", true);
        assertEquals("spent:hearthkeeper", spent);
        assertTrue(RegularHomePolicy.terminal(spent));
        assertNull(RegularHomePolicy.reserve(true, spent, "generic"));
        assertNull(RegularHomePolicy.reserve(true, spent, "wizards_tower"));
    }

    @Test
    void wrongBuildReceiptCannotConsumeOrReleaseAnotherHouse() {
        assertEquals("reserved:hearthkeeper",
            RegularHomePolicy.finish("reserved:hearthkeeper", "wizards_tower", true));
        assertEquals("spent:generic",
            RegularHomePolicy.finish("spent:generic", "generic", false));
        assertFalse(RegularHomePolicy.terminal("reserved:generic"));
        assertFalse(RegularHomePolicy.canUse(true, "reserved:generic"));
        assertFalse(RegularHomePolicy.canUse(true, "spent:generic"));
    }
}
