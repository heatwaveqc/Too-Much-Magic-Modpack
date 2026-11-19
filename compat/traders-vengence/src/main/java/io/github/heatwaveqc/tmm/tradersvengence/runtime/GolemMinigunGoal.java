package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.state.TraderBlacklist;
import java.util.EnumSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;

/**
 * NPC-only combat adapter. Reuses the existing/native target, never scans for targets.
 * Uses unupgraded PneumaticCraft standard ammo base range and damage, and
 * one firing attempt per tick after spin-up. Vanilla hurt resistance applies.
 */
public final class GolemMinigunGoal extends Goal {
    private static final ResourceKey<DamageType> MINIGUN_DAMAGE = ResourceKey.create(
        Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("traders_vengence", "minigun"));
    private final IronGolem golem;
    private final MinigunSpin spin = new MinigunSpin();
    private boolean running;
    private boolean firing;
    private int cooldown;

    public GolemMinigunGoal(IronGolem golem) {
        this.golem = golem;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean allowed(LivingEntity target) {
        if (!GolemMinigunEvents.armed(golem) || !(golem.level() instanceof ServerLevel world)
                || target == null || !target.isAlive() || target.level() != world
                || target == golem || golem.isAlliedTo(target) || !golem.canAttack(target)
                || golem.distanceToSqr(target) > MinigunBalance.RANGE * MinigunBalance.RANGE
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
            // Same standard (not armor-piercing) data-driven damage semantics as upstream.
            // Do not reset invulnerability timers or bypass armor/damage cooldown tags.
            var type = golem.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(MINIGUN_DAMAGE);
            target.hurt(new DamageSource(type, golem, golem), MinigunBalance.DAMAGE);
            cooldown = MinigunBalance.SHOT_INTERVAL;
        }
    }
    void updateSpin() {
        boolean trigger = running && allowed(golem.getTarget());
        if (trigger && spin.speed() == 0F) {
            // CC0 replacement: natural 1.19 s spin-up cue; combat spin timing is unchanged.
            golem.level().playSound(null, golem.blockPosition(), NpcPistolSounds.MINIGUN_SPINUP.get(),
                SoundSource.NEUTRAL, 1F, 1F);
        }
        spin.tick(trigger);
        firing = trigger && spin.ready();
        if (cooldown > 0) cooldown--;
    }
    boolean firing() { return firing; }
    float speed() { return spin.speed(); }
    float angle() { return spin.angle(); }
}
