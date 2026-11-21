package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
final class LedgerCodec {
 private LedgerCodec(){}
    private static final Codec<Track> TRACK_CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.BOOL.fieldOf("joined").forGetter(Track::joined),
        Codec.LONG.fieldOf("xp").forGetter(Track::xp),
        Codec.INT.fieldOf("rank").forGetter(Track::rank),
        Codec.INT.fieldOf("points").forGetter(Track::points),
        Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("skills").forGetter(Track::skills),
        Codec.STRING.listOf().fieldOf("milestones").forGetter(Track::milestones),
        Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("counters").forGetter(Track::counters)
    ).apply(i, Track::new));

    private static final Codec<Wallet> WALLET_CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.LONG.fieldOf("xp").forGetter(Wallet::xp),
        Codec.INT.fieldOf("rank").forGetter(Wallet::rank),
        Codec.INT.fieldOf("points").forGetter(Wallet::points),
        Codec.LONG.fieldOf("magic_levels").forGetter(Wallet::magicLevels),
        Codec.DOUBLE.fieldOf("magic_progress").forGetter(Wallet::magicProgress),
        Codec.STRING.listOf().fieldOf("recent_transactions").forGetter(Wallet::recentTransactions),
        Codec.STRING.listOf().fieldOf("first_completions").forGetter(Wallet::firstCompletions)
    ).apply(i, Wallet::new));
    public static final Codec<Ledger> CODEC = RecordCodecBuilder.create(i -> i.group(
        Codec.unboundedMap(Codec.STRING,TRACK_CODEC).fieldOf("paths").forGetter(Ledger::paths),
        Codec.unboundedMap(Codec.STRING,WALLET_CODEC).fieldOf("wallets").forGetter(Ledger::wallets),
        Codec.unboundedMap(Codec.STRING,Codec.STRING.listOf()).fieldOf("payments").forGetter(Ledger::payments)
    ).apply(i, Ledger::new));
}
