package io.github.heatwaveqc.traders.vengeance.runtime;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.AbstractVillager;

/**
 * Vanilla HurtByTargetGoal, narrowed to actual mob attackers rather than
 * random threats. No general nearest-hostile targeting and no player targeting
 * outside the shared permanent blacklist.
 */
public final class VillagerSelfDefenseGoal extends HurtByTargetGoal {
    private final AbstractVillager villager;

    public VillagerSelfDefenseGoal(AbstractVillager villager) {
        super(villager);
        this.villager = villager;
    }

    @Override
    public boolean canUse() {
        return villager.getLastHurtByMob() instanceof Mob attacker
            && attacker.isAlive()
            && attacker != villager
            && !villager.isAlliedTo(attacker)
            && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return villager.getTarget() instanceof Mob attacker
            && attacker == villager.getLastHurtByMob()
            && attacker.isAlive()
            && !villager.isAlliedTo(attacker)
            && super.canContinueToUse();
    }
}
