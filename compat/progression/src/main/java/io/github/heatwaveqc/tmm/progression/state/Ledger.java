package io.github.heatwaveqc.tmm.progression.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Map;

/** Immutable snapshots are written back to the NeoForge player attachment. */
public record Ledger(Map<String, Track> paths, Map<String, Wallet> wallets) {
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
        Codec.unboundedMap(Codec.STRING,WALLET_CODEC).fieldOf("wallets").forGetter(Ledger::wallets)
    ).apply(i, Ledger::new));
    public Ledger { paths=Map.copyOf(paths); wallets=Map.copyOf(wallets); }
    public Ledger(Map<String,Track> paths) { this(paths,Map.of()); }
    public static Ledger empty() { return new Ledger(Map.of(),Map.of()); }
    public Track get(String id) { return paths.getOrDefault(id,Track.empty()); }
    public Wallet wallet(String id) { return wallets.get(id); }
    public Ledger with(String id,Track state) {
        var copy=new HashMap<>(paths); copy.put(id,state);
        return new Ledger(copy,wallets);
    }
    public Ledger withWallet(String id,Wallet wallet) {
        var copy=new HashMap<>(wallets); copy.put(id,wallet);
        return new Ledger(paths,copy);
    }
}
