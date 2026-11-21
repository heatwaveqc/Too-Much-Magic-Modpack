package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import io.github.heatwaveqc.tmm.progression.mxp.hooks.EvilCraftPaidBlood;
import net.minecraft.world.entity.LivingEntity;
@Pseudo
@Mixin(targets="org.cyclops.evilcraft.item.ItemMace",remap=false)
public abstract class EvilCraftMacePaymentMixin {
    @WrapMethod(method="hurtEnemy",require=0,remap=false)
    private boolean tmm$hit(ItemStack stack,LivingEntity target,LivingEntity attacker,Operation<Boolean> original){
        return EvilCraftPaidBlood.direct(attacker,stack,()->original.call(stack,target,attacker));
    }
}
