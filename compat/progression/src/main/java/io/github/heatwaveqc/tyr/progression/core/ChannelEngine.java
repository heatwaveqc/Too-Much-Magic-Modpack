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
        var projection=config.masteryStages().get(path.id());
        int masteryStage=projection==null?(w==null?0:w.rank()):projection.stage(ledger.economy(),path.id());
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
            int stage=projection!=null&&bind!=null&&bind.levelSystem().id().equals(projection.systemId())
                    ?masteryStage:level==null?0:level.rank();
            for(int at:skill.autoRanks())if(stage>=at)earned++;
            if(earned>0)ranks.put(skill.id(),earned);else ranks.remove(skill.id());
        }
        for(var skill:path.skills()){
            var keys=config.achievements().get(new ChannelConfig.SkillKey(path.id(),skill.id()));
            if(keys==null)continue;
            int earned=0;
            for(String key:keys)
                if(ledger.economy().achievements().contains(key)||raw.milestones().contains(key))
                    earned++;
            if(earned>0)ranks.put(skill.id(),Math.min(earned,skill.maxRank()));
            else ranks.remove(skill.id());
        }
        return new Track(true,w==null?0:w.xp(),masteryStage,w==null?0:w.points(),
                ranks,raw.milestones(),raw.counters());
    }
    public static Ledger initialize(Ledger ledger,XpChannel system){
        if (system.magic()) {
            var spec=ExperienceEconomyRuntime.current().systems().get(system.id());
            if(spec==null)throw new IllegalArgumentException("Unconfigured XP system "+system.id());
            var state=ExperienceEconomy.initialize(ledger.economy(),spec);
            if(state!=ledger.economy()) ledger=ledger.withEconomy(state);
        }
        return ledger.wallet(system.id())==null ? ledger.withWallet(system.id(),Wallet.initial(system)):ledger;
    }
    public static Ledger join(Ledger ledger,Definition.Path path,XpChannel channel) {
        if(ledger.get(path.id()).joined())return ledger;
        // Milestones and deed counters are historical achievements, not
        // purchased rank state. Rejoining must never rearm one-time awards.
        Track previous=ledger.get(path.id());
        ledger=ledger.with(path.id(),new Track(true,0,0,0,previous.skills(),
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
        return ownership(ledger,path,after,ChannelConfig.empty());
    }
    public static Ledger ownership(Ledger ledger,Definition.Path path,Track after,ChannelConfig config){
        var skills=new java.util.HashMap<>(after.skills());
        for(var skill:path.skills())
            if(!skill.autoRanks().isEmpty()
                || config.achievements().containsKey(new ChannelConfig.SkillKey(path.id(),skill.id())))
                skills.remove(skill.id());
        return ledger.with(path.id(),new Track(after.joined(),0,0,0,skills,after.milestones(),after.counters()));
    }
    /** Count current paid ranks in this Path, never projected automatic ranks or available XP. */
    public static long investedRanks(Ledger ledger,String path,ChannelConfig config){
        long count=ledger.economy().paidRanks().stream().filter(r->r.path().equals(path)).count();
        // Native currencies have no economy receipt; their payment receipts are disjoint.
        String prefix=path+"|";
        for(var row:ledger.payments().entrySet())if(row.getKey().startsWith(prefix)){
            for(String receipt:row.getValue()){
                String value=receipt.startsWith("!")?receipt.substring(1):receipt;
                int split=value.lastIndexOf('|');
                if(split<1)continue;
                var currency=config.channels().get(value.substring(0,split));
                if(currency!=null&&!currency.magic()&&Integer.parseInt(value.substring(split+1))>0)count++;
            }
        }
        return count;
    }
    private static long gateInvestment(Ledger ledger,Definition.Path tree,ChannelConfig config,Definition.Skill skill){
        return skill.investedRankRequirements().isEmpty()?0:investedRanks(ledger,tree.id(),config);
    }
    public static Ledger buy(Ledger ledger,Definition.Path tree,ChannelConfig config,String skillId){
        if(config.achievements().containsKey(new ChannelConfig.SkillKey(tree.id(),skillId)))
            return ledger;
        var rule=ExperienceEconomyRuntime.current().skills()
                .get(new ExperienceEconomyConfig.SkillKey(tree.id(),skillId));
        if(rule!=null)return buy(ledger,tree,config,skillId,rule.sources().getFirst().system());
        var binding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
        var skill=tree.skill(skillId);if(binding==null||skill==null)return ledger;
        var w=ledger.wallet(binding.currency().id());var gate=ledger.wallet(binding.levelSystem().id());
        var view=view(ledger,tree,config);
        if(!view.joined()||w==null||gate==null)return ledger;
        var projection=config.masteryStages().get(tree.id());
        int gateRank=projection!=null&&binding.levelSystem().id().equals(projection.systemId())?view.rank():gate.rank();
        var before=new Track(true,view.xp(),gateRank,w.points(),view.skills(),view.milestones(),view.counters());
        // Shipped generic XP economy charges one point per paid rank.
        // Legacy native currencies retain their source-defined cost until migrated.
        int pointCost=binding.currency().magic()?1:skill.price(view.skillRank(skillId));
        var after=ProgressionMath.buy(tree,before,skillId,pointCost,gateInvestment(ledger,tree,config,skill));
        if(after==before)return ledger;
        Wallet reward=binding.awardCurrency()==null?null:ledger.wallet(binding.awardCurrency().id());
        if(binding.awardPoints()>0&&(reward==null||reward.points()>Integer.MAX_VALUE-binding.awardPoints()))return ledger;
        ExperienceEconomy.Ledger xpState=ledger.economy();
        if(binding.currency().magic()) {
            // The canonical current-XP ledger charges by commitment, not by
            // decrementing XP; the legacy view remains a read-only projection.
            var spec=ExperienceEconomyRuntime.current().systems().get(binding.currency().id());
            if(spec==null)throw new IllegalArgumentException("Missing XP buying ledger");
            xpState=ExperienceEconomy.buy(xpState,spec,tree.id(),skillId,
                pointCost,skill.maxRank(),skill.maxRank());
            if(xpState==ledger.economy())return ledger;
        }
        var next=ownership(ledger,tree,after,config)
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
        if(skill==null || !skill.autoRanks().isEmpty()
            || config.achievements().containsKey(new ChannelConfig.SkillKey(tree.id(),skillId))
            || !ledger.get(tree.id()).joined())return ledger;
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
        var projection=config.masteryStages().get(tree.id());
        var levelBinding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
        if(projection!=null&&levelBinding!=null&&levelBinding.levelSystem().id().equals(projection.systemId()))rank=view.rank();
        Track candidate=new Track(true,view.xp(),rank,Integer.MAX_VALUE,
                view.skills(),view.milestones(),view.counters());
        Track nextRank=ProgressionMath.buy(tree,candidate,skillId,source.pointsPerRank(),gateInvestment(ledger,tree,config,skill));
        if(nextRank==candidate)return ledger;
        var paid=ExperienceEconomy.buy(ledger.economy(),system,tree.id(),skillId,
                source.pointsPerRank(),source.maxRanks(),cap);
        if(paid==ledger.economy())return ledger;
        var binding=config.skills().get(new ChannelConfig.SkillKey(tree.id(),skillId));
        boolean permanent=binding!=null && binding.permanent();
        return ownership(ledger,tree,nextRank,config).withEconomy(paid)
                .receipt(tree.id(),skillId,systemId,source.pointsPerRank(),permanent);
    }

    /**
     * Configurable respec: refund, retain, or sacrifice investment in each
     * currency. No separate level counter changes; XP itself only decreases
     * when a selected policy expressly forfeits its committed value.
     * The legacy !receipt exception remains nonrefundable by default.
     */
    public static Ledger refund(Ledger ledger,Definition.Path tree){
        if(!ledger.get(tree.id()).joined())return ledger;
        var catalog=ExperienceEconomyRuntime.current();
        String prefix=tree.id()+"|";
        var paymentRows=new HashMap<>(ledger.payments());
        var retainedSkills=new HashMap<String,Integer>();
        var permanentBudget=new HashMap<String,Integer>();
        var burned=new HashMap<String,Long>();
        var nativeRefunds=new HashMap<String,Long>();

        for(var entry:ledger.payments().entrySet()){
            if(!entry.getKey().startsWith(prefix))continue;
            String skill=entry.getKey().substring(prefix.length());
            var kept=new ArrayList<String>();
            for(String receipt:entry.getValue()){
                boolean permanent=receipt.startsWith("!");
                String text=permanent?receipt.substring(1):receipt;
                int separator=text.lastIndexOf('|');
                if(separator<1)throw new IllegalStateException("Invalid respec receipt");
                String currency=text.substring(0,separator);
                int paid=Integer.parseInt(text.substring(separator+1));
                if(paid<0)throw new IllegalStateException("Negative point payment");
                var policy=catalog.policy(tree.id(),currency).onRespec();
                boolean keep=policy==ExperienceEconomy.DeparturePolicy.RETAIN
                        ||permanent&&policy==ExperienceEconomy.DeparturePolicy.REFUND;
                if(keep){
                    kept.add(receipt);
                    retainedSkills.merge(skill,1,Integer::sum);
                    if(catalog.systems().containsKey(currency))
                        permanentBudget.merge(skill+"|"+currency,1,Integer::sum);
                }else if(policy==ExperienceEconomy.DeparturePolicy.FORFEIT
                         &&catalog.systems().containsKey(currency)){
                    burned.merge(currency,(long)paid,Math::addExact);
                }else if(policy==ExperienceEconomy.DeparturePolicy.REFUND
                         &&!catalog.systems().containsKey(currency)){
                    nativeRefunds.merge(currency,(long)paid,Math::addExact);
                }
            }
            if(kept.isEmpty())paymentRows.remove(entry.getKey());
            else paymentRows.put(entry.getKey(),kept);
        }

        var original=ledger.economy();
        var retained=new ArrayList<ExperienceEconomy.PaidRank>();
        for(var rank:original.paidRanks()){
            if(!rank.path().equals(tree.id())){
                retained.add(rank);continue;
            }
            String key=rank.skill()+"|"+rank.system();
            int count=permanentBudget.getOrDefault(key,0);
            if(count>0){
                retained.add(rank);
                permanentBudget.put(key,count-1);
            }
        }
        var xp=new HashMap<>(original.currentXp());
        for(var entry:burned.entrySet()){
            var spec=catalog.systems().get(entry.getKey());
            var current=xp.get(entry.getKey());
            if(spec==null||current==null)throw new IllegalStateException("Missing respec XP system");
            var after=current.subtract(spec.xpPerPoint().multiply(BigDecimal.valueOf(entry.getValue())));
            if(after.signum()<0)throw new IllegalStateException("Respec forfeiture exceeds earned XP");
            xp.put(entry.getKey(),after);
        }
        var economy=new ExperienceEconomy.Ledger(xp,retained,
                original.achievements(),original.rewardReceipts(),original.recentTransactions());
        for(var spec:catalog.systems().values())ExperienceEconomy.available(economy,spec);
        var next=ledger.withEconomy(economy);
        for(var entry:nativeRefunds.entrySet()){
            Wallet wallet=next.wallet(entry.getKey());
            if(wallet==null)throw new IllegalStateException("Missing native respec wallet "+entry.getKey());
            next=next.withWallet(entry.getKey(),wallet.points(
                (int)Math.min(Integer.MAX_VALUE,(long)wallet.points()+entry.getValue())));
        }
        next=new Ledger(next.paths(),next.wallets(),paymentRows,next.economy());
        var raw=ledger.get(tree.id());
        return next.with(tree.id(),new Track(true,0,0,0,retainedSkills,
                raw.milestones(),raw.counters()));
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
        if(!channel.magic())throw new IllegalArgumentException("Not a configured XP-ledger source");
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
    /**
     * Complete a real Path departure, including receipts and native wallet
     * refunds, after the canonical per-system XP transaction succeeds.
     * RETAIN keeps ownership but the Path's effects remain disabled until join.
     */
    public static Ledger depart(Ledger source,Definition.Path tree,
                                ExperienceEconomyConfig.Catalog catalog) {
        Track original=source.get(tree.id());
        if(!original.joined())return source;
        Ledger next=pathChange(source,Set.of(tree.id()),catalog);
        var payments=new HashMap<>(next.payments());
        var retainedSkills=new HashMap<String,Integer>();
        String prefix=tree.id()+"|";
        for(var entry:source.payments().entrySet()){
            if(!entry.getKey().startsWith(prefix))continue;
            String skill=entry.getKey().substring(prefix.length());
            var kept=new ArrayList<String>();
            for(String receipt:entry.getValue()){
                boolean permanent=receipt.startsWith("!");
                String text=permanent?receipt.substring(1):receipt;
                int divider=text.lastIndexOf('|');
                if(divider<1)throw new IllegalStateException("Malformed skill payment receipt");
                String id=text.substring(0,divider);
                int points=Integer.parseInt(text.substring(divider+1));
                if(points<0)throw new IllegalStateException("Negative skill payment");
                var policy=catalog.policy(tree.id(),id).onLeave();
                if(policy==ExperienceEconomy.DeparturePolicy.RETAIN){
                    kept.add(receipt);
                    retainedSkills.merge(skill,1,Integer::sum);
                } else if(policy==ExperienceEconomy.DeparturePolicy.REFUND
                          && !catalog.systems().containsKey(id)){
                    // Generic XP point commitments are released in pathChange;
                    // native legacy wallets still need their own refund.
                    Wallet wallet=next.wallet(id);
                    if(wallet==null)throw new IllegalStateException("Missing native refund wallet "+id);
                    next=next.withWallet(id,wallet.points(
                        (int)Math.min(Integer.MAX_VALUE,(long)wallet.points()+points)));
                }
            }
            if(kept.isEmpty())payments.remove(entry.getKey());
            else payments.put(entry.getKey(),kept);
        }
        next=new Ledger(next.paths(),next.wallets(),payments,next.economy());
        Track dormant=new Track(false,0,0,0,retainedSkills,
                original.milestones(),original.counters());
        return next.with(tree.id(),dormant);
    }

    private static void checkId(String key) {
        if(key==null || key.length()>240 || !key.matches("[a-zA-Z0-9_.:/#@-]+"))
            throw new IllegalArgumentException("Invalid transaction ID");
    }
}
