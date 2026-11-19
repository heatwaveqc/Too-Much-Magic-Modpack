package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.NativePaidSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
@Pseudo
@Mixin(targets="com.Polarice3.Goety.common.items.magic.DarkWand",remap=false)
public abstract class GoetySoulPaymentMixin {
    private static final String RESULTS="MagicResults(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lcom/Polarice3/Goety/api/magic/ISpell;)V";
    @WrapMethod(method=RESULTS,require=0,remap=false)
    private void tmm$cast(ItemStack stack,Level level,LivingEntity caster,@Coerce Object spell,Operation<Void> original){
        NativePaidSources.Goety.cast(caster,()->original.call(stack,level,caster,spell));
    }
    @WrapOperation(method=RESULTS,at=@At(value="INVOKE",target="Lcom/Vivideru/Goety/common/magic/SpellTypeDamage;cast(Lcom/Polarice3/Goety/api/magic/ISpell;Ljava/lang/Runnable;)V"),require=0,remap=false)
    private void tmm$effect(@Coerce Object spell,Runnable effect,Operation<Void> original){
        original.call(spell,(Runnable)()->NativePaidSources.Goety.effect(effect));
    }
    @WrapOperation(method=RESULTS,at=@At(value="INVOKE",target="Lcom/Polarice3/Goety/utils/SEHelper;decreaseSouls(Lnet/minecraft/world/entity/player/Player;I)V"),require=0,remap=false)
    private void tmm$debit(Player player,int cost,Operation<Void> original){
        Object before=NativePaidSources.Goety.before(player);
        original.call(player,cost);
        NativePaidSources.Goety.removed(player,cost,before);
    }
}
