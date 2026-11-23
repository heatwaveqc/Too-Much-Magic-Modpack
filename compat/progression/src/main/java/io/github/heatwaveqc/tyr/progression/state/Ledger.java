package io.github.heatwaveqc.tyr.progression.state;

import io.github.heatwaveqc.tyr.progression.core.ExperienceEconomy;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Immutable player state. XP/point accounting is authoritative in {@code economy};
 * older per-channel Wallet values are retained as a compatibility projection for
 * existing presentation and native Path integrations during the engine cutover.
 */
public record Ledger(Map<String, Track> paths, Map<String, Wallet> wallets,
                     Map<String,List<String>> payments, ExperienceEconomy.Ledger economy) {
    public Ledger {
        paths = Map.copyOf(paths);
        wallets = Map.copyOf(wallets);
        var copy = new HashMap<String,List<String>>();
        payments.forEach((k,v) -> copy.put(k, List.copyOf(v)));
        payments = Map.copyOf(copy);
        if (economy == null) throw new IllegalArgumentException("Missing XP economy state");
    }

    public Ledger(Map<String,Track> paths, Map<String,Wallet> wallets, Map<String,List<String>> payments) {
        this(paths, wallets, payments, ExperienceEconomy.Ledger.empty());
    }
    public Ledger(Map<String,Track> paths) { this(paths, Map.of(), Map.of()); }
    public static Ledger empty() { return new Ledger(Map.of()); }

    public Track get(String id) { return paths.getOrDefault(id,Track.empty()); }

    /**
     * Legacy MXP display adapter. Runtime source logic never derives an award
     * from these cached display fields; the exact current-XP ledger owns them.
     * Other independent native channels retain their prior wallet until cutover.
     */
    public Wallet wallet(String id) {
        Wallet stored = wallets.get(id);
        if (!XpChannel.MXP.equals(id) || !economy.currentXp().containsKey(id)) return stored;
        BigDecimal total = economy.currentXp().get(id);
        BigDecimal goal = ExperienceEconomy.SystemSpec.mxp().xpPerPoint();
        long level = total.divide(goal, 0, RoundingMode.DOWN).longValueExact();
        long committed = ExperienceEconomy.committed(economy,id);
        long free = level - committed;
        if (free < 0 || free > Integer.MAX_VALUE)
            throw new IllegalStateException("MXP purchases exceed earned points or integer range");
        int rank = (int) Math.min(Integer.MAX_VALUE,(long)level+1);
        long display = total.longValue();
        double progress = total.subtract(goal.multiply(BigDecimal.valueOf(level))).doubleValue();
        String prefix = id + "|";
        var tx = economy.recentTransactions().stream()
            .filter(v -> v.startsWith(prefix)).map(v -> v.substring(v.lastIndexOf('|') + 1)).toList();
        var completions = economy.rewardReceipts().stream()
            .filter(v -> v.startsWith(prefix)).map(v -> v.substring(v.lastIndexOf('|') + 1)).toList();
        return new Wallet(display,rank,(int)free,level,progress,tx,completions);
    }

    public Ledger with(String id,Track state) {
        var copy = new HashMap<>(paths);copy.put(id,state);
        return new Ledger(copy,wallets,payments,economy);
    }
    public Ledger withWallet(String id,Wallet wallet) {
        var copy = new HashMap<>(wallets);copy.put(id,wallet);
        return new Ledger(paths,copy,payments,economy);
    }
    public Ledger withEconomy(ExperienceEconomy.Ledger state) {
        return new Ledger(paths,wallets,payments,state);
    }
    public Ledger receipt(String path,String skill,String currency,int paid){
        return receipt(path,skill,currency,paid,false);
    }
    public Ledger receipt(String path,String skill,String currency,int paid,boolean permanent) {
        var copy = new HashMap<>(payments);String key=path+"|"+skill;
        var list=new java.util.ArrayList<>(copy.getOrDefault(key,List.of()));
        list.add((permanent?"!":"")+currency+"|"+paid);copy.put(key,list);
        return new Ledger(paths,wallets,copy,economy);
    }
    public Ledger clearReceipts(String path) {
        var copy = new HashMap<>(payments);
        for (var key : new java.util.ArrayList<>(copy.keySet())) if(key.startsWith(path+"|")) {
            var retained=copy.get(key).stream().filter(value->value.startsWith("!")).toList();
            if(retained.isEmpty())copy.remove(key);else copy.put(key,retained);
        }
        return new Ledger(paths,wallets,copy,economy);
    }
}
