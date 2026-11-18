package io.github.heatwaveqc.tmm.progression.core;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import io.github.heatwaveqc.tmm.progression.state.Track;
import io.github.heatwaveqc.tmm.progression.state.Wallet;
import java.util.Map;
import java.util.List;

/** Shared channel transactions; tree-specific purchased ranks and milestones remain separate. */
public final class ChannelEngine {
    private ChannelEngine() {}
    public static Track view(Ledger ledger,Definition.Path path,XpChannel channel) {
        Track raw=ledger.get(path.id());
        if(!raw.joined()) return raw;
        Wallet w=ledger.wallet(channel.id());
        if(w==null) return Track.empty();
        return ProgressionMath.applyAutomatic(path,new Track(true,w.xp(),
                Math.min(w.rank(),path.maxRank()),w.points(),raw.skills(),raw.milestones(),raw.counters()));
    }
    public static Ledger join(Ledger ledger,Definition.Path path,XpChannel channel) {
        if(ledger.get(path.id()).joined()) return ledger;
        if(ledger.wallet(channel.id())==null) ledger=ledger.withWallet(channel.id(),Wallet.initial(channel));
        return ledger.with(path.id(),new Track(true,0,0,0,Map.of(),List.of(),Map.of()));
    }
    public static Ledger commit(Ledger ledger,Definition.Path path,XpChannel channel,Track after) {
        Wallet before=ledger.wallet(channel.id());
        if(before==null || !after.joined() || !ledger.get(path.id()).joined())
            throw new IllegalStateException("Unenrolled tree");
        Wallet updated=channel.earnsFrom(path.id())?before.curve(after):before.points(after.points());
        var purchases=new java.util.HashMap<>(after.skills());
        for(var skill:path.skills()) if(!skill.autoRanks().isEmpty()) purchases.remove(skill.id());
        Track raw=new Track(true,0,0,0,purchases,after.milestones(),after.counters());
        return ledger.with(path.id(),raw).withWallet(channel.id(),updated);
    }
    public static Ledger magic(Ledger ledger,XpChannel channel,String tx,String firstKey,
            double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        if(!channel.magic()) throw new IllegalArgumentException("Not MXP");
        checkId(tx); if(firstKey!=null) checkId(firstKey);
        Wallet wallet=ledger.wallet(channel.id());
        if(wallet==null) wallet=Wallet.initial(channel);
        if(wallet.recentTransactions().contains(tx)) return ledger;
        boolean first=firstKey!=null && !wallet.firstCompletions().contains(firstKey);
        var award=MxpMath.apply(new MxpMath.Counter(wallet.magicLevels(),wallet.magicProgress()),
                first?firstBase:repeatBase,first?firstFixed:repeatFixed,channel.growth());
        if(award.effectiveXP()<=0) return ledger;
        return ledger.withWallet(channel.id(),wallet.magic(award.state().lifetimeLevels(),
                award.state().progress(),tx,first?firstKey:null));
    }
    private static void checkId(String key) {
        if(key==null || key.length()>240 || !key.matches("[a-zA-Z0-9_.:/#@-]+"))
            throw new IllegalArgumentException("Invalid transaction ID");
    }
}
