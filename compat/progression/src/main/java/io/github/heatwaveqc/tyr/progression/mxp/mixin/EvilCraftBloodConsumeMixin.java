package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import io.github.heatwaveqc.tyr.progression.mxp.hooks.EvilCraftPaidBlood;
import net.neoforged.neoforge.fluids.FluidStack;
@Pseudo
@Mixin(targets="org.cyclops.evilcraft.core.item.ItemBloodContainer",remap=false)
public abstract class EvilCraftBloodConsumeMixin {
    @WrapMethod(method="consume(ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Lnet/neoforged/neoforge/fluids/FluidStack;",require=0,remap=false)
    private FluidStack tmm$consume(int amount,ItemStack stack,Player player,Operation<FluidStack> original){
        return EvilCraftPaidBlood.consume(player,stack,amount,()->original.call(amount,stack,player));
    }
}
