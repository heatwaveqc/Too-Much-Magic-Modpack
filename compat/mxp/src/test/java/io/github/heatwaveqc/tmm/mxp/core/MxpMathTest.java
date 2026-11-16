package io.github.heatwaveqc.tmm.mxp.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class MxpMathTest {
    @Test void firstPoolEqualsFirstPoint() {
        assertEquals(1000, MxpMath.normalizedBase(100, 100), 1e-12);
        assertEquals(10, MxpMath.normalizedBase(1, 100), 1e-12);
        assertEquals(1, MxpMath.normalizedBase(1, 1000), 1e-12);
    }

    @Test void ordinaryAwardRecalculatesWhenCrossingLevel() {
        var a = MxpMath.apply(new MxpMath.Counter(0, 990), 20, false);
        assertEquals(1, a.state().lifetimeLevels());
        assertEquals(10.0 / MxpMath.DEFAULT_GROWTH, a.state().progress(), 1e-9);
        assertEquals(10 + 10.0 / MxpMath.DEFAULT_GROWTH, a.effectiveXP(), 1e-9);
    }

    @Test void fixedAwardsNeverUseDivisor() {
        var first = MxpMath.apply(new MxpMath.Counter(80, 900), 2200, true);
        assertEquals(83, first.state().lifetimeLevels());
        assertEquals(100, first.state().progress(), 1e-9);
        assertEquals(2200, first.effectiveXP(), 1e-9);
    }

    @Test void ordinaryRewardsAtHighLevelAreSmallButFinite() {
        var a = MxpMath.apply(new MxpMath.Counter(30, 0), 200, false);
        assertEquals(200 / Math.pow(MxpMath.DEFAULT_GROWTH, 30), a.effectiveXP(), 1e-10);
        assertEquals(0, a.state().lifetimeLevels() - 30);
    }

    @Test void anHugeOrdinaryAwardCannotSkipRepricing() {
        var a = MxpMath.apply(new MxpMath.Counter(0, 0), 1000 + 1020 + 1040.4 + 180, false);
        assertEquals(3, a.state().lifetimeLevels());
        assertEquals(180 / Math.pow(MxpMath.DEFAULT_GROWTH, 3), a.state().progress(), 1e-9);
    }

    @Test void rejectsBadInputs() {
        assertThrows(IllegalArgumentException.class, () -> MxpMath.normalizedBase(1, 0));
        assertThrows(IllegalArgumentException.class, () -> MxpMath.apply(
                new MxpMath.Counter(0, 0), Double.POSITIVE_INFINITY, true));
        assertThrows(IllegalArgumentException.class, () -> new MxpMath.Counter(-1, 0));
    }

    @Test void firstRitualAndBossAreFixedAndRepeatsDivided() {
        MxpAccount acc = new MxpAccount();
        var r1 = acc.ritual("totemic:rain", "ritual-instance-1", 1000, 200);
        assertTrue(r1.firstCompletion());
        assertEquals(1, acc.counter().lifetimeLevels());
        assertEquals(1000, r1.effectiveXP(), 1e-6);
        assertTrue(acc.ritual("totemic:rain", "ritual-instance-1", 1000, 200).duplicate());
        var repeat = acc.ritual("totemic:rain", "ritual-instance-2", 1000, 200);
        assertFalse(repeat.firstCompletion());
        assertEquals(200 / MxpMath.DEFAULT_GROWTH, repeat.effectiveXP(), 1e-9);
        var b1 = acc.boss("minecraft:wither", "boss-uuid-1", 1000, 100, false);
        assertTrue(b1.firstCompletion());
        assertEquals(1000, b1.effectiveXP(), 1e-9);
        var b2 = acc.boss("minecraft:wither", "boss-uuid-2", 1000, 100, false);
        assertEquals(100 / Math.pow(MxpMath.DEFAULT_GROWTH, 2), b2.effectiveXP(), 1e-9);
    }

    @Test void specialBossRepeatIsFixedEvenAtVeryHighP() {
        MxpAccount acc = new MxpAccount();
        acc.restore(new MxpMath.Counter(5000, 0), 0,
                java.util.List.of(), java.util.List.of(), java.util.List.of(), java.util.List.of());
        assertEquals(1000, acc.boss("draconicevolution:chaos_guardian", "guardian1",
                1000, 100, true).effectiveXP());
        assertEquals(100, acc.boss("draconicevolution:chaos_guardian", "guardian2",
                500, 100, true).effectiveXP());
        assertEquals(5001, acc.counter().lifetimeLevels());
        assertEquals(100, acc.counter().progress(), 1e-9);
    }

    @Test void firstCraftIsDividedAndPersistenceSurvivesCopyAndRespec() {
        MxpAccount acc = new MxpAccount();
        acc.ritual("totemic:rain", "ritual1", 1000, 200);
        var copy = acc.copy();
        assertEquals(120 / MxpMath.DEFAULT_GROWTH, copy.craft("theurgy:transmutation", "craft1", 120, 50).effectiveXP(), 1e-8);
        assertEquals(200 / MxpMath.DEFAULT_GROWTH, copy.ritual("totemic:rain", "ritual2", 1000, 200).effectiveXP(), 1e-8);
        // No link to Tyz skill points or purchased skill ranks.
        assertEquals(0, acc.firstCrafts().size());
        assertEquals(1, copy.firstCrafts().size());
    }

    @Test void noAwardForSimulationOrFreeCast() {
        MxpAccount acc = new MxpAccount();
        assertEquals(0, acc.resource("psi:cad", "payment1", 100, 1000, 0).effectiveXP());
        assertTrue(acc.recentTransactions().isEmpty());
        assertEquals(100, acc.resource("psi:cad", "payment1", 100, 1000, 1).effectiveXP(), 1e-8);
        assertTrue(acc.resource("psi:cad", "payment1", 100, 1000, 1).duplicate());
    }

    @Test void fractionalCreditIsRetainedUntilDeliverable() {
        MxpAccount acc = new MxpAccount();
        for (int i = 0; i < 9; i++)
            acc.resource("psi:cad", "cast-" + i, 1, 100000, 1);
        assertEquals(0.09, acc.pendingDelivery(), 1e-10);
        assertEquals(0.09f, acc.deliverableNow(), 1e-6);
        acc.confirmedDelivery(0.09f);
        assertEquals(0, acc.pendingDelivery(), 1e-6);
    }

    @Test void extremelyHighLifetimeXPStillHasNoOverflow() {
        MxpAccount acc = new MxpAccount();
        acc.restore(new MxpMath.Counter(50000, 0), 0,
                java.util.List.of(), java.util.List.of("minecraft:wither"),
                java.util.List.of(), java.util.List.of());
        assertEquals(0, acc.boss("minecraft:wither", "wither-x", 1000, 200, false).effectiveXP());
        assertEquals(50000, acc.counter().lifetimeLevels());
    }
    @Test void defaultGrowthIs102AndConfigRangeIsValidated() {
        assertEquals(1.02, MxpMath.DEFAULT_GROWTH, 0.0);
        assertEquals(1.0, MxpMath.validateGrowth(1.0), 0.0);
        assertEquals(1.01, MxpMath.validateGrowth(1.01), 0.0);
        assertEquals(1.02, MxpMath.validateGrowth(1.02), 0.0);
        assertEquals(1.1, MxpMath.validateGrowth(1.1), 0.0);
        assertEquals(1.2, MxpMath.validateGrowth(1.2), 0.0);
        for (double invalid : new double[] {0.99, 1.21, Double.NaN,
                                              Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> MxpMath.validateGrowth(invalid));
            assertThrows(IllegalArgumentException.class, () -> MxpMath.apply(
                new MxpMath.Counter(0, 0), 1000, true, invalid));
        }
    }

    @Test void configuredGrowthRepricesAcrossLevelBoundary() {
        for (double rate : new double[] {1.0, 1.01, 1.02, 1.1, 1.2}) {
            var award = MxpMath.apply(new MxpMath.Counter(0, 990), 20, false, rate);
            assertEquals(1, award.state().lifetimeLevels());
            assertEquals(10.0 / rate, award.state().progress(), 1e-9);
            assertEquals(10 + 10.0 / rate, award.effectiveXP(), 1e-9);
        }
    }

    @Test void linearGrowthDoesNotIncreaseCostAtHighLifetimeLevel() {
        var a = MxpMath.apply(new MxpMath.Counter(5000, 990), 2010, false, 1.0);
        assertEquals(5003, a.state().lifetimeLevels());
        assertEquals(0, a.state().progress(), 1e-9);
        assertEquals(2010, a.effectiveXP(), 1e-9);
    }

    @Test void legacyRateIsAvailableExplicitlyWithoutChangingDefault() {
        var legacy = MxpMath.apply(new MxpMath.Counter(0, 990), 20, false, 1.2);
        assertEquals(10.0 / 1.2, legacy.state().progress(), 1e-9);
        var modern = MxpMath.apply(new MxpMath.Counter(0, 990), 20, false);
        assertEquals(10.0 / 1.02, modern.state().progress(), 1e-9);
    }

    @Test void accountGrowthChangePreservesLevelAndFixedRewards() {
        MxpAccount account = new MxpAccount();
        account.setGrowthFactor(1.2);
        account.ritual("ritual-a", "tx-1", 1000, 200);
        assertEquals(1, account.counter().lifetimeLevels());
        assertEquals(200 / 1.2, account.ritual("ritual-a", "tx-2", 1000, 200).effectiveXP(), 1e-9);
        long p = account.counter().lifetimeLevels();
        double before = account.counter().progress();
        account.setGrowthFactor(1.0);
        assertEquals(p, account.counter().lifetimeLevels());
        assertEquals(before, account.counter().progress(), 1e-9);
        assertEquals(50, account.resource("ars:test", "tx-3", 5, 100, 1).effectiveXP(), 1e-9);
        assertEquals(1000, account.boss("minecraft:wither", "tx-4", 1000, 100, false).effectiveXP(), 1e-9);
        assertEquals(1.0, account.copy().growthFactor(), 0.0);
        assertThrows(IllegalArgumentException.class, () -> account.setGrowthFactor(0.9));
    }

}
