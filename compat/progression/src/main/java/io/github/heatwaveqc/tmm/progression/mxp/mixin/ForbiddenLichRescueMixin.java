package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.runtime.forms.LichRescueReceipts;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
@Pseudo
@Mixin(targets="dev.sterner.witchery_forbidden_magic.feature.affliction.lich.LichdomEvents",remap=false)
public abstract class ForbiddenLichRescueMixin{
    @WrapMethod(method="onDeath(Lnet/neoforged/neoforge/event/entity/living/LivingDeathEvent;Lnet/minecraft/world/entity/LivingEntity;)V",require=0,remap=false)
    private static void tmm$rescue(LivingDeathEvent e,LivingEntity entity,Operation<Void> original){
        LichRescueReceipts.observe(e,true,()->original.call(e,entity));
    }
    @WrapOperation(method="onDeath",at=@At(value="INVOKE",target="Ldev/sterner/witchery_forbidden_magic/feature/affliction/lich/LichdomLeveling;onPhylacterySave(Lnet/minecraft/server/level/ServerPlayer;)V"),require=0,allow=1,remap=false)
    private static void tmm$success(@Coerce Object leveling,ServerPlayer p,Operation<Void> original){
        original.call(leveling,p);LichRescueReceipts.phylactery(p);
    }
}
