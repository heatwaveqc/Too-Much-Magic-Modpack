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

    private static XpSourceRouting.Candidate candidate(String fallback,String source,String receipt,String amount,boolean fixed){
        return new XpSourceRouting.Candidate(fallback,source,receipt,new BigDecimal(amount),fixed);
    }

    @Test void sameEventUsesOneMeasurementPerSourceWithExplicitRouting(){
        var routing=read("""
            [{"source":"test:kill","destinations":[{"system":"test:shared"},{"system":"test:combat"}]}]
            """).routing();
        var awards=routing.normalize(List.of(
            candidate("test:shared","test:kill",null,"20.00",false),
            candidate("test:combat","test:kill",null,"20",false),
            candidate("test:shared","test:magic",null,"50",false)));
        assertEquals(List.of(candidate("","test:kill",null,"20",false),
                candidate("test:shared","test:magic",null,"50",false)).stream()
                .map(c->new XpSourceRouting.Candidate(c.fallback(),c.source(),c.completion(),
                        c.nominal().stripTrailingZeros(),c.fixed())).toList(),awards);
        assertEquals(2,routing.destinations("test:kill",awards.getFirst().fallback()).size());
    }

    @Test void fallbackDestinationsRemainIndependentAndConflictsFailClosed(){
        var routing=read("[]").routing();
        var a=candidate("test:shared","test:kill",null,"20",false);
        var b=candidate("test:combat","test:kill",null,"20",false);
        assertEquals(2,routing.normalize(List.of(a,b,a)).size());
        var independent=candidate("test:shared","test:magic",null,"50",false);
        var conflicting=candidate("test:shared","test:kill",null,"25",false);
        assertEquals(List.of("test:magic"),routing.normalize(List.of(a,conflicting,independent))
                .stream().map(XpSourceRouting.Candidate::source).toList());
        var first=candidate("test:shared","test:kill","test:first","20",false);
        assertTrue(routing.normalize(List.of(a,first)).isEmpty());
    }

    @Test void sameTransactionDifferentSourcesRemainAdditive(){
        var system=read("[]").systems().get("test:shared");
        var first=ExperienceEconomy.earnModified(ExperienceEconomy.Ledger.empty(),system,
                "test:magic","test:cast",null,new BigDecimal("400"),false,1,1);
        var second=ExperienceEconomy.earnModified(first.ledger(),system,
                "test:mining","test:cast",null,new BigDecimal("120"),false,1,1);
        assertEquals(0,new BigDecimal("520").compareTo(second.ledger().currentXp().get(system.id())));
        assertFalse(ExperienceEconomy.earnModified(second.ledger(),system,
                "test:mining","test:cast",null,new BigDecimal("120"),false,1,1).accepted());
    }

    @Test void receiptProjectionAllowsLaterDestinationAndReportsExactXp(){
        var catalog=ExperienceEconomyConfig.read(JsonParser.parseString("""
            {"systems":[{"id":"test:shared","xp_per_point":100},
                        {"id":"test:combat","xp_per_point":250}],
             "source_routes":[{"source":"test:kill","destinations":[
                {"system":"test:shared"},{"system":"test:combat"}]}]}
            """).getAsJsonObject());
        var destinations=catalog.routing().destinations("test:kill","test:shared");
        var a=catalog.systems().get("test:shared");
        var b=catalog.systems().get("test:combat");
        var empty=ExperienceEconomy.Ledger.empty();
        var first=ExperienceEconomy.earnModified(empty,a,"test:kill","test:action",
                "test:first",new BigDecimal("175.25"),true,1,1).ledger();
        assertFalse(XpSourceRouting.alreadySettled(destinations,"test:kill","test:action",first));
        assertEquals(0,new BigDecimal("175.25").compareTo(XpSourceRouting.settled(
                destinations,"test:kill","test:action","test:first",empty,first)
                .get(a.id()).effectiveXp()));
        var later=ExperienceEconomy.earnModified(first,b,"test:kill","test:action",
                "test:first",new BigDecimal("300"),true,1,1).ledger();
        var settled=XpSourceRouting.settled(destinations,"test:kill","test:action","test:first",first,later);
        assertEquals(Set.of(b.id()),settled.keySet());
        assertTrue(settled.get(b.id()).first());
        assertEquals(0,new BigDecimal("300").compareTo(settled.get(b.id()).effectiveXp()));
        assertTrue(XpSourceRouting.alreadySettled(destinations,"test:kill","test:action",later));
        var repeat=ExperienceEconomy.earnModified(later,b,"test:kill","test:repeat",null,
                BigDecimal.ONE,false,1,0).ledger();
        var zero=XpSourceRouting.settled(destinations,"test:kill","test:repeat","test:first",later,repeat);
        assertFalse(zero.get(b.id()).first());
        assertEquals(0,zero.get(b.id()).effectiveXp().signum());
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
