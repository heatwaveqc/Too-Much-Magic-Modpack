package io.github.heatwaveqc.tmm.tradersvengence.runtime;

import io.github.heatwaveqc.tmm.tradersvengence.TradersVengence;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

/**
 * Use Iron's registered projectile rather than a homegrown weapon.
 * Reflective invocation keeps the Java module build independent of proprietary mod jars.
 * The exact methods are from Iron's 1.21 source: shoot(Vec3) and setDamage(float).
 */
public final class MagicMissileBridge {
    private static final ResourceLocation MAGIC_MISSILE =
        ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "magic_missile");
    private static boolean loggedFailure;

    private MagicMissileBridge() {}

    public static void fire(LivingEntity caster, LivingEntity target) {
        if (!(caster.level() instanceof ServerLevel level)) {
            return;
        }
        try {
            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(MAGIC_MISSILE)) {
                return;
            }
            EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(MAGIC_MISSILE);
            Entity entity = type.create(level);
            if (!(entity instanceof Projectile missile)) {
                return;
            }
            Vec3 origin = caster.getEyePosition().add(0, -0.2, 0);
            Vec3 aim = target.getEyePosition().subtract(origin);
            if (aim.lengthSqr() < 0.001) {
                return;
            }
            missile.setOwner(caster);
            missile.setPos(origin);
            entity.getClass().getMethod("setDamage", float.class)
                .invoke(entity, VengeanceRules.MISSILE_DAMAGE);
            entity.getClass().getMethod("shoot", Vec3.class)
                .invoke(entity, aim.normalize());
            level.addFreshEntity(missile);
        } catch (ReflectiveOperationException | RuntimeException exception) {
            if (!loggedFailure) {
                loggedFailure = true;
                TradersVengence.LOGGER.error("Iron's Magic Missile integration failed; no projectile launched", exception);
            }
        }
    }
}
