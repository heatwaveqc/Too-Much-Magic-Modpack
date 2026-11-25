package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.data.ChannelConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyConfig;
import io.github.heatwaveqc.tyr.progression.data.ExperienceEconomyRuntime;
import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Ledger;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.*;

/** Immutable, registry-driven skill transactions. No path-specific behavior. */
public final class AdminSkills {
    private AdminSkills() {}
    public record Reset(Ledger ledger, List<String> removed) {}

    public static boolean prerequisites(Definition.Skill skill, Track view) {
        return skill.requiresSkills().stream().allMatch(id -> view.skillRank(id) > 0)
            && (skill.requiresAnySkills().isEmpty()
                || skill.requiresAnySkills().stream().anyMatch(id -> view.skillRank(id) > 0))
            && view.milestones().containsAll(skill.requiresMilestones());
    }

    public static Reset reset(Ledger before, Definition.Path path, ChannelConfig config,
                              String skillId, boolean cascade, boolean forfeit) {
        return reduce(before,path,config,skillId,0,cascade,forfeit);
    }

    /** Keep earliest purchased ranks and release latest commitments in purchase order. */
    public static Reset reduce(Ledger before, Definition.Path path, ChannelConfig config,
                               String skillId,int desired,boolean cascade,boolean forfeit) {
        var oldView = ChannelEngine.view(before,path,config);
        if (!oldView.joined() || desired<0 || oldView.skillRank(skillId)<=desired)
            throw new IllegalArgumentException("Requested rank must be below owned rank on an active path");
        var removed = new ArrayList<String>();
        var next = remove(before,path,config,skillId,desired,forfeit);
        removed.add(skillId);
        // Re-evaluate actual AND/OR prerequisites after each removal, not merely ancestry.
        boolean changed;
        do {
            changed = false;
            var view = ChannelEngine.view(next,path,config);
            for (var skill : path.skills()) {
                if (view.skillRank(skill.id()) > 0 && prerequisites(skill,oldView)
                        && !prerequisites(skill,view)) {
                    if (!cascade) throw new IllegalArgumentException("Dependent skill " + skill.id()
                        + " would become invalid; use cascade");
                    next = remove(next,path,config,skill.id(),0,forfeit);
                    removed.add(skill.id());
                    changed = true;
                    break;
                }
            }
        } while (changed);
        return new Reset(next,List.copyOf(removed));
    }

    private static Ledger remove(Ledger before, Definition.Path path, ChannelConfig config,
                                 String skillId,int desired, boolean forfeit) {
        var catalog = ExperienceEconomyRuntime.current();
        var skill = path.skill(skillId);
        var key = new ChannelConfig.SkillKey(path.id(),skillId);
        var binding = config.skills().get(key);
        if (skill == null || !skill.autoRanks().isEmpty() || config.achievements().containsKey(key)
                || (!catalog.skills().containsKey(new ExperienceEconomyConfig.SkillKey(path.id(),skillId))
                    && (binding==null || !binding.currency().magic()
                        || !catalog.systems().containsKey(binding.currency().id()))))
            throw new IllegalArgumentException("Not a configured currency-purchased skill: " + skillId);
        if (binding != null && binding.awardPoints() > 0)
            throw new IllegalArgumentException("Skill grants native currency; explicit recovery handler required");
        var old = before.economy();
        var retained = new ArrayList<ExperienceEconomy.PaidRank>();
        var removedCosts = new HashMap<String,Long>();
        var expected = new ArrayList<String>();
        int seen=0;
        for (var rank : old.paidRanks()) {
            if (!rank.path().equals(path.id()) || !rank.skill().equals(skillId)) {
                retained.add(rank); continue;
            }
            if (!catalog.systems().containsKey(rank.system()))
                throw new IllegalStateException("Missing currency " + rank.system());
            expected.add(rank.system()+"|"+rank.pointCost());
            if(seen++<desired)retained.add(rank);
            else removedCosts.merge(rank.system(),(long)rank.pointCost(),Math::addExact);
        }
        String receiptKey = path.id()+"|"+skillId;
        var rows = new HashMap<>(before.payments());
        var actual = new ArrayList<String>();
        for (String row : rows.getOrDefault(receiptKey,List.of()))
            actual.add(row.startsWith("!") ? row.substring(1) : row);
        if (expected.isEmpty() || desired>=expected.size() || !actual.equals(expected)
                || expected.size()!=ChannelEngine.view(before,path,config).skillRank(skillId))
            throw new IllegalStateException("Purchase receipts or rank disagree for " + skillId);
        if(desired==0)rows.remove(receiptKey);
        else rows.put(receiptKey,List.copyOf(rows.get(receiptKey).subList(0,desired)));
        var next = new ExperienceEconomy.Ledger(old.currentXp(),retained,old.achievements(),
                old.rewardReceipts(),old.recentTransactions());
        if (forfeit) {
            var xp = new HashMap<>(old.currentXp());
            for (var cost : removedCosts.entrySet()) {
                var spec = catalog.systems().get(cost.getKey());
                xp.put(cost.getKey(),ExperienceEconomy.forfeitFunded(
                    AdminExperience.balance(old,spec),spec,next,cost.getValue()));
            }
            next = new ExperienceEconomy.Ledger(xp,retained,old.achievements(),
                old.rewardReceipts(),old.recentTransactions());
        }
        for (var system : catalog.systems().values()) ExperienceEconomy.available(next,system);
        var raw = before.get(path.id());
        var result = before.withEconomy(next).with(path.id(),raw.withSkill(skillId,desired,raw.points()));
        return new Ledger(result.paths(),result.wallets(),rows,result.economy());
    }
}
