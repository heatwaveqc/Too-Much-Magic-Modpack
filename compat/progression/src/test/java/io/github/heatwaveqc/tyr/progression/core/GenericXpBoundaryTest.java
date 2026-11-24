package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenericXpBoundaryTest {
    private static final String CUSTOM = "test:large_threshold";

    @Test void customThresholdProjectsActualRemainderWithoutChangingMxp() {
        var prior = ExperienceEconomyRuntime.current();
        try {
            var config = ExperienceEconomyConfig.read(JsonParser.parseString("""
                {"systems":[{"id":"tyr_progression:mxp","xp_per_point":1000},
                            {"id":"test:large_threshold","xp_per_point":2500}]}
                """).getAsJsonObject());
            ExperienceEconomyRuntime.install(config);
            var state = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(),
                    config.systems().get(CUSTOM), "test:large", null,
                    new BigDecimal("4000.25"), true, 1.0).ledger();
            state = ExperienceEconomy.earn(state,
                    config.systems().get("tyr_progression:mxp"), "test:mxp", null,
                    new BigDecimal("1200.5"), true, 1.0).ledger();
            var ledger = Ledger.empty().withEconomy(state);
            assertEquals(2, ledger.wallet(CUSTOM).rank());
            assertEquals(1, ledger.wallet(CUSTOM).points());
            assertEquals(1500.25, ledger.wallet(CUSTOM).magicProgress());
            assertEquals(200.5, ledger.wallet("tyr_progression:mxp").magicProgress());
            assertEquals(new BigDecimal("4000.25"), ledger.economy().currentXp().get(CUSTOM));
            assertTrue(ledger.wallets().isEmpty(), "Generic wallet must remain a projection");
            assertThrows(IllegalArgumentException.class, () ->
                    ledger.wallet(CUSTOM).magic(2, 1500.25, "test:legacy", null));
        } finally {
            ExperienceEconomyRuntime.install(prior);
        }
    }

    @Test void integerFieldsNeverSilentlyTruncateOrWrap() {
        for (String invalid : new String[]{"1.5", "4294967297"}) {
            assertThrows(ArithmeticException.class, () ->
                    ExperienceEconomyConfig.read(JsonParser.parseString(
                        "{\"schema\":" + invalid + ",\"systems\":[{\"id\":\"test:x\"}]}"
                    ).getAsJsonObject()));
            for (String field : new String[]{"total_rank_cap", "max_ranks", "points_per_rank"}) {
                String cap = field.equals("total_rank_cap") ? invalid : "5";
                String ranks = field.equals("max_ranks") ? invalid : "5";
                String cost = field.equals("points_per_rank") ? invalid : "1";
                String json = """
                    {"systems":[{"id":"test:x"}],
                     "skills":[{"path":"test:path","skill":"test:skill","total_rank_cap":%s,
                        "purchases":[{"system":"test:x","max_ranks":%s,"points_per_rank":%s}]}]}
                    """.formatted(cap, ranks, cost);
                assertThrows(ArithmeticException.class, () ->
                        ExperienceEconomyConfig.read(JsonParser.parseString(json).getAsJsonObject()));
            }
        }
    }

    @Test void pathologicalDecimalsCannotEnterConfigLedgerOrAward() {
        var tiny = new BigDecimal("1e-10000000");
        var huge = new BigDecimal("1e10000000");
        var spec = ExperienceEconomy.SystemSpec.mxp();
        var empty = ExperienceEconomy.Ledger.empty();
        assertThrows(IllegalArgumentException.class, () ->
                new ExperienceEconomy.SystemSpec(CUSTOM, tiny, 100, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () ->
                new ExperienceEconomy.SystemSpec(CUSTOM, BigDecimal.ONE, 100, huge));
        assertThrows(IllegalArgumentException.class, () ->
                new ExperienceEconomy.Ledger(Map.of(CUSTOM, tiny), java.util.List.of(),
                        Set.of(), Set.of(), Set.of()));
        assertThrows(IllegalArgumentException.class, () ->
                ExperienceEconomy.earn(empty, spec, "test:tiny", null, tiny, true, 1));
        assertTrue(empty.currentXp().isEmpty());
        assertTrue(empty.recentTransactions().isEmpty());
    }

    @Test void subminimumAwardsAreSettledOnceWithConfiguredRounding() {
        for (boolean up : new boolean[]{false, true}) {
            var spec = new ExperienceEconomy.SystemSpec(CUSTOM, new BigDecimal("1000"), 10000,
                    new BigDecimal("1e12"), new BigDecimal("0.01"), up);
            var state = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), spec,
                    "test:initial", null, new BigDecimal("1000000"), true, 1).ledger();
            var award = ExperienceEconomy.earn(state, spec, "test:small", "test:first",
                    BigDecimal.ONE, false, 100);
            assertTrue(award.accepted());
            assertEquals(0, award.effectiveXp().compareTo(up ? new BigDecimal("0.01") : BigDecimal.ZERO));
            assertTrue(award.ledger().rewardReceipts().stream().anyMatch(x -> x.endsWith("test:first")));
            var replay = ExperienceEconomy.earn(award.ledger(), spec, "test:other", "test:first",
                    BigDecimal.ONE, false, 1);
            assertFalse(replay.accepted());
            assertSame(award.ledger(), replay.ledger());
            var near = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), spec,
                    "test:near", null, new BigDecimal("0.005"), true, 1);
            assertEquals(0, near.effectiveXp().compareTo(up ? new BigDecimal("0.01") : BigDecimal.ZERO));
            var equal = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), spec,
                    "test:equal", null, new BigDecimal("0.01"), false, 1);
            assertEquals(0, equal.effectiveXp().compareTo(new BigDecimal("0.01")));
        }
    }

    @Test void highAwardsSaturateWithoutExpandingHugeDecimals() {
        var spec = new ExperienceEconomy.SystemSpec(CUSTOM, new BigDecimal("1000"),
                ExperienceEconomy.HARD_MAX_LEVEL, new BigDecimal("1e12"));
        var empty = ExperienceEconomy.Ledger.empty();
        var runaway = ExperienceEconomy.earn(empty, spec, "test:runaway", "test:first",
                new BigDecimal("1002"), false, 0.001);
        assertTrue(runaway.accepted());
        assertEquals(ExperienceEconomy.HARD_MAX_LEVEL, ExperienceEconomy.level(runaway.ledger(), spec));
        assertEquals(0, spec.maxXp().compareTo(runaway.ledger().currentXp().get(CUSTOM)));
        var atCap = ExperienceEconomy.earn(runaway.ledger(), spec, "test:capped", null,
                BigDecimal.ONE, false, 100);
        assertTrue(atCap.accepted());
        assertEquals(0, atCap.effectiveXp().signum());
        var fixed = ExperienceEconomy.earn(empty, spec, "test:fixed", null,
                new BigDecimal("1e12"), true, 1);
        assertEquals(0, spec.maxXp().compareTo(fixed.effectiveXp()));
    }

    @Test void largeFiniteAwardDoesNotFalselySaturateAndCrossingMathIsCorrect() {
        var spec = new ExperienceEconomy.SystemSpec(CUSTOM, new BigDecimal("1000"),
                100000, new BigDecimal("1e12"));
        var empty = ExperienceEconomy.Ledger.empty();
        var large = ExperienceEconomy.earn(empty, spec, "test:large", null,
                new BigDecimal("20000000"), false, 1.000001);
        long level = ExperienceEconomy.level(large.ledger(), spec);
        assertTrue(level > 10000 && level < 20000);
        // Independent reference: binary-search result must be bracketed by
        // the cumulative base cost of its two neighboring boundaries.
        double g = 1.000001;
        assertTrue(1000 * Math.expm1(level * Math.log(g)) / (g - 1) <= 20000000);
        assertTrue(1000 * Math.expm1((level + 1) * Math.log(g)) / (g - 1) > 20000000);
        var descending = ExperienceEconomy.earn(empty, spec, "test:descending", null,
                new BigDecimal("1800"), false, 0.5);
        assertEquals(0, descending.effectiveXp().compareTo(new BigDecimal("3400")));
        var ascending = ExperienceEconomy.earn(empty, spec, "test:ascending", null,
                new BigDecimal("7500"), false, 2);
        assertEquals(0, ascending.effectiveXp().compareTo(new BigDecimal("3062.5")));
    }

    @Test void capTakesPriorityWhenLessThanMinimumFits() {
        var spec = new ExperienceEconomy.SystemSpec(CUSTOM, BigDecimal.TEN, 1,
                new BigDecimal("1000"), new BigDecimal("1"), true);
        var state = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), spec,
                "test:initial", null, new BigDecimal("9.9"), true, 1).ledger();
        var award = ExperienceEconomy.earn(state, spec, "test:final", null,
                new BigDecimal("0.01"), false, 1);
        assertEquals(0, new BigDecimal("0.1").compareTo(award.effectiveXp()));
        assertEquals(1, ExperienceEconomy.level(award.ledger(), spec));
    }

    @Test void perSystemRewardPolicyIsValidatedAndIndependent() {
        var catalog = ExperienceEconomyConfig.read(JsonParser.parseString("""
                {"systems":[{"id":"test:a","max_level":100,"min_reward":0.1,"round_subminimum_up":true},
                            {"id":"test:b","max_level":200,"min_reward":0.01,"round_subminimum_up":false}]}
                """).getAsJsonObject());
        assertEquals(100, catalog.systems().get("test:a").maxLevel());
        assertTrue(catalog.systems().get("test:a").roundSubminimumUp());
        assertEquals(0, new BigDecimal("0.01").compareTo(catalog.systems().get("test:b").minReward()));
        assertFalse(catalog.systems().get("test:b").roundSubminimumUp());
        for (String invalid : new String[]{"\"max_level\":10000001", "\"min_reward\":0",
                "\"min_reward\":1e-129", "\"round_subminimum_up\":\"true\""}) {
            assertThrows(IllegalArgumentException.class, () -> ExperienceEconomyConfig.read(
                    JsonParser.parseString("{\"systems\":[{\"id\":\"test:a\"," + invalid + "}]}")
                            .getAsJsonObject()));
        }
    }
}
