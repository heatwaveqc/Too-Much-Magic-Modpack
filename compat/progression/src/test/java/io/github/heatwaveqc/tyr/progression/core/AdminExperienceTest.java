package io.github.heatwaveqc.tyr.progression.core;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminExperienceTest {
    private static final ExperienceEconomy.SystemSpec MXP = ExperienceEconomy.SystemSpec.mxp();
    private static final ExperienceEconomy.SystemSpec OTHER =
        new ExperienceEconomy.SystemSpec("test:other", BigDecimal.TEN, 100, BigDecimal.TEN);

    @Test void exactEditsPreserveReceiptsAndOtherCurrencies() {
        var state = ExperienceEconomy.earn(ExperienceEconomy.Ledger.empty(), MXP,
            "test:tx", "test:boss", new BigDecimal("1250.5"), true, 1.02).ledger()
            .withAchievement("test:achievement");
        state = AdminExperience.edit(state, OTHER, AdminExperience.Operation.SET, 17);
        var added = AdminExperience.edit(state, MXP, AdminExperience.Operation.ADD, 100);
        assertEquals(new BigDecimal("1350.5"), AdminExperience.balance(added, MXP));
        var reset = AdminExperience.edit(added, MXP, AdminExperience.Operation.RESET, 0);
        assertEquals(BigDecimal.ZERO, AdminExperience.balance(reset, MXP));
        assertEquals(new BigDecimal("17"), AdminExperience.balance(reset, OTHER));
        assertEquals(state.rewardReceipts(), reset.rewardReceipts());
        assertEquals(state.recentTransactions(), reset.recentTransactions());
        assertEquals(state.achievements(), reset.achievements());
    }

    @Test void rejectsNegativeOutOfBoundsAndUnbackedPurchases() {
        var empty = ExperienceEconomy.Ledger.empty();
        var funded = AdminExperience.edit(empty, MXP, AdminExperience.Operation.SET, 3000);
        var paid = ExperienceEconomy.buy(funded, MXP, "test:path","test:skill", 2, 3, 3);
        assertThrows(IllegalArgumentException.class, () -> AdminExperience.edit(paid, MXP, AdminExperience.Operation.SET, 1999));
        assertThrows(IllegalArgumentException.class, () -> AdminExperience.edit(paid, MXP, AdminExperience.Operation.RESET, 0));
        assertThrows(IllegalArgumentException.class, () -> AdminExperience.edit(empty, MXP, AdminExperience.Operation.SUBTRACT, 1));
        assertThrows(IllegalArgumentException.class, () -> AdminExperience.edit(empty, OTHER, AdminExperience.Operation.SET, 1001));
        var reduced = AdminExperience.edit(paid, MXP, AdminExperience.Operation.SUBTRACT, 1000);
        assertEquals(0, ExperienceEconomy.available(reduced, MXP));
        assertEquals(paid.paidRanks(), reduced.paidRanks());
    }

    @Test void regularFixedAndTrueAwardsHaveDistinctAccounting(){
        var before=AdminExperience.edit(ExperienceEconomy.Ledger.empty(),MXP,AdminExperience.Operation.SET,2000);
        var nominal=BigDecimal.valueOf(100);
        var regular=ExperienceEconomy.earnModified(before,MXP,"tyr_progression:admin_grant",
            "test:regular",null,nominal,false,1.02,1.5);
        var fixed=ExperienceEconomy.earnModified(before,MXP,"tyr_progression:admin_grant",
            "test:fixed",null,nominal,true,1.02,1.5);
        var exact=AdminExperience.edit(before,MXP,AdminExperience.Operation.ADD,100);
        assertTrue(regular.accepted());
        assertTrue(fixed.accepted());
        assertTrue(regular.effectiveXp().compareTo(fixed.effectiveXp())<0);
        assertEquals(0,fixed.effectiveXp().compareTo(BigDecimal.valueOf(150)));
        assertEquals(0,AdminExperience.balance(exact,MXP).compareTo(BigDecimal.valueOf(2100)));
        assertEquals(before.recentTransactions(),exact.recentTransactions());
        assertNotEquals(before.recentTransactions(),fixed.ledger().recentTransactions());
        assertEquals(before.rewardReceipts(),fixed.ledger().rewardReceipts());
    }
}
