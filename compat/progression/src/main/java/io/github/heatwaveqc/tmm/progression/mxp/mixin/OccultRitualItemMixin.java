package io.github.heatwaveqc.tmm.progression.mxp.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tmm.progression.mxp.hooks.OccultRitualContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="com.klikli_dev.occultism.common.ritual.Ritual",remap=false)
public abstract class OccultRitualItemMixin {
    @WrapOperation(method="dropResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/klikli_dev/occultism/common/blockentity/GoldenSacrificialBowlBlockEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)V",
        at=@At(value="INVOKE",target="Lnet/neoforged/neoforge/items/ItemStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"),require=0,remap=false)
    private void tmm$placed(ItemStackHandler handler,int slot,ItemStack stack,Operation<Void> original){
        boolean watch=OccultRitualContext.watchingItem(this,stack);
        boolean empty=watch&&handler.getStackInSlot(slot).isEmpty();
        ItemStack before=watch?stack.copy():ItemStack.EMPTY;
        original.call(handler,slot,stack);
        if(watch)OccultRitualContext.placedItem(this,before,handler.getStackInSlot(slot),empty);
    }
    @WrapOperation(method="dropResult(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lcom/klikli_dev/occultism/common/blockentity/GoldenSacrificialBowlBlockEntity;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Z)V",
        at=@At(value="INVOKE",target="Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$dropped(Level level,Entity entity,Operation<Boolean> original){
        boolean result=original.call(level,entity);
        OccultRitualContext.insertedItem(this,level,entity,result);
        return result;
    }
}
