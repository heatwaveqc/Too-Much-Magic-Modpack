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

import io.github.heatwaveqc.tyr.progression.mxp.hooks.PrimalPaidCast;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.level.Level;
@Pseudo
@Mixin(targets="com.verdantartifice.primalmagick.common.items.wands.AbstractWandItem",remap=false)
public abstract class PrimalWandPaymentMixin {
    @WrapMethod(method="use",require=0,remap=false)
    private InteractionResultHolder<ItemStack> tmm$use(Level level,Player player,InteractionHand hand,Operation<InteractionResultHolder<ItemStack>> original){
        return PrimalPaidCast.use(player,player.getItemInHand(hand),()->original.call(level,player,hand));
    }
    @WrapOperation(method="use",at=@At(value="INVOKE",target="Lcom/verdantartifice/primalmagick/common/wands/ManaManager;consumeMana(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lcom/verdantartifice/primalmagick/common/sources/SourceList;Lnet/minecraft/core/HolderLookup$Provider;)Z"),require=0,remap=false)
    private boolean tmm$consume(Player player,ItemStack wand,@Coerce Object sources,HolderLookup.Provider lookup,Operation<Boolean> original){
        return PrimalPaidCast.consume(player,wand,sources,()->original.call(player,wand,sources,lookup));
    }
}
