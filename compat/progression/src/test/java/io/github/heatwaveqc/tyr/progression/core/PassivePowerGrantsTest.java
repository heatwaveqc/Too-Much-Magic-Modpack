package io.github.heatwaveqc.tyr.progression.core;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class PassivePowerGrantsTest {
    private static final String FLY="tyr_progression:elytra_glide";
    private static final String TRACK="tyr_progression:tracking";

    @Test void oneGrantorCannotRevokeAnother() {
        var empty=PassivePowerGrants.empty();
        var both=empty.withSources(FLY,Set.of("tyr_progression:newbie","tyr_progression:monk"));
        assertTrue(both.active(FLY));
        var monkOnly=both.withSources(FLY,Set.of("tyr_progression:monk"));
        assertTrue(monkOnly.active(FLY));
        var none=monkOnly.withSources(FLY,Set.of());
        assertFalse(none.active(FLY));
        assertTrue(empty.sources().isEmpty());
        assertEquals(Set.of("tyr_progression:newbie","tyr_progression:monk"),
                     both.sources().get(FLY));
    }

    @Test void independentPassivePowersHaveNoSharedWalletOrState() {
        var state=PassivePowerGrants.empty()
                .withSources(FLY,Set.of("tyr_progression:newbie"))
                .withSources(TRACK,Set.of("tyr_progression:hunter"));
        assertEquals(Set.of(FLY,TRACK),state.activeIds());
        assertEquals(Set.of(TRACK),state.withSources(FLY,Set.of()).activeIds());
    }

    @Test void clientProjectionSortedBoundedWithoutSourceInformation() {
        var left=PassivePowerGrants.empty()
                .withSources(TRACK,Set.of("tyr_progression:hunter"))
                .withSources(FLY,Set.of("tyr_progression:newbie"));
        var right=PassivePowerGrants.empty()
                .withSources(FLY,Set.of("tyr_progression:newbie"))
                .withSources(TRACK,Set.of("tyr_progression:hunter"));
        assertEquals(FLY+"\n"+TRACK,left.encodeActive());
        assertEquals(left.encodeActive(),right.encodeActive());
        assertEquals(left.activeIds(),PassivePowerGrants.decodeActive(left.encodeActive()));
        assertFalse(left.encodeActive().contains("newbie"));
        assertFalse(left.encodeActive().contains("hunter"));
    }

    @Test void invalidOrDuplicateNetworkKeysCannotExpandEntitlements() {
        assertThrows(IllegalArgumentException.class, ()->
                PassivePowerGrants.decodeActive(FLY+"\n"+FLY));
        assertThrows(IllegalArgumentException.class, ()->
                PassivePowerGrants.decodeActive("elytra_glide"));
        assertThrows(IllegalArgumentException.class, ()->
                PassivePowerGrants.decodeActive(FLY+"\n"));
        assertThrows(IllegalArgumentException.class, ()->
                PassivePowerGrants.empty().withSources(FLY,Set.of("bad source")));
        assertEquals(Set.of(),PassivePowerGrants.decodeActive(""));
        assertThrows(IllegalArgumentException.class, ()->
                PassivePowerGrants.decodeActive("x:power".repeat(5000)));
        var mutable=new HashMap<String,Set<String>>();
        mutable.put(FLY,Set.of("tyr_progression:newbie"));
        var immutable=new PassivePowerGrants(mutable);
        mutable.clear();
        assertTrue(immutable.active(FLY));
    }

    @Test void expiryAndClockResetFailClosed() {
        assertFalse(PassivePowerLease.valid(-1,1));
        assertTrue(PassivePowerLease.valid(100,100));
        assertTrue(PassivePowerLease.valid(100,140));
        assertFalse(PassivePowerLease.valid(100,141));
        assertFalse(PassivePowerLease.valid(100,99));
    }
}
