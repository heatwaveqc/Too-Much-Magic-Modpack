package io.github.heatwaveqc.tyr.progression.runtime.external;

import static org.junit.jupiter.api.Assertions.*;

import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExternalRewardPolicyTest {
    private static final ExperienceEconomy.SystemSpec MXP = ExperienceEconomy.SystemSpec.mxp();
    private static final String ID = "tyr_progression:astral_perk_point";

    private static ExperienceEconomy.Ledger withEarnedMxp(long total) {
        return new ExperienceEconomy.Ledger(
                Map.of(MXP.id(), BigDecimal.valueOf(total)),
                List.of(), Set.of(), Set.of(), Set.of());
    }
    private static ExternalRewardPolicy.Offer offer(int base, int step, int cap) {
        return new ExternalRewardPolicy.Offer(ID,"astral_perk_token",MXP.id(),base,step,cap);
    }

    @Test void nativeMasteryTracksProviderCapNotASeparateThreshold() {
        assertFalse(ExternalRewardPolicy.nativeMastery(false,100,100));
        assertFalse(ExternalRewardPolicy.nativeMastery(true,99,100));
        assertTrue(ExternalRewardPolicy.nativeMastery(true,100,100));
        assertTrue(ExternalRewardPolicy.nativeMastery(true,30,30));
        assertFalse(ExternalRewardPolicy.nativeMastery(true,30,60));
        assertTrue(ExternalRewardPolicy.nativeMastery(true,60,60));
        assertFalse(ExternalRewardPolicy.nativeMastery(true,100,-1));
        assertFalse(ExternalRewardPolicy.nativeMastery(true,-1,100));
        assertFalse(ExternalRewardPolicy.nativeMastery(true,1,0));
    }

    @Test void onePointPurchaseDoesNotSpendStoredXp() {
        var before = withEarnedMxp(5000);
        var plan = ExternalRewardPolicy.prepare(before,MXP,offer(1,0,263));
        assertNotNull(plan);
        assertEquals(before.currentXp(),plan.charged().currentXp());
        assertEquals(4,ExperienceEconomy.available(plan.charged(),MXP));
    }

    @Test void tmmAstralMasteryHas263FlatPricePurchases() {
        var sale=offer(1,0,263);
        var initial=withEarnedMxp(300000);
        var state=initial;
        for (int i=1;i<=263;i++) {
            assertEquals(1,ExternalRewardPolicy.price(sale,i-1));
            var plan=ExternalRewardPolicy.prepare(state,MXP,sale);
            assertNotNull(plan);
            assertEquals(i,plan.purchaseIndex());
            assertEquals(1,plan.pricePoints());
            state=plan.charged();
        }
        assertEquals(263,ExternalRewardPolicy.owned(state,sale));
        assertEquals(37,ExperienceEconomy.available(state,MXP));
        assertEquals(initial.currentXp(),state.currentXp());
        assertEquals(-1,ExternalRewardPolicy.price(sale,263));
        assertNull(ExternalRewardPolicy.prepare(state,MXP,sale));
    }

    @Test void disabledUntilOwnerSetsAnActualMxpPrice() {
        var disabled=offer(0,0,0);
        assertEquals(-1,ExternalRewardPolicy.price(disabled,0));
        assertNull(ExternalRewardPolicy.prepare(withEarnedMxp(100000),MXP,disabled));
    }

    @Test void exactlyOnePermanentPaidRankAndStableNativeTokenForEachSuccessfulSale() {
        var sale=offer(2,1,0);
        var before=withEarnedMxp(6000);
        assertEquals(6,ExperienceEconomy.available(before,MXP));
        var first=ExternalRewardPolicy.prepare(before,MXP,sale);
        assertNotNull(first);
        assertEquals(2,first.pricePoints());
        assertEquals("tyr_progression:astral_purchase_000001",first.tokenId());
        assertEquals(4,ExperienceEconomy.available(first.charged(),MXP));
        assertEquals(1,ExternalRewardPolicy.owned(first.charged(),sale));

        var second=ExternalRewardPolicy.prepare(first.charged(),MXP,sale);
        assertNotNull(second);
        assertEquals(3,second.pricePoints());
        assertEquals("tyr_progression:astral_purchase_000002",second.tokenId());
        assertEquals(1,ExperienceEconomy.available(second.charged(),MXP));
        assertNull(ExternalRewardPolicy.prepare(second.charged(),MXP,sale));
    }

    @Test void noFreePointFromInsufficientBalanceOrMismatchedCurrency() {
        var sale=offer(3,0,0);
        assertNull(ExternalRewardPolicy.prepare(withEarnedMxp(2999),MXP,sale));
        var foreign=new ExperienceEconomy.SystemSpec("tmm:other",
                new BigDecimal("1000"),10000,new BigDecimal("1000000"));
        assertNull(ExternalRewardPolicy.prepare(withEarnedMxp(9000),foreign,sale));
    }

    @Test void capAndPriceOverflowFailClosed() {
        var capped=offer(1,0,1);
        var first=ExternalRewardPolicy.prepare(withEarnedMxp(9000),MXP,capped);
        assertNotNull(first);
        assertNull(ExternalRewardPolicy.prepare(first.charged(),MXP,capped));
        var huge=offer(Integer.MAX_VALUE,Integer.MAX_VALUE,0);
        assertEquals(-1,ExternalRewardPolicy.price(huge,1));
        assertThrows(IllegalArgumentException.class,
                ()->ExternalRewardPolicy.tokenId("other",1));
        assertThrows(IllegalArgumentException.class,
                ()->offer(-1,0,0));
    }

    @Test void pathDepartureCannotRefundExternalNativeReward() {
        var sale=offer(1,0,0);
        var first=ExternalRewardPolicy.prepare(withEarnedMxp(3000),MXP,sale);
        assertNotNull(first);
        var changed=ExperienceEconomy.transition(first.charged(),
                Map.of(MXP.id(),MXP),Set.of("tyr_progression:monk"),
                Map.of(), ExperienceEconomy.DeparturePolicy.FORFEIT,
                Set.of(MXP.id()),true);
        assertEquals(1,ExternalRewardPolicy.owned(changed,sale));
        assertEquals(0,ExperienceEconomy.available(changed,MXP));
    }

    @Test void purchasedBonusTokensArePlayerLocal() {
        var sale=offer(1,0,0);
        var playerA=ExternalRewardPolicy.prepare(withEarnedMxp(2000),MXP,sale);
        assertNotNull(playerA);
        var playerB=withEarnedMxp(2000);
        assertEquals(1,ExternalRewardPolicy.owned(playerA.charged(),sale));
        assertEquals(0,ExternalRewardPolicy.owned(playerB,sale));
        assertEquals("tyr_progression:astral_purchase_000001",
                ExternalRewardPolicy.tokenId(sale.kind(),1));
    }
}
