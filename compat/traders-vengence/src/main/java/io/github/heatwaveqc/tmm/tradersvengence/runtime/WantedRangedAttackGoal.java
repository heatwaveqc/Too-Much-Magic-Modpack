package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;

/**
 * Wrap vanilla ranged combat; don't let ordinary villager AI fire or chase
 * unblacklisted targets set by unrelated faction/brain systems.
 */
public final class WantedRangedAttackGoal extends RangedAttackGoal {
    private final Mob mob;

    public WantedRangedAttackGoal(RangedAttackMob attacker, double speed, int cooldown, float range) {
        super(attacker, speed, cooldown, range);
        this.mob = (Mob) attacker;
    }

    private boolean hasWantedTarget() {
        return mob.level() instanceof ServerLevel world
            && mob.getTarget() instanceof ServerPlayer player
            && !player.isCreative() && !player.isSpectator()
            && TraderBlacklist.get(world).contains(player.getUUID());
    }

    @Override
    public boolean canUse() {
        return hasWantedTarget() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return hasWantedTarget() && super.canContinueToUse();
    }
}
