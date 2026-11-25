package io.github.heatwaveqc.tyr.progression.runtime;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import java.util.function.BooleanSupplier;

/** Fail closed: only native direct attack provenance, never generic owner attribution. */
public final class DirectDodge {
    private DirectDodge(){}
    private record Scope(Entity attacker,Entity target){}
    private static final ThreadLocal<Scope> SCOPE=new ThreadLocal<>();
    public static boolean attack(Entity attacker,Entity target,BooleanSupplier original){
        if(attacker.level().isClientSide())return original.getAsBoolean();
        Scope previous=SCOPE.get();SCOPE.set(new Scope(attacker,target));
        try{return original.getAsBoolean();}finally{
            if(previous==null)SCOPE.remove();else SCOPE.set(previous);
        }
    }
    public static boolean eligible(ServerPlayer defender,DamageSource source){
        if(source.is(DamageTypeTags.IS_EXPLOSION)||source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
            ||FlyingKick.areaAttack(source)||source.getEntity()==defender)return false;
        if(source.getDirectEntity() instanceof AbstractArrow
            &&(source.is(DamageTypes.ARROW)||source.is(DamageTypes.TRIDENT)))return true;
        Scope scope=SCOPE.get();
        return scope!=null&&scope.target()==defender&&scope.attacker()==source.getEntity()
            &&scope.attacker()==source.getDirectEntity()
            &&(source.is(DamageTypes.PLAYER_ATTACK)||source.is(DamageTypes.MOB_ATTACK)
                ||source.is(DamageTypes.MOB_ATTACK_NO_AGGRO));
    }
}
