package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.model.XpChannel;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import io.github.heatwaveqc.tyr.progression.state.Wallet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import io.github.heatwaveqc.tyr.progression.data.ChannelConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import java.util.List;

/** Shared channel transactions; tree-specific purchased ranks and milestones remain separate. */
public final class ChannelEngine {
    private ChannelEngine() {}
    public static Track view(Ledger ledger,Definition.Path path,ChannelConfig config) {
        Track raw=ledger.get(path.id());if(!raw.joined())return raw;
        var def=config.paths().get(path.id());if(def==null)return Track.empty();
        Wallet w=ledger.wallet(def.id());
        var ranks=new java.util.HashMap<>(raw.skills());
        var catalog=ExperienceEconomyRuntime.current();
        // Effective paid rank is the sum of separately purchased contributions.
        // The contribution ledger, not a second Track.rank counter, owns it.
        for(var skill:path.skills()){
            if(catalog.skills().containsKey(new ExperienceEconomyConfig.SkillKey(path.id(),skill.id()))){
                int bought=ExperienceEconomy.effectiveRank(ledger.economy(),path.id(),skill.id());
                if(bought==0)ranks.remove(skill.id());
                else ranks.put(skill.id(),bought);
            }
        }
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
        if (system.magic()) {
            var state=ExperienceEconomy.initialize(ledger.economy(),ExperienceEconomy.SystemSpec.mxp());
            if(state!=ledger.economy()) ledger=ledger.withEconomy(state);
        }
        return ledger.wallet(system.id())==null ? ledger.withWallet(system.id(),Wallet.initial(system)):ledger;
    }
    public static Ledger join(Ledger ledger,Definition.Path path,XpChannel channel) {
        if(ledger.get(path.id()).joined())return ledger;
        // Milestones and deed counters are historical achievements, not
        // purchased rank state. Rejoining must never rearm one-time awards.
        Track previous=ledger.get(path.id());
        ledger=ledger.with(path.id(),new Track(true,0,0,0,Map.of(),
                previous.milestones(),previous.counters()));
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
        var rule=ExperienceEconomyRuntime.current().skills()
                .get(new ExperienceEconomyConfig.SkillKey(tree.id(),skillId));
        if(rule!=null)return buy(ledger,tree,config,skillId,rule.sources().getFirst().system());
        var binding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
        var skill=tree.skill(skillId);if(binding==null||skill==null)return ledger;
        var w=ledger.wallet(binding.currency().id());var gate=ledger.wallet(binding.levelSystem().id());
        var view=view(ledger,tree,config);
        if(!view.joined()||w==null||gate==null)return ledger;
        var before=new Track(true,view.xp(),gate.rank(),w.points(),view.skills(),view.milestones(),view.counters());
        var after=ProgressionMath.buy(tree,before,skillId);if(after==before)return ledger;
        Wallet reward=binding.awardCurrency()==null?null:ledger.wallet(binding.awardCurrency().id());
        if(binding.awardPoints()>0&&(reward==null||reward.points()>Integer.MAX_VALUE-binding.awardPoints()))return ledger;
        ExperienceEconomy.Ledger xpState=ledger.economy();
        if(binding.currency().magic()) {
            // The canonical current-XP ledger charges by commitment, not by
            // decrementing XP; the legacy view remains a read-only projection.
            xpState=ExperienceEconomy.buy(xpState,ExperienceEconomy.SystemSpec.mxp(),
                tree.id(),skillId,skill.price(view.skillRank(skillId)),skill.maxRank(),skill.maxRank());
            if(xpState==ledger.economy())return ledger;
        }
        var next=ownership(ledger,tree,after)
              .withWallet(binding.currency().id(),w.points(after.points()))
              .withEconomy(xpState)
              .receipt(tree.id(),skillId,binding.currency().id(),w.points()-after.points(),binding.permanent());
        if(binding.awardPoints()>0)next=next.withWallet(binding.awardCurrency().id(),reward.points(reward.points()+binding.awardPoints()));
        return next;
    }
    /**
     * Currency-selected purchase for a declarative multi-system skill.
     * The same prerequisites and mutual exclusion rules are checked once
     * against the aggregated effective rank; only the chosen ledger is charged.
     */
    public static Ledger buy(Ledger ledger,Definition.Path tree,ChannelConfig config,
                             String skillId,String systemId) {
        var catalog=ExperienceEconomyRuntime.current();
        var key=new ExperienceEconomyConfig.SkillKey(tree.id(),skillId);
        var rules=catalog.skills().get(key);
        if(rules==null) {
            var binding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
            if(binding==null || !binding.currency().id().equals(systemId))return ledger;
            return buy(ledger,tree,config,skillId);
        }
        var skill=tree.skill(skillId);
        if(skill==null || !skill.autoRanks().isEmpty() || !ledger.get(tree.id()).joined())return ledger;
        ExperienceEconomyConfig.PurchaseSource source=null;
        for(var candidate:rules.sources())if(candidate.system().equals(systemId)){
            source=candidate;break;
        }
        var system=catalog.systems().get(systemId);
        if(source==null||system==null)return ledger;
        Track view=view(ledger,tree,config);
        int owned=view.skillRank(skillId);
        int cap=Math.min(skill.maxRank(),rules.totalRankCap());
        if(owned>=cap || ExperienceEconomy.available(ledger.economy(),system)<source.pointsPerRank())
            return ledger;
        // ProgressionMath remains the single authoritative skill prerequisite
        // and choice-group evaluator. Purchasing cost comes from source config.
        long displayLevel=ExperienceEconomy.level(ledger.economy(),system)+1L;
        int rank=(int)Math.min(Integer.MAX_VALUE,displayLevel);
        Track candidate=new Track(true,view.xp(),rank,Integer.MAX_VALUE,
                view.skills(),view.milestones(),view.counters());
        Track nextRank=ProgressionMath.buy(tree,candidate,skillId);
        if(nextRank==candidate)return ledger;
        var paid=ExperienceEconomy.buy(ledger.economy(),system,tree.id(),skillId,
                source.pointsPerRank(),source.maxRanks(),cap);
        if(paid==ledger.economy())return ledger;
        var binding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
        boolean permanent=binding!=null && binding.permanent();
        return ownership(ledger,tree,nextRank).withEconomy(paid)
                .receipt(tree.id(),skillId,systemId,source.pointsPerRank(),permanent);
    }

