package io.github.heatwaveqc.tyr.progression.runtime;

import io.github.heatwaveqc.tyr.progression.data.MechanicalSpec;
import io.github.heatwaveqc.tyr.progression.mixin.ProjectileOwnerAccess;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

/** Return the original incoming projectile through the native aim-deflection hook. */
public final class ProjectileReturn {
    private ProjectileReturn(){}
    private static final String RETURNED="tyr_progression:projectile_returned";
    public static boolean tryReturn(ServerPlayer p,ProjectileImpactEvent event){
        if(event.isCanceled()||!p.isAlive()||p.isCreative()||p.isSpectator()||NewbieMode.isNewbie(p)
            ||p instanceof net.neoforged.neoforge.common.util.FakePlayer||FeignDeath.active(p))return false;
        var projectile=event.getProjectile();
        // A trident is a durable thrown item, not arrow ammunition. Do not transfer it.
        if(!(projectile instanceof AbstractArrow)&&!(projectile instanceof LargeFireball)
            ||projectile instanceof ThrownTrident||projectile.level()!=p.level()||!projectile.isAlive()
            ||projectile.getPersistentData().getBoolean(RETURNED)||projectile.getOwner()==p
            ||projectile.getDeltaMovement().lengthSqr()<0.001)return false;
        var owner=projectile.getOwner();
        if(owner instanceof LivingEntity living&&(p.isAlliedTo(living)||living.isAlliedTo(p)
                ||!MechanicalEffects.areaAllowed(p,living)))return false;
        var selected=EffectRuntime.active(p).stream().filter(a->{
            var fx=a.effect();
            return fx.type().equals("projectile_return")
                &&(owner!=null||MechanicalSpec.bool(fx.params(),"allow_ownerless",true))
                &&(!(owner instanceof Player)||MechanicalSpec.bool(fx.params(),"allow_player_projectiles",false))
                &&(fx.selector().isEmpty()||fx.selector().equals("*")
                   ||owner instanceof LivingEntity living&&Selectors.entity(fx.selector(),living));
        }).findFirst().orElse(null);
        if(selected==null)return false;
        if(!projectile.deflect(ProjectileDeflection.AIM_DEFLECT,p,p,true))return false;
        // No second item/arrow, pickup reward or duplicated return. Use normal
        // owner-exit collision gating for the newly redirected original.
        ((ProjectileOwnerAccess)projectile).tmm$leftOwner(false);
        projectile.getPersistentData().putBoolean(RETURNED,true);
        if(projectile instanceof AbstractArrow arrow)arrow.pickup=AbstractArrow.Pickup.DISALLOWED;
        projectile.hasImpulse=true;
        event.setCanceled(true);
        AttackIdleDefense.offensiveAction(p);
        return true;
    }
}
