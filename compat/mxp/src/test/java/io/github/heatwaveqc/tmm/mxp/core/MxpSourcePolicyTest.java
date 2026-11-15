package io.github.heatwaveqc.tmm.mxp.core;

import io.github.heatwaveqc.tmm.mxp.MxpService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class MxpSourcePolicyTest {
    @Test void everyRegisteredFirstBossIsExactlyOneThousand() {
        assertEquals(1000.0, MxpService.FIRST_BOSS_MXP);
        var account = new MxpAccount();
        account.restore(new MxpMath.Counter(125, 5), 0,
                java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
        var first = account.boss("minecraft:wither", "first-boss-uuid",
                MxpService.FIRST_BOSS_MXP, 500, false);
        assertEquals(1000.0, first.effectiveXP(), 1e-10);
        assertTrue(first.firstCompletion());
        assertEquals(126, first.lifetimeLevels());
        assertEquals(5, first.progress(), 1e-10);
        var repeat = account.boss("minecraft:wither", "second-boss-uuid",
                MxpService.FIRST_BOSS_MXP, 500, false);
        assertEquals(500.0 / Math.pow(1.2, 126), repeat.effectiveXP(), 1e-12);
    }

    @Test void onlyInventoryOrCuriosTabletAndManaRingTypesAreEligible() {
        assertTrue(PortableManaPolicy.eligibleStorage("botania:mana_tablet"));
        assertTrue(PortableManaPolicy.eligibleStorage("botania:mana_ring"));
        assertTrue(PortableManaPolicy.eligibleStorage("botania:mana_ring_greater"));
        assertFalse(PortableManaPolicy.eligibleStorage("botania:mana_pool"));
        assertFalse(PortableManaPolicy.eligibleStorage("botania:mana_mirror"));
        assertFalse(PortableManaPolicy.eligibleStorage("botania:mana_battery"));
        assertFalse(PortableManaPolicy.eligibleStorage("ars_nouveau:mana_gem"));
    }

    @Test void portableSourcesCannotClaimExternalArsFunding() {
        assertEquals(0.0, PortableManaPolicy.eligibleNominal(100, 0, 100));
        assertEquals(25.0, PortableManaPolicy.eligibleNominal(100, 25, 100));
        assertEquals(100.0, PortableManaPolicy.eligibleNominal(100, 125, 100));
        assertEquals(0.0, PortableManaPolicy.eligibleNominal(100, 100, 0));
        assertEquals(0.0, PortableManaPolicy.eligibleNominal(100, 100, Double.NaN));
        assertEquals(500000.0, PortableManaPolicy.FIRST_PORTABLE_POOL);
    }
}
