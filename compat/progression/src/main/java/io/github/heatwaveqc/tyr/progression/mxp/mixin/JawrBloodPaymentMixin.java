package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrBloodPayments;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

/** Exact native 0.5.39 decrement descriptor, verified by the previous pinned audit. */
@Pseudo
@Mixin(targets="dev.sterner.witchery.feature.blood_pool.BloodPoolHandler",remap=false)
public abstract class JawrBloodPaymentMixin {
    @WrapMethod(method="increaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V",require=0,remap=false)
    private static void tmm$feeding(LivingEntity entity,int amount,Operation<Void> original){
        io.github.heatwaveqc.tyr.progression.runtime.forms.VampireEffects.drink(entity,amount,value->original.call(entity,value));
    }
    @WrapMethod(method="decreaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V",require=0,remap=false)
    private static void tmm$decrease(LivingEntity entity,int amount,Operation<Void> original){
        JawrBloodPayments.decrease(entity,amount,()->original.call(entity,amount));
    }
}
