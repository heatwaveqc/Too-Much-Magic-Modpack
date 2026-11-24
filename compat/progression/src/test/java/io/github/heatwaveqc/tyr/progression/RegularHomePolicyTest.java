package io.github.heatwaveqc.tyr.progression;

import io.github.heatwaveqc.tyr.progression.foundation.RegularHomePolicy;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegularHomePolicyTest {
    @Test
    void personalGraduationAndNativeLegacyMigrationNeverDuplicate() {
        var none=RegularHomePolicy.NativeGrant.NOT_GIVEN;
        var given=RegularHomePolicy.NativeGrant.GIVEN;
        var unknown=RegularHomePolicy.NativeGrant.UNKNOWN;
        assertEquals("locked",RegularHomePolicy.reconcile(null,false,none));
        assertFalse(RegularHomePolicy.canUse(true,null));
        assertEquals("legacy_review",RegularHomePolicy.reconcile(null,true,none));
        assertEquals("spent_legacy_prefab",RegularHomePolicy.reconcile(null,false,given));
        assertEquals("spent_legacy_prefab",RegularHomePolicy.graduate("spent_legacy_prefab"));
        assertEquals("pending_graduation",RegularHomePolicy.graduate("locked"));
        assertEquals("pending_graduation",RegularHomePolicy.reconcile("pending_graduation",true,unknown));
        assertEquals("available",RegularHomePolicy.reconcile("pending_graduation",true,none));
        assertEquals("pending_graduation",RegularHomePolicy.reconcile("pending_graduation",false,none));
        assertEquals("spent_legacy_prefab",RegularHomePolicy.reconcile("available",true,given));
        assertEquals("spent_generic",RegularHomePolicy.reconcile("spent:generic",true,none));
        assertEquals("reserved_witch_cottage",RegularHomePolicy.reconcile("reserved:witch_cottage",true,none));
        assertEquals("spent_generic",RegularHomePolicy.graduate("spent_generic"));
        assertTrue(RegularHomePolicy.reserve(true,"available","generic").matches("[a-z0-9_./-]+"));
        assertTrue(RegularHomePolicy.finish("reserved_generic","generic",true).matches("[a-z0-9_./-]+"));
    }

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
        assertEquals("reserved_witch_cottage",
            RegularHomePolicy.reserve(true, "available", "witch_cottage"));
        assertNull(RegularHomePolicy.reserve(true, "reserved_witch_cottage", "generic"));
        assertNull(RegularHomePolicy.reserve(true, "spent_witch_cottage", "witch_cottage"));
        assertNull(RegularHomePolicy.reserve(true, "available", "evil_uploaded_nbt"));
    }

    @Test
    void successfulConstructionIsPermanentButCleanFailureAllowsRetry() {
        String reservation = RegularHomePolicy.reserve(true, "available", "hearthkeeper");
        assertEquals("available", RegularHomePolicy.finish(reservation, "hearthkeeper", false));
        String spent = RegularHomePolicy.finish(reservation, "hearthkeeper", true);
        assertEquals("spent_hearthkeeper", spent);
        assertTrue(RegularHomePolicy.terminal(spent));
        assertNull(RegularHomePolicy.reserve(true, spent, "generic"));
        assertNull(RegularHomePolicy.reserve(true, spent, "wizards_tower"));
    }

    @Test
    void wrongBuildReceiptCannotConsumeOrReleaseAnotherHouse() {
        assertEquals("reserved_hearthkeeper",
            RegularHomePolicy.finish("reserved_hearthkeeper", "wizards_tower", true));
        assertEquals("spent_generic",
            RegularHomePolicy.finish("spent_generic", "generic", false));
        assertFalse(RegularHomePolicy.terminal("reserved_generic"));
        assertFalse(RegularHomePolicy.canUse(true, "reserved_generic"));
        assertFalse(RegularHomePolicy.canUse(true, "spent_generic"));
    }
}
