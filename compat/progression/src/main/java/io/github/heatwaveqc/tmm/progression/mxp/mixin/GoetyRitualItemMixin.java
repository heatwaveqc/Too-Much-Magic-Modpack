package io.github.heatwaveqc.tmm.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.GoetyRitualContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets={"com.Polarice3.Goety.common.ritual.CraftItemRitual","com.Polarice3.Goety.common.ritual.LocateRitual"},remap=false)
public abstract class GoetyRitualItemMixin {
    @WrapOperation(method="finish(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/Polarice3/Goety/common/blocks/entities/DarkAltarBlockEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",at=@At(value="INVOKE",target="Lnet/neoforged/neoforge/items/IItemHandler;insertItem(ILnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/item/ItemStack;"),require=0,allow=1,remap=false)
    private ItemStack tmm$insert(IItemHandler handler,int slot,ItemStack stack,boolean simulate,Operation<ItemStack> original){
        ItemStack remainder=original.call(handler,slot,stack,simulate);
        GoetyRitualContext.inserted(this,handler,slot,stack,simulate,remainder);
        return remainder;
    }
}
