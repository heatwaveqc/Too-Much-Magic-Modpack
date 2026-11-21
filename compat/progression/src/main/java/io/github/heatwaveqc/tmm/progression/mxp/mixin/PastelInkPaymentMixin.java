package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.PastelInkPayments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

/** Pastel 1.21.1 source-family player-only Ink API, not the Container/machine overload. */
@Pseudo
@Mixin(targets="earth.terrarium.pastel.api.energy.InkPowered",remap=false)
public interface PastelInkPaymentMixin {
    String PLAYER_PAY="tryDrainEnergy(Lnet/minecraft/world/entity/player/Player;Learth/terrarium/pastel/api/energy/color/InkColor;J)Z";

    @WrapMethod(method=PLAYER_PAY,require=0,remap=false)
    private static boolean tmm$pay(Player player,@Coerce Object color,long price,Operation<Boolean> original){
        return PastelInkPayments.pay(player,color,price,()->original.call(player,color,price));
    }

    @WrapOperation(method=PLAYER_PAY,at=@At(value="INVOKE",target="Learth/terrarium/pastel/api/energy/InkPowered;tryDrainEnergy(Lnet/minecraft/world/item/ItemStack;Learth/terrarium/pastel/api/energy/color/InkColor;JLnet/minecraft/world/entity/player/Player;)J"),require=0,remap=false)
    private static long tmm$drain(ItemStack stack,@Coerce Object color,long amount,Player player,Operation<Long> original){
        return PastelInkPayments.drain(stack,color,amount,player,()->original.call(stack,color,amount,player));
    }
}
