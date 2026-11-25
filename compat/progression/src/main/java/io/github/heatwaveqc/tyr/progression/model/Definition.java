package io.github.heatwaveqc.tyr.progression.model;

import java.util.List;
import java.util.Map;

/** Data-only schema. Implementations are shared by Monk, Hunter and future paths. */
public final class Definition {
    private Definition() {}

    public record Path(String id, String exclusiveGroup, List<Long> rankXp,
                       int pointsPerRank, List<Activity> activities,
                       List<Milestone> milestones, List<Skill> skills) {
        public Path {
            rankXp = List.copyOf(rankXp);
            activities = List.copyOf(activities);
            milestones = List.copyOf(milestones);
            skills = List.copyOf(skills);
        }
        public int maxRank() { return rankXp.size(); }
        public Skill skill(String id) {
            return skills.stream().filter(skill -> skill.id().equals(id)).findFirst().orElse(null);
        }
    }
    /** event: kill, block_break, external; selector: namespaced ID or #tag. */
    public record Activity(String event, String selector, long xp, int cooldownTicks) {}
    public record Criterion(String event, String selector, int count) {}
    public record Milestone(String id, List<Criterion> criteria, List<String> after,
                             long xp, int points, String title, boolean notifyProgress,
                             String fixedRewardSystem, long fixedRewardXp) {
        public Milestone(String id,List<Criterion> criteria,List<String> after,long xp,int points) {
            this(id,criteria,after,xp,points,"",false,"",0);
        }
        public Milestone(String id,List<Criterion> criteria,List<String> after,long xp,int points,
                         String title,boolean notifyProgress) {
            this(id,criteria,after,xp,points,title,notifyProgress,"",0);
        }
        public Milestone {
            criteria=List.copyOf(criteria);
            after=List.copyOf(after);
            if(title==null||title.length()>120||title.chars().anyMatch(Character::isISOControl)
                    ||fixedRewardSystem==null||fixedRewardXp<0||fixedRewardXp>1000000
                    ||fixedRewardXp>0&&!fixedRewardSystem.matches("[a-z0-9_.-]+:[a-z0-9_./-]+")
                    ||fixedRewardXp==0&&!fixedRewardSystem.isBlank()
                    ||fixedRewardXp>0&&(xp!=0||points!=0))
                throw new IllegalArgumentException("Invalid milestone reward/label");
        }
    }
    /** Costs increase by stepCost for each purchased rank. */
    public record Skill(String id, int maxRank, int requiredRank, int baseCost, int stepCost,
                        List<String> requiresSkills, List<String> requiresAnySkills,
                        List<String> requiresMilestones, String choiceGroup,
                        List<Integer> autoRanks, List<Effect> effects, List<Integer> rankCosts,List<Integer> rankRequirements,
                        List<Integer> investedRankRequirements) {
        public Skill(String id,int maxRank,int requiredRank,int baseCost,int stepCost,
                     List<String> requiresSkills,List<String> requiresAnySkills,List<String> requiresMilestones,
                     String choiceGroup,List<Integer> autoRanks,List<Effect> effects,
                     List<Integer> rankCosts,List<Integer> rankRequirements){
            this(id,maxRank,requiredRank,baseCost,stepCost,requiresSkills,requiresAnySkills,
                requiresMilestones,choiceGroup,autoRanks,effects,rankCosts,rankRequirements,List.of());
        }
        public Skill(String id,int maxRank,int requiredRank,int baseCost,int stepCost,
                     List<String> requiresSkills,List<String> requiresAnySkills,List<String> requiresMilestones,
                     String choiceGroup,List<Integer> autoRanks,List<Effect> effects){
            this(id,maxRank,requiredRank,baseCost,stepCost,requiresSkills,requiresAnySkills,requiresMilestones,choiceGroup,autoRanks,effects,List.of(),List.of());
        }
        public Skill {
            requiresSkills = List.copyOf(requiresSkills);
            requiresAnySkills = List.copyOf(requiresAnySkills);
            requiresMilestones = List.copyOf(requiresMilestones);
            autoRanks = List.copyOf(autoRanks);
            effects = List.copyOf(effects);
            rankCosts=List.copyOf(rankCosts);rankRequirements=List.copyOf(rankRequirements);
            investedRankRequirements=List.copyOf(investedRankRequirements);
            if(!investedRankRequirements.isEmpty()&&(investedRankRequirements.size()!=maxRank
                || !autoRanks.isEmpty() || investedRankRequirements.stream().anyMatch(n->n<0)))
                throw new IllegalArgumentException("Invalid rank_invested_requirements");
            for(int i=1;i<investedRankRequirements.size();i++)
                if(investedRankRequirements.get(i)<investedRankRequirements.get(i-1))
                    throw new IllegalArgumentException("Invested rank gates must not decrease");
            if(!rankRequirements.isEmpty()&&(rankRequirements.size()!=maxRank||rankRequirements.stream().anyMatch(n->n<1)))throw new IllegalArgumentException("Invalid rank_level_requirements");
            if(!rankCosts.isEmpty()&&(rankCosts.size()!=maxRank||rankCosts.stream().anyMatch(n->n<0)))throw new IllegalArgumentException("Invalid rank_costs");
        }
        public int nextRequiredRank(int owned){return rankRequirements.isEmpty()?requiredRank:rankRequirements.get(Math.max(0,Math.min(owned,rankRequirements.size()-1)));}
        public int nextInvestedRequirement(int owned){
            return investedRankRequirements.isEmpty()?0:investedRankRequirements.get(Math.max(0,Math.min(owned,investedRankRequirements.size()-1)));
        }
        public int price(int owned) {
            if(!rankCosts.isEmpty())return rankCosts.get(Math.max(0,Math.min(owned,rankCosts.size()-1)));
            return (int) Math.min(Integer.MAX_VALUE, (long) baseCost + (long) stepCost * owned);
        }
    }
    /** attribute, melee_bonus, damage_multiplier, break_speed, harvest_tier. */
    public record Effect(String type, String attribute, String operation,
                         String selector, String weapon, String damageTag,
                         double amountPerRank, int tier, boolean emptyHand, boolean unarmored,
                         Map<String,String> params,List<Double> rankValues) {
        public Effect(String type,String attribute,String operation,String selector,String weapon,String damageTag,
                      double amountPerRank,int tier,boolean emptyHand,boolean unarmored,Map<String,String> params){
            this(type,attribute,operation,selector,weapon,damageTag,amountPerRank,tier,emptyHand,unarmored,params,List.of());
        }
        public Effect { params=Map.copyOf(params);rankValues=List.copyOf(rankValues); }
        /** rank_values are cumulative outputs, not successive increments. Existing handlers scale once. */
        public Effect atRank(int rank){
            if(rankValues.isEmpty()||rank<1)return this;
            return new Effect(type,attribute,operation,selector,weapon,damageTag,
                rankValues.get(Math.min(rank,rankValues.size())-1)/rank,tier,emptyHand,unarmored,params);
        }
    }
}
