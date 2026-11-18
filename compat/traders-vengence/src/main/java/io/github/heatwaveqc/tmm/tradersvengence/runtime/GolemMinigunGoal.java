package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import java.util.EnumSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;

/**
 * NPC-only combat adapter. Reuses the existing/native target, never scans for targets.
 * The spin state is adapted from the donor; damage uses the game's native mob attack.
 * Conservative provisional game-balance constants, pending owner/playtest tuning.
 */
public final class GolemMinigunGoal extends Goal {
    public static final float RANGE = 16F;
    public static final float DAMAGE = 5F;
    public static final int SHOT_INTERVAL = 10;
    private final IronGolem golem;
    private final MinigunSpin spin = new MinigunSpin();
    private boolean running;
    private int cooldown;

    public GolemMinigunGoal(IronGolem golem) {
        this.golem = golem;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean allowed(LivingEntity target) {
        if (!GolemMinigunEvents.armed(golem) || !(golem.level() instanceof ServerLevel world)
                || target == null || !target.isAlive() || target.level() != world
                || target == golem || golem.isAlliedTo(target) || !golem.canAttack(target)
                || golem.distanceToSqr(target) > RANGE * RANGE
                || !golem.getSensing().hasLineOfSight(target)) return false;
        if (target instanceof Player) {
            return target instanceof ServerPlayer player && !player.isCreative()
                && !player.isSpectator() && TraderBlacklist.get(world).contains(player.getUUID());
        }
        return true;
    }

    @Override public boolean canUse() { return allowed(golem.getTarget()); }
    @Override public boolean canContinueToUse() { return allowed(golem.getTarget()); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void start() { running = true; }
    @Override public void stop() { running = false; }

    @Override public void tick() {
        LivingEntity target = golem.getTarget();
        if (!allowed(target)) { running = false; return; }
        running = true;
        golem.getNavigation().stop();
        golem.getLookControl().setLookAt(target, 30F, 30F);
        if (spin.ready() && cooldown == 0) {
            // No player-obtainable equipment, projectiles or ammunition.
            // Recheck targeting/line of sight every shot, including after a pardon.
            target.hurt(golem.damageSources().mobAttack(golem), DAMAGE);
            cooldown = SHOT_INTERVAL;
        }
    }
    void updateSpin() {
        spin.tick(running && allowed(golem.getTarget()));
        if (cooldown > 0) cooldown--;
    }
    float speed() { return spin.speed(); }
    float angle() { return spin.angle(); }
}
