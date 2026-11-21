package io.github.heatwaveqc.tyr.progression.mxp.mixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.JawrRitualOutput;
import io.github.heatwaveqc.tyr.progression.mxp.hooks.MxpReflect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import java.util.UUID;
@Pseudo
@Mixin(targets="dev.sterner.witchery.content.ritual_event.RotRitualEvent",remap=false)
public abstract class JawrRotRitualMixin{
    @WrapOperation(method={"transformEntity(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V","transformItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/item/ItemEntity;)V"},at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),require=0,remap=false)
    private boolean tmm$spawn(ServerLevel level,Entity entity,Operation<Boolean> original){
        boolean accepted=original.call(level,entity);JawrRitualOutput.spawned(this,level,entity,accepted);return accepted;
    }
    @WrapOperation(method="transformBlock(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",at=@At(value="INVOKE",target="Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"),require=0,remap=false)
    private boolean tmm$block(ServerLevel level,BlockPos pos,BlockState state,Operation<Boolean> original){
        BlockState before=level.getBlockState(pos);
        boolean accepted=original.call(level,pos,state);
        JawrRitualOutput.changed(this,level,accepted&&!before.equals(level.getBlockState(pos)));
        return accepted;
    }
}
