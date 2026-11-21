package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.mxp.BossRewards;
import io.github.heatwaveqc.tyr.progression.mxp.SpecialBossNative;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Pseudo
@Mixin(targets="com.brandon3055.draconicevolution.entity.guardian.DraconicGuardianEntity",remap=false)
public abstract class ChaosGuardianVictoryMixin {
    @WrapMethod(method="attackDragonFrom(Lnet/minecraft/world/damagesource/DamageSource;F)Z",require=0,remap=false)
    private boolean tmm$healthAttack(DamageSource cause,float amount,Operation<Boolean> original){
        LivingEntity self=(LivingEntity)(Object)this;
        boolean watch=SpecialBossNative.pinned("draconicevolution","3.1.4.633")&&self.getHealth()>0&&amount>0;
        boolean result=original.call(cause,amount);
        if(watch&&result&&self.isDeadOrDying())BossRewards.chaosLethalHit(self,cause);
        return result;
    }
    @WrapOperation(method="tickDeath()V",at=@At(value="INVOKE",target="Lcom/brandon3055/draconicevolution/entity/guardian/GuardianFightManager;processDragonDeath(Lcom/brandon3055/draconicevolution/entity/guardian/DraconicGuardianEntity;)V"),require=0,remap=false)
    private void tmm$terminalManager(@Coerce Object manager,@Coerce Object guardian,Operation<Void> original){
        original.call(manager,guardian);
        if(guardian==(Object)this)BossRewards.chaosCompleted((LivingEntity)guardian);
    }
}
