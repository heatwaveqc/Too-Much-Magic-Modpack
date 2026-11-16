package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;

/**
 * Reuses vanilla ranged combat, but allows only:
 *   (1) blacklisted survival-mode players; or
 *   (2) the specific mob that injured this villager.
 *
 * Critically, villagers must not start hunting every nearby hostile mob.
 * Llamas remain player-vendetta-only and use their vanilla ranged attack.
 */
public final class WantedRangedAttackGoal extends RangedAttackGoal {
    private final Mob mob;

    public WantedRangedAttackGoal(RangedAttackMob attacker, double speed, int cooldown, float range) {
        super(attacker, speed, cooldown, range);
        this.mob = (Mob) attacker;
    }

    public static boolean mayRetaliate(Mob defender, LivingEntity target) {
        if (defender.level() instanceof ServerLevel world
            && target instanceof ServerPlayer player
            && !player.isCreative() && !player.isSpectator()
            && TraderBlacklist.get(world).contains(player.getUUID())) {
            return true;
        }
        return defender instanceof Villager
            && target instanceof Mob attacker
            && attacker.isAlive()
            && attacker != defender
            && !defender.isAlliedTo(attacker)
            && defender.getLastHurtByMob() == attacker;
    }

    private boolean hasWantedTarget() {
        return mayRetaliate(mob, mob.getTarget());
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
