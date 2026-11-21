package io.github.heatwaveqc.traders.vengeance.runtime;

import java.util.EnumSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.npc.AbstractVillager;

/** Server-authoritative NPC combat. No actual obtainable item, ammo or player firing API. */
public final class NpcPistolGoal extends Goal {
    private static final ResourceKey<DamageType> BULLET = ResourceKey.create(Registries.DAMAGE_TYPE,
        ResourceLocation.fromNamespaceAndPath("traders_vengeance", "pistol_bullet"));
    private final AbstractVillager defender;
    private final PistolCycle cycle = new PistolCycle();
    public NpcPistolGoal(AbstractVillager defender) {
        this.defender = defender;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }
    private boolean allowed(LivingEntity target) {
        return defender.level() instanceof ServerLevel
            && target != null && target.isAlive() && target.level() == defender.level()
            && target != defender && !defender.isAlliedTo(target)
            && defender.canAttack(target)
            && WantedRangedAttackGoal.mayRetaliate(defender, target);
    }
    @Override public boolean canUse() { return allowed(defender.getTarget()); }
    @Override public boolean canContinueToUse() { return allowed(defender.getTarget()); }
    @Override public boolean requiresUpdateEveryTick() { return true; }
    @Override public void stop() { defender.getNavigation().stop(); }
    @Override public void tick() {
        LivingEntity target = defender.getTarget();
        if (!allowed(target)) return;
        defender.getLookControl().setLookAt(target, 30F, 30F);
        boolean ready = defender.distanceToSqr(target) <= PistolCycle.RANGE * PistolCycle.RANGE
            && defender.getSensing().hasLineOfSight(target);
        if (ready) defender.getNavigation().stop();
        else if (defender.tickCount % 10 == 0) defender.getNavigation().moveTo(target, 0.9);
        switch (cycle.tick(ready)) {
            case SHOT -> {
                if (!allowed(target) || !defender.getSensing().hasLineOfSight(target)) return;
                var type = defender.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(BULLET);
                // The donor game uses normal hurt resistance; no bypass or headshot bonuses.
                target.hurt(new DamageSource(type, defender, defender), PistolCycle.DAMAGE);
                defender.swing(InteractionHand.MAIN_HAND);
                play(NpcPistolSounds.FIRE.get(), 3F);
            }
            case MAG_OUT -> play(NpcPistolSounds.MAG_OUT.get(), 0.7F);
            case MAG_IN -> play(NpcPistolSounds.MAG_IN.get(), 0.7F);
            case SLIDE -> play(NpcPistolSounds.SLIDE.get(), 0.7F);
            case NONE -> { }
        }
    }
    private void play(SoundEvent sound, float volume) {
        defender.level().playSound(null, defender.blockPosition(), sound, SoundSource.NEUTRAL, volume, 1F);
    }
}
