package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MahouBoundaryDamage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/** Resolve the actual hurt argument only under a trusted boundary frame. */
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.util.EffectUtil",remap=false)
public abstract class MahouTerminalDamageMixin {
    @WrapOperation(method="genericAttack(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/entity/LivingEntity;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
        require=0,remap=false)
    private static boolean tmm$hurt(LivingEntity victim,DamageSource source,float amount,Operation<Boolean> original,
            LivingEntity expectedVictim,float nativeAmount,DamageSource expectedSource,LivingEntity caster){
        if(victim!=expectedVictim||source!=expectedSource)return original.call(victim,source,amount);
        return MahouBoundaryDamage.hurt(victim,source,caster,()->original.call(victim,source,amount));
    }
}
