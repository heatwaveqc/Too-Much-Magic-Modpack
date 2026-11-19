package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import io.github.heatwaveqc.tmm.progression.runtime.forms.LichEffects;
import io.github.heatwaveqc.tmm.progression.runtime.forms.LichSoulCapacity;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.NativePaidSources;
import net.minecraft.world.entity.player.Player;
@Pseudo
@Mixin(targets="com.Polarice3.Goety.utils.SEHelper",remap=false)
public abstract class GoetyLichEnergyMixin {
    @WrapMethod(method="increaseSESouls(Lnet/minecraft/world/entity/player/Player;I)Z",require=0,remap=false)
    private static boolean tmm$capacity(Player p,int amount,Operation<Boolean> original){
        return LichSoulCapacity.gain(p,amount,()->original.call(p,amount));
    }
    @ModifyExpressionValue(method="setSoulsAmount(Lnet/minecraft/world/entity/player/Player;I)V",at=@At(value="INVOKE",target="Lnet/neoforged/neoforge/common/ModConfigSpec$ConfigValue;get()Ljava/lang/Object;"),require=0,allow=2,remap=false)
    private static Object tmm$assignment(Object cap,Player p){return LichSoulCapacity.assignmentCap(cap,p);}
    @ModifyExpressionValue(method="increaseSouls(Lnet/minecraft/world/entity/player/Player;I)V",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/common/events/spell/GoetyEventFactory;onSoulEnergyGain(Lnet/minecraft/world/entity/player/Player;I)I"),require=0,allow=1,remap=false)
    private static int tmm$earned(int finalGain,Player p,int requested){return LichEffects.earned(p,finalGain,requested);}
    @ModifyExpressionValue(method="decreaseSoulsNoDiscount(Lnet/minecraft/world/entity/player/Player;I)V",at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/common/events/spell/GoetyEventFactory;onSoulEnergyLoss(Lnet/minecraft/world/entity/player/Player;I)I"),require=0,allow=1,remap=false)
    private static int tmm$finalCost(int actualPrice,Player p){NativePaidSources.Goety.price(p,actualPrice);return actualPrice;}
}
