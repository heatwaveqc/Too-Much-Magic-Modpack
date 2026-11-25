package io.github.heatwaveqc.tyr.progression.mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.runtime.AttackIdleDefense;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class AttackIdleResistanceMixin {
    @WrapMethod(method="getDamageAfterMagicAbsorb")
    private float tmm$attackIdle(DamageSource source,float amount,Operation<Float> original){
        return AttackIdleDefense.nativeDamage((LivingEntity)(Object)this,()->original.call(source,amount));
    }
}
