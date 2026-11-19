package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.runtime.forms.VampireEffects;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ability.DrinkBloodAbility",remap=false)
public abstract class JawrFeedingMixin {
    @WrapOperation(method="transferBlood(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/LivingEntity;)I",at=@At(value="INVOKE",target="Ldev/sterner/witchery/feature/blood_pool/BloodPoolHandler;increaseBlood(Lnet/minecraft/world/entity/LivingEntity;I)V"),require=0,remap=false)
    private void tmm$gain(LivingEntity recipient,int amount,Operation<Void> original,ServerPlayer feeder,LivingEntity victim){
        if(recipient==feeder)VampireEffects.feed(feeder,victim,amount,value->original.call(recipient,value));
        else original.call(recipient,amount);
    }
}
