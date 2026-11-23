package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.JsonOps;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** A persisted economy owns current decimal XP; display levels are derived. */
class EconomyLedgerCodecTest {
    @Test void currentXpAndPurchaseProvenanceRoundTripAcrossSaveReload() {
        var economy=new ExperienceEconomy.Ledger(
            Map.of(XpChannel.MXP,new BigDecimal("15437.625")),
            List.of(new ExperienceEconomy.PaidRank("test:core","test:power",XpChannel.MXP,1)),
            Set.of("test:historical_achievement"),
            Set.of("tyr_progression:mxp|test:boss|boss:test:one"),
            Set.of("tyr_progression:mxp|test:spell|spell:test:operation")
        );
        var original=Ledger.empty().withEconomy(economy);
        var json=LedgerCodec.CODEC.encodeStart(JsonOps.INSTANCE,original).getOrThrow();
        var decoded=LedgerCodec.CODEC.parse(JsonOps.INSTANCE,json).getOrThrow();
        assertEquals(0,decoded.economy().currentXp().get(XpChannel.MXP)
            .compareTo(new BigDecimal("15437.625")));
        assertEquals(15,decoded.wallet(XpChannel.MXP).magicLevels());
        assertEquals(437.625,decoded.wallet(XpChannel.MXP).magicProgress(),1e-9);
        assertEquals(14,decoded.wallet(XpChannel.MXP).points());
        assertEquals(1,decoded.economy().paidRanks().size());
        assertEquals(economy.achievements(),decoded.economy().achievements());
        assertEquals(economy.rewardReceipts(),decoded.economy().rewardReceipts());
        assertEquals(economy.recentTransactions(),decoded.economy().recentTransactions());
    }
}
