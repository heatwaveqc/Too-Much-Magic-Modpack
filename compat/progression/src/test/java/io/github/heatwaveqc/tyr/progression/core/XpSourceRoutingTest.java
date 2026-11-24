package io.github.heatwaveqc.tyr.progression.core;

import com.google.gson.JsonParser;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig;
import io.github.heatwaveqc.tyr.progression.data.XpSourceRouting;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class XpSourceRoutingTest {
    private static ExperienceEconomyConfig.Catalog read(String routes) {
        return ExperienceEconomyConfig.read(JsonParser.parseString("""
            {"systems":[{"id":"test:shared"},{"id":"test:combat",
              "earning_path":"test:hunter","default_reward_multiplier":0}],
             "source_routes":%s}
            """.formatted(routes)).getAsJsonObject());
    }

    @Test void explicitRoutesReplaceFallbackAndSupportMultipleCurrencies() {
        var catalog=read("""
          [{"source":"test:kill","destinations":[{"system":"test:shared"},
            {"system":"test:combat","multiplier":2}]},
           {"source":"test:disabled","destinations":[]}]
          """);
        var routing=catalog.routing();
        assertEquals(List.of(new XpSourceRouting.Destination("test:shared",1),
                new XpSourceRouting.Destination("test:combat",2)),
                routing.destinations("test:kill","test:shared"));
        assertEquals(List.of(),routing.destinations("test:disabled","test:shared"));
        assertEquals(List.of(),routing.destinations("test:missing",null));
        assertEquals(List.of(new XpSourceRouting.Destination("test:shared",1)),
                routing.destinations("test:missing","test:shared"));
    }

    @Test void systemEarningGateAndDefaultMultiplierAreIndependent() {
        var routing=read("[]").routing();
        assertTrue(routing.policy("test:shared").eligible(path->false));
        assertEquals(1.0,routing.policy("test:shared").defaultMultiplier());
        assertFalse(routing.policy("test:combat").eligible(path->false));
        assertTrue(routing.policy("test:combat").eligible(Set.of("test:hunter")::contains));
        assertEquals(0.0,routing.policy("test:combat").defaultMultiplier());
        var enable=new XpModifierMath.Modifier("test:enable",10,false,
                XpModifierMath.Operation.REPLACE,1,null,null);
        assertEquals(1.0,XpModifierMath.resolve(XpModifierMath.Kind.REWARD,
                routing.policy("test:combat").defaultMultiplier(),List.of(enable)).value());
    }

    @Test void twoDestinationsKeepIndependentXpAndPointsAndDedupe() {
        var catalog=read("[]");
        var a=catalog.systems().get("test:shared");
        var b=catalog.systems().get("test:combat");
        var first=ExperienceEconomy.earnModified(ExperienceEconomy.Ledger.empty(),a,
                "test:kill","test:action","test:first",new BigDecimal("1500"),false,1,1);
        var second=ExperienceEconomy.earnModified(first.ledger(),b,
                "test:kill","test:action","test:first",new BigDecimal("1500"),false,1,2);
        assertEquals(1,ExperienceEconomy.level(second.ledger(),a));
        assertEquals(3,ExperienceEconomy.level(second.ledger(),b));
        var purchased=ExperienceEconomy.buy(second.ledger(),b,"test:hunter","test:skill",1,5,5);
        assertEquals(second.ledger().currentXp(),purchased.currentXp());
        assertEquals(3,ExperienceEconomy.level(purchased,b));
        assertEquals(2,ExperienceEconomy.available(purchased,b));
        assertEquals(1,ExperienceEconomy.available(purchased,a));
        assertFalse(ExperienceEconomy.earnModified(purchased,b,"test:kill",
                "test:action","test:first",BigDecimal.ONE,false,1,1).accepted());
    }

    @Test void invalidOrDuplicateRouteDestinationsCannotLoad() {
        for(String destinations : List.of(
                "[{\"system\":\"test:unknown\"}]",
                "[{\"system\":\"test:shared\"},{\"system\":\"test:shared\"}]",
                "[{\"system\":\"test:shared\",\"multiplier\":-1}]",
                "[{\"system\":\"test:shared\",\"multiplier\":\"2\"}]")) {
            assertThrows(IllegalArgumentException.class,()->read(
                    "[{\"source\":\"test:kill\",\"destinations\":"+destinations+"}]"));
        }
        assertThrows(IllegalArgumentException.class,()->read("""
            [{"source":"test:kill","destinations":[]},{"source":"test:kill","destinations":[]}]
            """));
    }
}
