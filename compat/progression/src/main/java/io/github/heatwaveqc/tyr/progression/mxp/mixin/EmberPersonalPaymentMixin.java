package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.NativePaidSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.world.entity.player.Player;
@Pseudo
@Mixin(targets="com.rekindled.embers.util.EmberInventoryUtil",remap=false)
public abstract class EmberPersonalPaymentMixin {
    @WrapMethod(method="removeEmber",require=0,remap=false)
    private static void tmm$personal(Player player,double amount,Operation<Void> original){
        NativePaidSources.Embers.spend(player,amount,()->original.call(player,amount));
    }
    @WrapOperation(method="removeEmber",at=@At(value="INVOKE",target="Lcom/rekindled/embers/api/event/EmberRemoveEvent;getFinal()D"),require=0,remap=false)
    private static double tmm$price(@Coerce Object event,Operation<Double> original){
        double cost=original.call(event);NativePaidSources.Embers.price(cost);return cost;
    }
    @WrapOperation(method="removeEmber",at=@At(value="INVOKE",target="Lcom/rekindled/embers/api/power/IEmberCapability;removeAmount(DZ)D"),require=0,remap=false)
    private static double tmm$debit(@Coerce Object cap,double request,boolean remove,Operation<Double> original){
        double before=NativePaidSources.Embers.before(cap);
        double result=original.call(cap,request,remove);
        NativePaidSources.Embers.removed(cap,request,remove,result,before);
        return result;
    }
}
