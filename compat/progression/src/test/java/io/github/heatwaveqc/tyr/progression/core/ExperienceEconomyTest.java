package io.github.heatwaveqc.tyr.progression.core;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExperienceEconomyTest {
    private static final ExperienceEconomy.SystemSpec MXP = ExperienceEconomy.SystemSpec.mxp();
    private static final ExperienceEconomy.SystemSpec CULTIVATION =
            new ExperienceEconomy.SystemSpec("test:cultivation",
                    new BigDecimal("100"), 10000, new BigDecimal("1e12"));

    private static ExperienceEconomy.Ledger earnFixed(ExperienceEconomy.Ledger state,
                                                       ExperienceEconomy.SystemSpec system,
                                                       String receipt, long amount) {
        return ExperienceEconomy.earn(state, system, receipt, null,
                BigDecimal.valueOf(amount), true, 1.02).ledger();
    }
    private static ExperienceEconomy.Ledger buyMany(ExperienceEconomy.Ledger state,
                                                      ExperienceEconomy.SystemSpec system,
                                                      String path, String skill, int count) {
        for (int i = 0; i < count; ++i)
            state = ExperienceEconomy.buy(state, system, path, skill, 1, 10000, 10000);
        return state;
    }

    @Test void derivedLevelsAndAvailablePointsDoNotStoreIndependentRanks() {
        var state = earnFixed(ExperienceEconomy.Ledger.empty(), MXP, "test:earn1", 3499);
        assertEquals(3, ExperienceEconomy.level(state, MXP));
        assertEquals(3, ExperienceEconomy.available(state, MXP));
        var paid = ExperienceEconomy.buy(state, MXP,
                "test:core", "test:wellspring", 1, 5, 8);
        assertEquals(new BigDecimal("3499"), paid.currentXp().get(MXP.id()));
        assertEquals(3, ExperienceEconomy.level(paid, MXP));
        assertEquals(2, ExperienceEconomy.available(paid, MXP));
        assertEquals(1, ExperienceEconomy.committed(paid, MXP.id()));
        assertEquals(1, ExperienceEconomy.effectiveRank(paid, "test:core", "test:wellspring"));
    }

    @Test void oneSkillCanStackIndependentCurrenciesWithoutDoubleBuying() {
        var state = earnFixed(ExperienceEconomy.Ledger.empty(), MXP, "test:mxp_award", 6000);
        state = earnFixed(state, CULTIVATION, "test:cultivation_award", 600);
        for (int i = 0; i < 4; ++i)
            state = ExperienceEconomy.buy(state, MXP, "test:monk", "test:meditation", 1, 5, 8);
        for (int i = 0; i < 3; ++i)
            state = ExperienceEconomy.buy(state, CULTIVATION, "test:monk", "test:meditation", 1, 3, 8);
        assertEquals(7, ExperienceEconomy.effectiveRank(state, "test:monk", "test:meditation"));
        assertEquals(4, ExperienceEconomy.contributedRanks(state, "test:monk", "test:meditation", MXP.id()));
        assertEquals(3, ExperienceEconomy.contributedRanks(state, "test:monk", "test:meditation", CULTIVATION.id()));
        assertSame(state, ExperienceEconomy.buy(state, CULTIVATION,
                "test:monk", "test:meditation", 1, 3, 8));
        var eighth = ExperienceEconomy.buy(state, MXP,
                "test:monk", "test:meditation", 1, 5, 8);
        assertEquals(8, ExperienceEconomy.effectiveRank(eighth, "test:monk", "test:meditation"));
        assertSame(eighth, ExperienceEconomy.buy(eighth, MXP,
                "test:monk", "test:meditation", 1, 5, 8));
        assertEquals(1, ExperienceEconomy.available(eighth, MXP));
        assertEquals(3, ExperienceEconomy.available(eighth, CULTIVATION));
    }

    @Test void dynamicGrowthIsResolvedAtEarningTimeAndRecalculatesAtLevelBoundary() {
        var state = earnFixed(ExperienceEconomy.Ledger.empty(), MXP, "test:initial", 1000);
        var steep = ExperienceEconomy.earn(state, MXP, "test:steep", null,
                new BigDecimal("1000"), false, 100.0);
        assertEquals(0, steep.effectiveXp().compareTo(new BigDecimal("10")));
        assertEquals(1, ExperienceEconomy.level(steep.ledger(), MXP));

        var neutral = ExperienceEconomy.earn(state, MXP, "test:neutral", null,
                new BigDecimal("1000"), false, 1.0);
        assertEquals(0, neutral.effectiveXp().compareTo(new BigDecimal("1000")));
        assertEquals(2, ExperienceEconomy.level(neutral.ledger(), MXP));

        var fast = ExperienceEconomy.earn(state, MXP, "test:fast", null,
                new BigDecimal("0.5"), false, 0.001);
        assertEquals(0, fast.effectiveXp().compareTo(new BigDecimal("500")));
        assertEquals(1, ExperienceEconomy.level(fast.ledger(), MXP));

        var threshold = ExperienceEconomy.earn(
                earnFixed(ExperienceEconomy.Ledger.empty(), MXP, "test:at_zero", 500),
                MXP, "test:cross", null, new BigDecimal("1000"), false, 2.0);
        assertEquals(0, threshold.effectiveXp().compareTo(new BigDecimal("750")));
        assertEquals(1, ExperienceEconomy.level(threshold.ledger(), MXP));
        assertEquals(0, threshold.ledger().currentXp().get(MXP.id()).compareTo(new BigDecimal("1250")));
    }

    @Test void extremeGrowthSaturatesAtConfiguredMaximum() {
        var limited = new ExperienceEconomy.SystemSpec("test:limited",
                new BigDecimal("1000"), 10, new BigDecimal("1e12"));
        var initial = earnFixed(ExperienceEconomy.Ledger.empty(), limited, "test:starter", 1000);
        var capped = ExperienceEconomy.earn(
                initial, limited, "test:runaway", null, new BigDecimal("2"), false, 0.001);
        assertTrue(capped.accepted());
        assertEquals(9000, capped.effectiveXp().intValueExact());
        assertEquals(10, ExperienceEconomy.level(capped.ledger(), limited));
        assertEquals(1000, initial.currentXp().get(limited.id()).intValueExact());
        assertEquals(1, ExperienceEconomy.level(initial, limited));
        assertThrows(IllegalArgumentException.class,
                () -> ExperienceEconomy.validateGrowth(0.00099));
        assertThrows(IllegalArgumentException.class,
                () -> ExperienceEconomy.validateGrowth(100.001));
        assertEquals(0.001, ExperienceEconomy.validateGrowth(0.001));
        assertEquals(100.0, ExperienceEconomy.validateGrowth(100.0));
        assertThrows(IllegalArgumentException.class, () -> ExperienceEconomy.earn(
                initial, limited, "test:nan", null, BigDecimal.ONE, false, Double.NaN));
    }

    @Test void monkForfeitRebasesToCoreAndOtherSurvivingPathInvestment() {
        var state = earnFixed(ExperienceEconomy.Ledger.empty(), MXP,
                "test:initial_earn", 40400).withAchievement("test:break_stone");
        state = buyMany(state, MXP, "test:core", "test:core_stat", 15);
        state = buyMany(state, MXP, "test:monk", "test:enlightenment", 20);
        state = buyMany(state, MXP, "test:hunter", "test:hunting", 3);
        assertEquals(40, ExperienceEconomy.level(state, MXP));
        assertEquals(2, ExperienceEconomy.available(state, MXP));

        var after = ExperienceEconomy.transition(state, Map.of(MXP.id(), MXP),
                Set.of("test:monk"), Map.of(), ExperienceEconomy.DeparturePolicy.FORFEIT, true, true);
        assertEquals(0, after.currentXp().get(MXP.id()).compareTo(new BigDecimal("18000")));
        assertEquals(18, ExperienceEconomy.level(after, MXP));
        assertEquals(0, ExperienceEconomy.available(after, MXP));
        assertEquals(0, ExperienceEconomy.effectiveRank(after, "test:monk", "test:enlightenment"));
        assertEquals(15, ExperienceEconomy.effectiveRank(after, "test:core", "test:core_stat"));
        assertEquals(3, ExperienceEconomy.effectiveRank(after, "test:hunter", "test:hunting"));
        assertTrue(after.achievements().contains("test:break_stone"));
        assertTrue(after.recentTransactions().contains(MXP.id()
                + "|tyr_progression:default|test:initial_earn"));
        assertSame(after, ExperienceEconomy.transition(after, Map.of(MXP.id(), MXP),
                Set.of(), Map.of(), ExperienceEconomy.DeparturePolicy.FORFEIT, true, false));
    }

    @Test void departureCanRetainRefundOrForfeitIndependently() {
        var state = earnFixed(ExperienceEconomy.Ledger.empty(), MXP, "test:earn", 4500);
        state = buyMany(state, MXP, "test:core", "test:core", 1);
        state = buyMany(state, MXP, "test:monk", "test:monk", 2);
        assertEquals(1, ExperienceEconomy.available(state, MXP));
        var specs = Map.of(MXP.id(), MXP);

        var refunded = ExperienceEconomy.transition(state, specs, Set.of("test:monk"),
                Map.of(), ExperienceEconomy.DeparturePolicy.REFUND, true, true);
        assertEquals(3000, refunded.currentXp().get(MXP.id()).intValueExact());
        assertEquals(2, ExperienceEconomy.available(refunded, MXP));
        assertEquals(0, ExperienceEconomy.effectiveRank(refunded, "test:monk", "test:monk"));

        var retained = ExperienceEconomy.transition(state, specs, Set.of("test:monk"),
                Map.of(), ExperienceEconomy.DeparturePolicy.RETAIN, true, true);
        assertEquals(3000, retained.currentXp().get(MXP.id()).intValueExact());
        assertEquals(0, ExperienceEconomy.available(retained, MXP));
        assertEquals(2, ExperienceEconomy.effectiveRank(retained, "test:monk", "test:monk"));

        var forfeitedKeepFree = ExperienceEconomy.transition(state, specs, Set.of("test:monk"),
                Map.of(), ExperienceEconomy.DeparturePolicy.FORFEIT, false, true);
        assertEquals(2500, forfeitedKeepFree.currentXp().get(MXP.id()).intValueExact());
        assertEquals(1, ExperienceEconomy.available(forfeitedKeepFree, MXP));

        var mixed = ExperienceEconomy.transition(state, specs, Set.of("test:monk"),
                Map.of(new ExperienceEconomy.PathSystem("test:monk", MXP.id()),
                        ExperienceEconomy.DeparturePolicy.REFUND),
                ExperienceEconomy.DeparturePolicy.FORFEIT, true, true);
        assertEquals(2, ExperienceEconomy.available(mixed, MXP));
    }

    @Test void onePathChangeCanWipeFreeMxpButPreserveOtherSystemsFreeXp() {
        var state = earnFixed(ExperienceEconomy.Ledger.empty(), MXP,
                "test:earned_mxp", 4500);
        state = earnFixed(state, CULTIVATION, "test:earned_cultivation", 250);
        state = buyMany(state, MXP, "test:core", "test:core_stat", 1);
        state = buyMany(state, MXP, "test:monk", "test:monk_power", 2);
        state = ExperienceEconomy.buy(state, CULTIVATION,
                "test:monk", "test:meditation", 1, 3, 3);

        var after = ExperienceEconomy.transition(state,
                Map.of(MXP.id(), MXP, CULTIVATION.id(), CULTIVATION),
                Set.of("test:monk"), Map.of(),
                ExperienceEconomy.DeparturePolicy.FORFEIT,
                Set.of(MXP.id()), true);
        assertEquals(1000, after.currentXp().get(MXP.id()).intValueExact());
        assertEquals(150, after.currentXp().get(CULTIVATION.id()).intValueExact());
        assertEquals(0, ExperienceEconomy.available(after, MXP));
        assertEquals(1, ExperienceEconomy.available(after, CULTIVATION));
    }

    @Test void independentSourceCategoriesAndCurrenciesAreAdditiveButDuplicatesAreNot() {
        var source = ExperienceEconomy.Ledger.empty();
        var mana = ExperienceEconomy.earn(source, MXP, "test:mana_debit",
                "test:one_spell", null, new BigDecimal("1000"), true, 1.02);
        var block = ExperienceEconomy.earn(mana.ledger(), MXP, "test:verified_block",
                "test:one_spell", null, new BigDecimal("200"), true, 1.02);
        var cultivation = ExperienceEconomy.earn(block.ledger(), CULTIVATION,
                "test:verified_block", "test:one_spell", null,
                new BigDecimal("30"), true, 1.02);
        assertTrue(mana.accepted());
        assertTrue(block.accepted());
        assertTrue(cultivation.accepted());
        assertEquals(1200, cultivation.ledger().currentXp().get(MXP.id()).intValueExact());
        assertEquals(30, cultivation.ledger().currentXp().get(CULTIVATION.id()).intValueExact());
        var duplicate = ExperienceEconomy.earn(cultivation.ledger(), MXP,
                "test:verified_block", "test:one_spell", null,
                new BigDecimal("200"), true, 1.02);
        assertFalse(duplicate.accepted());
        assertSame(cultivation.ledger(), duplicate.ledger());
    }

    @Test void firstCompletionReceiptsSurviveResetAndCannotBeReplayed() {
        var first = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), MXP,
                "test:first_tx", "test:boss_first", new BigDecimal("1000"), true, 1.02);
        var reset = ExperienceEconomy.transition(first.ledger(), Map.of(MXP.id(), MXP),
                Set.of(), Map.of(), ExperienceEconomy.DeparturePolicy.FORFEIT, true, true);
        assertEquals(0, reset.currentXp().get(MXP.id()).signum());
        var replay = ExperienceEconomy.earn(reset, MXP, "test:second_tx",
                "test:boss_first", new BigDecimal("1000"), true, 1.02);
        assertFalse(replay.accepted());
        assertSame(reset, replay.ledger());
    }
}