    public static Ledger refund(Ledger ledger,Definition.Path tree){
        if(!ledger.get(tree.id()).joined())return ledger;
        var prefix=tree.id()+"|";var next=ledger;
        var permanentRanks=new java.util.HashMap<String,Integer>();
        // Respec refunds purchased ranks to their original XP system while
        // retaining explicitly irreversible grants. XP itself never increases.
        var retained=new ArrayList<ExperienceEconomy.PaidRank>();
        var permanentBudget=new HashMap<String,Integer>();
        for(var row:ledger.payments().entrySet()) if(row.getKey().startsWith(prefix)) {
            String skill=row.getKey().substring(prefix.length());
            for(String receipt:row.getValue()) if(receipt.startsWith("!")) {
                int end=receipt.lastIndexOf('|');
                if(end<=1)throw new IllegalStateException("Invalid permanent purchase receipt");
                permanentBudget.merge(skill+"|"+receipt.substring(1,end),1,Integer::sum);
            }
        }
        for(var rank:ledger.economy().paidRanks()) {
            if(!rank.path().equals(tree.id())){
                retained.add(rank);continue;
            }
            String key=rank.skill()+"|"+rank.system();
            int budget=permanentBudget.getOrDefault(key,0);
            if(budget>0) {
                retained.add(rank);
                permanentBudget.put(key,budget-1);
            }
        }
        var previous=ledger.economy();
        next=next.withEconomy(new ExperienceEconomy.Ledger(previous.currentXp(),retained,
            previous.achievements(),previous.rewardReceipts(),previous.recentTransactions()));
        for(var row:ledger.payments().entrySet())if(row.getKey().startsWith(prefix))for(String receipt:row.getValue()){
            if(receipt.startsWith("!")){permanentRanks.merge(row.getKey().substring(prefix.length()),1,Integer::sum);continue;}
            int sep=receipt.lastIndexOf('|');String id=receipt.substring(0,sep);int amount=Integer.parseInt(receipt.substring(sep+1));
            Wallet w=next.wallet(id);if(w==null||amount<0)throw new IllegalStateException("Invalid purchase receipt");
            if(!XpChannel.MXP.equals(id))
                next=next.withWallet(id,w.points((int)Math.min(Integer.MAX_VALUE,(long)w.points()+amount)));
        }
        var raw=ledger.get(tree.id());
        return next.with(tree.id(),new Track(true,0,0,0,permanentRanks,raw.milestones(),raw.counters())).clearReceipts(tree.id());
    }
    public static Ledger magic(Ledger ledger,XpChannel channel,String tx,String firstKey,
            double firstBase,double repeatBase,boolean firstFixed,boolean repeatFixed) {
        return magic(ledger,channel,"tyr_progression:default",tx,firstKey,
                firstBase,repeatBase,firstFixed,repeatFixed);
    }

