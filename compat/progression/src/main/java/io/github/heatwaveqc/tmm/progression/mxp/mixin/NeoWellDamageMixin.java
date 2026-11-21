package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.NeoWellDamage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

/** Neo Vitae 1.1.53, inherited by the Deus Well; never targets Torment Nexus. */
@Pseudo
@Mixin(targets="com.breakinblocks.neovitae.ritual.types.RitualWellOfSuffering",remap=false)
public abstract class NeoWellDamageMixin {
    @WrapOperation(method="performRitual(Lcom/breakinblocks/neovitae/ritual/IMasterRitualStone;)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
        require=0,remap=false)
    private boolean tmm$wellDamage(LivingEntity victim,DamageSource source,float amount,Operation<Boolean> original,
            @Coerce Object master){
        return NeoWellDamage.hurt(this,master,victim,source,()->original.call(victim,source,amount));
    }
}
