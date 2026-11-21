package io.github.heatwaveqc.tyr.progression.state;

import java.util.HashMap;
import java.util.Map;

/** Immutable snapshots are written back to the NeoForge player attachment. */
public record Ledger(Map<String, Track> paths, Map<String, Wallet> wallets, Map<String,java.util.List<String>> payments) {
    public Ledger { paths=Map.copyOf(paths); wallets=Map.copyOf(wallets);
        var copy=new HashMap<String,java.util.List<String>>();payments.forEach((k,v)->copy.put(k,java.util.List.copyOf(v)));payments=Map.copyOf(copy); }
    public Ledger(Map<String,Track> paths) { this(paths,Map.of(),Map.of()); }
    public static Ledger empty() { return new Ledger(Map.of(),Map.of(),Map.of()); }
    public Track get(String id) { return paths.getOrDefault(id,Track.empty()); }
    public Wallet wallet(String id) { return wallets.get(id); }
    public Ledger with(String id,Track state) {
        var copy=new HashMap<>(paths); copy.put(id,state);
        return new Ledger(copy,wallets,payments);
    }
    public Ledger withWallet(String id,Wallet wallet) {
        var copy=new HashMap<>(wallets); copy.put(id,wallet);
        return new Ledger(paths,copy,payments);
    }
    public Ledger receipt(String path,String skill,String currency,int paid){return receipt(path,skill,currency,paid,false);}
    public Ledger receipt(String path,String skill,String currency,int paid,boolean permanent) {
        var copy=new HashMap<>(payments);String key=path+"|"+skill;
        var list=new java.util.ArrayList<>(copy.getOrDefault(key,java.util.List.of()));
        list.add((permanent?"!":"")+currency+"|"+paid);copy.put(key,list);
        return new Ledger(paths,wallets,copy);
    }
    public Ledger clearReceipts(String path) {
        var copy=new HashMap<>(payments);
        for(var key:new java.util.ArrayList<>(copy.keySet()))if(key.startsWith(path+"|")){
            var retained=copy.get(key).stream().filter(value->value.startsWith("!")).toList();
            if(retained.isEmpty())copy.remove(key);else copy.put(key,retained);
        }
        return new Ledger(paths,wallets,copy);
    }
}
