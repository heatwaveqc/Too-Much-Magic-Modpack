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

    @Test void extremeGrowthCannotCreateUnboundedDecimalsOrConsumeReceipts() {
        var spec = ExperienceEconomy.SystemSpec.mxp();
        var state = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), spec,
                "test:initial", null, new BigDecimal("1000000"), true, 1).ledger();
        for (double growth : new double[]{100, 0.001}) {
            var rejected = ExperienceEconomy.earn(state, spec, "test:too_small", "test:first",
                    BigDecimal.ONE, false, growth);
            assertFalse(rejected.accepted());
            assertSame(state, rejected.ledger());
            assertEquals(0, rejected.effectiveXp().signum());
        }
        var limited = new ExperienceEconomy.SystemSpec(CUSTOM, BigDecimal.ONE, 10,
                new BigDecimal("1e12"));
        var tooLarge = ExperienceEconomy.earn(state, limited, "test:fixed_limit", "test:first",
                new BigDecimal("1000000"), true, 1);
        assertFalse(tooLarge.accepted());
        assertSame(state, tooLarge.ledger());
        assertEquals(new BigDecimal("1000000"), state.currentXp().get(spec.id()));
        assertTrue(state.rewardReceipts().isEmpty());
        var normal = ExperienceEconomy.earn(state, spec, "test:too_small", "test:first",
                BigDecimal.ONE, false, 1.02);
        assertTrue(normal.accepted());
        assertTrue(normal.effectiveXp().signum() > 0);
        assertTrue(normal.effectiveXp().scale() <= ExperienceEconomy.MAX_XP_SCALE);
    }
}
