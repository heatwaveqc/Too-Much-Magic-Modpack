package io.github.heatwaveqc.traders.vengeance.runtime;

import io.github.heatwaveqc.traders.vengeance.state.TraderBlacklist;
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
import net.minecraft.world.phys.Vec3;

/**
 * NPC-only combat adapter. Reuses the existing/native target, never scans for targets.
 * Uses unupgraded PneumaticCraft standard ammo base range and damage, and
 * one firing attempt per tick after spin-up. Vanilla hurt resistance applies.
 */
public final class GolemMinigunGoal extends Goal {
    private static final ResourceKey<DamageType> MINIGUN_DAMAGE = ResourceKey.create(
        Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("traders_vengeance", "minigun"));
    private final IronGolem golem;
    private final MinigunSpin spin = new MinigunSpin();
    private boolean running;
    private boolean firing;
    private boolean deployed;
    private int cooldown;
    private int rocketCooldown;
    private static final int ROCKET_INTERVAL = 120; // six seconds; minigun keeps operating

    public GolemMinigunGoal(IronGolem golem) {
        this.golem = golem;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean validTarget(LivingEntity target) {
        if (!GolemMinigunEvents.armed(golem) || !(golem.level() instanceof ServerLevel world)
                || target == null || !target.isAlive() || target.level() != world
                || target == golem || golem.isAlliedTo(target) || !golem.canAttack(target)) return false;
        if (target instanceof Player) {
            return target instanceof ServerPlayer player && !player.isCreative()
                && !player.isSpectator() && TraderBlacklist.get(world).contains(player.getUUID());
        }
        return true;
    }

    private boolean allowed(LivingEntity target) {
        return validTarget(target)
            && golem.distanceToSqr(target) <= MinigunBalance.RANGE * MinigunBalance.RANGE
            && golem.getSensing().hasLineOfSight(target);
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
        if (rocketCooldown == 0 && golem.level() instanceof ServerLevel world
                && GolemRocketTargeting.mayLaunch(world, golem, target)) {
            // Same native current target as the minigun; no competing goals or homing.
            Vec3 forward = golem.getLookAngle();
            Vec3 across = new Vec3(forward.x, 0D, forward.z).normalize();
            if (across.lengthSqr() > 0.0001D) {
                Vec3 left = new Vec3(across.z, 0D, -across.x);
                Vec3 muzzle = golem.position().add(left.scale(0.62D))
                    .add(0D, 2.15D, 0D).add(across.scale(0.55D));
                Vec3 towards = target.getEyePosition().subtract(muzzle);
                if (towards.lengthSqr() > 0.0001D) {
                    if (world.addFreshEntity(new GolemRocket(world, golem, muzzle, towards))) {
                        rocketCooldown = ROCKET_INTERVAL;
                    }
                }
            }
        }
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
        deployed = validTarget(golem.getTarget());
        boolean trigger = running && allowed(golem.getTarget());
        if (trigger && spin.speed() == 0F) {
            // CC0 replacement: natural 1.19 s spin-up cue; combat spin timing is unchanged.
            golem.level().playSound(null, golem.blockPosition(), NpcPistolSounds.MINIGUN_SPINUP.get(),
                SoundSource.NEUTRAL, 1F, 1F);
        }
        spin.tick(trigger);
        firing = trigger && spin.ready();
        if (cooldown > 0) cooldown--;
        if (rocketCooldown > 0) rocketCooldown--;
    }
    boolean deployed() { return deployed; }
    boolean currentDeployment() { return validTarget(golem.getTarget()); }
    boolean firing() { return firing; }
    boolean running() { return running; }
    float speed() { return spin.speed(); }
    float angle() { return spin.angle(); }
}
