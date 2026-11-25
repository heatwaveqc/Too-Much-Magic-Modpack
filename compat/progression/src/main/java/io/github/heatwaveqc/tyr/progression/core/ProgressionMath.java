package io.github.heatwaveqc.tyr.progression.core;

import io.github.heatwaveqc.tyr.progression.model.Definition;
import io.github.heatwaveqc.tyr.progression.state.Track;
import java.util.List;
import java.util.Map;

/** Side-effect-free progression math: can be unit-tested without starting a world. */
public final class ProgressionMath {
    private ProgressionMath() {}

    public static Track join(Definition.Path spec, Track old) {
        return old.joined() ? old : applyAutomatic(spec, old.joinedAtRankOne(spec.pointsPerRank()));
    }

    public static Track grantXp(Definition.Path spec, Track old, long award) {
        if (!old.joined() || award <= 0) return old;
        long xp = old.xp() > Long.MAX_VALUE - award ? Long.MAX_VALUE : old.xp() + award;
        int rank = old.rank();
        int points = old.points();
        List<Long> thresholds = spec.rankXp();
        while (rank < thresholds.size() && xp >= thresholds.get(rank)) {
            rank++;
            points = (int) Math.min(Integer.MAX_VALUE, (long) points + spec.pointsPerRank());
        }
        return applyAutomatic(spec, old.progress(xp, rank, points));
    }

    public static Track buy(Definition.Path spec, Track old, String skillId) {
        var skill=spec.skill(skillId);
        return skill==null?old:buy(spec,old,skillId,skill.price(old.skillRank(skillId)));
    }

    /** Same prerequisite/choice evaluation with a currency-configured point price. */
    public static Track buy(Definition.Path spec, Track old, String skillId, int pointCost) {
        return buy(spec,old,skillId,pointCost,0);
    }

    /** Invested rank count is derived from authoritative purchase receipts by the caller. */
    public static Track buy(Definition.Path spec, Track old, String skillId, int pointCost,long investedRanks) {
        Definition.Skill skill = spec.skill(skillId);
        if (!old.joined() || skill == null || pointCost < 0) return old;
        int owned = old.skillRank(skillId);
        if(old.rank()<skill.nextRequiredRank(owned)
            || investedRanks<skill.nextInvestedRequirement(owned))return old;
        if (!skill.autoRanks().isEmpty()) return old; // baseline grants are not purchased
        if (owned >= skill.maxRank() || old.points() < pointCost) return old;
        if (owned == 0 && !skill.choiceGroup().isEmpty()) {
            for (Definition.Skill other : spec.skills()) {
                if (!other.id().equals(skill.id()) &&
                    skill.choiceGroup().equals(other.choiceGroup()) && old.skillRank(other.id()) > 0)
                    return old;
            }
        }
        for (String required : skill.requiresSkills()) {
            if (old.skillRank(required) < 1) return old;
        }
        if (!skill.requiresAnySkills().isEmpty() &&
            skill.requiresAnySkills().stream().noneMatch(needed -> old.skillRank(needed) > 0)) return old;
        if (!old.milestones().containsAll(skill.requiresMilestones())) return old;
        return old.withSkill(skillId, owned + 1, old.points() - pointCost);
    }

    public static Track refundAll(Definition.Path spec, Track old) {
        long refund = 0;
        for (Definition.Skill skill : spec.skills()) {
            int owned = skill.autoRanks().isEmpty() ? old.skillRank(skill.id()) : 0;
            for (int n = 0; n < owned; n++) refund += skill.price(n);
        }
        return applyAutomatic(spec, new Track(old.joined(), old.xp(), old.rank(),
            (int) Math.min(Integer.MAX_VALUE, (long) old.points() + refund),
            Map.of(), old.milestones(), old.counters()));
    }

    public static Track applyAutomatic(Definition.Path spec, Track state) {
        if (!state.joined()) return state;
        Track result = state;
        for (Definition.Skill skill : spec.skills()) {
            if (skill.autoRanks().isEmpty()) continue;
            int earned = 0;
            for (int at : skill.autoRanks()) if (state.rank() >= at) earned++;
            if (result.skillRank(skill.id()) != earned)
                result = result.withSkill(skill.id(), earned, result.points());
        }
        return result;
    }

    /** Legacy milestones can pre-count. Strict milestones only collect once prerequisites
     * were completed at the start of this authoritative signal. */
    public static boolean canCollectMilestone(Definition.Milestone milestone,Track initial) {
        return !milestone.strictOrder() || initial.milestones().containsAll(milestone.after());
    }

    public static Track collectMilestoneCriteria(Definition.Path path,Track initial,Signal signal) {
        if(!initial.joined())return initial;
        Track next=initial;
        for(var milestone:path.milestones()){
            if(initial.completed(milestone.id())||!canCollectMilestone(milestone,initial))continue;
            for(int i=0;i<milestone.criteria().size();i++){
                var criterion=milestone.criteria().get(i);
                String key=counterKey(milestone.id(),i);
                if(signal.matches(criterion.event(),criterion.selector())
                    &&next.criterionCount(key)<criterion.count())
                    next=recordCriterion(next,key,1);
            }
        }
        return next;
    }

    /** Persistent, namespaced criterion counters. */
    public static String counterKey(String milestone, int criterion) {
        return milestone + "/" + criterion;
    }

    public static Track recordCriterion(Track old, String key, int amount) {
        if (!old.joined() || amount <= 0) return old;
        int n = old.criterionCount(key);
        return old.withCounter(key, (int) Math.min(Integer.MAX_VALUE, (long) n + amount));
    }
}
