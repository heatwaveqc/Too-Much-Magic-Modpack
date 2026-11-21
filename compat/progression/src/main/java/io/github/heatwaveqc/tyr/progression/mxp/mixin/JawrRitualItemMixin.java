package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.SummonItemRitualEvent",remap=false)
public abstract class JawrRitualItemMixin {
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Ldev/sterner/witchery/content/entity/FloatingItemEntity;setItem(Lnet/minecraft/world/item/ItemStack;)V"),require=0,remap=false)
    private void tmm$initialize(@Coerce Object entity,ItemStack stack,Operation<Void> original){
        original.call(entity,stack);JawrRitualOutput.initializedItem(this,entity,stack);
    }
    @WrapOperation(method="tick(Ldev/sterner/witchery/content/block/ritual/ritual_event/RitualContext;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);JawrRitualOutput.spawned(this,level,entity,accepted);return accepted;
    }
}
