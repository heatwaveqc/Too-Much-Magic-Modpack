package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.runtime.forms.NativeBiteContext;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ability.DrinkBloodAbility",remap=false)
public abstract class JawrVampireBiteMixin {
    @WrapOperation(method="handleTargetDamage",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),require=0,remap=false)
    private boolean tmm$creatureBite(LivingEntity target,DamageSource source,float amount,Operation<Boolean> original){
        return NativeBiteContext.hurt(NativeBiteContext.VAMPIRE,target,source,()->original.call(target,source,amount));
    }
    @WrapOperation(method="handleTargetDamage",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/player/Player;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),require=0,remap=false)
    private boolean tmm$playerBite(Player target,DamageSource source,float amount,Operation<Boolean> original){
        return NativeBiteContext.hurt(NativeBiteContext.VAMPIRE,target,source,()->original.call(target,source,amount));
    }
}
