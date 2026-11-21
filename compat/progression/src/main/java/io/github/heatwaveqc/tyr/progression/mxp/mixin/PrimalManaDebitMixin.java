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
@Pseudo
@Mixin(targets="com.verdantartifice.primalmagick.common.wands.ManaManager",remap=false)
public abstract class PrimalManaDebitMixin {
    @WrapOperation(method="consumeMana(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lcom/verdantartifice/primalmagick/common/sources/SourceList;Lnet/minecraft/core/HolderLookup$Provider;)Z",
        at=@At(value="INVOKE",target="Lcom/verdantartifice/primalmagick/common/wands/ManaManager;deductManaInner(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lcom/verdantartifice/primalmagick/common/sources/Source;I)I"),require=0,remap=false)
    private static int tmm$debit(Player player,ItemStack wand,@Coerce Object source,int amount,Operation<Integer> original){
        return PrimalPaidCast.debit(player,wand,source,amount,()->original.call(player,wand,source,amount));
    }
}
