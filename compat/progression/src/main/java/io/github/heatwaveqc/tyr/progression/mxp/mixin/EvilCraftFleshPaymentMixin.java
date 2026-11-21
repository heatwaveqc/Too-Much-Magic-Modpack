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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
@Pseudo
@Mixin(targets="org.cyclops.evilcraft.item.ItemRejuvenatedFlesh",remap=false)
public abstract class EvilCraftFleshPaymentMixin {
    @WrapMethod(method="finishUsingItem",require=0,remap=false)
    private ItemStack tmm$bite(ItemStack stack,Level world,LivingEntity eater,Operation<ItemStack> original){
        return EvilCraftPaidBlood.direct(eater,stack,()->original.call(stack,world,eater));
    }
}
