package io.github.heatwaveqc.tyr.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/** Pre-stable player save schema; no established worlds require old-wallet migration. */
final class LedgerCodec {
    private LedgerCodec() {}

    private static final Codec<Track> TRACK_CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.BOOL.fieldOf("joined").forGetter(Track::joined),
        Codec.LONG.fieldOf("xp").forGetter(Track::xp),
        Codec.INT.fieldOf("rank").forGetter(Track::rank),
        Codec.INT.fieldOf("points").forGetter(Track::points),
        Codec.unboundedMap(Codec.STRING,Codec.INT).fieldOf("skills").forGetter(Track::skills),
        Codec.STRING.listOf().fieldOf("milestones").forGetter(Track::milestones),
        Codec.unboundedMap(Codec.STRING,Codec.INT).fieldOf("counters").forGetter(Track::counters)
    ).apply(i,Track::new));

    private static final Codec<Wallet> WALLET_CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.LONG.fieldOf("xp").forGetter(Wallet::xp),
        Codec.INT.fieldOf("rank").forGetter(Wallet::rank),
        Codec.INT.fieldOf("points").forGetter(Wallet::points),
        Codec.LONG.fieldOf("magic_levels").forGetter(Wallet::magicLevels),
        Codec.DOUBLE.fieldOf("magic_progress").forGetter(Wallet::magicProgress),
        Codec.STRING.listOf().fieldOf("recent_transactions").forGetter(Wallet::recentTransactions),
        Codec.STRING.listOf().fieldOf("first_completions").forGetter(Wallet::firstCompletions)
    ).apply(i,Wallet::new));

    private static final Codec<BigDecimal> DECIMAL_XP =
            Codec.STRING.xmap(BigDecimal::new,BigDecimal::toPlainString);
    private static final Codec<Set<String>> RECEIPT_SET =
            Codec.STRING.listOf().xmap(LinkedHashSet::new,ArrayList::new);

    private static final Codec<ExperienceEconomy.PaidRank> PAID_RANK =
            RecordCodecBuilder.create(i -> i.group(
                Codec.STRING.fieldOf("path").forGetter(ExperienceEconomy.PaidRank::path),
                Codec.STRING.fieldOf("skill").forGetter(ExperienceEconomy.PaidRank::skill),
                Codec.STRING.fieldOf("system").forGetter(ExperienceEconomy.PaidRank::system),
                Codec.INT.fieldOf("cost").forGetter(ExperienceEconomy.PaidRank::pointCost)
            ).apply(i,ExperienceEconomy.PaidRank::new));

    private static final Codec<ExperienceEconomy.Ledger> ECONOMY_CODEC =
            RecordCodecBuilder.create(i -> i.group(
                Codec.unboundedMap(Codec.STRING,DECIMAL_XP).fieldOf("current_xp")
                    .forGetter(ExperienceEconomy.Ledger::currentXp),
                PAID_RANK.listOf().fieldOf("paid_ranks")
                    .forGetter(ExperienceEconomy.Ledger::paidRanks),
                RECEIPT_SET.fieldOf("achievements")
                    .forGetter(ExperienceEconomy.Ledger::achievements),
                RECEIPT_SET.fieldOf("one_time_rewards")
                    .forGetter(ExperienceEconomy.Ledger::rewardReceipts),
                RECEIPT_SET.fieldOf("recent_transactions")
                    .forGetter(ExperienceEconomy.Ledger::recentTransactions)
            ).apply(i,ExperienceEconomy.Ledger::new));

    public static final Codec<Ledger> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.unboundedMap(Codec.STRING,TRACK_CODEC).fieldOf("paths").forGetter(Ledger::paths),
        Codec.unboundedMap(Codec.STRING,WALLET_CODEC).fieldOf("wallets").forGetter(Ledger::wallets),
        Codec.unboundedMap(Codec.STRING,Codec.STRING.listOf()).fieldOf("payments")
            .forGetter(Ledger::payments),
        ECONOMY_CODEC.fieldOf("economy").forGetter(Ledger::economy)
    ).apply(i,Ledger::new));
}
