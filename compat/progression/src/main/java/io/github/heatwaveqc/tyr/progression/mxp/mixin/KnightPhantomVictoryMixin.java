package io.github.heatwaveqc.tyr.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import io.github.heatwaveqc.tyr.progression.mxp.BossRewards;
import io.github.heatwaveqc.tyr.progression.mxp.SpecialBossNative;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;

@Pseudo
@Mixin(targets="twilightforest.entity.boss.KnightPhantom",remap=false)
public abstract class KnightPhantomVictoryMixin {
    @Shadow @Final private static EntityDataAccessor<Boolean> IT_IS_OVER;
    @WrapMethod(method="postmortem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V",require=0,remap=false)
    private void tmm$groupVictory(ServerLevel level,DamageSource cause,Operation<Void> original){
        LivingEntity self=(LivingEntity)(Object)this;
        boolean watch=SpecialBossNative.pinned("twilightforest","4.8.3345")&&!self.getEntityData().get(IT_IS_OVER);
        original.call(level,cause);
        if(watch&&self.getEntityData().get(IT_IS_OVER))BossRewards.knightCompleted(self,cause);
    }
}