    /** One verified category may credit one player once per committed operation. */
    public static Ledger magic(Ledger ledger,XpChannel channel,String sourceCategory,
            String tx,String firstKey,double firstBase,double repeatBase,
            boolean firstFixed,boolean repeatFixed) {
        if(!channel.magic())throw new IllegalArgumentException("Not a configurable MXP source");
        checkId(tx);if(firstKey!=null)checkId(firstKey);
        ledger=initialize(ledger,channel);
        String firstReceipt=firstKey==null?null:channel.id()+"|"+sourceCategory+"|"+firstKey;
        var state=ledger.economy();
        boolean first=firstReceipt!=null&&!state.rewardReceipts().contains(firstReceipt);
        if(firstReceipt!=null&&!first&&repeatBase==0)return ledger;
        double base=first?firstBase:repeatBase;
        var spec=ExperienceEconomyRuntime.current().systems().get(channel.id());
        if(spec==null)throw new IllegalArgumentException("Unconfigured XP system "+channel.id());
        var result=ExperienceEconomy.earn(state,spec,
                sourceCategory,tx,first?firstKey:null,BigDecimal.valueOf(base),
                first?firstFixed:repeatFixed,channel.growth());
        return result.accepted()?ledger.withEconomy(result.ledger()):ledger;
    }

    /** Verified server-origin source grant for any declared XP system. */
    public static Ledger awardConfigured(Ledger ledger,ExperienceEconomyConfig.Catalog catalog,
                                          String systemId,String sourceCategory,String transaction,
                                          String firstReceipt,BigDecimal nominalBase,boolean fixed,
                                          double effectiveGrowth) {
        var spec=catalog.systems().get(systemId);
        if(spec==null)return ledger;
        var result=ExperienceEconomy.earn(ledger.economy(),spec,sourceCategory,transaction,
                firstReceipt,nominalBase,fixed,effectiveGrowth);
        return result.accepted()?ledger.withEconomy(result.ledger()):ledger;
    }

    /** Owner's TMM default: actual membership change forfeits free MXP and
     * all departed Path purchases while preserving Core/other active Paths and
     * permanent achievement/first-completion receipts. */
    public static Ledger pathChange(Ledger ledger,Set<String> departed){
        return pathChange(ledger,departed,ExperienceEconomyRuntime.current());
    }

    /** One transaction over all configured currencies, not an MXP-only reset. */
    public static Ledger pathChange(Ledger ledger,Set<String> departed,
                                    ExperienceEconomyConfig.Catalog catalog){
        var old=ledger.economy();
        if(old.currentXp().isEmpty())return ledger;
        var overrides=new HashMap<ExperienceEconomy.PathSystem,ExperienceEconomy.DeparturePolicy>();
        var wipe=new HashSet<String>();
        for(var system:catalog.systems().keySet()){
            if(departed.isEmpty()){
                if(catalog.defaultTransition().wipeUnallocated())wipe.add(system);
                continue;
            }
            for(var path:departed){
                var policy=catalog.policy(path,system);
                overrides.put(new ExperienceEconomy.PathSystem(path,system),policy.onLeave());
                if(policy.wipeUnallocated())wipe.add(system);
            }
        }
        var after=ExperienceEconomy.transition(old,catalog.systems(),departed,
            overrides,catalog.defaultTransition().onLeave(),wipe,true);
        return ledger.withEconomy(after);
    }
    private static void checkId(String key) {
        if(key==null || key.length()>240 || !key.matches("[a-zA-Z0-9_.:/#@-]+"))
            throw new IllegalArgumentException("Invalid transaction ID");
    }
}
