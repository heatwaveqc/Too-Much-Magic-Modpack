package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.runtime.forms.VampireEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.world.damagesource.DamageSource;
@Pseudo
@Mixin(targets="dev.sterner.witchery.feature.affliction.vampire.VampireEvents",remap=false)
public abstract class JawrVampireEffectsMixin {
    @WrapOperation(method={"handleBloodHunger","handleBloodHealing"},at=@At(value="INVOKE",target="Ldev/sterner/witchery/feature/blood_pool/BloodPoolHandler;decreaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V"),require=0,remap=false)
    private void tmm$selfDebit(LivingEntity entity,int amount,Operation<Void> original){
        VampireEffects.selfDebit(entity,amount,value->original.call(entity,value));
    }
    @WrapOperation(method="handleSunDamage",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerPlayer;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),require=0,remap=false)
    private boolean tmm$sun(ServerPlayer p,DamageSource source,float amount,Operation<Boolean> original){
        return original.call(p,source,VampireEffects.sun(p,amount));
    }
}
