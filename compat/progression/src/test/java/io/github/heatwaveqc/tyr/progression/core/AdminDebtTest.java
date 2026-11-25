package io.github.heatwaveqc.tyr.progression.core;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminDebtTest {
    private static final ExperienceEconomy.SystemSpec MXP=ExperienceEconomy.SystemSpec.mxp();
    private static final String PATH="test:path", SKILL="test:skill";

    @Test void grantsCommitPointsWithoutMintingAndBlockOrdinaryPurchasesUntilRepaid() {
        var empty=ExperienceEconomy.Ledger.empty();
        var debt=ExperienceEconomy.grantRank(empty,MXP,PATH,SKILL,2,3,3);
        assertEquals(BigDecimal.ZERO,debt.currentXp().get(MXP.id()));
        assertEquals(2,ExperienceEconomy.debt(debt,MXP));
        assertEquals(0,ExperienceEconomy.available(debt,MXP));
        assertTrue(debt.paidRanks().getFirst().administrative());
        assertSame(debt,ExperienceEconomy.buy(debt,MXP,PATH,SKILL,1,3,3));
        var funded=AdminExperience.edit(debt,MXP,AdminExperience.Operation.ADD,3000);
        assertEquals(1,ExperienceEconomy.available(funded,MXP));
        var purchased=ExperienceEconomy.buy(funded,MXP,PATH,SKILL,1,3,3);
        assertEquals(3,ExperienceEconomy.committed(purchased,MXP.id()));
        assertEquals(1,ExperienceEconomy.ordinaryCommitted(purchased,MXP.id()));
        assertEquals(0,ExperienceEconomy.available(purchased,MXP));
        assertThrows(IllegalArgumentException.class,()->AdminExperience.edit(purchased,MXP,AdminExperience.Operation.SET,999));
        var reduced=AdminExperience.edit(purchased,MXP,AdminExperience.Operation.SET,1000);
        assertEquals(2,ExperienceEconomy.debt(reduced,MXP));
    }

    @Test void debtDepartureNeverMintsXpOrInvalidatesRetainedOrdinaryPurchases() {
        var funded=AdminExperience.edit(ExperienceEconomy.Ledger.empty(),MXP,AdminExperience.Operation.SET,1000);
        var ordinary=ExperienceEconomy.buy(funded,MXP,"test:core",SKILL,1,3,3);
        var debt=ExperienceEconomy.grantRank(ordinary,MXP,PATH,SKILL,2,3,3);
        var forfeited=ExperienceEconomy.transition(debt,Map.of(MXP.id(),MXP),Set.of(PATH),Map.of(),
                ExperienceEconomy.DeparturePolicy.FORFEIT,true,true);
        assertEquals(new BigDecimal("1000"),forfeited.currentXp().get(MXP.id()));
        assertEquals(1,ExperienceEconomy.committed(forfeited,MXP.id()));
        assertEquals(0,ExperienceEconomy.debt(forfeited,MXP));
        var refunded=ExperienceEconomy.transition(debt,Map.of(MXP.id(),MXP),Set.of(PATH),Map.of(),
                ExperienceEconomy.DeparturePolicy.REFUND,true,true);
        assertEquals(new BigDecimal("1000"),refunded.currentXp().get(MXP.id()));
        assertEquals(0,ExperienceEconomy.available(refunded,MXP));
    }

    @Test void capsAndCurrenciesRemainIndependent() {
        var one=ExperienceEconomy.grantRank(ExperienceEconomy.Ledger.empty(),MXP,PATH,SKILL,1,1,2);
        assertSame(one,ExperienceEconomy.grantRank(one,MXP,PATH,SKILL,1,1,2));
        var other=new ExperienceEconomy.SystemSpec("test:other",BigDecimal.TEN,10,BigDecimal.TEN);
        var two=ExperienceEconomy.grantRank(one,other,PATH,SKILL,2,2,2);
        assertEquals(1,ExperienceEconomy.debt(two,MXP));
        assertEquals(2,ExperienceEconomy.debt(two,other));
        assertSame(two,ExperienceEconomy.grantRank(two,other,PATH,SKILL,1,2,2));
    }
}
