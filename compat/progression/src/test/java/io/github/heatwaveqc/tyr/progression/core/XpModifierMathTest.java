package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Kind;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Modifier;
import io.github.heatwaveqc.tyr.progression.core.XpModifierMath.Operation;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XpModifierMathTest {
    private static Modifier m(String id, int priority, boolean stack, Operation op, double value) {
        return new Modifier(id, priority, stack, op, value, null, null);
    }

    @Test void highestExclusivePriorityWinsAndEqualExclusiveConflictFailsClosed() {
        var a = m("a", 5, false, Operation.ADD, 1);
        var b = m("b", 9, false, Operation.REPLACE, 3);
        var c = m("c", 9, false, Operation.MULTIPLY, 2);
        var winner = XpModifierMath.resolve(Kind.REWARD, 1, List.of(a,b));
        assertEquals(3, winner.value());
        assertEquals(List.of("b"), winner.applied());
        var conflict = XpModifierMath.resolve(Kind.REWARD, 1, List.of(c,a,b));
        assertEquals(1, conflict.value());
        assertTrue(conflict.conflict());
        assertTrue(conflict.applied().isEmpty());
    }

    @Test void explicitStackUsesAscendingPriorityAndStableIdOrder() {
        var a = m("a", 1, true, Operation.ADD, 0.5);
        var b = m("b", 2, true, Operation.MULTIPLY, 2);
        var c = m("c", 2, true, Operation.ADD, 0.25);
        var exclusive = m("exclusive", 0, false, Operation.REPLACE, 100);
        var one = XpModifierMath.resolve(Kind.REWARD, 1, List.of(c,exclusive,b,a));
        var two = XpModifierMath.resolve(Kind.REWARD, 1, List.of(a,b,c,exclusive));
        assertEquals(one,two);
        assertEquals(3.25,one.value());
        assertEquals(List.of("a","b","c"),one.applied());
    }

    @Test void legacyEnlightenmentAndSafetyBounds() {
        var legacy = m("legacy",0,false,Operation.ADD,-0.019);
        assertEquals(1.001,XpModifierMath.resolve(Kind.GROWTH,1.02,List.of(legacy)).value(),1e-12);
        assertEquals(0.001,XpModifierMath.resolve(Kind.GROWTH,1.02,
                List.of(m("extreme",0,false,Operation.ADD,-100))).value());
        assertEquals(100,XpModifierMath.resolve(Kind.GROWTH,1.02,
                List.of(m("extreme",0,false,Operation.MULTIPLY,1000))).value());
        var bounded = new Modifier("bounded",0,false,Operation.MULTIPLY,10.0,null,2.0);
        assertEquals(2,XpModifierMath.resolve(Kind.REWARD,1,List.of(bounded)).value());
    }

    @Test void catalogReadsScopedRulesAndRejectsMalformedFields() {
        var catalog=ExperienceEconomyConfig.read(JsonParser.parseString("""
            {"systems":[{"id":"test:mxp"}],
             "modifiers":[{"id":"test:skill_bonus","kind":"reward","system":"test:mxp",
               "source":"test:kill","path":"test:hunter","skill":"test:mastery",
               "min_rank":2,"effect_index":0,"operation":"add","value":0.25,
               "per_rank":true,"stack":true,"priority":10}]}
            """).getAsJsonObject());
        var rule=catalog.modifiers().getFirst();
        assertTrue(rule.targets("test:mxp","test:kill",false));
        assertFalse(rule.targets("test:mxp","test:other",false));
        assertFalse(rule.targets("test:mxp","test:kill",true));
        assertEquals(0.75,rule.contribution(3).value());
        for(String invalid : new String[]{"\"system\":\"test:missing\"", "\"stack\":\"true\"",
                "\"min_rank\":1.5", "\"minimum\":-1", "\"bogus\":0"}) {
            String json="{\"systems\":[{\"id\":\"test:mxp\"}],\"modifiers\":[{"
                    + "\"id\":\"test:m\",\"kind\":\"reward\",\"path\":\"test:p\","
                    + "\"skill\":\"test:s\",\"operation\":\"add\",\"value\":1,"+invalid+"}]}";
            assertThrows(RuntimeException.class,()->ExperienceEconomyConfig.read(
                    JsonParser.parseString(json).getAsJsonObject()));
        }
    }

    @Test void modifierSettlementAppliesBeforeGrowthAndMinimumAndPreservesReceipts() {
        var spec = new ExperienceEconomy.SystemSpec("test:xp", new BigDecimal("1000"), 10,
                new BigDecimal("1000"), new BigDecimal("0.01"), true);
        var empty = ExperienceEconomy.Ledger.empty();
        var initial = ExperienceEconomy.earn(empty,spec,"test:init",null,new BigDecimal("1000"),true,1).ledger();
        var award = ExperienceEconomy.earnModified(initial,spec,"test:kill","test:one",null,
                new BigDecimal("100"),false,2,3);
        assertEquals(0,new BigDecimal("150").compareTo(award.effectiveXp()));
        var suppress = ExperienceEconomy.earnModified(award.ledger(),spec,"test:kill","test:zero",
                "test:first",BigDecimal.ONE,false,2,0);
        assertTrue(suppress.accepted());
        assertEquals(0,suppress.effectiveXp().signum());
        assertFalse(ExperienceEconomy.earnModified(suppress.ledger(),spec,"test:kill",
                "test:retry","test:first",BigDecimal.ONE,false,1,1).accepted());
        var cap = ExperienceEconomy.earnModified(initial,spec,"test:kill","test:large",
                null,new BigDecimal("1000"),true,1,100);
        assertEquals(10,ExperienceEconomy.level(cap.ledger(),spec));
        assertThrows(IllegalArgumentException.class,()->ExperienceEconomy.earnModified(initial,
                spec,"test:kill","test:invalid",null,new BigDecimal("1001"),true,1,0.01));
    }
}
