package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

/**
 * Ordinary LargeFireball flight/collision, one custom tiny detonation.
 * Tiny block-damage profile adapted from AE2 1.21.1 TinyTNTPrimedEntity
 * (LGPL-3.0-or-later); see THIRD_PARTY_NOTICES.md.
 */
public final class GolemRocket extends LargeFireball {
    private static final float BLAST_STRENGTH = 0.2F;
    private static final float DAMAGE = 6.0F;
    private static final double ENTITY_REACH = 1.5D;
    private static final int MAX_AGE = 200;

    public GolemRocket(EntityType<? extends LargeFireball> type, Level level) {
        super(type, level);
    }

    public GolemRocket(ServerLevel level, IronGolem owner, Vec3 muzzle, Vec3 towards) {
        this(GolemRocketTypes.ROCKET.get(), level);
        Vec3 heading = towards.normalize();
        setPos(muzzle);
        setOwner(owner);
        // Standard 1.21.1 AbstractHurtingProjectile initial directional motion;
        // native LargeFireball.tick() retains the normal acceleration, drag and collision.
        setDeltaMovement(heading.scale(0.1D));
        accelerationPower = 0.1D;
    }

    @Override protected boolean shouldBurn() { return false; }

    @Override public void tick() {
        super.tick();
        if (!level().isClientSide() && tickCount > MAX_AGE) discard();
    }

    @Override
    protected void onHit(HitResult result) {
        // Deliberately do not call LargeFireball.onHit(): it would explode at
        // ghast power instead of at the AE2 Tiny TNT scale and set things alight.
        if (result.getType() == HitResult.Type.MISS || isRemoved()) return;
        if (level() instanceof ServerLevel server) {
            tinyBlast(server, result.getLocation());
            discard(); // no second detonation from a second collision callback
        }
    }

    private void tinyBlast(ServerLevel level, Vec3 pos) {
        // AE2 uses a 0.2-strength Explosion as the damage source/event context,
        // but performs its entity damage and weak-block destruction separately.
        var explosion = new Explosion(level, this, pos.x, pos.y, pos.z,
            BLAST_STRENGTH, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY);
        var box = new AABB(pos.x - ENTITY_REACH, pos.y - ENTITY_REACH, pos.z - ENTITY_REACH,
            pos.x + ENTITY_REACH, pos.y + ENTITY_REACH, pos.z + ENTITY_REACH);
        List<Entity> nearby = level.getEntities(this, box);
        EventHooks.onExplosionDetonate(level, explosion, nearby, BLAST_STRENGTH * 2.0D);

        for (Entity entity : nearby) {
            if (entity.isAlive()) entity.hurt(level.damageSources().explosion(explosion), DAMAGE);
        }

        // The owner accepts the *native AE2 weak-block pattern*, including
        // multiple destroyed soft blocks. No village or claim-specific shield.
        if (level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            double centerY = pos.y - 0.25D;
            for (int x = (int)(pos.x - 2); x <= pos.x + 2; x++) {
                for (int y = (int)(centerY - 2); y <= centerY + 2; y++) {
                    for (int z = (int)(pos.z - 2); z <= pos.z + 2; z++) {
                        BlockPos at = new BlockPos(x, y, z);
                        if (!level.hasChunkAt(at)) continue;
                        BlockState state = level.getBlockState(at);
                        if (state.isAir()) continue;
                        double dx = x + 0.5D - pos.x;
                        double dy = y + 0.5D - centerY;
                        double dz = z + 0.5D - pos.z;
                        float force = (float)(2.3D - (dx * dx + dy * dy + dz * dz));
                        float fluid = state.getFluidState().isEmpty()
                            ? 0F : state.getFluidState().getExplosionResistance();
                        float resistance = Math.max(
                            state.getBlock().getExplosionResistance(state, level, at, explosion),
                            fluid);
                        if (force - (resistance + 0.3F) * 0.11F <= 0.01F) continue;

                        if (state.canDropFromExplosion(level, at, explosion)) {
                            Block.dropResources(state, level, at, level.getBlockEntity(at));
                        }
                        level.removeBlock(at, false);
                        state.onBlockExploded(level, at, explosion);
                    }
                }
            }
        }

        level.playSound(null, pos.x, pos.y, pos.z,
            SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1.1F, 1.45F);
        level.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y, pos.z,
            2, 0.18D, 0.18D, 0.18D, 0.0D);
        level.sendParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z,
            14, 0.4D, 0.35D, 0.4D, 0.05D);
    }
}
