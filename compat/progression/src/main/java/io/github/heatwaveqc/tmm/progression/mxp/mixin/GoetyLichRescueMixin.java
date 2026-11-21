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

import net.minecraft.world.entity.player.Player;
@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.events.SoulEnergyEvents",remap=false)
public abstract class GoetyLichRescueMixin{
    @WrapMethod(method="onLivingDeathEvent",require=0,remap=false)
    private static void tmm$rescue(LivingDeathEvent e,Operation<Void> original){
        LichRescueReceipts.observe(e,false,()->original.call(e));
    }
    @WrapOperation(method="onLivingDeathEvent",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/utils/SEHelper;teleportToArca(Lnet/minecraft/world/entity/player/Player;)Z"),require=0,remap=false)
    private static boolean tmm$teleport(Player p,Operation<Boolean> original){
        boolean ok=original.call(p);LichRescueReceipts.arcaTeleport(p,ok);return ok;
    }
    @WrapOperation(method="onLivingDeathEvent",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/utils/SEHelper;decreaseSoulsNoDiscount(Lnet/minecraft/world/entity/player/Player;I)V"),require=0,remap=false)
    private static void tmm$paid(Player p,int price,Operation<Void> original){
        LichRescueReceipts.arcaDebit(p,price,()->original.call(p,price));
    }
}
