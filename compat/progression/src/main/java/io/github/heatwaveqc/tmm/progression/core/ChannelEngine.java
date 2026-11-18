package io.github.heatwaveqc.tmm.progression.core;

import io.github.heatwaveqc.tmm.progression.model.Definition;
import io.github.heatwaveqc.tmm.progression.model.XpChannel;
import io.github.heatwaveqc.tmm.progression.state.Ledger;
import io.github.heatwaveqc.tmm.progression.state.Track;
import io.github.heatwaveqc.tmm.progression.state.Wallet;
import java.util.Map;
import io.github.heatwaveqc.tmm.progression.data.ChannelConfig;
import java.util.List;

/** Shared channel transactions; tree-specific purchased ranks and milestones remain separate. */
public final class ChannelEngine {
    private ChannelEngine() {}
    public static Track view(Ledger ledger,Definition.Path path,ChannelConfig config) {
        Track raw=ledger.get(path.id());if(!raw.joined())return raw;
        var def=config.paths().get(path.id());if(def==null)return Track.empty();
        Wallet w=ledger.wallet(def.id());
        var ranks=new java.util.HashMap<>(raw.skills());
        for(var skill:path.skills())if(!skill.autoRanks().isEmpty()){
            var bind=config.skills().get(new ChannelConfig.SkillKey(path.id(),skill.id()));
            var level=bind==null?null:ledger.wallet(bind.levelSystem().id());
            int earned=0;
            if(level!=null)for(int at:skill.autoRanks())if(level.rank()>=at)earned++;
            if(earned>0)ranks.put(skill.id(),earned);else ranks.remove(skill.id());
        }
        return new Track(true,w==null?0:w.xp(),w==null?0:w.rank(),w==null?0:w.points(),
                ranks,raw.milestones(),raw.counters());
    }
    public static Ledger initialize(Ledger ledger,XpChannel system){
        return ledger.wallet(system.id())==null ? ledger.withWallet(system.id(),Wallet.initial(system)):ledger;
    }
    public static Ledger join(Ledger ledger,Definition.Path path,XpChannel channel) {
        if(ledger.get(path.id()).joined())return ledger;
        ledger=ledger.with(path.id(),new Track(true,0,0,0,Map.of(),List.of(),Map.of()));
        if(channel.magic()||channel.earningPath().isEmpty()||ledger.get(channel.earningPath()).joined())
            ledger=initialize(ledger,channel);
        return ledger;
    }
    public static Ledger grant(Ledger ledger,XpChannel system,long xp,int points){
        if(system.magic()||xp<0||points<0)throw new IllegalArgumentException("Invalid native grant");
        ledger=initialize(ledger,system);
        Wallet w=ledger.wallet(system.id());
        var state=new Track(true,w.xp(),w.rank(),w.points(),Map.of(),List.of(),Map.of());
        var after=ProgressionMath.grantXp(system.source(),state,xp).grantPoints(points);
        return ledger.withWallet(system.id(),w.curve(after));
    }
    public static Ledger ownership(Ledger ledger,Definition.Path path,Track after){
        var skills=new java.util.HashMap<>(after.skills());
        for(var skill:path.skills())if(!skill.autoRanks().isEmpty())skills.remove(skill.id());
        return ledger.with(path.id(),new Track(after.joined(),0,0,0,skills,after.milestones(),after.counters()));
    }
    public static Ledger buy(Ledger ledger,Definition.Path tree,ChannelConfig config,String skillId){
        var binding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
        var skill=tree.skill(skillId);if(binding==null||skill==null)return ledger;
        var w=ledger.wallet(binding.currency().id());var gate=ledger.wallet(binding.levelSystem().id());
        var view=view(ledger,tree,config);
        if(!view.joined()||w==null||gate==null)return ledger;
        var before=new Track(true,view.xp(),gate.rank(),w.points(),view.skills(),view.milestones(),view.counters());
        var after=ProgressionMath.buy(tree,before,skillId);if(after==before)return ledger;
        Wallet reward=binding.awardCurrency()==null?null:ledger.wallet(binding.awardCurrency().id());
        if(binding.awardPoints()>0&&(reward==null||reward.points()>Integer.MAX_VALUE-binding.awardPoints()))return ledger;
        var next=ownership(ledger,tree,after).withWallet(binding.currency().id(),w.points(after.points()))
              .receipt(tree.id(),skillId,binding.currency().id(),w.points()-after.points(),binding.permanent());
        if(binding.awardPoints()>0)next=next.withWallet(binding.awardCurrency().id(),reward.points(reward.points()+binding.awardPoints()));
        return next;
    }
    public static Ledger refund(Ledger ledger,Definition.Path tree){
        if(!ledger.get(tree.id()).joined())return ledger;
        var prefix=tree.id()+"|";var next=ledger;
        var permanentRanks=new java.util.HashMap<String,Integer>();
        for(var row:ledger.payments().entrySet())if(row.getKey().startsWith(prefix))for(String receipt:row.getValue()){
            if(receipt.startsWith("!")){permanentRanks.merge(row.getKey().substring(prefix.length()),1,Integer::sum);continue;}
            int sep=receipt.lastIndexOf('|');String id=receipt.substring(0,sep);int amount=Integer.parseInt(receipt.substring(sep+1));
            Wallet w=next.wallet(id);if(w==null||amount<0)throw new IllegalStateException("Invalid purchase receipt");
            next=next.withWallet(id,w.points((int)Math.min(Integer.MAX_VALUE,(long)w.points()+amount)));
        }
        var raw=ledger.get(tree.id());
        return next.with(tree.id(),new Track(true,0,0,0,permanentRanks,raw.milestones(),raw.counters())).clearReceipts(tree.id());
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
