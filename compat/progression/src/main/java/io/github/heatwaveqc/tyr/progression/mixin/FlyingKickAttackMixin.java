package io.github.heatwaveqc.tyr.progression.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.runtime.FlyingKick;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public abstract class FlyingKickAttackMixin {
    @WrapMethod(method="attack")
    private void tmm$attack(Entity target,Operation<Void> original){
        var player=(Player)(Object)this;
        io.github.heatwaveqc.tyr.progression.runtime.DirectDodge.attack(player,target,()->{
            FlyingKick.attack(player,target,()->original.call(target));return true;
        });
    }
    @WrapOperation(method="attack",at=@At(value="INVOKE",target="Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean tmm$primary(Entity target,DamageSource source,float amount,Operation<Boolean> original){
        return FlyingKick.primary(target,source,amount,()->original.call(target,source,amount));
    }
}
