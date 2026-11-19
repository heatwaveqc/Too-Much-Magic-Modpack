package io.github.heatwaveqc.tmm.progression.state;

import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import java.util.List;
import java.util.ArrayList;

/** One persistent XP/point balance per channel, never one balance per tree. */
public record Wallet(long xp, int rank, int points, long magicLevels, double magicProgress,
                     List<String> recentTransactions, List<String> firstCompletions) {
    public Wallet {
        if (xp < 0 || rank < 1 || points < 0 || magicLevels < 0 ||
            !Double.isFinite(magicProgress) || magicProgress < 0 || magicProgress >= 1000 ||
            recentTransactions.size() > 1024 || firstCompletions.size() > 65536)
            throw new IllegalArgumentException("Invalid XP wallet");
        recentTransactions=List.copyOf(recentTransactions);
        firstCompletions=List.copyOf(firstCompletions);
    }
    public static Wallet initial(XpChannel channel) {
        return new Wallet(0,1,channel.magic()?0:channel.source().pointsPerRank(),0,0,List.of(),List.of());
    }
    public Wallet points(int value) {
        return new Wallet(xp,rank,value,magicLevels,magicProgress,recentTransactions,firstCompletions);
    }
    public Wallet curve(Track view) {
        return new Wallet(view.xp(),view.rank(),view.points(),0,0,recentTransactions,firstCompletions);
    }
    public Wallet magic(long levels,double progress,String tx,String first) {
        long gained=levels-magicLevels;
        if(gained<0) throw new IllegalArgumentException("MXP cannot rewind");
        int nextRank=levels>=Integer.MAX_VALUE-1L?Integer.MAX_VALUE:(int)levels+1;
        long display=levels>=Long.MAX_VALUE/1000?Long.MAX_VALUE:levels*1000+(long)progress;
        int nextPoints=gained>=Integer.MAX_VALUE-points?Integer.MAX_VALUE:points+(int)gained;
        var transactions=new ArrayList<>(recentTransactions);
        transactions.add(tx);
        if(transactions.size()>1024) transactions.remove(0);
        var firsts=new ArrayList<>(firstCompletions);
        if(first!=null) firsts.add(first);
        return new Wallet(display,nextRank,nextPoints,levels,progress,transactions,firsts);
    }
}
