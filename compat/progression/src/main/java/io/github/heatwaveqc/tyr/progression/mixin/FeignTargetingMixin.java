package io.github.heatwaveqc.tyr.progression.mixin;

import io.github.heatwaveqc.tyr.progression.runtime.FeignDeath;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetingConditions.class)
public abstract class FeignTargetingMixin {
    @Shadow @Final private boolean isCombat;
    @Inject(method="test",at=@At("HEAD"),cancellable=true)
    private void tmm$feign(LivingEntity base,LivingEntity target,CallbackInfoReturnable<Boolean> cir){
        if(isCombat&&base instanceof Mob mob&&target instanceof ServerPlayer p&&FeignDeath.hidesFrom(mob,p))
            cir.setReturnValue(false);
    }
}
