package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.MahouBoundaryDamage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/** Exact Mahou 1.36.31 native call sites, including the scheduled resistant-target fallback. */
@Pseudo
@Mixin(targets="stepsword.mahoutsukai.tile.boundary.DrainLifeBoundaryMahoujinTileEntity",remap=false)
public abstract class MahouBoundaryDamageMixin {
    @WrapOperation(method="drainLife(Lnet/minecraft/world/entity/LivingEntity;)Z",
        at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/util/EffectUtil;magicAttack(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/entity/LivingEntity;)V"),
        require=0,remap=false)
    private void tmm$primary(LivingEntity victim,float amount,LivingEntity caster,Operation<Void> original){
        MahouBoundaryDamage.attack(this,victim,caster,false,()->original.call(victim,amount,caster));
    }
    @WrapOperation(method="lambda$doBarrier$0(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/entity/player/Player;)V",
        at=@At(value="INVOKE",target="Lstepsword/mahoutsukai/util/EffectUtil;genericAttack(Lnet/minecraft/world/entity/LivingEntity;FLnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/entity/LivingEntity;)V"),
        require=0,remap=false)
    private void tmm$fallback(LivingEntity victim,float amount,DamageSource source,LivingEntity caster,Operation<Void> original){
        MahouBoundaryDamage.attack(this,victim,caster,true,()->original.call(victim,amount,source,caster));
    }
}
